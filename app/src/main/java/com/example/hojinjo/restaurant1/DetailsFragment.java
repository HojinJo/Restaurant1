package com.example.hojinjo.restaurant1;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    int index;
    MDBHelper menudbhelper;
    DBHelper restdbhelper;
    Cursor c;
    Cursor cursor;
    Cursor menucursor;

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

        c=restdbhelper.getRestaurantIDByName(rest_name);
        if (c.moveToNext()) {
            cursor = menudbhelper.getAllMenusByID(c.getInt(0));
            if (cursor.moveToNext())
                menucursor=menudbhelper.getOneMenuByID(cursor.getString(index));
        }

        if (menucursor != null) {
            if (menucursor.moveToNext()) {
                ImageView menuImage = view.findViewById(R.id.imageView);
                menuImage.setImageURI(Uri.parse(menucursor.getString(1)));

                TextView description = (TextView) view.findViewById(R.id.textView6);
                description.setText(menucursor.getString(4));//맞는지모르겠음..

                TextView menuName = (TextView) view.findViewById(R.id.textView1);
                menuName.setText(menucursor.getString(2));

                TextView menuPrice = (TextView) view.findViewById(R.id.textView2);
                menuPrice.setText(menucursor.getString(3));
            }
        }
        return view;
    }

}
