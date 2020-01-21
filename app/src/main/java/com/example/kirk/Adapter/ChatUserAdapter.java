package com.example.kirk.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kirk.GeneralUsers.MessagingActivity;
import com.example.kirk.Message;
import com.example.kirk.Model.User;
import com.example.kirk.R;

import java.util.Collections;
import java.util.List;

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.ViewHolder> {
    private Context mContext;
    private List<Message> mUsers;
    private boolean ischat;
    private List<User> users;

    public ChatUserAdapter(Context mContext, List<Message> mUsers, boolean ischat, List<User> users) {
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.ischat = ischat;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chatuser_item, parent, false);
        return new ChatUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (users == null) {
            holder.last_msg.setText(mUsers.get(position).message);
            final User user = mUsers.get(position).user;
            holder.username.setText(user.getUsername());
            if (user.getImageurl().equals("default")) {
                holder.profile_image.setImageResource(R.mipmap.ic_launcher);
            } else {
                try {
                    Glide.with(mContext).load(user.getImageurl()).into(holder.profile_image);
                } catch (Exception e) {
                }
            }

            if (ischat) {
                if(user.getStatus()!=null){
                    if (user.getStatus().equals("online")) {
                        holder.img_on.setVisibility(View.VISIBLE);
                        holder.img_off.setVisibility(View.GONE);
                    } else {
                        holder.img_on.setVisibility(View.GONE);
                        holder.img_off.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MessagingActivity.class);
                    intent.putExtra("userid", user.getId());
                    mContext.startActivity(intent);
                }
            });
        } else {
            final User user = users.get(position);
            holder.username.setText(user.getUsername());
            if (user.getImageurl().equals("default")) {
                holder.profile_image.setImageResource(R.mipmap.ic_launcher);
            } else {
                try {
                    Glide.with(mContext).load(user.getImageurl()).into(holder.profile_image);
                } catch (Exception e) {
                }
            }

            if (ischat) {
                if (user.getStatus().equals("online")) {
                    holder.img_on.setVisibility(View.VISIBLE);
                    holder.img_off.setVisibility(View.GONE);
                } else {
                    holder.img_on.setVisibility(View.GONE);
                    holder.img_off.setVisibility(View.VISIBLE);
                }
            } else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MessagingActivity.class);
                    intent.putExtra("userid", user.getId());
                    mContext.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mUsers == null ? users.size() : mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;
        private TextView last_msg;

        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            last_msg = itemView.findViewById(R.id.last_msg);


        }
    }
}
