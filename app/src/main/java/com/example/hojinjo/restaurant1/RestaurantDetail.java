package com.example.hojinjo.restaurant1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
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

public class RestaurantDetail extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        final ArrayList<MyItem> data = new ArrayList<MyItem>();
        data.add(new MyItem(R.drawable.dosirak, "도시락 정식", "7500"));
        data.add(new MyItem(R.drawable.chicken, "닭고기 정식", "7500"));
        data.add(new MyItem(R.drawable.curry, "카레 라이스", "7500"));
        data.add(new MyItem(R.drawable.omu, "오므라이스", "7500"));
        data.add(new MyItem(R.drawable.hamburg, "함박 스테이크", "8500"));


        final MyAdapter adapter = new MyAdapter(this, R.layout.list_food, data);


        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            public void onItemClick (AdapterView < ? > parent, View vClicked,
                                     int position, long id){
                Intent intent = new Intent(getApplicationContext(),MenuDetail.class);

                intent.putExtra("menu",data.get(position).nMenu);
                if(data.get(position).nMenu.equals("도시락 정식")){
                    intent.putExtra("score", "3.5");
                }
                else if(data.get(position).nMenu.equals("닭고기 정식")){
                    intent.putExtra("score", "3.9");
                }
                else if(data.get(position).nMenu.equals("카레 라이스")){
                    intent.putExtra("score", "4.2");
                }
                else if(data.get(position).nMenu.equals("오므라이스")){
                    intent.putExtra("score", "4.9");
                }
                else{
                    intent.putExtra("score", "3.3");
                }
                intent.putExtra("price",data.get(position).nPrice);
                intent.putExtra("img",data.get(position).mIcon);

                startActivity(intent);
            }
        });
        ImageButton btn = (ImageButton)findViewById(R.id.dialButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:021234567"));
                startActivity(intent);
            }
        });



    }

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
            convertView = inflater.inflate(mResource, parent,false);
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


    MyItem(int aIcon, String aMenu, String aPrice/*, String aReview*/) {
        mIcon = aIcon;
        nMenu = aMenu;
        nPrice = aPrice;

    }
}

