package com.example.mohamed.lostchilds.View.found;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed.lostchilds.R;
import com.example.mohamed.lostchilds.View.Lost.AddItem_Lost;
import com.example.mohamed.lostchilds.View.Map.AddFoundMap;
import com.example.mohamed.lostchilds.View.Map.MapsActivity;
import com.example.mohamed.lostchilds.common.Common;
import com.example.mohamed.lostchilds.model.FoundModel;
import com.example.mohamed.lostchilds.model.LostModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class AddItem_Found extends AppCompatActivity {

    ImageView back, childimage, takeimge;
    TextView username, publish;
    EditText edit_phone, edit_name, edit_description, edit_helper;
    int REQUEST_CODE_1=11;
    double Latitude,Longitude=0;
    FoundModel foundModel;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    Uri saveuri;
    private final int pickImageRequest = 71;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item__found);

        initViews();

        databaseReference = FirebaseDatabase.getInstance().getReference().child(Common.FoundChildCategory);

        storageReference = FirebaseStorage.getInstance().getReference("images/");

        username.setText(Common.currentUser.getName());

        takeimge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImage();
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_name.getText().toString().length() == 0) {
                    edit_name.setError("name not entered");
                    edit_name.requestFocus();
                } else if (edit_phone.getText().toString().length() == 0) {
                    edit_phone.setError("phone not entered");
                    edit_phone.requestFocus();
                } else if (edit_description.getText().toString().length() == 0) {
                    edit_description.setError("edit_description not entered");
                    edit_description.requestFocus();
                } else {

                    if (Latitude != 0) {
                        uploadData();
                    }else {

                        Toast.makeText(AddItem_Found.this, "Select Location", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void uploadData() {

        if (saveuri != null) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Uploading ...");
            dialog.show();

            String imgName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imgName);
            imageFolder.putFile(saveuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
                            Toast.makeText(AddItem_Found.this, "تم النشر", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    ArrayList<String> users = new ArrayList<String>();
                                    users.add(Common.currentUser.getName());
                                    if (TextUtils.isEmpty(uri.toString())) {

                                        foundModel = new FoundModel(
                                                edit_name.getText().toString(),
                                                edit_phone.getText().toString(),
                                                edit_description.getText().toString(),
                                                String.valueOf(R.drawable.ic_account_circle_black_24dp),
                                                Common.getDate(),
                                                edit_helper.getText().toString()
                                                ,Latitude,Longitude
                                        );
                                    } else {
                                        foundModel = new FoundModel(
                                                edit_name.getText().toString(),
                                                edit_phone.getText().toString(),
                                                edit_description.getText().toString(),
                                                uri.toString(),
                                                Common.getDate(),
                                                edit_helper.getText().toString(),
                                                Latitude,Longitude
                                        );
                                    }
                                    databaseReference.child(String.valueOf(System.currentTimeMillis())).setValue(foundModel);
                                    Toast.makeText(AddItem_Found.this, "New post was added", Toast.LENGTH_SHORT).show();

                                    //hide keyboard
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(AddItem_Found.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int progress = (int) (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            dialog.setMessage("Upload      " + progress + "    %  ");
                        }
                    });

        }
    }

    private void publishPost() {
        ProgressDialog progressDialog = new ProgressDialog(AddItem_Found.this);
        progressDialog.setMessage("please wait ......");
        progressDialog.show();

        foundModel = new FoundModel();
        foundModel.setChild_name(edit_name.getText().toString());
        foundModel.setDescription(edit_description.getText().toString());
        foundModel.setPhone(edit_phone.getText().toString());
        foundModel.setLatidude(Latitude);
        foundModel.setLongitude(Longitude);

        databaseReference.push().setValue(foundModel);

        progressDialog.dismiss();
        Toast.makeText(this, "تم النشر", Toast.LENGTH_SHORT).show();

        //hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void initViews() {
        edit_description = (EditText) findViewById(R.id.description);
        edit_phone = (EditText) findViewById(R.id.editphone);
        edit_name = (EditText) findViewById(R.id.editname);
        edit_helper = (EditText) findViewById(R.id.edithelper);
        username = (TextView) findViewById(R.id.username);
        takeimge = (ImageView) findViewById(R.id.takeimge);
        childimage = (ImageView) findViewById(R.id.childimage);
        publish = (TextView) findViewById(R.id.publish);
        back = (ImageView) findViewById(R.id.back);

    }

    private void ChooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select picture"), pickImageRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        if (resultCode == RESULT_OK ) {


            if (requestCode == pickImageRequest ) {
                saveuri = data.getData();
            }else if (requestCode==REQUEST_CODE_1){


                Latitude = data.getDoubleExtra("getLatitude",0);
                Longitude = data.getDoubleExtra("getLongitude",0);
                Toast.makeText(this, ""+Latitude+Longitude, Toast.LENGTH_SHORT).show();

            }
        }
    }


    public void AddLocation(View view) {

        Intent intent = new Intent(AddItem_Found.this, AddFoundMap.class);
        startActivityForResult(intent, REQUEST_CODE_1);


    }


}
