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

import com.example.goalmeet.Adapter.Adapter_Games;
import com.example.goalmeet.Class.Game;
import com.example.goalmeet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class GamesFragment extends Fragment {
    private RecyclerView fragmentGames_LST_games;
    private ValueEventListener valueEventListener;
    private ArrayList<Game> games;
    private DatabaseReference reference;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);
        loadTeamsFromFireBase(view);

        return view;
    }

    private void loadTeamsFromFireBase(View view) {
        fragmentGames_LST_games = view.findViewById(R.id.games_LST_teams);
        fragmentGames_LST_games.setLayoutManager(new LinearLayoutManager(getActivity()));
        games = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("games");
        Log.d("bbbb", " " + reference);
        readData(new FireBaseCallBack() {
            @Override
            public void onCallback(ArrayList<Game> list) {
                Adapter_Games adapter = new Adapter_Games(getActivity(), list);
                fragmentGames_LST_games.setAdapter(adapter);
            }
        });

    }

    private void readData(FireBaseCallBack fireBaseCallBack) {
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                games.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Game game = snapshot.getValue(Game.class);
                    games.add(game);
                }
                fireBaseCallBack.onCallback(games);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private interface FireBaseCallBack {
        void onCallback(ArrayList<Game> list);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (valueEventListener != null)
            reference.removeEventListener(valueEventListener);
    }
}
