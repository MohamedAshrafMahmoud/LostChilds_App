package com.example.mohamed.lostchilds.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mohamed.lostchilds.R;
import com.example.mohamed.lostchilds.View.Comments.Comments;
import com.example.mohamed.lostchilds.View.Map.MapsActivity;
import com.example.mohamed.lostchilds.common.Common;
import com.example.mohamed.lostchilds.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;

public class Profile extends AppCompatActivity {

    ImageView P_Image;
    EditText New_Name,New_Email,New_Password,New_Phone;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
     ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initview();
        GetData();
    }

    private void initview(){
        New_Name=findViewById(R.id.new_name);
        New_Email=findViewById(R.id.new_email);
        New_Password=findViewById(R.id.new_password);
        New_Phone=findViewById(R.id.new_phone);
        User username=new User();

        Toast.makeText(this, ""+username.getEmail(), Toast.LENGTH_SHORT).show();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("User");


        progressDialog = new ProgressDialog(Profile.this);
        progressDialog.setMessage("please wait ....");


    }
    private void GetData(){
        progressDialog.show();

        databaseReference.child(Common.currentUser.getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               User user=dataSnapshot.getValue(User.class);

               New_Name.setText(Common.currentUser.getName());
               New_Email.setText(user.getEmail());
               New_Password.setText(user.getPassword());
               New_Phone.setText(user.getPhone());

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void UpDate_Data(){


        progressDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    progressDialog.dismiss();
                    User user = new User(New_Phone.getText().toString(), New_Email.getText().toString(), New_Password.getText().toString());
                    databaseReference.child("mohamed").setValue(user);
                    Toast.makeText(Profile.this, "SignUp sucessfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }




    private void Upload() {

      User  userInformation = new User(
              New_Phone.getText().toString(),
                New_Email.getText().toString(),
                New_Password.getText().toString()


        );

        databaseReference.child(Common.currentUser.getName()).setValue(userInformation);
        startActivity(new Intent(Profile.this, Main.class));
        Toast.makeText(this, "تم بنجاح", Toast.LENGTH_SHORT).show();


    }




    public void Choose_Image(View view) {

        get_P_Image();
    }

    private void get_P_Image(){

        Intent in = new Intent();
        in.setType("image/*");
        in.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(in, "select picture"), 1);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                P_Image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void Send_Update(View view) {

        if (New_Email.getText().toString().length() == 0) {
            New_Email.setError("name not entered");
            New_Email.requestFocus();
        } else if (New_Password.getText().toString().length() == 0) {
            New_Password.setError("phone not entered");
            New_Password.requestFocus();

        } else if (New_Phone.getText().toString().length() == 0) {
            New_Phone.setError("email not entered");
            New_Phone.requestFocus();
        }  else {

            Upload();
        }
    }
}
