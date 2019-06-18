package com.example.mohamed.lostchilds.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

public class SignUp extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseReference;


    TextView name, phone, email, password, signUp;
    String  Image="null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        name = (TextView) findViewById(R.id.edtname);
        phone = (TextView) findViewById(R.id.edtphone);
        email = (TextView) findViewById(R.id.edtemail);
        password = (TextView) findViewById(R.id.edtpass);
        signUp = (TextView) findViewById(R.id.signUp);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectToInternet(getBaseContext())) {


                    if (name.getText().toString().length() == 0) {
                        name.setError("name not entered");
                        name.requestFocus();
                    } else if (phone.getText().toString().length() == 0) {
                        phone.setError("phone not entered");
                        phone.requestFocus();

                    } else if (email.getText().toString().length() == 0) {
                        email.setError("email not entered");
                        email.requestFocus();
                    } else if (password.getText().toString().length() == 0) {
                        password.setError("Password not entered");
                        password.requestFocus();
                    } else {


                        final ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
                        progressDialog.setMessage("please wait ....");
                        progressDialog.show();


                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child(name.getText().toString()).exists()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(SignUp.this, "name alredy registered ", Toast.LENGTH_SHORT).show();

                                } else {
                                    progressDialog.dismiss();
                                    User user = new User(phone.getText().toString(), email.getText().toString(), password.getText().toString(),"null");
                                    databaseReference.child(name.getText().toString()).setValue(user);
                                    Toast.makeText(SignUp.this, "SignUp sucessfully", Toast.LENGTH_SHORT).show();
                                    user.setName(name.getText().toString());
                                    Common.currentUser=user;

                                    startActivity(new Intent(SignUp.this, Main.class));
                                    finish();

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                }

            }
        });


    }
}


