package com.example.hojinjo.restaurant1;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    int index;
    MDBHelper menudbhelper;
    DBHelper restdbhelper;
    Cursor restc;
    public DetailsFragment() {
        // Required empty public constructor
    }

    public void setSelection(int i) {
        index = i;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_details, container, false);

        menudbhelper=new MDBHelper(getContext());
        restdbhelper = new DBHelper(getContext());
        Cursor cursor=restdbhelper.getRestaurantIDByName(restc.getString(1));//어느 레스토랑인지 알아내기
        cursor=menudbhelper.getOneMenuByName(cursor.getString());

        ImageView menuImage = view.findViewById(R.id.imageView);
        menuImage.setImageURI();
        TextView menuName= (TextView)view.findViewById(R.id.textView1);
        menuName.setText();
        TextView menuPrice = (TextView)view.findViewById(R.id.textView2);
        menuPrice.setText();
        TextView description = (TextView)view.findViewById(R.id.textView6);
        description.setText();
        return view;
    }

    private void getFood(){

    }
}
