package com.example.mohamed.lostchilds.View.Comments;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed.lostchilds.Adapter.CommentsViewHolder;
import com.example.mohamed.lostchilds.Adapter.FoundViewHolder;
import com.example.mohamed.lostchilds.R;
import com.example.mohamed.lostchilds.View.Map.MapsActivity;
import com.example.mohamed.lostchilds.View.SignIn;
import com.example.mohamed.lostchilds.common.Common;
import com.example.mohamed.lostchilds.model.ChatMessage;
import com.example.mohamed.lostchilds.model.FoundModel;
import com.example.mohamed.lostchilds.model.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.HashMap;

public class Comments extends AppCompatActivity {
    private String Post_Id;
    FirebaseRecyclerAdapter<ChatMessage, CommentsViewHolder> adapter;

    DatabaseReference databaseReference;
    RecyclerView List_Comments;
    EditText text_Comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        savedInstanceState=getIntent().getExtras();
        Post_Id=savedInstanceState.getString("postkey");
        List_Comments=findViewById(R.id.list_of_comment);
        text_Comment=findViewById(R.id.input);

        databaseReference = FirebaseDatabase.getInstance().getReference("Comments").child(Post_Id);

        List_Comments = (RecyclerView) findViewById(R.id.list_of_comment);
        List_Comments.setHasFixedSize(true);
        List_Comments.setLayoutManager(new LinearLayoutManager(this));

        GetComments();




}

    private void GetComments() {



        adapter=new FirebaseRecyclerAdapter<ChatMessage, CommentsViewHolder>(ChatMessage.class, R.layout.message, CommentsViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(CommentsViewHolder viewHolder, ChatMessage model, int position) {


                viewHolder.name.setText(model.getPublisher());
                viewHolder.comment.setText(model.getComment());

            }
        };

        adapter.notifyDataSetChanged();
        List_Comments.setAdapter(adapter);





    }

    private void AddComment(){
    SignIn signIn=new SignIn();
    DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Comments").child(Post_Id);
    if (text_Comment.getText().toString().isEmpty()){

        Toast.makeText(Comments.this, " write comment" , Toast.LENGTH_SHORT).show();
    }else {

    HashMap<String,Object> hashMap=new HashMap<>();
    hashMap.put("comment",text_Comment.getText().toString());
    hashMap.put("publisher",signIn.Email);
    Toast.makeText(Comments.this, signIn.Email, Toast.LENGTH_SHORT).show();
    reference.push().setValue(hashMap);
    }


}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void Upload(View view) {


        AddComment();
    }
}
