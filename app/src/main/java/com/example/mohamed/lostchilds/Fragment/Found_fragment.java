package com.example.mohamed.lostchilds.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed.lostchilds.Adapter.FoundViewHolder;
import com.example.mohamed.lostchilds.Adapter.LostViewHolder;
import com.example.mohamed.lostchilds.R;
import com.example.mohamed.lostchilds.View.Comments.Comments;
import com.example.mohamed.lostchilds.View.Map.MapsActivity;
import com.example.mohamed.lostchilds.View.Profile;
import com.example.mohamed.lostchilds.View.SignIn;
import com.example.mohamed.lostchilds.View.found.AddItem_Found;
import com.example.mohamed.lostchilds.common.Common;
import com.example.mohamed.lostchilds.model.FoundModel;
import com.example.mohamed.lostchilds.model.LostModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.turf.TurfConstants;
import com.mapbox.turf.TurfConversion;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.expressions.Expression.eq;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class Found_fragment extends Fragment {

    private static final int REQUEST_VIDEO_CODE = 1000;
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<FoundModel, FoundViewHolder> adapter;

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

        Mapbox.getInstance(getActivity(), getString(R.string.access_token));

        FacebookSdk.sdkInitialize(getContext().getApplicationContext());

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


        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(getActivity());


        return view;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void loadMenu() {

        adapter = new FirebaseRecyclerAdapter<FoundModel, FoundViewHolder>(FoundModel.class, R.layout.display_item__found, FoundViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(final FoundViewHolder viewHolder, FoundModel model, int position) {


                if (model.getPublisher_name().equals(Common.currentUser.getName())){

                    viewHolder.settings.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.settings.setVisibility(View.INVISIBLE);

                }
                viewHolder.username.setText(model.getPublisher_name());
                viewHolder.date.setText(model.getDate());
                viewHolder.childname.setText(model.getChild_name());
                viewHolder.phone_number.setText(model.getPhone());
                viewHolder.helper_name.setText(model.getHelper());
                viewHolder.description.setText(model.getDescription());
                viewHolder.age.setText(model.getAge());
                Picasso.get().load(model.getPublisher_image()).placeholder(R.drawable.ic_account_circle_black_24dp).into(viewHolder.user_img);
                Picasso.get().load(model.getChild_img()).placeholder(R.drawable.ic_account_circle_black_24dp).into(viewHolder.child_img);




                // shared link
//                viewHolder.share.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        //create call back
//                        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
//                            @Override
//                            public void onSuccess(Sharer.Result result) {
//                                Toast.makeText(getContext(), "share Sucessful", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onCancel() {
//                                Toast.makeText(getContext(), "share Canceled !!", Toast.LENGTH_SHORT).show();
//
//                            }
//
//                            @Override
//                            public void onError(FacebookException error) {
//                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//
//                        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
//                                .setQuote("Shared post")
//                                .setContentUrl(Uri.parse("https://youtube.com"))
//                                .build();
//
//                        if (ShareDialog.canShow(ShareLinkContent.class)) {
//                            shareDialog.show(shareLinkContent);
//                        }
//
//
////                        SharePhoto sharePhoto1 = new SharePhoto.Builder()
////                                .setBitmap()
////                                .build();
////
////
////                        ShareContent shareContent = new ShareMediaContent.Builder()
////                                .addMedium(sharePhoto1)
////                                   .build();
////
////                         shareDialog.show(shareContent, ShareDialog.Mode.AUTOMATIC);
//
//
//                    }
//                });

                ////////////////////////////////////////////////////////////////////////

                // shared photo
                viewHolder.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
//                        progressDialog.setMessage("please wait ....");
//                        progressDialog.show();



                        //create call back
                        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                            @Override
                            public void onSuccess(Sharer.Result result) {

//                                progressDialog.dismiss();

                                Toast.makeText(getContext(), "share Sucessful", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancel() {
//                                progressDialog.dismiss();

                                Toast.makeText(getContext(), "share Canceled !!", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onError(FacebookException error) {

//                                progressDialog.dismiss();
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

                        Picasso.get().
                                load(model.getChild_img()).
                                into(target);

                    }
                });


                ////////////////////////////////////////////////////////////////////////

                // shared video
//                viewHolder.share.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        //create call back
//                        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
//                            @Override
//                            public void onSuccess(Sharer.Result result) {
//                                Toast.makeText(getContext(), "share Sucessful", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onCancel() {
//                                Toast.makeText(getContext(), "share Canceled !!", Toast.LENGTH_SHORT).show();
//
//                            }
//
//                            @Override
//                            public void onError(FacebookException error) {
//                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//
//                        Intent intent = new Intent();
//                        intent.setType("video/*");
//                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                        startActivityForResult(Intent.createChooser(intent, "Selected video"), REQUEST_VIDEO_CODE);
//                    }
//                });


                ////////////////////////////////////////////////////////////////////////

                viewHolder.map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent = new Intent(getActivity(), MapsActivity.class);
                        intent.putExtra("Latitude", model.getLatidude());
                        intent.putExtra("Longitude", model.getLongitude());
                        startActivity(intent);


                    }
                });

                ////////////////////////////////////////////////////////////////////////

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

                ////////////////////////////////////////////////////////////////////////

                viewHolder.user_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), Profile.class);
                        intent.putExtra("name", model.getPublisher_name());
                        startActivity(intent);
                    }
                });

                ////////////////////////////////////////////////////////////////////////

                viewHolder.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), Comments.class);
                        intent.putExtra("postkey", adapter.getRef(position).getKey());
                        startActivity(intent);


                    }
                });


            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////


    // for videoshare
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == RESULT_OK) {
//            if (requestCode == REQUEST_VIDEO_CODE) {
//                Uri selectedvideo = data.getData();
//
//                ShareVideo shareVideo = new ShareVideo.Builder()
//                        .setLocalUrl(selectedvideo)
//                        .build();
//
//                ShareVideoContent shareVideoContent = new ShareVideoContent.Builder()
//                        .setContentTitle("shared video")
//                        .setContentDescription("important video")
//                        .setVideo(shareVideo)
//                        .build();
//
//                if (shareDialog.canShow(ShareVideoContent.class)) {
//                    shareDialog.show(shareVideoContent);
//                }
//            }
//        }
//    }
}