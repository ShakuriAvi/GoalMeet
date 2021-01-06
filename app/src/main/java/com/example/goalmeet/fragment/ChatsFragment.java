package com.example.goalmeet.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goalmeet.Adapter.Adapter_User;
import com.example.goalmeet.Class.ChatList;
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
import java.util.List;

public class ChatsFragment extends Fragment {
    private Adapter_User adapter_user;
    private List<User> mUser;
    private RecyclerView recyclerView;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;

    private List<ChatList> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.chat_LST_userContact);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("chatList");//.child(firebaseUser.getUid());
        Log.d("vvv"," " + firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();

                //Loop for all users:
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {



                    ChatList chatList =  snapshot.getValue(ChatList.class);
                    chatList.setIdSender( snapshot.getKey());
                    if (chatList.getIdSender().equals(firebaseUser.getUid()) || firebaseUser.getUid().equals(chatList.getId())) {


                        usersList.add(chatList);
                    }


                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void chatList() {
        //Getting all recent chats
        mUser = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();

                //Loop for all users:
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    for(ChatList chatList : usersList) {
                        if (!user.getId().equals(firebaseUser.getUid()) ) {

                            if (user.getId().equals(chatList.getIdSender()) || user.getId().equals(chatList.getId()) ) {
                                mUser.add(user);
                                break;
                            }
                        }
                    }

                }
                adapter_user = new Adapter_User(getContext(),mUser,true);
                recyclerView.setAdapter(adapter_user);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

