package com.example.hojinjo.restaurant1;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class RestaurantActivity extends AppCompatActivity implements RestaurantDetail.OnTitleSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
=======

>>>>>>> 37f5f6d2e337e172149a4954dbb074719b289879
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
