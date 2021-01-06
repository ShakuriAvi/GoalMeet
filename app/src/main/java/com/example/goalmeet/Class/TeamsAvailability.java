package com.example.goalmeet.Class;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeamsAvailability {
    public static ArrayList<Team> generateTeams() {
        ArrayList<Team> teamsAvailable = new ArrayList<>();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference  reference = FirebaseDatabase.getInstance().getReference("teams");
        Log.d("bbbb", "33333 " + reference);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teamsAvailable.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Team team = snapshot.getValue(Team.class);
                    Log.d("bbbb", "444444 " + team.getCity() + team.getNameSymbol()+team.getTheManager()+team.getDescription()+ team.getName() );
                    teamsAvailable.add(team);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        return teamsAvailable;
    }
}
