package com.example.mohamed.lostchilds.View.Lost;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mohamed.lostchilds.R;

public class AddItem_Lost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item__lost);
    }

    public void goback(View view) {
        startActivity( new Intent(AddItem_Lost.this,Lost.class));

    }
}
