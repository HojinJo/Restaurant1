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

    static int index=-1;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public void setSelection(int i) { index = i; }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        ImageView ivimg = (ImageView) view.findViewById(R.id.imageView);
        TextView tvmenu = (TextView) view.findViewById(R.id.textView1);
        TextView tvprice = (TextView) view.findViewById(R.id.textView2);
        TextView score = (TextView) view.findViewById(R.id.textView6);//점수

        if(index>=0) {
            ivimg.setImageResource(Menu.IMAGE[index]);
            tvmenu.setText(Menu.MENU[index]);
            tvprice.setText(Menu.PRICE[index]);
            score.setText(Menu.SCORE[index]);
        }
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    return view;
    }



}
