package com.example.mohamed.lostchilds.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mohamed.lostchilds.R;
import com.example.mohamed.lostchilds.common.Common;
import com.example.mohamed.lostchilds.model.User;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    EditText name,password;
    Button btnsignin;
    CheckBox rememberme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("User");

        name=(EditText)findViewById(R.id.etemail);
        password=(EditText)findViewById(R.id.etpass);
        btnsignin=(Button) findViewById(R.id.login);
        rememberme=(CheckBox) findViewById(R.id.checkremember);






        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().length() == 0) {
                    name.setError("name required");
                    name.requestFocus();
                } else if (password.getText().toString().length() == 0) {
                    password.setError("password required");
                    password.requestFocus();

                } else if (Common.isConnectToInternet(getBaseContext())) {


                    SignUser(name.getText().toString(), password.getText().toString());

                } else {
                    Toast.makeText(SignIn.this, "Check your internet Connection !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
/* //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// */
    private void SignUser(String s, String s1) {

        final ProgressDialog progressDialog = new ProgressDialog(SignIn.this);
        progressDialog.setMessage("please wait ....");
        progressDialog.show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(name.getText().toString()).exists()){
                    progressDialog.dismiss();

                    User user=dataSnapshot.child(name.getText().toString()).getValue(User.class);
                    user.setName(name.getText().toString());

                    if (user.getPassword().equals(password.getText().toString())){
                        Intent intent=new Intent(SignIn.this,main.class);
                        Common.currentUser=user;

                        //save data when checkbox rememberme
                        if (rememberme.isChecked()) {
                            SharedPreferences.Editor edit = getSharedPreferences("da", Context.MODE_PRIVATE).edit();
                            edit.putString("name", name.getText().toString());
                            edit.putString("password", password.getText().toString());
                            edit.commit();
                        }

                        startActivity(intent);
                        finish();
                        
                        databaseReference.removeEventListener(this);
                        
                        
                    }else {
                        Toast.makeText(SignIn.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(SignIn.this, "name not exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void goSignUp(View view) {
        startActivity(new Intent(SignIn.this,SignUp.class));
    }
}
