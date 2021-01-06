package com.example.goalmeet.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goalmeet.Activity.MessageActivity;
import com.example.goalmeet.Adapter.Adapter_SymbolTeam;
import com.example.goalmeet.Class.Team;
import com.example.goalmeet.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.example.goalmeet.Other.Constants.SP_FILE;

public class TeamFragment extends Fragment {
    private Gson gson;
    private  Team team;
    RecyclerView.LayoutManager layoutManager;
    private RecyclerView fragmentSymbol_LST_teams;
    private ArrayList<String> nameSymbols;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private LinearLayout team_LAY_addPlayer;
    private ImageView team_IMG_symbol,team_IMG_addPlayer,team_IMG_saveDescription,team_IMG_saveCity,team_IMG_saveNameTeam,team_IMG_message;
    private TextInputLayout team_ETXT_city,team_ETXT_description,team_ETXT_addPlayer,team_ETXT_name;
    private TextView team_TXT_playerTeam,team_TXT_nameManager,team_TXT_editSymbol,team_TXT_editName,team_TXT_editCity,team_TXT_editPlayer,team_TXT_editDescription;
    private Switch setting_SWITCH_searchPlayer;
    private ArrayList<TextView> arrayOfPermissiomEdit;
    private ArrayList<TextInputLayout> arrayEditETXT;
    private boolean availableToNewPlayer = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team, container, false);
        getActivity().getFragmentManager().popBackStack();

        initView(view);
        clickOnEdit(); // if the user is manager
        return view;
    }




    private void getTeamFromJson() {

        gson = new Gson();
        SharedPreferences prefs = getActivity().getSharedPreferences(SP_FILE, getActivity().MODE_PRIVATE);
        String json = prefs.getString("theTeam", null);
            team = gson.fromJson(json, Team.class);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();


    }

    private void initView(View view) {

        arrayOfPermissiomEdit = new ArrayList<>();
        arrayEditETXT = new ArrayList<>();

        team_TXT_editCity= view.findViewById(R.id.team_TXT_editCity);
        team_ETXT_city = view.findViewById(R.id.team_ETXT_city);
        team_IMG_saveCity = view.findViewById(R.id.team_IMG_saveCity);
        arrayOfPermissiomEdit.add(team_TXT_editCity);
        arrayEditETXT.add(team_ETXT_city);

        team_TXT_nameManager = view.findViewById(R.id.team_TXT_nameManager);

        team_ETXT_name= view.findViewById(R.id.team_ETXT_name);
        team_TXT_editName= view.findViewById(R.id.team_TXT_editName);
        team_IMG_saveNameTeam = view.findViewById(R.id.team_IMG_saveNameTeam);
        arrayOfPermissiomEdit.add(team_TXT_editName);

        team_LAY_addPlayer = view.findViewById(R.id.team_LAY_addPlayer);
        team_TXT_playerTeam = view.findViewById(R.id.team_TXT_playerTeam);
        team_ETXT_addPlayer = view.findViewById(R.id.team_ETXT_addPlayer);
        team_TXT_editPlayer = view.findViewById(R.id.team_TXT_editPlayer);
        team_IMG_addPlayer = view.findViewById(R.id.team_IMG_addPlayer);
        arrayOfPermissiomEdit.add(team_TXT_editPlayer);
        arrayEditETXT.add(team_ETXT_addPlayer);

        team_IMG_message=view.findViewById(R.id.team_IMG_message);
        team_IMG_symbol = view.findViewById(R.id.team_IMG_symbol);
        team_TXT_editSymbol= view.findViewById(R.id.team_TXT_editSymbol);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        fragmentSymbol_LST_teams = view.findViewById(R.id.fragmentSymbol_LST_teams);
        fragmentSymbol_LST_teams.setLayoutManager(layoutManager);
        arrayOfPermissiomEdit.add(team_TXT_editSymbol);

        team_TXT_editDescription= view.findViewById(R.id.team_TXT_editDescription);
        team_ETXT_description= view.findViewById(R.id.team_ETXT_description);
        team_IMG_saveDescription = view.findViewById(R.id.team_IMG_saveDescription);
        arrayOfPermissiomEdit.add(team_TXT_editDescription);
        arrayEditETXT.add(team_ETXT_description);

        setting_SWITCH_searchPlayer = view.findViewById(R.id.setting_SWITCH_searchPlayer);


        getIdSymbol(view);
        getTeamFromJson();
        initTeamInView();
        permisionToUser();

    }
    private void initTeamInView() {
        Glide.with(this).load((getResources().getIdentifier(team.getNameSymbol(), "drawable", getContext().getPackageName()))).into(team_IMG_symbol);
        team_ETXT_name.setHint(" Name Club: " + team.getName());
        team_ETXT_city.setHint("City: " +team.getCity());
        team_ETXT_description.setHint("About Team: " + team.getDescription());
        team_TXT_nameManager.setText("Name Manager: " + team.getNameManager());

    }


    private void permisionToUser() {

        if(!firebaseUser.getUid().equals(team.getTheManager())){

            for (int i = 0; i < arrayOfPermissiomEdit.size(); i++) {
                arrayOfPermissiomEdit.get(i).setVisibility(View.GONE);

            }
            for (int i = 0; i <arrayEditETXT.size() ; i++) {
                arrayEditETXT.get(i).setEnabled(false);
            }
            setting_SWITCH_searchPlayer.setEnabled(false);
            team_IMG_message.setVisibility(View.VISIBLE);
        }
    }

    private void clickOnEdit() {

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
            }
        });
        team_TXT_editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                team_ETXT_name.setEnabled(true);
                team_IMG_saveNameTeam.setVisibility(View.VISIBLE);
            }
        });
        team_IMG_saveNameTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                team.setName(team_ETXT_name.getEditText().getText().toString());
                team_ETXT_name.setHint(team.getName());
                team_ETXT_name.setEnabled(false);
                team_IMG_saveNameTeam.setVisibility(View.GONE);
            }
        });
        setting_SWITCH_searchPlayer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                availableToNewPlayer = isChecked;
                Log.d("cccc","click: " + isChecked);
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
                team.addCadre(team_ETXT_addPlayer.getEditText().getText().toString());
                Log.d("cccc","111111 " );
                team_TXT_playerTeam.setText("" + team_TXT_playerTeam.getText() +team_ETXT_addPlayer.getEditText().getText().toString() + ", ");
                Log.d("cccc","22222 " );
                team_LAY_addPlayer.setVisibility(View.GONE);
                Log.d("cccc","3333" );
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
                        Log.d("bbbb", " " + nameSymbols.get(position));
                        fragmentSymbol_LST_teams.setVisibility(View.GONE);
                    }
                });
                fragmentSymbol_LST_teams.setAdapter(adapter);
            }
        });
        team_IMG_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MessageActivity.class);
                i.putExtra("userId",team.getTheManager());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(i);

            }
        });
    }

    private void getIdSymbol(View view) {
        nameSymbols = new ArrayList<>();
        for (int i = 1; i < 31; i++) {
            String name = "symbol_" + (i);
            nameSymbols.add(i - 1, name);
        }
    }
}
