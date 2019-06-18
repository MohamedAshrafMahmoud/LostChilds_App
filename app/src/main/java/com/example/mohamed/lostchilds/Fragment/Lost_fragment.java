package com.example.mohamed.lostchilds.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.mohamed.lostchilds.View.Comments.Comments;
import com.example.mohamed.lostchilds.View.Lost.AddItem_Lost;
import com.example.mohamed.lostchilds.View.Profile;
import com.example.mohamed.lostchilds.common.Common;
import com.example.mohamed.lostchilds.model.LostModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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
                viewHolder.username.setText(model.getPublisher_name());
                viewHolder.date.setText(model.getDate());
                viewHolder.childname.setText(model.getChild_name());
                viewHolder.phone_number.setText(model.getPhone());
                viewHolder.adress.setText(model.getAdress());
                viewHolder.description.setText(model.getDescription());
                Picasso.get().load(model.getPublisher_image()).into(viewHolder.user_img);
                Picasso.get().load(model.getChild_img()).into(viewHolder.child_img);


                viewHolder.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), Comments.class);
                        intent.putExtra("postkey",adapter.getRef(position).getKey());
                        startActivity(intent);




                    }
                });

                viewHolder.user_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), Profile.class);
                        intent.putExtra("name",model.getPublisher_name());
                        startActivity(intent);                     }
                });

                viewHolder.settings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                        builder.setMessage("Do you want to delet this post?");
                        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Query applesQuery = databaseReference.child(adapter.getRef(position).getKey());

                                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                            appleSnapshot.getRef().removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builder.show();

                    }
                });



            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

}