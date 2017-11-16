package com.example.hojinjo.restaurant1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
<<<<<<< HEAD
import android.support.v7.app.ActionBar;
=======
import android.support.v4.content.ContextCompat;
>>>>>>> 22a85be24e54520b42b579d37a221ec2d4145772
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.data;


public class RestaurantDetail extends Fragment {
    int mCurCheckPosition = -1;

    public interface OnTitleSelectedListener {
        public void onTitleSelected(int i);          //액티비티로 전달할 메세지 인터페이스
    }
    public RestaurantDetail() {
        // Required empty public constructor
    }
    MyAdapter adapter;
    ListView listview;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      /*  ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }*/
        View rootView = inflater.inflate(R.layout.activity_restaurant_detail, container, false);

        final ArrayList<MyItem> data = new ArrayList<MyItem>();
        data.add(new MyItem(R.drawable.dosirak, "도시락 정식", "7500"));
        data.add(new MyItem(R.drawable.chicken, "닭고기 정식", "7500"));
        data.add(new MyItem(R.drawable.curry, "카레라이스", "7500"));
        data.add(new MyItem(R.drawable.omu, "오므라이스", "7500"));
        data.add(new MyItem(R.drawable.hamburg, "함박스테이크", "8500"));

         adapter = new MyAdapter(getActivity(), R.layout.list_food, data);

        listview = (ListView)rootView.findViewById(R.id.listView);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override         //리스트에서 항목을 선택했을 때 호출
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCurCheckPosition = i;
                Activity activity = getActivity();
                ((OnTitleSelectedListener)activity).onTitleSelected(i);    //TitlesFragment 와 연결된 액티비티
            }
        });
        ImageButton btn = (ImageButton)rootView.findViewById(R.id.dialButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:021234567"));
                startActivity(intent);
            }
        });
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        return rootView;
    }

   /*옵션메뉴*/
//출처: hashcode.co.kr에서 프래그먼트일 경우 옵션메뉴 만들 때 onCreateOptionsMenu 함수 정의, setHasOptionsMenu(true) 코드 참조

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.add(0, 1, 0, "메뉴등록");

       super.onCreateOptionsMenu(menu, inflater);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                Intent intent=new Intent(getContext(), RegisterM.class);
                startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
/////


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