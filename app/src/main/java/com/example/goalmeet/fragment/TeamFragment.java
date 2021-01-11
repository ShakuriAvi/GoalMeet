package com.example.goalmeet.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goalmeet.Activity.MessageActivity;
import com.example.goalmeet.Adapter.Adapter_SymbolTeam;
import com.example.goalmeet.Class.Request;
import com.example.goalmeet.Class.Team;
import com.example.goalmeet.Class.User;
import com.example.goalmeet.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.goalmeet.Other.Constants.SP_FILE;

public class TeamFragment extends Fragment {
    private Gson gson;
    private Team team;
    private User user;
    private Request request;
    private Dialog dialogNewRequsetJoin, dialogNewMatchGame;

    private RecyclerView.LayoutManager layoutManager;
    private ValueEventListener teamListener,requestListener;
    private RecyclerView fragmentSymbol_LST_teams;
    private ArrayList<String> nameSymbols;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private SharedPreferences prefs;
    private LinearLayout team_LAY_addPlayer;
    private Button team_BTN_requestJoin, popuprequset_BTN_confirm, popuprequset_BTN_showProfil, popuprequset_BTN_remove, team_BTN_inviteGame;
    private ImageView team_IMG_symbol, team_IMG_addPlayer, team_IMG_saveDescription, team_IMG_saveCity, team_IMG_message;
    private TextInputLayout team_ETXT_city, team_ETXT_description, team_ETXT_addPlayer, team_ETXT_name;
    private TextView team_TXT_playerTeam, team_TXT_nameManager, team_TXT_editSymbol, team_TXT_editCity, team_TXT_editPlayer, team_TXT_editDescription, popuprequset_TXT_teams, popup_TXT_answer;
    private Switch team_SWITCH_searchPlayer;
    private ArrayList<TextView> arrayOfPermissiomEdit;
    private ArrayList<TextInputLayout> arrayEditETXT;
    private boolean availableToNewPlayer = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team, container, false);
        getActivity().getFragmentManager().popBackStack();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        initView(view);

        return view;
    }


    private void getTeamFromJson(View view) {
        gson = new Gson();
        prefs = getActivity().getSharedPreferences(SP_FILE, getActivity().MODE_PRIVATE);
        String userToString = prefs.getString("theUser", null);
        user = gson.fromJson(userToString, User.class);
        String pressOnTeam = prefs.getString("pressOnTeam", null);
        if (pressOnTeam != null) {//if user arrive to fragment from listTeamFragment or createTeamFragment
            team = gson.fromJson(pressOnTeam, Team.class);
            initUi(view);
        } else {//if user arrive to fragment from Main Activity (from nav)


            reference = FirebaseDatabase.getInstance().getReference("teams").child(user.getNameClub());
            teamListener= reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    team = dataSnapshot.getValue(Team.class);
                    initUi(view);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();


    }

    private void initUi(View view) {
        nameSymbols(view);
        initTeamInView();
        permisionToUser(view);
    }

    private void initView(View view) {

        arrayOfPermissiomEdit = new ArrayList<>();
        arrayEditETXT = new ArrayList<>();

        team_TXT_editCity = view.findViewById(R.id.team_TXT_editCity);
        team_ETXT_city = view.findViewById(R.id.team_ETXT_city);
        team_IMG_saveCity = view.findViewById(R.id.team_IMG_saveCity);
        arrayOfPermissiomEdit.add(team_TXT_editCity);
        arrayEditETXT.add(team_ETXT_city);

        team_TXT_nameManager = view.findViewById(R.id.team_TXT_nameManager);
        team_ETXT_name = view.findViewById(R.id.team_ETXT_name);

        team_LAY_addPlayer = view.findViewById(R.id.team_LAY_addPlayer);
        team_TXT_playerTeam = view.findViewById(R.id.team_TXT_playerTeam);
        team_ETXT_addPlayer = view.findViewById(R.id.team_ETXT_addPlayer);
        team_TXT_editPlayer = view.findViewById(R.id.team_TXT_editPlayer);
        team_IMG_addPlayer = view.findViewById(R.id.team_IMG_addPlayer);
        arrayOfPermissiomEdit.add(team_TXT_editPlayer);
        arrayEditETXT.add(team_ETXT_addPlayer);

        team_BTN_inviteGame = view.findViewById(R.id.team_BTN_inviteGame);
        team_BTN_requestJoin = view.findViewById(R.id.team_BTN_requestJoin);
        team_IMG_message = view.findViewById(R.id.team_IMG_message);
        team_IMG_symbol = view.findViewById(R.id.team_IMG_symbol);
        team_TXT_editSymbol = view.findViewById(R.id.team_TXT_editSymbol);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        fragmentSymbol_LST_teams = view.findViewById(R.id.fragmentSymbol_LST_teams);
        fragmentSymbol_LST_teams.setLayoutManager(layoutManager);
        arrayOfPermissiomEdit.add(team_TXT_editSymbol);

        team_TXT_editDescription = view.findViewById(R.id.team_TXT_editDescription);
        team_ETXT_description = view.findViewById(R.id.team_ETXT_description);
        team_IMG_saveDescription = view.findViewById(R.id.team_IMG_saveDescription);
        arrayOfPermissiomEdit.add(team_TXT_editDescription);
        arrayEditETXT.add(team_ETXT_description);

        team_SWITCH_searchPlayer = view.findViewById(R.id.team_SWITCH_searchPlayer);

        getTeamFromJson(view);

    }

    private void initTeamInView() {
        Log.d("mmmm", "1111");
        Glide.with(this).load((getResources().getIdentifier(team.getNameSymbol(), "drawable", getContext().getPackageName()))).into(team_IMG_symbol);
        team_ETXT_name.setHint(" Name Club: " + team.getName());
        team_ETXT_city.setHint("City: " + team.getCity());
        team_ETXT_description.setHint("About Team: " + team.getDescription());
        team_TXT_nameManager.setText("Name Manager: " + team.getNameManager());
        team_SWITCH_searchPlayer.setChecked(team.getFullCadre());
        Log.d(" zzzz", " " + team.getCadre() + team.getFullCadre() + team.getTheManager());
        team_TXT_playerTeam.setText(team_TXT_playerTeam.getText() + team.getCadre());

    }


    private void permisionToUser(View view) {

        if (!firebaseUser.getUid().equals(team.getTheManager())) {


            if (!user.getNameClub().equals(team.getName()) && !user.getIsManager()) {//if the user is guest but don't manager of other teams
                team_BTN_requestJoin.setVisibility(View.VISIBLE);
            } else if (user.getIsManager()) {//if user is'nt manager of this team but he manager of other team
                team_BTN_inviteGame.setVisibility(View.VISIBLE);
            }


            for (int i = 0; i < arrayOfPermissiomEdit.size(); i++) {
                arrayOfPermissiomEdit.get(i).setVisibility(View.GONE);

            }
            for (int i = 0; i < arrayEditETXT.size(); i++) {
                arrayEditETXT.get(i).setEnabled(false);
            }

            team_SWITCH_searchPlayer.setEnabled(false);
            team_IMG_message.setVisibility(View.VISIBLE);
            clickOnButtonGuest();

        } else {//if the user is the manager of team
            dialogNewRequsetJoin = new Dialog(getActivity());
            dialogNewRequsetJoin.setContentView(R.layout.popup_user_request_join);
            popuprequset_BTN_remove = dialogNewRequsetJoin.findViewById(R.id.popuprequset_BTN_remove);
            popuprequset_BTN_showProfil = dialogNewRequsetJoin.findViewById(R.id.popuprequset_BTN_showProfil);
            popuprequset_BTN_confirm = dialogNewRequsetJoin.findViewById(R.id.popuprequset_BTN_confirm);
            popuprequset_TXT_teams = dialogNewRequsetJoin.findViewById(R.id.popuprequset_TXT_teams);
            clickOnEditManager(); // if the user is manager
            reference = FirebaseDatabase.getInstance().getReference("request");
            requestListener=reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        request = snapshot.getValue(Request.class);

                        if (request.getReceiver().equals(firebaseUser.getUid()) && request.getSeen() == false && request.getFromTeam().equals("")) {
                            dialogNewRequsetJoin.getWindow();
                            dialogNewRequsetJoin.show();
                            popuprequset_TXT_teams.setText(" You have a new request to join from " + request.getNameSender() + ". Do you approve? ");
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void clickOnButtonGuest() {
        team_IMG_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MessageActivity.class);
                i.putExtra("userId", team.getTheManager());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(i);

            }
        });
        team_BTN_requestJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
                team_BTN_requestJoin.setEnabled(false);
                Toast.makeText(getActivity(), "request to join send", Toast.LENGTH_SHORT).show();
            }
        });
        team_BTN_inviteGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
                team_BTN_inviteGame.setEnabled(false);
                Toast.makeText(getActivity(), "request for match game send", Toast.LENGTH_SHORT).show();
            }

        });


    }

    private void sendRequest() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("request");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", user.getId());
        hashMap.put("nameSender", user.getUserName());
        hashMap.put("receiver", team.getTheManager());
        hashMap.put("fromTeam", user.getNameClub());
        hashMap.put("toTeam", team.getName());
        hashMap.put("seen", false);
        hashMap.put("confirmApplication", false);
        Request request = new Request(user.getId(), team.getTheManager(), user.getNameClub(), team.getName(), false, false, user.getUserName());
        reference.child(team.getTheManager() + user.getId()).setValue(request);
    }


    private void clickOnEditManager() {

        team_TXT_editCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                team_ETXT_city.setEnabled(true);
                team_IMG_saveCity.setVisibility(View.VISIBLE);
            }
        });
        team_IMG_saveCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                team.setCity(team_ETXT_city.getEditText().getText().toString());
                team_ETXT_city.setHint(team.getCity());
                team_ETXT_city.setEnabled(false);
                team_IMG_saveCity.setVisibility(View.GONE);
                changeAttributeInFireBase("city", team.getCity());
            }
        });

        team_SWITCH_searchPlayer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                reference = FirebaseDatabase.getInstance().getReference("teams").child(team.getName());
                HashMap<String, Object> map = new HashMap<>();
                map.put("fullCadre", isChecked);
                reference.updateChildren(map);
            }
        });


        team_TXT_editDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                team_ETXT_description.setEnabled(true);
                team_IMG_saveDescription.setVisibility(View.VISIBLE);

            }
        });
        team_IMG_saveDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                team.setDescription(team_ETXT_description.getEditText().getText().toString());
                team_ETXT_description.setHint(team.getName());
                team_ETXT_description.setEnabled(false);
                team_IMG_saveDescription.setVisibility(View.GONE);
                changeAttributeInFireBase("description", team.getDescription());
            }
        });

        team_TXT_editPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                team_LAY_addPlayer.setVisibility(View.VISIBLE);
            }
        });
        team_IMG_addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                team.setCadre(team.getCadre() + ", " + team_ETXT_addPlayer.getEditText().getText().toString());
                team_TXT_playerTeam.setText("player name : " + team.getCadre());
                team_LAY_addPlayer.setVisibility(View.GONE);
                changeAttributeInFireBase("cadre", team.getCadre());
            }
        });

        team_TXT_editSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentSymbol_LST_teams.setVisibility(View.VISIBLE);
                Adapter_SymbolTeam adapter = new Adapter_SymbolTeam(getActivity(), nameSymbols);
                adapter.setClickListener(new Adapter_SymbolTeam.ItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        team.setNameSymbol(nameSymbols.get(position));
                        Toast.makeText(getActivity(), "A group is selected", Toast.LENGTH_SHORT).show();
                        team_IMG_symbol.setImageResource(getResources().getIdentifier(nameSymbols.get(position), "drawable", getContext().getPackageName()));
                        changeAttributeInFireBase("nameSymbol", team.getNameSymbol());
                        fragmentSymbol_LST_teams.setVisibility(View.GONE);
                    }
                });
                fragmentSymbol_LST_teams.setAdapter(adapter);
            }
        });

        popuprequset_BTN_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFromManagerAnswer(true, true);

                team.setCadre(team.getCadre() + ", " + request.getNameSender());
                changeAttributeInFireBase("cadre", team.getCadre());
                team_TXT_playerTeam.setText("player name : " + team.getCadre());
                dialogNewRequsetJoin.dismiss();
                reference = FirebaseDatabase.getInstance().getReference("users").child(request.getSender());
                HashMap<String, Object> map = new HashMap<>();
                map.put("nameClub", team.getName());
                reference.updateChildren(map);
            }
        });
        popuprequset_BTN_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFromManagerAnswer(true, false);
                dialogNewRequsetJoin.dismiss();
            }
        });
        popuprequset_BTN_showProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = getActivity().getSharedPreferences(SP_FILE, getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("userId", request.getSender());
                editor.apply();
                dialogNewRequsetJoin.dismiss();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.teamFragment, new ProfilFragment()).commit();
            }
        });
    }

    private void changeAttributeInFireBase(String key, String value) {
        reference = FirebaseDatabase.getInstance().getReference("teams").child(team.getName());
        HashMap<String, Object> map = new HashMap<>();
        map.put(key, value);
        reference.updateChildren(map);
    }

    private void updateFromManagerAnswer(boolean seen, boolean confirmApplication) {
        reference = FirebaseDatabase.getInstance().getReference("request").child(team.getTheManager());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("seen", seen);
        hashMap.put("confirmApplication", confirmApplication);
        reference.updateChildren(hashMap);
    }

    private void nameSymbols(View view) {
        nameSymbols = new ArrayList<>();
        for (int i = 1; i < 31; i++) {
            String name = "symbol_" + (i);
            nameSymbols.add(i - 1, name);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(requestListener!=null)
            reference.removeEventListener(requestListener);
        if(teamListener!=null)
            reference.removeEventListener(teamListener);
    }
}
