package com.example.hojinjo.restaurant1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterM extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_m);

        Button btn=(Button)findViewById(R.id.registerMenu);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                     Intent intent=new Intent(getApplicationContext(), RestaurantDetail.class);
                     startActivity(intent);
                    }
                });
    }



}
