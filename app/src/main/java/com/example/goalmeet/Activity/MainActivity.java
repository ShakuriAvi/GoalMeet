package com.example.goalmeet.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.goalmeet.Class.RequestJoin;
import com.example.goalmeet.Class.User;
import com.example.goalmeet.R;
import com.example.goalmeet.fragment.ChatsFragment;
import com.example.goalmeet.fragment.CreateTeamFragment;
import com.example.goalmeet.fragment.ListTeamFragment;
import com.example.goalmeet.fragment.ProfilFragment;
import com.example.goalmeet.fragment.TeamFragment;
import com.example.goalmeet.fragment.UsersFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static com.example.goalmeet.Other.Constants.SP_FILE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Firebase
    private FirebaseUser firebaseUser;
    private Toolbar activity_toolbar;
    private DatabaseReference myRef;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SharedPreferences prefs;
    private  SharedPreferences.Editor editor;
    private User user;
    private Dialog mDialog;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("bbbb", "" + this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        initView();
        if (savedInstanceState == null) {
            myRef = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);
                    String teamToString = user.getNameClub();
                    prefs = getSharedPreferences(SP_FILE, MODE_PRIVATE);
                    editor = prefs.edit();
                    editor.putString("theTeamFromMainActivity", teamToString);
                    editor.putString("nameOfUser", user.getUserName());
                    editor.apply();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfilFragment()).commit();
                    navigationView.setCheckedItem(R.id.nav_ITEM_myProfil);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void initView() {
        activity_toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(activity_toolbar);
        mDialog = new Dialog(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, activity_toolbar, R.string.Navigation_drawer_open, R.string.Navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_ITEM_myProfil:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfilFragment()).commit();
                break;
            case R.id.nav_ITEM_myTeam:

                if (!user.getNameClub().equals("")) {
                    String teamToString = user.getNameClub();
                    prefs = getSharedPreferences(SP_FILE, MODE_PRIVATE);
                    editor = prefs.edit();
                    editor.putString("theTeamFromMainActivity", teamToString);
                    editor.putString("nameOfUser", user.getUserName());
                    editor.apply();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TeamFragment()).commit();

                } else {
                    mDialog.setContentView(R.layout.popup);
                    mDialog.getWindow();
                    mDialog.show();

                    //Toast.makeText(MainActivity.this, "There is no team you belong to, please select a team from the list of available teams looking for players and contact the team manager.", Toast.LENGTH_LONG).show();
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListTeamFragment()).commit();

                }
                break;

            case R.id.nav_ITEM_avalibaleTeam:
                prefs = getSharedPreferences(SP_FILE, MODE_PRIVATE);
                editor = prefs.edit();
                Log.d("qqq"," " + user.getUserName());
                editor.putString("nameOfUser", user.getUserName());
                editor.apply();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListTeamFragment()).commit();
                break;

            case R.id.nav_ITEM_createTeam:

                prefs = getSharedPreferences(SP_FILE, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("nameOfUser", user.getUserName());
                editor.apply();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreateTeamFragment()).commit();


                break;
            case R.id.nav_ITEM_users:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UsersFragment()).commit();
                break;

            case R.id.nav_ITEM_chats:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChatsFragment()).commit();
                break;
            case R.id.nav_ITEM_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void checkStatus(String status) {
        myRef = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        myRef.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkStatus("offline");
    }
}


