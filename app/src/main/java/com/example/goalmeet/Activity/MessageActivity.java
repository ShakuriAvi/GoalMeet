package com.example.goalmeet.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goalmeet.Adapter.Adapter_Message;
import com.example.goalmeet.Class.Chat;
import com.example.goalmeet.Class.User;
import com.example.goalmeet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.goalmeet.Other.Constants.SP_FILE;

public class MessageActivity extends AppCompatActivity {

    private TextView message_TXT_username;
    private ImageView message_IMG_profile;
    private ImageButton message_BTN_send;
    private EditText message_ETXT_send;
    private Intent intent;
    private  FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private Adapter_Message adapterMessage;
    private List<Chat> mChat;
    private RecyclerView recyclerView;
    private  String userId;
    private ValueEventListener seenListener,readListener,chatListListener;

    @Override
    protected void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
        manageMessage();
       // clickOnImage();
        Log.d("aaaa","4444");
    }

    private void initView() {

        message_TXT_username = findViewById(R.id.message_TXT_username);
        message_IMG_profile = findViewById(R.id.message_IMG_profil);
        message_BTN_send = findViewById(R.id.message_BTN_send);
        message_ETXT_send = findViewById(R.id.message_ETXT_send);
        recyclerView = findViewById(R.id.message_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }


    private void manageMessage(){
            intent = getIntent();

            userId = intent.getStringExtra("userId");
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("users").child(userId);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    User user = snapshot.getValue(User.class);
                    message_TXT_username.setText(user.getUserName());
                    if(!user.getImageURL().equals("defult"))
                        Glide.with(MessageActivity.this).load(user.getImageURL()).into(message_IMG_profile);
                    else{
                        message_IMG_profile.setImageResource(R.drawable.binoculars);
                    }
                    readMessage( firebaseUser.getUid(), userId, user.getImageURL());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            message_BTN_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msg = message_ETXT_send.getText().toString();
                    if(!msg.equals("")){
                        sendMessage(firebaseUser.getUid() , userId , msg);

                    }else{
                        Toast.makeText(MessageActivity.this,"Please send a non empty msg",Toast.LENGTH_SHORT);
                    }
                    message_ETXT_send.setText("");
                }

            });

           seenMessage(userId);

        }
    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("seen",false);
        reference.child("chats").push().setValue(hashMap);

        //Adding User to chat fragment :Latest Chats with contacts

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chatList").child(firebaseUser.getUid());
       chatListListener= chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    chatRef.child("id").setValue(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessage(String myId, String userId, String imageURL){
        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("chats");
      readListener=  reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    Log.d("vvv","getSeen: " + chat.getSeen() +" message: "+ chat.getMessage()+" receiver " + chat.getReceiver() +" Sender  "+ chat.getSender());
                    if((chat.getReceiver().equals(myId) && chat.getSender().equals(userId)) || (chat.getReceiver().equals(userId) && chat.getSender().equals(myId))){
                        mChat.add(chat);


                    }

                }

                adapterMessage = new Adapter_Message(MessageActivity.this,mChat,imageURL);
                recyclerView.setAdapter(adapterMessage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void seenMessage(String userId){
        reference = FirebaseDatabase.getInstance().getReference("chats");

        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userId)){
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("seen",true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void checkStatus(String status){
        reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        reference.updateChildren(hashMap);
    }

//    private void clickOnImage() {
//        message_IMG_profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MessageActivity.this, MainActivity.class);
//                SharedPreferences prefs = getSharedPreferences(SP_FILE,MODE_PRIVATE);
//                SharedPreferences.Editor editor = prefs.edit();
//                Log.d("tttt","1 "+ userId);
//                editor.putString("userId" , userId);
//                editor.apply();
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
//                finish();
//            }
//        });
//    }


    @Override
    protected void onResume() {
        super.onResume();
        checkStatus("online");

    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        checkStatus("offline");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(chatListListener!=null)
            reference.removeEventListener(chatListListener);
        if(seenListener!=null)
            reference.removeEventListener(seenListener);
        if(readListener!=null)
            reference.removeEventListener(readListener);
    }
}

