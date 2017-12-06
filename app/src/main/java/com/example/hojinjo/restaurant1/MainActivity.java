package com.example.hojinjo.restaurant1;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Uri restUri;
    Uri uri;

    final String TAG = "AnimationTest";

    ImageView mFirework;
    ImageView mCountDown;
    int mScreenHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_r);

        mFirework = (ImageView) findViewById(R.id.fire);

        ImageButton cameraBtn = (ImageButton) findViewById(R.id.cameraButton);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        Button registerBtn = (Button) findViewById(R.id.registeration);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertRecord();
                String restimg = restUri.toString();
                Intent intent = new Intent(getApplicationContext(), RestaurantActivity.class);
                intent.putExtra("RESTIMG",restimg);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mScreenHeight = displaymetrics.heightPixels;

        startFireValuePropertyAnimation();
        // startFireObjectPropertyAnimation();
        startFireTweenAnimation();
    }

//    private void startCountDownFrameAnimation() {
//        mFirework.setBackgroundResource(R.anim.fire);
//        AnimationDrawable countdownAnim = (AnimationDrawable) mFirework.getBackground();
//        countdownAnim.start();
//    }

    private void startFireTweenAnimation(){
        Animation fire_anim= AnimationUtils.loadAnimation(this, R.anim.star);
        mFirework.startAnimation(fire_anim);
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
/*    private void startRocketPropertyAnimationByXML() {
        AnimatorSet rocketDogSet = new AnimatorSet();

        AnimatorSet rocketAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.fire);
        rocketAnimator.setTarget(mRocket);

        rocketAnimator.setStartDelay(2000);
        rocketAnimator.start();
        rocketAnimator.addListener(animatorListener);


    }*/

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
        // animatorSet.setStartDelay(2000);//2초후에 시작
        animatorSet.setDuration(2000);//2초동안 지속
        animatorSet.start();
        //필요x   animatorSet.addListener(animatorListener);

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



    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    String mPhotoFileName;
    File mPhotoFile;

    static final int REQUEST_IMAGE_CAPTURE = 2;

    public void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //1. 카메라 앱으로 찍은 이미지를 저장할 파일 객체 생성
            mPhotoFileName = "IMG"+currentDateFormat()+".jpg";
            mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);

            if (mPhotoFile !=null) {
                //2. 생성된 파일 객체에 대한 Uri 객체를 얻기
                restUri = FileProvider.getUriForFile(this, "com.example.hojinjo.restaurant1", mPhotoFile);

                //3. Uri 객체를 Extras를 통해 카메라 앱으로 전달
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,restUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else
                Toast.makeText(getApplicationContext(), "file null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (mPhotoFileName != null) {
                mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);
                uri = Uri.fromFile(mPhotoFile);
                ImageView imageView = (ImageView) findViewById(R.id.cameraButton);
                imageView.setImageURI(uri);
            } else
                Toast.makeText(getApplicationContext(), "mPhotoFile is null", Toast.LENGTH_SHORT).show();
        }
    }

    DBHelper rDbHelper;

    private void insertRecord() {
        rDbHelper = new DBHelper(this);
        ImageView restimg = (ImageView)findViewById(R.id.cameraButton);
        EditText name = (EditText)findViewById(R.id.edit_rname);
        EditText address = (EditText)findViewById(R.id.edit_address);
        EditText phone = (EditText)findViewById(R.id.edit_phone);

        long nOfRows = rDbHelper.insertUserByMethod(uri.toString(),name.getText().toString(),
                                                      address.getText().toString(),phone.getText().toString());
        if (nOfRows >0)
            Toast.makeText(this,nOfRows+" Record Inserted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"No Record Inserted", Toast.LENGTH_SHORT).show();
    }

}
