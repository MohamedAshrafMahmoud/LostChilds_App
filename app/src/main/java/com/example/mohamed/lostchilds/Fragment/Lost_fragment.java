package com.example.mohamed.lostchilds.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mohamed.lostchilds.Adapter.LostViewHolder;
import com.example.mohamed.lostchilds.R;
import com.example.mohamed.lostchilds.View.Comments.Comments;
import com.example.mohamed.lostchilds.View.Lost.AddItem_Lost;
import com.example.mohamed.lostchilds.View.Profile;
import com.example.mohamed.lostchilds.common.Common;
import com.example.mohamed.lostchilds.model.LostModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class Lost_fragment extends Fragment {

    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<LostModel, LostViewHolder> adapter;

    FloatingActionButton navigation;
    RecyclerView recyclerView;


    CallbackManager callbackManager;
    ShareDialog shareDialog;


    // for facebook image share
    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto sharePhoto = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();

            if (ShareDialog.canShow(SharePhotoContent.class)) {

                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(sharePhoto)
                        .build();
//                shareDialog.show(content);
                shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);

            }
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lost, container, false);

        FacebookSdk.sdkInitialize(getContext().getApplicationContext());


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


        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(getActivity());


        return view;

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    private void loadMenu() {

        adapter = new FirebaseRecyclerAdapter<LostModel, LostViewHolder>(LostModel.class, R.layout.display_item__lost, LostViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(LostViewHolder viewHolder, LostModel model, int position) {
                viewHolder.username.setText(model.getPublisher_name());
                viewHolder.date.setText(model.getDate());
                viewHolder.childname.setText(model.getChild_name());
                viewHolder.phone_number.setText(model.getPhone());
                viewHolder.adress.setText(model.getAdress());
                viewHolder.age.setText(model.getAge());
                viewHolder.description.setText(model.getDescription());
                Picasso.get().load(model.getPublisher_image()).into(viewHolder.user_img);
                Picasso.get().load(model.getChild_img()).into(viewHolder.child_img);

////////////////////////////////////////////////////////////////////////////////////////////////////

                viewHolder.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), Comments.class);
                        intent.putExtra("postkey", adapter.getRef(position).getKey());
                        startActivity(intent);


                    }
                });

////////////////////////////////////////////////////////////////////////////////////////////////////
                // shared photo
                viewHolder.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //create call back
                        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                            @Override
                            public void onSuccess(Sharer.Result result) {
                                Toast.makeText(getContext(), "share Sucessful", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancel() {
                                Toast.makeText(getContext(), "share Canceled !!", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onError(FacebookException error) {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

                        Picasso.get().
                                load(model.getChild_img()).
                                into(target);
//
//

                    }
                });

////////////////////////////////////////////////////////////////////////////////////////////////////

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
////////////////////////////////////////////////////////////////////////////////////////////////////

                viewHolder.user_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), Profile.class);
                        intent.putExtra("name", model.getPublisher_name());
                        startActivity(intent);
                    }
                });
            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

}