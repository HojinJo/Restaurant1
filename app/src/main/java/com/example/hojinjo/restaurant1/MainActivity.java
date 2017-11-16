package com.example.hojinjo.restaurant1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements RestaurantDetail.OnTitleSelectedListener {

    private DBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_r);

        ImageButton cameraBtn = (ImageButton) findViewById(R.id.cameraButton);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakeVideoIntent();
            }
        });

        Button registerBtn = (Button) findViewById(R.id.registeration);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertRecord();
                Intent intent = new Intent(getApplicationContext(), RestaurantDetail.class);
                startActivity(intent);
            }
        });

    }

/*    final int REQUEST_CODE_READ_CONTACTS = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts();
            } else {
                Toast.makeText(getApplicationContext(), "READ_CONTACTS 접근 권한이 필요합니다", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    String mVideoFileName;
    File mVideoFile;

    static final int REQUEST_VIDEO_CAPTURE = 2;

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            //1. 카메라 앱으로 찍은 동영상을 저장할 파일 객체 생성
            mVideoFileName = "VIDEO"+currentDateFormat()+".mp4";
            mVideoFile = new File(getExternalFilesDir(Environment.DIRECTORY_MOVIES), mVideoFileName);

            if (mVideoFile != null) {
                //2. 생성된 파일 객체에 대한 Uri 객체를 얻기
                Uri videoUri = FileProvider.getUriForFile(this, "com.example.hojinjo.practice10", mVideoFile);

                //3. Uri 객체를 Extras를 통해 카메라 앱으로 전달
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }
        }
    }
    private void insertRecord() {
        EditText name = (EditText)findViewById(R.id.edit_name);
        EditText address = (EditText)findViewById(R.id.edit_address);
        EditText phone = (EditText)findViewById(R.id.edit_phone);

        long nOfRows = mDbHelper.insertUserByMethod(name.getText().toString(),address.getText().toString(),phone.getText().toString());
        if (nOfRows >0)
            Toast.makeText(this,nOfRows+" Record Inserted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"No Record Inserted", Toast.LENGTH_SHORT).show();
    }

///////////////////////////프레그먼트 화면에따라
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
