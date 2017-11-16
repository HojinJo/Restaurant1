package com.example.hojinjo.restaurant1;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class RegisterR extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_r);

//        Button cameraBtn = (Button) findViewById(R.id.cameraButton);
//        cameraBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dispatchTakeVideoIntent();
//            }
//        });
    }

//    String mVideoFileName;
//
//    private void dispatchTakeVideoIntent() {
//        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//
//        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
//            //1. 카메라 앱으로 찍은 동영상을 저장할 파일 객체 생성
//            mVideoFileName = "VIDEO"+currentDateFormat()+".mp4";
//            File destination = new File(getExternalFilesDir(Environment.DIRECTORY_MOVIES), mVideoFileName);
//
//            if (destination != null) {
//                //2. 생성된 파일 객체에 대한 Uri 객체를 얻기
//                Uri videoUri = FileProvider.getUriForFile(this, "com.example.hojinjo.practice10", destination);
//
//                //3. Uri 객체를 Extras를 통해 카메라 앱으로 전달
//                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
//                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
//            }
//        }
//    }

}
