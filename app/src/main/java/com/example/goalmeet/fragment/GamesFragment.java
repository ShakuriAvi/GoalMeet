package com.example.goalmeet.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.goalmeet.Adapter.Adapter_Games;
import com.example.goalmeet.Adapter.Adapter_Teams;
import com.example.goalmeet.Class.Game;
import com.example.goalmeet.Class.Team;
import com.example.goalmeet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.example.goalmeet.Other.Constants.SP_FILE;


public class GamesFragment extends Fragment {
    private RecyclerView fragmentGames_LST_games;
    private ValueEventListener valueEventListener;
    private ArrayList<Game> games;
    private ArrayList<Team> teams;
    private DatabaseReference reference;
    private View view;
    private boolean flag =false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_games, container, false);
     //   initView();
        loadTeamsFromFireBase();

        Log.d("bbbb","33333 ");
        return view;
    }



    private void loadTeamsFromFireBase() {
        fragmentGames_LST_games = view.findViewById(R.id.games_LST_teams);
        fragmentGames_LST_games.setLayoutManager(new LinearLayoutManager(getActivity()));
        games = new ArrayList<>();
        teams = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("games");
        Log.d("bbbb"," "+reference);
        valueEventListener= reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                games.clear();
                teams.clear();
                Log.d("bbbb", "1111");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Game game = snapshot.getValue(Game.class);
                    games.add(game);
                    Adapter_Games adapter = new Adapter_Games(getActivity(), games);
                    fragmentGames_LST_games.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        reference.removeEventListener(valueEventListener);
    }
}
