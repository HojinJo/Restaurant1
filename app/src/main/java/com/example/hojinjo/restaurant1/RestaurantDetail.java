package com.example.hojinjo.restaurant1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import javax.crypto.spec.RC2ParameterSpec;


public class RestaurantDetail extends Fragment {
    int mCurCheckPosition = -1;
    final int REQUEST_CODE_READ_CONTACTS = 1;
    private MDBHelper mDbHelper;
    private DBHelper rDbHelper;
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
        /*액션바 생성*/
       ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View rootView = inflater.inflate(R.layout.activity_restaurant_detail, container, false);

        rDbHelper = new DBHelper(getActivity());

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) { // 권한이 없으므로, 사용자에게 권한 요청 다이얼로그 표시
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        } else // 권한 있음! 해당 데이터나 장치에 접근!
        {
            getRestaurant();
            getMenu();
            //viewAllToListView();
        }

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

        String[] projection = {
                RContract.Restaurant.KEY_NAME,
                RContract.Restaurant.KEY_ADDRESS,
                RContract.Restaurant.KEY_PHONE
        };
        c=.query(RContract.Restaurant.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null);

        c.moveToLast();

        if(c.moveToLast()){
            TextView tv1 = rootView.findViewById(R.id.textView4);
            tv1.setText(c.getShort(1));

            TextView tv2 = rootView.findViewById(R.id.textView3);
            tv2.setText(c.getString(2));

            TextView tv3 = rootView.findViewById(R.id.textView2);
            tv3.setText(c.getString(3));
        }
    }

    private void getMenu() {
        String [] projection = {
                MContract.Menu.KEY_NAME,
                MContract.Menu.KEY_PRICE,
                MContract.Menu.KEY_DESCRIPTION
        };

//        c = getActivity().getContentResolver().query(
//                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  // 조회할 데이터 URI
//                projection,         // 조회할 컬럼 들
//                null,    // 선택될 행들에 대한선택될 행들에 대한 조건절
//                null,      // 조건절에 필요한 파라미터
//                null);              // 정렬 안    저장한 연락처를 가르키는 커서
//
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
     inflater.inflate(R.menu.main_menu, menu);

     super.onCreateOptionsMenu(menu, inflater);
    }

 /*액션아이템 액티비티 전환=동작*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                Intent intent=new Intent(getContext(), RegisterM.class);
                startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
/////

/////////DB에 저장한거 불러오는 리스트뷰
   /*private void viewAllToListView() {

        Cursor cursor = mDbHelper.getAllMenusByMethod();

        android.widget.SimpleCursorAdapter adapter = new android.widget.SimpleCursorAdapter(getContext(),
                R.layout.activity_restaurant_detail, cursor, new String[]{
                MContract.Menu.KEY_NAME,
                MContract.Menu.KEY_PRICE,
                MContract.Menu.KEY_DESCRIPTION},
                new int[]{R.id.edit_name, R.id.edit_price, R.id.edit_menu}, 0);

        ListView lv = (ListView)rootView.findViewById(R.id.listView);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Adapter adapter = adapterView.getAdapter();

                mName.setText(((Cursor)adapter.getItem(i)).getString(1));//넘겨준 값 써야함
                mPrice.setText(((Cursor)adapter.getItem(i)).getString(2));
                mMenu.setText(((Cursor)adapter.getItem(i)).getString(3));
            }
        });
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }
*/

//////////////

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
    }
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