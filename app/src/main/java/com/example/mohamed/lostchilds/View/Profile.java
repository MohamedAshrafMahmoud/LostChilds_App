package com.example.mohamed.lostchilds.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed.lostchilds.R;
import com.example.mohamed.lostchilds.common.Common;
import com.example.mohamed.lostchilds.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {

    TextView Name,Phone,Email,Password;
    ImageView _Image;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    LinearLayout genderLayout;

    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("User");
        progressDialog = new ProgressDialog(Profile.this);
        progressDialog.setMessage("please wait ....");
        savedInstanceState=getIntent().getExtras();
        username=savedInstanceState.getString("name");
        initiview();
        GetData();
    }

    private void initiview(){

        Name=findViewById(R.id._name);
        Phone=findViewById(R.id._phone);
        Email=findViewById(R.id._email);
        Password=findViewById(R.id._password);
        _Image=findViewById(R.id.img_profile);
        genderLayout=findViewById(R.id.genderLayout);

    }

    private void GetData(){
        progressDialog.show();

        databaseReference.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);

                if (username.equals(Common.currentUser.getName())){

                }else {

                    genderLayout.setVisibility(View.GONE);

                }

                Name.setText(Common.currentUser.getName());
                Email.setText(user.getEmail());
                Password.setText(user.getPassword());
                Phone.setText(user.getPhone());
                Picasso.get().load(dataSnapshot.child("p_image").getValue().toString()).into(_Image);




                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {



            case R.id.edit:
                startActivity(new Intent(Profile.this, Update.class));

                return true;

        }

        return false;
    }

}
