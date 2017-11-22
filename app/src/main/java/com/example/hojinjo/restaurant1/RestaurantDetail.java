package com.example.hojinjo.restaurant1;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class RestaurantDetail extends Fragment {
    final int REQUEST_CODE_READ_CONTACTS = 1;
    int mCurCheckPosition = -1;
    MDBHelper mDbHelper;
    DBHelper rDbHelper;
    Cursor c;
    View rootView;
    String restimg;
    String menuimg;

    public RestaurantDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*액션바 생성*/
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        rootView = inflater.inflate(R.layout.activity_restaurant_detail, container, false);

        rDbHelper = new DBHelper(getContext());
        mDbHelper = new MDBHelper(getContext());

        getRestaurant();
        getMenu();

        ImageButton btn = (ImageButton) rootView.findViewById(R.id.dialButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.moveToLast();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + c.getString(3)));
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void getRestaurant() {

        c = rDbHelper.getAllRestaurants();

        if (c.moveToLast()) {
            /*업소 등록 이미지 받기*/
            Uri restUri;
            restUri = Uri.parse(c.getString(4));
            ImageView restImg = rootView.findViewById(R.id.imageView2);
            restImg.setImageURI(restUri);

            TextView tv1 = rootView.findViewById(R.id.textView4);
            String s = c.getString(1);

            tv1.setText(s);

            TextView tv2 = rootView.findViewById(R.id.textView3);
            String s2 = c.getString(2);
            tv2.setText(s2);

            TextView tv3 = rootView.findViewById(R.id.textView2);
            String s3 = c.getString(3);
            tv3.setText(s3);
        }
    }

    private void getMenu() {

        Log.i("Restaurant1", "getMuenu(): [Name] getString(1)=" + c.getString(1));
        Log.i("Restaurant1", "getMuenu(): [ID] getString(0)=" + c.getString(0));
        final Cursor cursor = mDbHelper.getAllMenusByID(c.getInt(0));

        Log.i("Restaurant1", "count=" + cursor.getCount());

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getContext(),
                R.layout.list_food, cursor, new String[]{
                MContract.Menu.KEY_MENUIMG,
                MContract.Menu.KEY_NAME,
                MContract.Menu.KEY_PRICE},
                new int[]{R.id.iconItem, R.id.textItem1, R.id.textItem2}, 0);

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor cur, int columnIndex) {
                if (view.getId() == R.id.iconItem) {
                    Log.i("Restaurant1", "RestaurantDetail: c.getInt(0)=" + c.getInt(0));
                    cur = mDbHelper.getAllMenusByID(c.getInt(0));
                    Log.i("Restaurant1", "RestaurantDetail: cur = mDbHelper.getAllMenusByID(c.getInt(0))=" + cur.getCount());
                    if (cur.moveToNext()) {
                        String cc = cur.getString(1);
                        ((ImageView) view).setImageURI(Uri.parse(cc));
                    }
                    return true;
                }
                return false;
            }
        });
        //https://stackoverflow.com/questions/26627258/simplecursoradapter-with-imageview-and-textview-from-sqlite
        ListView lv = (ListView) rootView.findViewById(R.id.listView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                mCurCheckPosition = i;
                Activity activity = getActivity();
                ((OnTitleSelectedListener)activity).onTitleSelected(i);

            }
        });
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


//출처: hashcode.co.kr에서 프래그먼트일 경우 옵션메뉴 만들 때 onCreateOptionsMenu 함수 정의, setHasOptionsMenu(true) 코드 참조

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {

        MenuInflater minflater = getActivity().getMenuInflater();
        minflater.inflate(R.menu.main_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    /*액션아이템 액티비티 전환=동작*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//                Intent intent=new Intent(getContext(), RegisterM.class);
//                startActivity(intent);

        Log.i("Restaurant1", "c.getString(1)=" + c.getString(1));
        Cursor cursor = rDbHelper.getRestaurantIDByName(c.getString(1));
        if (cursor.moveToNext()) {
            Intent intentid = new Intent(getContext(), RegisterM.class);
            Log.i("Restaurant1", "cursor.getInt(0)=" + cursor.getInt(0));
            intentid.putExtra("restaurant_id", cursor.getInt(0));
            Log.i("Restaurant1", "restaurant_id=" + cursor.getInt(0));
            startActivity(intentid);
        }

        return super.onOptionsItemSelected(item);
    }

    public interface OnTitleSelectedListener {
        public void onTitleSelected(int i);          //액티비티로 전달할 메세지 인터페이스
    }
}