package com.example.goalmeet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goalmeet.Activity.MessageActivity;
import com.example.goalmeet.Class.User;
import com.example.goalmeet.R;

import java.util.List;

public class Adapter_User extends RecyclerView.Adapter<Adapter_User.ViewHolder> {
    private List<User> mUsers;
    private Context context;
    private boolean isChat;

    public Adapter_User(Context context, List<User> mUsers,boolean isChat){
        this.context= context;
        this.mUsers = mUsers;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycleview_user_profil, parent,false);
        return new Adapter_User.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.userName.setText(user.getUserName());
        holder.city.setText("defult");
        holder.description.setText("blalaala");
        if(!user.getImageURL().equals("defult")){
            Glide.with(context).load(user.getImageURL()).into(holder.image);
        }
        else {
            holder.image.setImageResource(R.drawable.user);
        }

        //status check
        if(isChat){
            if(user.getStatus().equals("online")){
                holder.userProfil_IMG_statusOn.setVisibility(View.VISIBLE);
                holder.userProfil_IMG_statusOff.setVisibility(View.GONE);

            }
            else{
                holder.userProfil_IMG_statusOn.setVisibility(View.GONE);
                holder.userProfil_IMG_statusOff.setVisibility(View.VISIBLE);
            }
        }else{
            holder.userProfil_IMG_statusOn.setVisibility(View.GONE);
            holder.userProfil_IMG_statusOff.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MessageActivity.class);
                i.putExtra("userId",user.getId());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(i);

            }
        });
    }




    @Override
    public int getItemCount() {
        return mUsers.size();
    }
    User getItem(int id) {
        return mUsers.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView userName;
        public TextView city;
        public TextView description;
        public ImageView image;
        public ImageView userProfil_IMG_statusOn;
        public ImageView userProfil_IMG_statusOff;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userProfil_TXT_name);
            city = itemView.findViewById(R.id.userProfil_TXT_city);
            description = itemView.findViewById(R.id.userProfil_TXT_description);
            image = itemView.findViewById(R.id.userProfil_IMG_symbol);
            userProfil_IMG_statusOn =  itemView.findViewById(R.id.userProfil_IMG_statusOn);
            userProfil_IMG_statusOff = itemView.findViewById(R.id.userProfil_IMG_statusOff);
        }
    }
}
