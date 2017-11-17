package com.example.hojinjo.restaurant1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        /*권환 여부 확인*/
        if (ContextCompat.checkSelfPermission(RegisterM.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) { // 권한이 없을 경우
            ActivityCompat.requestPermissions(RegisterM.this,
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        } else // 권한 있다면 해당 데이터나 장치에 접근!
            getContacts();
        Button btn=(Button)findViewById(R.id.registerMenu);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                     Intent intent=new Intent(getApplicationContext(), RestaurantDetail.class);
                     startActivity(intent);

                     insertRecord();
                    }
                });



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

        String[] contactsColumns = { // 쿼리결과인 Cursor 객체로부터 출력할 열들
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

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

}
