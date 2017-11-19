package com.example.hojinjo.restaurant1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.app.ActionBar;//버전에 맞게 v7제거 stackoverflow 사이트 참조

import android.support.v4.content.ContextCompat;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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

    //RegisterM registerM = new RegisterM();   //이렇게하면 안됌

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
            getMenu();
            viewAllToListView();


        ImageButton btn = (ImageButton)rootView.findViewById(R.id.dialButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:021234567"));
                startActivity(intent);
            }
        });
       // listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        return rootView;
    }

    private void getRestaurant() {

        c=rDbHelper.getAllRestaurantsByID();
      //  c.moveToLast();

        if(c.moveToLast()){
            TextView tv1 = rootView.findViewById(R.id.textView4);
            String s = c.getString(0);
            tv1.setText(s);


            TextView tv2 = rootView.findViewById(R.id.textView3);
            String s2 = c.getString(1);
            tv2.setText(s2);

            TextView tv3 = rootView.findViewById(R.id.textView2);
            String s3 = c.getString(2);
            tv3.setText(s3);
        }
    }

    private void getMenu() {
        c = mDbHelper.getAllMenusByID("1");
//viewalltolist와야함        c = mDbHelper.getAllMenusByID(registerM.restid);


//        while(c.moveToNext()) {
//            mDbHelper.insertUserByMethod(c.getString(1), c.getString(2) ,c.getString(3));
//        }
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
        return super.onOptionsItemSelected(item); //여기로이사
    }
/////

      /*DB에 저장한거 불러오는 리스트뷰*/
   private void viewAllToListView() {

        c = mDbHelper.getAllMenusByMethod();

        android.widget.SimpleCursorAdapter adapter = new android.widget.SimpleCursorAdapter(getContext(),
                R.layout.list_food, c, new String[]{
               // registerM.mPhotoFile.toString(),

                MContract.Menu.KEY_MENUIMG,
                MContract.Menu.KEY_NAME,
                MContract.Menu.KEY_PRICE},
                new int[]{
               R.id.iconItem,
                        R.id.textItem1, R.id.textItem2}, 0);

        ListView lv = (ListView)rootView.findViewById(R.id.listView);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Adapter adapter = adapterView.getAdapter();

                /*Bundle사용하는법은 stackoverflaw 사이트에서 참조*/
                //링크: https://stackoverflow.com/questions/41381102/attempt-to-invoke-virtual-method-java-lang-string-android-os-bundle-getstringj
                Uri myUri;
                Bundle extras = getActivity().getIntent().getExtras();
                if (extras != null)
                {
                    myUri=Uri.parse(extras.getString("MENUIMG"));
                    ImageView imageView1 = rootView.findViewById(R.id.iconItem);
                    imageView1.setImageURI(myUri);//저장한 uri를 보여줘야함
                }
               // Uri myUri=Uri.parse(extras.getString("MENUIMG"));//uri 받았는데 안됨...

              /* ImageView imageView1 = rootView.findViewById(R.id.iconItem);
               imageView1.setImageURI(myUri);//저장한 uri를 보여줘야함*/

                TextView textView1 = rootView.findViewById(R.id.textItem1);
                textView1.setText(((Cursor)adapter.getItem(i)).getString(2));

                TextView textView2 = rootView.findViewById(R.id.textItem2);
                textView2.setText(((Cursor)adapter.getItem(i)).getString(3));

                //registerM.mName.setText(((Cursor)adapter.getItem(i)).getString(1));//넘겨준 값 써야함
                //registerM.mPrice.setText(((Cursor)adapter.getItem(i)).getString(2));
                //registerM.mDesc.setText(((Cursor)adapter.getItem(i)).getString(3));
            }
        });
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

//////////////

  /*  class MyAdapter extends BaseAdapter {    //리스트 뷰 어댑터
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
            ImageView icon = (ImageView) convertView.findViewById(R.id.iconItem);
            icon.setImageResource(mItems.get(position).mIcon);

            // Set Text 01
            TextView name = (TextView) convertView.findViewById(R.id.textItem1);
            name.setText(mItems.get(position).nMenu);

            // Set Text 02
            TextView age = (TextView) convertView.findViewById(R.id.textItem2);
            age.setText(mItems.get(position).nPrice);

            return convertView;
        }
    }*/
    class MyItem {
        int mIcon; // image
        String nMenu; // menu
        String nPrice;  // price


        MyItem(int aIcon, String aMenu, String aPrice) {
            mIcon = aIcon;
            nMenu = aMenu;
            nPrice = aPrice;

        }
    }
}