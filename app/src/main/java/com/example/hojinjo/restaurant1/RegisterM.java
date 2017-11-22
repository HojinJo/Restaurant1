package com.example.hojinjo.restaurant1;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterM extends AppCompatActivity {

    EditText mName;
    EditText mPrice;
    EditText mDesc;
    String mPhotoFileName;
    File mPhotoFile;
    Cursor c;
    Uri imageUri;
    String restid;
    final int REQUEST_CODE_READ_CONTACTS = 1;
    MDBHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_m);

        Intent it = getIntent();
        restid=""+it.getIntExtra("restaurant_id",0);

        Log.i("Restaurant1","RegistM:onCreate():restaurant_id="+restid);

        ImageButton camera = (ImageButton) findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dispatchTakePictureIntent();
            }
        });

        Button btn = (Button) findViewById(R.id.registerMenu);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                insertRecord();

                Intent intent = new Intent(getApplicationContext(), RestaurantActivity.class);
                intent.putExtra("IMAGEURI", imageUri.toString());//URI 보내기...
                startActivity(intent);
//
//                mName= (EditText)findViewById(R.id.edit_name);
//                mPrice = (EditText)findViewById(R.id.edit_price);
//                mDesc = (EditText)findViewById(R.id.edit_menu);
            }
        });
    }

    private void insertRecord() {
        EditText name = (EditText)findViewById(R.id.edit_name);
        EditText price = (EditText)findViewById(R.id.edit_price);
        EditText menu = (EditText)findViewById(R.id.edit_menu);
        ImageView menuimg=(ImageView)findViewById(R.id.menuimg);
        mDbHelper = new MDBHelper(this);

        long nOfRows = mDbHelper.insertUserByMethod(imageUri.toString(), name.getText().toString(), price.getText().toString(), menu.getText().toString(), restid);

        if (nOfRows >0) {
            Toast.makeText(this, nOfRows + " Record Inserted", Toast.LENGTH_SHORT).show();
        }else
            {Toast.makeText(this,"No Record Inserted", Toast.LENGTH_SHORT).show();
            }
    }

//// 카메라 앱 실행
    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }


    static final int REQUEST_IMAGE_CAPTURE = 2;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //1. 카메라 앱으로 찍은 이미지를 저장할 파일 객체 생성
            mPhotoFileName = "IMG"+currentDateFormat()+".jpg";
            mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);

            if (mPhotoFile !=null) {
                //2. 생성된 파일 객체에 대한 Uri 객체를 얻기
                imageUri = FileProvider.getUriForFile(this, "com.example.hojinjo.restaurant1", mPhotoFile);
                 //imageUri를 전역변수로 바꿨음
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
                Uri uri = Uri.fromFile(mPhotoFile);
                ImageView imageView = (ImageView) findViewById(R.id.camera);
                imageView.setImageURI(uri);
            } else
                Toast.makeText(getApplicationContext(), "mPhotoFile is null", Toast.LENGTH_SHORT).show();
        }
    }

}
