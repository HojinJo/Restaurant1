package com.example.hojinjo.restaurant1;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ToRestaurantDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_restaurant_detail);
            RestaurantDetail restdetail = new RestaurantDetail();
            //위도 경도 값 //여기서 받은 인텐트값을 레스토랑 디테일로 어떻게 전달할까??
            Intent intent=getIntent();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, restdetail).commit();

    }

}
