package com.example.hojinjo.restaurant1;


import android.content.Intent;
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
        TextView tv1 = (TextView)view.findViewById(R.id.textView1);
        TextView tv2 = (TextView)view.findViewById(R.id.textView2);
        TextView tv3 = (TextView)view.findViewById(R.id.textView5);
        TextView tv4 = (TextView)view.findViewById(R.id.textView6);

        ImageView image = view.findViewById(R.id.imageView);

        tv1.setText(Menu.MENU[index]);
        tv2.setText(Menu.PRICE[index]);
        tv4.setText(Menu.SCORE[index]);
        image.setImageResource(Menu.IMAGE[index]);

        return view;
    }
}
