package com.example.goalmeet.Activity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.goalmeet.Class.User;
import com.example.goalmeet.R;
import com.example.goalmeet.fragment.ChatsFragment;
import com.example.goalmeet.fragment.CreateTeamFragment;
import com.example.goalmeet.fragment.GamesFragment;
import com.example.goalmeet.fragment.ListTeamFragment;
import com.example.goalmeet.fragment.ProfilFragment;
import com.example.goalmeet.fragment.RequestsFragment;
import com.example.goalmeet.fragment.TeamFragment;
import com.example.goalmeet.fragment.UsersFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.util.HashMap;

import static com.example.goalmeet.Other.Constants.SP_FILE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Firebase
    private FirebaseUser firebaseUser;
    private Toolbar activity_toolbar;
    private DatabaseReference myRef,reference;
    private Gson gson = new Gson();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private ValueEventListener myValueEventListener;
    private User user;
    private Dialog mDialog;
    private String userId;
    private ImageView header_IMG_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("bbbb", "" + this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        initView();
        if (savedInstanceState == null) {
            myRef = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
            myValueEventListener = myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);
//                    if (user.getImageURL().equals("defult"))
//                        header_IMG_profile.setImageResource(R.drawable.user);
//                    else
//                        Glide.with(getApplicationContext()).load(user.getImageURL()).into(header_IMG_profile);
                    String teamToString = user.getNameClub();
                    prefs = getSharedPreferences(SP_FILE, MODE_PRIVATE);
                    editor = prefs.edit();


                    userId = prefs.getString("userId", null);
                    Log.d("tttt","2 "+ userId);
                   // editor.putString("userId" , userId);
                    editor.putString("theTeamFromMainActivity", teamToString);
                    editor.putString("nameOfUser", user.getUserName());
                    editor.apply();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfilFragment(),"FRAGMENT_TAG").commit();
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
        header_IMG_profile = findViewById(R.id.header_IMG_profile);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, activity_toolbar, R.string.Navigation_drawer_open, R.string.Navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_ITEM_myProfil:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfilFragment()).commit();
                break;
            case R.id.nav_ITEM_myTeam:

                if (!user.getNameClub().equals("")) {
                    Gson gson = new Gson();
                    String userToString = gson.toJson(user);
                    prefs = getSharedPreferences(SP_FILE, MODE_PRIVATE);
                    editor = prefs.edit();

                    editor.putString("theUser", userToString);
                    editor.apply();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TeamFragment()).commit();

                } else {
                    mDialog.setContentView(R.layout.popup_status_myteam);
                    mDialog.getWindow();
                    mDialog.show();

                    //Toast.makeText(MainActivity.this, "There is no team you belong to, please select a team from the list of available teams looking for players and contact the team manager.", Toast.LENGTH_LONG).show();
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListTeamFragment()).commit();

                }
                break;

            case R.id.nav_ITEM_avalibaleTeam:
                Gson gson = new Gson();
                String userToString = gson.toJson(user);
                prefs = getSharedPreferences(SP_FILE, MODE_PRIVATE);
                editor = prefs.edit();
                editor.putString("theUser", userToString);
                editor.apply();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListTeamFragment()).commit();
                break;
            case R.id.nav_ITEM_games:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GamesFragment()).commit();
                break;
            case R.id.nav_ITEM_createTeam:

                prefs = getSharedPreferences(SP_FILE, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("nameOfUser", user.getUserName());
                editor.apply();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreateTeamFragment()).commit();


                break;
            case R.id.nav_ITEM_players:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UsersFragment()).commit();
                break;
            case R.id.nav_ITEM_requests:
                gson = new Gson();
                String userString = gson.toJson(user);
                prefs = getSharedPreferences(SP_FILE, MODE_PRIVATE);
                editor = prefs.edit();
                editor.putString("theUser", userString);
                editor.apply();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RequestsFragment()).commit();
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



    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRef.removeEventListener(myValueEventListener);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("qqqq","12345");
        Fragment profilFragment = getSupportFragmentManager().findFragmentByTag("FRAGMENT_TAG");
        if(profilFragment!=null)
            profilFragment.onActivityResult(requestCode,resultCode,data);
        Log.d("qqqq","request code" + requestCode +" result code" + resultCode+" data"  + data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}


