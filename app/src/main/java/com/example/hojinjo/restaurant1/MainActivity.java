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
    String restAddress, restLatitude, restLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_r);

       /* Intent fromMap=getIntent();
        restAddress = fromMap.getStringExtra("address");*/
        EditText address = (EditText)findViewById(R.id.edit_address);
        rDbHelper = new DBHelper(this);
        Intent fromMap=getIntent();
        restAddress = fromMap.getStringExtra("address");//받은 주소 스트링
        restLatitude=fromMap.getStringExtra("latitude");//받은 위도
        restLongitude=fromMap.getStringExtra("longitude");//받은 경도

        Log.i("MainActivity", "getFromMapIntent=" + restAddress);
        address.setText(restAddress);//주소스트링 설정

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

        ImageView restimg = (ImageView)findViewById(R.id.cameraButton);
        EditText name = (EditText)findViewById(R.id.edit_rname);
        EditText address = (EditText)findViewById(R.id.edit_address);
        EditText phone = (EditText)findViewById(R.id.edit_phone);


        long nOfRows = rDbHelper.insertUserByMethod(uri.toString(),name.getText().toString(),
                                                      address.getText().toString(),phone.getText().toString(), restLatitude, restLongitude);
        if (nOfRows >0)
            Toast.makeText(this,nOfRows+" Record Inserted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"No Record Inserted", Toast.LENGTH_SHORT).show();
    }

}
