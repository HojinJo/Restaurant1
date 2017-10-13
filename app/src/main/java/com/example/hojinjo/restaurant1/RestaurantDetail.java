package com.example.hojinjo.restaurant1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class RestaurantDetail extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        ArrayList<MyItem> data = new ArrayList<MyItem>();
        data.add(new MyItem(R.drawable.dosirak, "도시락 정식", "7500"));
        data.add(new MyItem(R.drawable.chicken, "닭고기 정식", "7500"));
        data.add(new MyItem(R.drawable.curry, "카레 라이스", "7500"));
        data.add(new MyItem(R.drawable.omu, "오므라이스", "7500"));
        data.add(new MyItem(R.drawable.hamburg, "함박스테이크", "8500"));


        final MyAdapter adapter = new MyAdapter(this, R.layout.list_food, data);


        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            public void onItemClick (AdapterView < ? > parent, View vClicked,
                                     int position, long id){
                //   String name = (String) ((TextView)vClicked.findViewById(R.id.textItem1)).getText();
                String name = ((MyItem) adapter.getItem(position)).nMenu;
                Toast.makeText(RestaurantDetail.this, name + " selected",
                        Toast.LENGTH_SHORT).show();
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
