package com.example.hojinjo.restaurant1;

import android.animation.Animator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Animation extends AppCompatActivity {
    ImageView hamburger;
    ImageView cola;
    ImageView pizza;
    int mScreenHeight;
    Intent toMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        hamburger = (ImageView) findViewById(R.id.hamburger);
        cola=(ImageView)findViewById(R.id.cola);
        pizza=(ImageView)findViewById(R.id.pizza);

    }

    @Override
    protected void onResume () {
        super.onResume();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mScreenHeight = displaymetrics.heightPixels;
        //  startFireValuePropertyAnimation();
        //startFireObjectPropertyAnimation();
        startTweenAnimation();//트윈 에니메이션 사용

    }
    android.view.animation.Animation.AnimationListener animationListener = new android.view.animation.Animation.AnimationListener() {
        @Override
        public void onAnimationStart(android.view.animation.Animation animation) {

        }

        @Override
        public void onAnimationEnd(android.view.animation.Animation animation) {
            finish();
            startActivity(new Intent(getApplicationContext(), RestaurantMap.class));
        }

        @Override
        public void onAnimationRepeat(android.view.animation.Animation animation) {

        }
    };

    /*Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            finish();
            startActivity(new Intent(getApplicationContext(), RestaurantMap.class));
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };*/

    private void startTweenAnimation() {
        android.view.animation.Animation animation = AnimationUtils.loadAnimation(this, R.anim.star);
        hamburger.startAnimation(animation);
        cola.startAnimation(animation);
        pizza.startAnimation(animation);
        animation.setAnimationListener(animationListener);
    }


}
