package com.example.hojinjo.restaurant1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RestaurantMap extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_map);
    }

    public void onMapReady(GoogleMap googleMap) {
        LatLng hansung = new LatLng(37.5817891, 127.008175);
        googleMap.addMarker(new MarkerOptions().position(hansung).title("한성대학교"));
        // move the camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(hansung));

    }
}
