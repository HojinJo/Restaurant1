package com.example.hojinjo.restaurant1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterM extends AppCompatActivity {

    EditText mName;
    EditText mPrice;
    EditText mMenu;
    Cursor c;
    final int REQUEST_CODE_READ_CONTACTS = 1;
    private MDBHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_m);

        ImageButton camera=(ImageButton)findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        Button btn=(Button)findViewById(R.id.registerMenu);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        insertRecord();
                     Intent intent=new Intent(getApplicationContext(), RestaurantActivity.class);
                     startActivity(intent);


                    }
                });


                  /*권환 여부 확인*/
        if (ContextCompat.checkSelfPermission(RegisterM.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) { // 권한이 없을 경우
            ActivityCompat.requestPermissions(RegisterM.this,
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        } else // 권한 있다면 해당 데이터나 장치에 접근!
            getContacts();

        mName= (EditText)findViewById(R.id.edit_name);
        mPrice = (EditText)findViewById(R.id.edit_price);
        mMenu= (EditText)findViewById(R.id.edit_menu);

        mDbHelper = new MDBHelper(this);
    }


    /*쿼리구성*/
    private void getContacts() {
        String[] projection = {//반환할 열들
                mName.getText().toString(),
                mPrice.getText().toString(),
                mMenu.getText().toString()
        };
       /* *//*3.1*//*
        String selectionClause = ContactsContract.CommonDataKinds.Phone.TYPE + " = ? ";
        // 연락처 전화번호 타입에 따른 행 선택을 위한 선택 절
        String[] selectionArgs = {""+ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE};
        // 전화번호 타입이 'MOBILE'인 것을 지정*/

        c=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                null,
                null,
                null);//연락처 제공자에 저장된 이름과 전화번호를 읽기

        /*String[] contactsColumns = { // 쿼리결과인 Cursor 객체로부터 출력할 열들
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };*/

        /*3.1, 3.2- sqlite에 저장하는 코드, 새로운거 추가하는코드*/
        while(c.moveToNext()) {
            mDbHelper.insertUserByMethod(c.getString(0), c.getString(1), c.getString(2));
        }}

    private void insertRecord() {
        EditText name = (EditText)findViewById(R.id.edit_name);
        EditText price = (EditText)findViewById(R.id.edit_price);
        EditText menu = (EditText)findViewById(R.id.edit_menu);

        long nOfRows = mDbHelper.insertUserByMethod(name.getText().toString(),price.getText().toString(), menu.getText().toString());
        if (nOfRows >0)
            Toast.makeText(this,nOfRows+" Record Inserted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"No Record Inserted", Toast.LENGTH_SHORT).show();
    }

//// 카메라 앱 실행
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
                Uri uri = Uri.fromFile(mPhotoFile);
                ImageView imageView = (ImageView) findViewById(R.id.camera);
                imageView.setImageURI(uri);
            } else
                Toast.makeText(getApplicationContext(), "mPhotoFile is null", Toast.LENGTH_SHORT).show();
        }
    }

}
