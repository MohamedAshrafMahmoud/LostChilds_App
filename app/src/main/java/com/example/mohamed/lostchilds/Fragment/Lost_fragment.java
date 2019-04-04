package com.example.mohamed.lostchilds.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mohamed.lostchilds.Adapter.LostViewHolder;
import com.example.mohamed.lostchilds.R;
import com.example.mohamed.lostchilds.View.Lost.AddItem_Lost;
import com.example.mohamed.lostchilds.common.Common;
import com.example.mohamed.lostchilds.model.LostModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Lost_fragment extends Fragment {

    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<LostModel, LostViewHolder> adapter;

    FloatingActionButton navigation;
    RecyclerView recyclerView;

    public Lost_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lost, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference(Common.LostChildCategory);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerhome);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadMenu();

        navigation = (FloatingActionButton) view.findViewById(R.id.fab);

        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddItem_Lost.class));
            }
        });

        return view;
    }

    private void loadMenu() {

        adapter = new FirebaseRecyclerAdapter<LostModel, LostViewHolder>(LostModel.class, R.layout.display_item__lost, LostViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(LostViewHolder viewHolder, LostModel model, int position) {
                viewHolder.username.setText(Common.currentUser.getName());
                viewHolder.date.setText(model.getDate());
                viewHolder.childname.setText(model.getChild_name());
                viewHolder.phone_number.setText(model.getPhone());
                viewHolder.adress.setText(model.getAdress());
                viewHolder.description.setText(model.getDescription());
                Picasso.get().load(model.getChild_img()).into(viewHolder.child_img);
            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

}