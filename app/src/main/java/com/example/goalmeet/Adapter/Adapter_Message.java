package com.example.goalmeet.Adapter;

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
import com.example.goalmeet.Activity.MessageActivity;
import com.example.goalmeet.Class.Chat;
import com.example.goalmeet.Class.User;
import com.example.goalmeet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class Adapter_Message extends RecyclerView.Adapter<Adapter_Message.ViewHolder> {
    private List<Chat> mChat;
    private Context context;
    private String imgURL;

    //Firebase
    FirebaseUser firebaseUser;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public Adapter_Message(Context context, List<Chat> mChat,String imgURL){
        this.context= context;
        this.mChat = mChat;
        this.imgURL = imgURL;
    }

    @NonNull
    @Override
    public  Adapter_Message.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new Adapter_Message.ViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new Adapter_Message.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Message.ViewHolder holder, int position) {
        Chat chat = mChat.get(position);
        holder.show_message.setText(chat.getMessage());
        if(position == mChat.size()-1){
            if(chat.getSeen()){
                holder.chatItem_TXT_seen.setText("Seen");
            }else{
                holder.chatItem_TXT_seen.setText("Delivered");
            }
        }else{
            holder.chatItem_TXT_seen.setVisibility(View.GONE);
        }
    }




    @Override
    public int getItemCount() {
        return mChat.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public TextView chatItem_TXT_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.chatItem_TXT_showMessage);
            chatItem_TXT_seen = itemView.findViewById(R.id.chatItem_TXT_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
}
