package com.example.mohamed.lostchilds.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mohamed.lostchilds.R;

public class LostViewHolder extends RecyclerView.ViewHolder {

    public ImageView user_img, child_img, settings;
    public TextView username, date, childname, phone_number, adress, description,age;
    public LinearLayout comment, share;

    public LostViewHolder(View itemView) {
        super(itemView);

        user_img = (ImageView) itemView.findViewById(R.id.userimage);
        child_img = (ImageView) itemView.findViewById(R.id.childimage);
        settings = (ImageView) itemView.findViewById(R.id.settings);
        username = (TextView) itemView.findViewById(R.id.username);
        date = (TextView) itemView.findViewById(R.id.date);
        childname = (TextView) itemView.findViewById(R.id.childname);
        age = (TextView) itemView.findViewById(R.id.childage);
        phone_number = (TextView) itemView.findViewById(R.id.phonenumber);
        adress = (TextView) itemView.findViewById(R.id.adress);
        description = (TextView) itemView.findViewById(R.id.description);
        comment = (LinearLayout) itemView.findViewById(R.id.linercomment);
        share = (LinearLayout) itemView.findViewById(R.id.linershare);
    }
}
