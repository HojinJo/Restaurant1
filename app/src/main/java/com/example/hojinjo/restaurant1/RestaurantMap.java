package com.example.hojinjo.restaurant1;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//지도 추가
public class RestaurantMap extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    final String TAG = "AnimationTest";
    EditText edit;
    Address bestResult;
    ImageView mFirework;
    int mScreenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        mFirework = (ImageView) findViewById(R.id.fire);

        final Geocoder geocoder = new Geocoder(this);
        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
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
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));

            }
        });
            }
      
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
    };


    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap=googleMap;
       /* LatLng hansung = new LatLng(37.5817891, 127.008175);
        googleMap.addMarker(new MarkerOptions().position(hansung).title("한성대학교"));
        // move the camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(hansung));*/

    }
}
