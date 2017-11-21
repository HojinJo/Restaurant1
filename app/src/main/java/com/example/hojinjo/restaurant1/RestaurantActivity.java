package com.example.hojinjo.restaurant1;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RestaurantActivity extends AppCompatActivity implements RestaurantDetail.OnTitleSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String restimg = intent.getStringExtra("RESTIMG");
<<<<<<< HEAD
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
        Bundle rimgBundle = new Bundle();
        rimgBundle.putString("RESTIMG",restimg);
        fragment.setArguments(rimgBundle);
=======
        String menuimg=intent.getStringExtra("IMAGEURI");
        //RestaurantDetail restaurantDetail = new RestaurantDetail();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
        Bundle imgBundle = new Bundle();
        imgBundle.putString("RESTIMG",restimg);

        Bundle menuBundle=new Bundle();
        menuBundle.putString("IMAGEURI", menuimg);

        fragment.setArguments(imgBundle);
        fragment.setArguments(menuBundle);
>>>>>>> 547a695532a3cd1ddd38004f9709b896992f0639
        //출처: http://jizard.tistory.com/66 [JIZARD]
    }
    public void onTitleSelected(int i) {
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {     //가로보기용
            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setSelection(i);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, detailsFragment).commit();
        }
        else {          //세로보기용
            Intent intent = new Intent(this, MenuDetail.class);
            intent.putExtra("index", i);
            startActivity(intent);
        }
    }
}
