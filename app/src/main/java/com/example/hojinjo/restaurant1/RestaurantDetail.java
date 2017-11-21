package com.example.hojinjo.restaurant1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import android.app.ActionBar;//버전에 맞게 v7제거 stackoverflow 사이트 참조

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;


import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;


public class RestaurantDetail extends Fragment {
    int mCurCheckPosition = -1;
    final int REQUEST_CODE_READ_CONTACTS = 1;
    MDBHelper mDbHelper;
    DBHelper rDbHelper;
    Cursor c;
    View rootView;
    String restimg;
    String menuimg;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            return;
        }
        restimg= getArguments().getString("RESTIMG");
        menuimg= getArguments().getString("IMAGEURI");//메뉴 이미지 받기

    }

    public interface OnTitleSelectedListener {
        public void onTitleSelected(int i);          //액티비티로 전달할 메세지 인터페이스
    }
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

        ImageButton btn = (ImageButton)rootView.findViewById(R.id.dialButton);
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

        Log.i("rest_id","getString(1)="+c.getString(1));
        final Cursor cursor = mDbHelper.getAllMenusByID(c.getInt(0));


        ArrayList<MyItem> data= new ArrayList<MyItem>();

        if (cursor.moveToNext()){
        data.add( new MyItem(c.getString(1),c.getString(2),c.getString(3)));
        }
        cursor.moveToLast();

        MyAdapter adapter = new MyAdapter(getContext(),R.layout.list_food,data);

        ListView lv = (ListView)rootView.findViewById(R.id.listView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Uri menuUri;
                menuUri=Uri.parse(cursor.getString(1));
                ImageView menuImg=rootView.findViewById(R.id.iconItem);
                menuImg.setImageURI(menuUri);

                //메뉴이름 디테일프래그먼트로 넘김
                String menuname = (String)((TextView)view.findViewById(R.id.textItem1)).getText();
                String menuprice=(String) ((TextView)view.findViewById(R.id.textItem2)).getText();
                Intent todetail = new Intent(getContext(), MenuDetail.class);
                todetail.putExtra("MENU", menuname);
                todetail.putExtra("PRICE", menuprice);
                startActivity(todetail);
            }
        });
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }


//출처: hashcode.co.kr에서 프래그먼트일 경우 옵션메뉴 만들 때 onCreateOptionsMenu 함수 정의, setHasOptionsMenu(true) 코드 참조

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

 @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {

     MenuInflater minflater = getActivity().getMenuInflater();
     minflater.inflate(R.menu.main_menu, menu);

     super.onCreateOptionsMenu(menu, inflater);
    }

 /*액션아이템 액티비티 전환=동작*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                Intent intent=new Intent(getContext(), RegisterM.class);
                startActivity(intent);

        Cursor cursor=rDbHelper.getRestaurantIDByName(c.getString(1));
        if(cursor.moveToNext()){
            Intent intentid = new Intent(getContext(), RegisterM.class);
            intentid.putExtra("RESTID",cursor.getInt(0) );
            startActivity(intentid);
        }

        return super.onOptionsItemSelected(item);
    }



    class MyAdapter extends BaseAdapter {    //리스트 뷰 어댑터
        private Context mContext;
        private int mResource;
        private ArrayList<MyItem> mItems = new ArrayList<MyItem>();

        public MyAdapter(Context context, int resource, ArrayList<MyItem> items) {
            mContext = context;
            mItems = items;
            mResource = resource;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(mResource, parent, false);
            }

            // Set Icon
            Uri imgUri;
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras != null)
            {
                imgUri=Uri.parse(mItems.get(position).mIcon);
                ImageView icon = convertView.findViewById(R.id.iconItem);
                icon.setImageURI(imgUri);
            }

            // Set Text 01
            TextView name = (TextView) convertView.findViewById(R.id.textItem1);
            name.setText(mItems.get(position).nMenu);

            // Set Text 02
            TextView age = (TextView) convertView.findViewById(R.id.textItem2);
            age.setText(mItems.get(position).nPrice);

            return convertView;
        }
    }
    class MyItem {
        String mIcon; // image
        String nMenu; // menu
        String nPrice;  // price


        MyItem(String aIcon, String aMenu, String aPrice) {
            mIcon = aIcon;
            nMenu = aMenu;
            nPrice = aPrice;

        }
    }
}