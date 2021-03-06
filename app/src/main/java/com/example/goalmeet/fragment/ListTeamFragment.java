package com.example.goalmeet.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goalmeet.Adapter.Adapter_Teams;
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

public class ListTeamFragment extends Fragment {
    private RecyclerView fragmentlist_LST_teams;
    private ArrayList<Team> teamsAvailable;
    private ValueEventListener valueEventListener;
    private DatabaseReference reference;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_team, container, false);
        loadTeamsFromFireBase();
        Log.d("bbbb", "33333 ");
        return view;
    }

    private void loadTeamsFromFireBase() {
        teamsAvailable = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("teams");
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teamsAvailable.clear();
                Log.d("bbbb", "11111 ");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Team team = snapshot.getValue(Team.class);

                    teamsAvailable.add(team);
                }
                Log.d("bbbb", "2222 ");
                fragmentlist_LST_teams = view.findViewById(R.id.fragmentlist_LST_teams);
                fragmentlist_LST_teams.setLayoutManager(new LinearLayoutManager(getActivity()));
                Adapter_Teams adapter = new Adapter_Teams(getActivity(), teamsAvailable);
                moveToProfilTeam(adapter);
                fragmentlist_LST_teams.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }

        });
    }

    private void moveToProfilTeam(Adapter_Teams adapter) {
        adapter.setClickListener(new Adapter_Teams.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                SharedPreferences prefs = getActivity().getSharedPreferences(SP_FILE, getActivity().MODE_PRIVATE);
                //  String userName = prefs.getString("nameOfUser", null);
                SharedPreferences.Editor editor = prefs.edit();
                String userToString = prefs.getString("theUser", null);
                Gson gson = new Gson();
                String teamToString = gson.toJson(teamsAvailable.get(position));
                editor.putString("pressOnTeam", teamToString);
                editor.putString("theUser", userToString);
                editor.apply();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.listTeamFragment, new TeamFragment()).commit();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (valueEventListener != null)
            reference.removeEventListener(valueEventListener);
    }
}
