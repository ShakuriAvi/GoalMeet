package com.example.goalmeet.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goalmeet.Adapter.Adapter_Request;

import com.example.goalmeet.Adapter.Adapter_Teams;
import com.example.goalmeet.Class.Request;
import com.example.goalmeet.Class.User;
import com.example.goalmeet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.example.goalmeet.Other.Constants.SP_FILE;

public class RequestsFragment extends Fragment {
    private ArrayList<Request> requests;
    private RecyclerView fragmentRequest_LST_request;
    private FirebaseUser firebaseUser;
    private SharedPreferences prefs;
    private ValueEventListener valueEventListener;
    private  User user;
    private DatabaseReference reference;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);
        loadTeamsFromFireBase(view);
        return view;
    }
    private void loadTeamsFromFireBase(View view) {
        Gson gson = new Gson();
        fragmentRequest_LST_request = view.findViewById(R.id.fragmentRequest_LST_teams);
        fragmentRequest_LST_request.setLayoutManager(new LinearLayoutManager(getActivity()));
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        prefs = getActivity().getSharedPreferences(SP_FILE, getActivity().MODE_PRIVATE);
        String userToString = prefs.getString("theUser", null);
        user = gson.fromJson(userToString, User.class);
        requests = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("request");
     valueEventListener=   reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requests.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Request request= snapshot.getValue(Request.class);
                    if(request.getReceiver().equals(firebaseUser.getUid()) && request.getSeen() == false)
                        requests.add(request);
                }
                Adapter_Request adapter = new Adapter_Request(getActivity(), requests ,user,fragmentManager);
                fragmentRequest_LST_request.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }

        });




    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        reference.removeEventListener(valueEventListener);
    }
}
