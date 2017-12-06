package com.example.hojinjo.restaurant1;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.app.Activity;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
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

//지도 추가
public class RestaurantMap extends AppCompatActivity implements OnMapReadyCallback {
    private Activity activity;

    GoogleMap mGoogleMap;
    final String TAG = "AnimationTest";
    EditText edit;
    Address bestResult;
    ImageView mFirework;
    int mScreenHeight;

    SharedPreferences setting;
    public static final String PREFERENCES_GROUP = "LoginInfo";//키값=xml파일이름
    public static final String PREFERENCES_ATTR1 = "selected";//키값

    final private int REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION = 0;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mCurrentLocation;

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

        mFirework = (ImageView) findViewById(R.id.fire);

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
                String str = edit.getText().toString();
                try {
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.KOREA);
                    List<Address> addresses = geocoder.getFromLocationName(str, 1);
                    if (addresses.size() > 0) {
                        bestResult = (Address) addresses.get(0);

                        txt.setText(String.format("[ %s , %s ]",
                                bestResult.getLatitude(),
                                bestResult.getLongitude()));
                    }
                } catch (IOException e) {
                    Log.e(getClass().toString(), "Failed in using Geocoder.", e);
                    return;
                }

                LatLng location = new LatLng(bestResult.getLatitude(), bestResult.getLongitude());
                mGoogleMap.addMarker(
                        new MarkerOptions().
                                position(location).
                                title(str).
                                alpha(0.8f)/*.
                                snippet("4호선")*/
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
                ///////////////////////////////////눌렀을떄 마커뜨기
                return true;
            case R.id.option1:
                item.setChecked(true);
                saveName("1km");
                /////////////////////////1km
                break;
            case R.id.option2:
                item.setChecked(true);
                saveName("2km");
                /////////////////////////2km
                break;
            case R.id.option3:
                item.setChecked(true);
                saveName("3km");
                /////////////////////////3km
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveName(String text) {
        SharedPreferences.Editor editor = setting.edit();
        editor.putString(PREFERENCES_ATTR1, text);
        editor.commit();

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

   /* final Geocoder geocoder = new Geocoder(this);
    Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View view){
        //주소로부터 위치 얻기
        TextView txt = (TextView) findViewById(R.id.result);
        edit = (EditText) findViewById(R.id.edit_text);
        String str = edit.getText().toString();
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.KOREA);
            List<Address> addresses = geocoder.getFromLocationName(str, 1);
            if (addresses.size() > 0) {
                bestResult = (Address) addresses.get(0);

                txt.setText(String.format("[ %s , %s ]",
                        bestResult.getLatitude(),
                        bestResult.getLongitude()));
            }
        } catch (IOException e) {
            Log.e(getClass().toString(), "Failed in using Geocoder.", e);
            return;
        }

        LatLng location = new LatLng(bestResult.getLatitude(), bestResult.getLongitude());
        mGoogleMap.addMarker(
                new MarkerOptions().
                        position(location).
                        title(str).
                        alpha(0.8f)*//*.
                                snippet("4호선")*//*
        );
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

    @Override
    protected void onResume() {
        super.onResume();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mScreenHeight = displaymetrics.heightPixels;

        startFireObjectPropertyAnimation();
    }

    //objectanimator
    private void startFireObjectPropertyAnimation() {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mFirework,//타켓
                "alpha",//변화시킬 프로퍼티
                1, 0);//값의 범위
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mFirework,"scaleX",0,1.0f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(mFirework,"scaleY",0,1.0f);

        AnimatorSet animatorSet=new AnimatorSet();

        AnimatorSet scaleanimatorset = new AnimatorSet();
        scaleanimatorset.playTogether(scaleXAnimator,scaleYAnimator);//두개는 동시에
        animatorSet.play(alphaAnimator).after(scaleanimatorset);

        animatorSet.setDuration(2000);//2초 동안 수행

        animatorSet.start();

    }

    private void startFireValuePropertyAnimation() {
        ValueAnimator alphaAnimator = ValueAnimator.ofFloat(1, 0);//1~0
        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {//값이 변경될 때마다 호출
            public void onAnimationUpdate(ValueAnimator valueAnimator){
                float value = (float) valueAnimator.getAnimatedValue();//애니메이션 값 획득
                mFirework.setAlpha(value);//값 적용
            }
        });

        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                mFirework.setScaleX(value);
                mFirework.setScaleY(value);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());//가속 후 감속
        animatorSet.play(alphaAnimator).after(scaleAnimator); //scale alpha 순서
        animatorSet.setDuration(2000);//2초동안 지속
        animatorSet.start();


    }


    Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animator) {
            Log.i(TAG, "onAnimationStart");
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            Log.i(TAG, "onAnimationEnd");
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            Log.i(TAG, "onAnimationCancel");
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
            Log.i(TAG, "onAnimationRepeat");
        }
    };*/


    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap=googleMap;
        LatLng hansung = new LatLng(37.5817891, 127.008175);
        googleMap.addMarker(new MarkerOptions().position(hansung).title("한성대학교"));
        // move the camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(hansung));
    }


    class MyMarkerClickListener implements GoogleMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {

            // 다이얼로그 바디
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(activity);
            // 다이얼로그 메세지
            alertdialog.setMessage("기본 다이얼로그 입니다.");

            // 확인버튼
            alertdialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(activity, MainActivity.class);
                    startActivity(intent);
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
        /*    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

            // 제목셋팅
            alertDialogBuilder.setTitle("맛집 등록");

            // AlertDialog 셋팅
            alertDialogBuilder
                    .setMessage("새로운 맛집으로 등록하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("아니요",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    dialog.cancel();//다이얼로그 취소
                                }
                            })
                    .setNegativeButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                  Intent intent = new Intent(activity, MainActivity.class);
                                  startActivity(intent);
                                }
                            });

            // 다이얼로그 생성
            AlertDialog alertDialog = alertDialogBuilder.create();

            // 다이얼로그 보여주기
            alertDialog.show();*/

            return false;
        }
    }
}
