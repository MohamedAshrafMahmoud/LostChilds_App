package com.example.mohamed.lostchilds.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mohamed.lostchilds.R;

public class CommentsViewHolder extends RecyclerView.ViewHolder {

    public TextView name,comment;

    public CommentsViewHolder(View itemView) {
        super(itemView);

        name=itemView.findViewById(R.id.name);
        comment=itemView.findViewById(R.id.comment);


    }
}