package com.example.mohamed.lostchilds.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohamed.lostchilds.Fragment.Lost_fragment;
import com.example.mohamed.lostchilds.R;
import com.example.mohamed.lostchilds.Fragment.Found_fragment;
import com.example.mohamed.lostchilds.View.news.News;
import com.example.mohamed.lostchilds.Adapter.ViewPagerAdapter;
import com.example.mohamed.lostchilds.common.Common;
import com.example.mohamed.lostchilds.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class Main extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    //This is our viewPager
    private ViewPager viewPager;


    //Fragments
    private Toolbar mTopToolbar;
    ImageView main_img_profile;
    TextView main_username;

    Found_fragment found;
    News news;
    Lost_fragment lost;
    MenuItem prevMenuItem;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mTopToolbar = findViewById(R.id.toolbar);
        main_img_profile=findViewById(R.id.main_img_profile);
        main_username=findViewById(R.id.main_username);
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("User");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait ....");

        savedInstanceState=getIntent().getExtras();
        GetImage_Profile();

        setSupportActionBar(mTopToolbar);
        mTopToolbar.setTitleTextColor(getResources().getColor(R.color.mapboxWhite));
        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //Initializing the bottomNavigationView
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_news:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.navigation_found:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.navigation_lost:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        setupViewPager(viewPager);


        main_img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, Profile.class);
                intent.putExtra("name",Common.currentUser.getName());
                startActivity(intent);            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        news = new News();
        found = new Found_fragment();
        lost = new Lost_fragment();
        adapter.addFragment(news);
        adapter.addFragment(found);
        adapter.addFragment(lost);
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout:

                startActivity(new Intent(Main.this, SignIn.class));
                return true;

            case R.id.profile:
                Intent intent = new Intent(this, Profile.class);
                intent.putExtra("name",Common.currentUser.getName());
                startActivity(intent);
                return true;

        }

        return false;
    }

    private void GetImage_Profile(){
        progressDialog.show();

        databaseReference.child(Common.currentUser.getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Common.currentUser_image=dataSnapshot.child("p_image").getValue().toString();
                main_username.setText(Common.currentUser.getName());

                Picasso.get().load(dataSnapshot.child("p_image").getValue().toString()).placeholder(R.drawable.ic_account_circle_black_24dp).into(main_img_profile);



                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}