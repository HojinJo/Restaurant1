package com.example.hojinjo.restaurant1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

    public interface OnTitleSelectedListener {
        public void onTitleSelected(int i);          //액티비티로 전달할 메세지 인터페이스
    }
    public RestaurantDetail() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       /*myUri= Uri.parse(geturi);*/

        /*액션바 생성*/
       ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
         rootView = inflater.inflate(R.layout.activity_restaurant_detail, container, false);

        rDbHelper = new DBHelper(getContext());
        mDbHelper = new MDBHelper(getContext());

        getRestaurant();
        //getMenu();

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
            /*Bundle getExtras = getActivity().getIntent().getExtras();
            if (getExtras != null) {*/
                restUri = Uri.parse(c.getString(4));
                ImageView restImg = rootView.findViewById(R.id.imageView2);
                restImg.setImageURI(null);
                restImg.setImageURI(restUri);
            /*Bitmap bitmap = BitmapFactory.decodeFile(c.getString(4));
            ImageView restImg = rootView.findViewById(R.id.imageView2);
            restImg.setImageBitmap(bitmap);*/


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
        Cursor cursor = mDbHelper.getAllMenusByID(c.getInt(0));
        ArrayList<MyItem> data= new ArrayList<MyItem>();
        if (cursor.moveToPosition(1)){
            if (cursor.moveToNext()) {
                data.add(new MyItem(cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            }
        }cursor.moveToLast();
        MyAdapter adapter = new MyAdapter(getContext(),R.layout.list_food,data);

        ListView lv = (ListView)rootView.findViewById(R.id.listView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Adapter adapter = adapterView.getAdapter();

                /*Bundle사용하는법은 stackoverflaw 사이트에서 참조*/
                //링크: https://stackoverflow.com/questions/41381102/attempt-to-invoke-virtual-method-java-lang-string-android-os-bundle-getstringj
                /*Uri myUri;
                Bundle extras = getActivity().getIntent().getExtras();
                if (extras != null)
                {
                    myUri=Uri.parse(extras.getString("MENUIMG"));
                    ImageView imageView1 = rootView.findViewById(R.id.iconItem);
                    imageView1.setImageURI(myUri);//저장한 uri를 보여줘야함
                }
                // Uri myUri=Uri.parse(extras.getString("MENUIMG"));//uri 받았는데 안됨...

                TextView textView1 = rootView.findViewById(R.id.textItem1);
                textView1.setText(((Cursor)adapter.getItem(i)).getString(2));

                TextView textView2 = rootView.findViewById(R.id.textItem2);
                textView2.setText(((Cursor)adapter.getItem(i)).getString(3));*/

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

        Cursor cursor=rDbHelper.getRestaurantIDByName(c.getString(1));  //rDbHelper.getRestaurantIDByName(name.getText().toString()) --> id
        if(cursor.moveToNext()){
            Intent intentid = new Intent(getContext(), RegisterM.class);
            intentid.putExtra("RESTID",cursor.getInt(0) );
            startActivity(intentid);
        }//아이템으로 이사

        return super.onOptionsItemSelected(item); //여기로이사
    }
/////

      /*DB에 저장한거 불러오는 리스트뷰*/
   /*private void viewAllToListView() {

        c = mDbHelper.getAllMenusByMethod();

        android.widget.SimpleCursorAdapter adapter = new android.widget.SimpleCursorAdapter(getContext(),
                R.layout.list_food, c, new String[]{
                MContract.Menu.KEY_MENUIMG,
                MContract.Menu.KEY_NAME,
                MContract.Menu.KEY_PRICE},
                new int[]{R.id.iconItem, R.id.textItem1, R.id.textItem2}, 0);

        ListView lv = (ListView)rootView.findViewById(R.id.listView);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Adapter adapter = adapterView.getAdapter();

                mCurCheckPosition = i;
                Activity activity = getActivity();
                ((OnTitleSelectedListener)activity).onTitleSelected(i);
                //Intent toMenuDetail = new Intent(getContext(), DetailsFragment.);
                *//*Bundle사용하는법은 stackoverflaw 사이트에서 참조*//*
                //링크: https://stackoverflow.com/questions/41381102/attempt-to-invoke-virtual-method-java-lang-string-android-os-bundle-getstringj
                Uri menuUri;
                Bundle extras = getActivity().getIntent().getExtras();
                if (extras != null)
                {
                    menuUri=Uri.parse(extras.getString("MENUIMG"));
                    ImageView imageView1 = rootView.findViewById(R.id.iconItem);
                    imageView1.setImageURI(menuUri);//저장한 uri를 보여줘야함
                }

                TextView textView1 = rootView.findViewById(R.id.textItem1);
                textView1.setText(((Cursor)adapter.getItem(i)).getString(2));

                TextView textView2 = rootView.findViewById(R.id.textItem2);
                textView2.setText(((Cursor)adapter.getItem(i)).getString(3));

            }
        });
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }*/


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