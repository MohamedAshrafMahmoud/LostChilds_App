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
import com.example.mohamed.lostchilds.common.Common;
import com.example.mohamed.lostchilds.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class Update extends AppCompatActivity {

    ImageView P_Image;
    EditText New_Name,New_Email,New_Password,New_Phone;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
     ProgressDialog progressDialog;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        initview();
        GetData();
    }

    private void initview(){
        New_Name=findViewById(R.id.new_name);
        New_Email=findViewById(R.id.new_email);
        New_Password=findViewById(R.id.new_password);
        New_Phone=findViewById(R.id.new_phone);
        P_Image=findViewById(R.id.P_img_profile);

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("User");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        progressDialog = new ProgressDialog(Update.this);
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
                Picasso.get().load(dataSnapshot.child("p_image").getValue().toString()).into(P_Image);
                Toast.makeText(Update.this, ""+dataSnapshot.child("p_image").getValue(), Toast.LENGTH_SHORT).show();



                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
             uri = data.getData();
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

            uploadImage();
        }
    }


    private void uploadImage() {

        if(uri != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();


                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {


                                    User  userInformation = new User(
                                            New_Phone.getText().toString(),
                                            New_Email.getText().toString(),
                                            New_Password.getText().toString(),
                                            uri.toString());

                                    databaseReference.child(Common.currentUser.getName()).setValue(userInformation);
                                    startActivity(new Intent(Update.this, Main.class));
                                    Toast.makeText(Update.this, "تم بنجاح", Toast.LENGTH_SHORT).show();


                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Update.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
}
