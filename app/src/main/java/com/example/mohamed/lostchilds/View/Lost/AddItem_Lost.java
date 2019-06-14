package com.example.mohamed.lostchilds.View.Lost;

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
import com.example.mohamed.lostchilds.common.Common;
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

public class AddItem_Lost extends AppCompatActivity {


    ImageView childimage, takeimge;
    TextView username, publish;
    EditText edit_phone, edit_name, edit_description, edit_adress;

    LostModel lostModel;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    Uri saveuri;
    private final int pickImageRequest = 71;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item__lost);

        initViews();

        databaseReference = FirebaseDatabase.getInstance().getReference().child(Common.LostChildCategory);
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
                } else if (edit_adress.getText().toString().length() == 0) {
                    edit_adress.setError("edit_adress not entered");
                    edit_adress.requestFocus();
                } else {
                    uploadData();

                }
            }
        });


    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////

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
                            Toast.makeText(AddItem_Lost.this, "تم النشر", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    ArrayList<String> users = new ArrayList<String>();
                                    users.add(Common.currentUser.getName());
                                    if (TextUtils.isEmpty(uri.toString())) {

                                        lostModel = new LostModel(
                                                edit_name.getText().toString(),
                                                edit_phone.getText().toString(),
                                                edit_description.getText().toString(),
                                                edit_adress.getText().toString(),
                                                String.valueOf(R.drawable.ic_account_circle_black_24dp),
                                                Common.getDate()
                                        );
                                    } else {
                                        lostModel = new LostModel(
                                                edit_name.getText().toString(),
                                                edit_phone.getText().toString(),
                                                edit_description.getText().toString(),
                                                edit_adress.getText().toString(),
                                                uri.toString(),
                                                Common.getDate());
                                    }
                                    databaseReference.child(String.valueOf(System.currentTimeMillis())).setValue(lostModel);
                                    Toast.makeText(AddItem_Lost.this, "New post was added", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(AddItem_Lost.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        ProgressDialog progressDialog = new ProgressDialog(AddItem_Lost.this);
        progressDialog.setMessage("please wait ...");
        progressDialog.show();

        lostModel = new LostModel();
        lostModel.setChild_name(edit_name.getText().toString());
        lostModel.setDescription(edit_description.getText().toString());
        lostModel.setPhone(edit_phone.getText().toString());
        lostModel.setAdress(edit_adress.getText().toString());
        lostModel.setDate(Common.getDate());

        databaseReference.push().setValue(lostModel);

        progressDialog.dismiss();
        Toast.makeText(this, "تم النشر", Toast.LENGTH_SHORT).show();

        //hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void initViews() {
        edit_description = (EditText) findViewById(R.id.description);
        edit_phone = (EditText) findViewById(R.id.edtphone);
        edit_name = (EditText) findViewById(R.id.edtname);
        edit_adress = (EditText) findViewById(R.id.edtadress);
        username = (TextView) findViewById(R.id.username);
        takeimge = (ImageView) findViewById(R.id.takeimge);
        childimage = (ImageView) findViewById(R.id.childimage);
        publish = (TextView) findViewById(R.id.publish);

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

        if (requestCode == pickImageRequest && resultCode == RESULT_OK && data != null && data.getData() != null) {
            saveuri = data.getData();
         }
    }

}