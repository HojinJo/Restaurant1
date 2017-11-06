package com.example.hojinjo.restaurant1;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        DetailsFragment details = new DetailsFragment();
        details.setSelection(getIntent().getIntExtra("index",-1));
        getSupportFragmentManager().beginTransaction().replace(R.id.details, details).commit();

        ImageView ivimg = (ImageView)findViewById(R.id.imageView);

        TextView tvmenu = (TextView)findViewById(R.id.textView1);

        TextView tvprice = (TextView)findViewById(R.id.textView2);

        TextView score=(TextView)findViewById(R.id.textView6);//점수

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();

        score.setText(intent.getStringExtra("score"));
        tvmenu.setText(intent.getStringExtra("menu"));
        tvprice.setText(intent.getStringExtra("price"));
        ivimg.setImageResource(intent.getIntExtra("img",0));

    }
}
