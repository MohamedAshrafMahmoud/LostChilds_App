package com.example.mohamed.lostchilds.View.Lost;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mohamed.lostchilds.R;

public class Lost extends Fragment {

    FloatingActionButton navigation;
    public Lost() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_lost, container, false);

        navigation=(FloatingActionButton)view.findViewById(R.id.fab);

        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity( new Intent(getContext(),AddItem_Lost.class));
            }
        });

        return view;
    }

}