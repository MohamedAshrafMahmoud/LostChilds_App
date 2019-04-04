package com.example.mohamed.lostchilds.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mohamed.lostchilds.Adapter.FoundViewHolder;
import com.example.mohamed.lostchilds.Adapter.LostViewHolder;
import com.example.mohamed.lostchilds.R;
import com.example.mohamed.lostchilds.View.found.AddItem_Found;
import com.example.mohamed.lostchilds.common.Common;
import com.example.mohamed.lostchilds.model.FoundModel;
import com.example.mohamed.lostchilds.model.LostModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;

public class Found_fragment extends Fragment {

    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<FoundModel, FoundViewHolder> adapter;

    FloatingActionButton navigation;
    RecyclerView recyclerView;


    public Found_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_found, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference(Common.FoundChildCategory);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerhome);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadMenu();

        navigation = (FloatingActionButton) view.findViewById(R.id.fab);

        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddItem_Found.class));
            }
        });

        return view;
    }

    private void loadMenu() {

        adapter = new FirebaseRecyclerAdapter<FoundModel, FoundViewHolder>(FoundModel.class, R.layout.display_item__found, FoundViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(final FoundViewHolder viewHolder, FoundModel model, int position) {
                viewHolder.username.setText(Common.currentUser.getName());
                viewHolder.date.setText(model.getDate());
                viewHolder.childname.setText(model.getChild_name());
                viewHolder.phone_number.setText(model.getPhone());
                viewHolder.helper_name.setText(model.getHelper());
                viewHolder.description.setText(model.getDescription());
                Picasso.get().load(model.getChild_img()).into(viewHolder.child_img);

                viewHolder.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                    }
                });
            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

}