package com.example.hojinjo.restaurant1;

import android.Manifest;
import android.animation.Animator;
import android.content.pm.PackageManager;
import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static java.lang.Double.parseDouble;

//지도 추가
public class RestaurantMap extends AppCompatActivity implements OnMapReadyCallback {
    private Activity activity;

    GoogleMap mGoogleMap;
    final String TAG = "AnimationTest";
    EditText edit;
    Address bestResult;
    String str;
    Double latitude, longitude;
    int request_code=1;
    SharedPreferences setting;
    public static final String PREFERENCES_GROUP = "LoginInfo";//키값=xml파일이름
    public static final String PREFERENCES_ATTR1 = "selected";//키값
    double distance;
    double meter;
    final private int REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION = 0;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mCurrentLocation;
    DBHelper rDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_map);
        setting = getSharedPreferences(PREFERENCES_GROUP, MODE_PRIVATE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (!checkLocationPermissions()) {
            requestLocationPermissions(REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION);
        } else {
            getLastLocation();
        }

        final Geocoder geocoder = new Geocoder(this);
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                //주소로부터 위치 얻기
                TextView txt = (TextView) findViewById(R.id.result);
                edit = (EditText) findViewById(R.id.edit_text);
                 str = edit.getText().toString();
                try {
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.KOREA);
                    List<Address> addresses = geocoder.getFromLocationName(str, 1);
                    if (addresses.size() > 0) {
                        bestResult = (Address) addresses.get(0);
                        latitude=bestResult.getLatitude();
                        longitude=bestResult.getLongitude();
                        txt.setText(String.format("[ %s , %s ]",
                                bestResult.getLatitude(),
                                bestResult.getLongitude()));
                    }
                } catch (IOException e) {
                    Log.e(getClass().toString(), "Failed in using Geocoder.", e);
                    return;
                }

                LatLng location = new LatLng(bestResult.getLatitude(), bestResult.getLongitude());
                rDbHelper=new DBHelper(getApplicationContext());
                Cursor c=rDbHelper.getLocation();
                c.moveToFirst();
                while(c.moveToNext()) {
                    if (latitude.equals(c.getString(2)) && longitude.equals(c.getString(3))) {
                        mGoogleMap.addMarker(
                                new MarkerOptions().
                                        position(location).
                                        title(str).
                                        icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).
                                        alpha(0.8f)
                                /*snippet("4호선")*/
                        );
                   /* mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                    mGoogleMap.setOnMarkerClickListener(new MyMarkerClickListener());*/
                    }
                }

                    mGoogleMap.addMarker(
                            new MarkerOptions().
                                    position(location).
                                    title(str).
                                    alpha(0.8f)
                    );
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                mGoogleMap.setOnMarkerClickListener(new MyMarkerClickListener());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);

        String text = setting.getString(PREFERENCES_ATTR1, "");

        if (text.equals("1km")) {
            menu.findItem(R.id.option1).setChecked(true);
        } else if (text.equals("2km")) {
            menu.findItem(R.id.option2).setChecked(true);
        } else if (text.equals("3km")) {
            menu.findItem(R.id.option3).setChecked(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /*액션아이템 액티비티 전환=동작*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_location:
                getAllMaker();
                ///////////////////////////////////눌렀을떄 마커뜨기
                return true;
            case R.id.option1:
                item.setChecked(true);
                getOnekm();
                /////////////////////////1km
                break;
            case R.id.option2:
                item.setChecked(true);
                getTwokm();
                /////////////////////////2km
                break;
            case R.id.option3:
                item.setChecked(true);
                getThreekm();
                /////////////////////////3km
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean checkLocationPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermissions(int requestCode) {
        ActivityCompat.requestPermissions(
                RestaurantMap.this,            // MainActivity 액티비티의 객체 인스턴스를 나타냄
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},        // 요청할 권한 목록을 설정한 String 배열
                requestCode    // 사용자 정의 int 상수. 권한 요청 결과를 받을 때
        );
    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        Task task = mFusedLocationClient.getLastLocation();       // Task<Location> 객체 반환
        task.addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    mCurrentLocation = location;
                    LatLng curlocation = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curlocation, 15));
                    //updateUI();
                } else
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.no_location_detected),
                            Toast.LENGTH_SHORT)
                            .show();
            }
        });
    }



        public void onMapReady (GoogleMap googleMap){
        mGoogleMap = googleMap;
    }


        class MyMarkerClickListener implements GoogleMap.OnMarkerClickListener {

            @Override
            public boolean onMarkerClick(Marker marker) {

                // 다이얼로그 바디
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(activity);
                // 다이얼로그 메세지
                alertdialog.setMessage("맛집을 등록하시겠습니까?");
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                // 확인버튼
                alertdialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edit = (EditText) findViewById(R.id.edit_text);
                        str = edit.getText().toString();
                        Intent toMain = new Intent(getApplicationContext(), MainActivity.class);
                        toMain.putExtra("address", str);/////////////////////////////////
                        toMain.putExtra("latitude", latitude.toString());
                        toMain.putExtra("longitude", longitude.toString());
                        //에딧텍스트창 스트링도 같이 넘김, 위도경도도 같이 넘거야함
                        startActivity(toMain);
                    }
                });

                // 취소버튼
                alertdialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                // 메인 다이얼로그 생성
                AlertDialog alert = alertdialog.create();

                // 타이틀
                alert.setTitle("맛집 등록");
                // 다이얼로그 보기
                alert.show();


                //  출처: http://taehyun71.tistory.com/4 [코딩하는 블로그]


                return false;
            }
        }

        public void getAllMaker(){
            rDbHelper=new DBHelper(getApplicationContext());
            Cursor c=rDbHelper.getLocation();
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    Log.i("RestaurantMap", "getLocation Lat=" + c.getString(1));
                    Log.i("RestaurantMap", "getLocation Lon=" + c.getString(2));
                    LatLng location = new LatLng(parseDouble(c.getString(1)), parseDouble(c.getString(2)));
                    mGoogleMap.addMarker(
                            new MarkerOptions().
                                    position(location).
                                    title(c.getString(1)).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).
                                    alpha(0.8f)
                    );
                }
            }
        }
        /*1km 반경*/
        public void getOnekm() {
            rDbHelper = new DBHelper(getApplicationContext());
            Cursor cur = rDbHelper.getLocation();
            Location baseLoc = new Location("base");
            baseLoc.setLatitude(mCurrentLocation.getLatitude());
            baseLoc.setLongitude(mCurrentLocation.getLongitude());

            Location limitLoc = new Location("limit");
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    limitLoc.setLatitude(parseDouble(cur.getString(1)));
                    limitLoc.setLongitude(parseDouble(cur.getString(2)));
                    LatLng location = new LatLng(parseDouble(cur.getString(1)), parseDouble(cur.getString(2)));
                    float distance = baseLoc.distanceTo(limitLoc);
                    if (distance < 1000) {
                        mGoogleMap.addMarker(
                                new MarkerOptions().
                                        position(location).
                                        title(cur.getString(1)).
                                        icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).
                                        alpha(0.8f)
                        );
                    }
                }
            }
        }

        public void getTwokm() {
        rDbHelper = new DBHelper(getApplicationContext());
        Cursor cur = rDbHelper.getLocation();
        Location baseLoc = new Location("base");
        baseLoc.setLatitude(mCurrentLocation.getLatitude());
        baseLoc.setLongitude(mCurrentLocation.getLongitude());

        Location limitLoc = new Location("limit");
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                limitLoc.setLatitude(parseDouble(cur.getString(1)));
                limitLoc.setLongitude(parseDouble(cur.getString(2)));
                LatLng location = new LatLng(parseDouble(cur.getString(1)), parseDouble(cur.getString(2)));
                float distance = baseLoc.distanceTo(limitLoc);
                if (distance < 2000) {
                    mGoogleMap.addMarker(
                            new MarkerOptions().
                                    position(location).
                                    title(cur.getString(1)).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).
                                    alpha(0.8f)
                    );
                }
            }
        }
    }
    public void getThreekm() {
        rDbHelper = new DBHelper(getApplicationContext());
        Cursor cur = rDbHelper.getLocation();
        Location baseLoc = new Location("base");
        baseLoc.setLatitude(mCurrentLocation.getLatitude());
        baseLoc.setLongitude(mCurrentLocation.getLongitude());

        Location limitLoc = new Location("limit");
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                limitLoc.setLatitude(parseDouble(cur.getString(1)));
                limitLoc.setLongitude(parseDouble(cur.getString(2)));
                LatLng location = new LatLng(parseDouble(cur.getString(1)), parseDouble(cur.getString(2)));
                float distance = baseLoc.distanceTo(limitLoc);
                if (distance < 3000) {
                    mGoogleMap.addMarker(
                            new MarkerOptions().
                                    position(location).
                                    title(cur.getString(1)).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).
                                    alpha(0.8f)
                    );
                }
            }
        }
    }

            //db의 위도, 경도 배열로 저장
          /*  Double[] latitude= new Double[cur.getCount()];
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    for(int i = 0; i<latitude.length ; i++){
                        latitude[i]= parseDouble(cur.getString(1));
                    }
                }
            }
            Double[] longitude= new Double[cur.getCount()];
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    for(int i = 0; i<latitude.length ; i++){
                        latitude[i]= parseDouble(cur.getString(2));
                    }
                }
            }*/

            /*Cursor cur2=rDbHelper.getLocation();
            if(cur.getCount()>0){
                while(cur.moveToNext()) {
                    //모든 등록된 행마다 distanceTo 검사(while) if 1000보다 작으면 마커 띄우기
                    baseLoc.setLatitude(parseDouble(cur.getString(1)));
                    baseLoc.setLongitude(parseDouble(cur.getString(2)));
                    if (cur2.getCount() > 0) {
                        while (cur2.moveToNext()) {//1번째와 나머지들 비교->2번째와 나머지들 비교
                            LatLng location2 = new LatLng(parseDouble(cur2.getString(1)), parseDouble(cur2.getString(2)));
                            limitLoc.setLatitude(parseDouble(cur2.getString(1)));
                            limitLoc.setLongitude(parseDouble(cur2.getString(2)));
                            distance = baseLoc.distanceTo(limitLoc);
                            if (distance < 1000) {
                                mGoogleMap.addMarker(
                                        new MarkerOptions().
                                                position(location2).
                                                title(cur.getString(1)).
                                                icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).
                                                alpha(0.8f)
                                );
                            }

                        }
                    }
                }
            }*/

      }
