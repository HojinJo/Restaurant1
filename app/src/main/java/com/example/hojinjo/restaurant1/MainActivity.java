package com.example.hojinjo.restaurant1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
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
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_r);

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
                Intent intent = new Intent(getApplicationContext(), RestaurantActivity.class);
                intent.putExtra("RESTIMG",uri.toString());
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

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //1. 카메라 앱으로 찍은 이미지를 저장할 파일 객체 생성
            mPhotoFileName = "IMG"+currentDateFormat()+".jpg";
            mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);

            if (mPhotoFile !=null) {
                //2. 생성된 파일 객체에 대한 Uri 객체를 얻기
                Uri imageUri = FileProvider.getUriForFile(this, "com.example.hojinjo.restaurant1", mPhotoFile);

                //3. Uri 객체를 Extras를 통해 카메라 앱으로 전달
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
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

        long nOfRows = rDbHelper.insertUserByMethod(restimg.getResources().toString(),name.getText().toString(),address.getText().toString(),phone.getText().toString());
        if (nOfRows >0)
            Toast.makeText(this,nOfRows+" Record Inserted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"No Record Inserted", Toast.LENGTH_SHORT).show();

       /* Cursor cursor=rDbHelper.getRestaurantIDByName(name.getText().toString());  //rDbHelper.getRestaurantIDByName(name.getText().toString()) --> id
            if(cursor.moveToNext()){
                Intent intentid = new Intent(getApplicationContext(), RegisterM.class);
                intentid.putExtra("RESTID",cursor.getInt(0) );  //커서존재하는지 다음으로
                startActivity(intentid);
        }//아이템으로 이사*/
    }

}
