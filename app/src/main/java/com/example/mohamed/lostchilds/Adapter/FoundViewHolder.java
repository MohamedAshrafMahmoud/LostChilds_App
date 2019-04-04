package com.example.mohamed.lostchilds.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mohamed.lostchilds.R;

public class FoundViewHolder extends RecyclerView.ViewHolder {

    public ImageView user_img,child_img,settings;
    public TextView username,date,childname,helper_name,phone_number,description;
    public LinearLayout comment,share;

    public FoundViewHolder(View itemView) {
        super(itemView);

        user_img=(ImageView)itemView.findViewById(R.id.userimage);
        child_img=(ImageView)itemView.findViewById(R.id.childimage);
        settings=(ImageView)itemView.findViewById(R.id.settings);
        username=(TextView) itemView.findViewById(R.id.username);
        date=(TextView) itemView.findViewById(R.id.date);
        childname=(TextView) itemView.findViewById(R.id.childname);
        helper_name=(TextView) itemView.findViewById(R.id.helpername);
        phone_number=(TextView) itemView.findViewById(R.id.phonenumber);
        description=(TextView) itemView.findViewById(R.id.description);
        comment=(LinearLayout) itemView.findViewById(R.id.linercomment);
        share=(LinearLayout) itemView.findViewById(R.id.linershare);
    }
}
