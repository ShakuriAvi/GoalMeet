package com.example.goalmeet.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.HashMap;

import static com.example.goalmeet.Other.Constants.SP_FILE;


public class CreateTeamFragment extends Fragment {
    private Team team;
    private ArrayList<String> nameSymbol;
    private RecyclerView fragmentSymbol_LST_teams;
    RecyclerView.LayoutManager layoutManager;
    private TextInputLayout fragmentCreate_ETXT_fullName,fragmentCreate_ETXT_city,fragmentCreate_ETXT_description;
    private Button fragmentCreate_BTN_chooseSymbol;
    private String symbolChoice;
    private SharedPreferences prefs;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_team, container, false);
        initFindView(view);
        initTeam();
        return view;
    }
    private void initFindView(View view) {
        getNameSymbol(view);

        fragmentCreate_ETXT_city = view.findViewById(R.id.fragmentCreate_ETXT_city);
        fragmentCreate_ETXT_fullName =view.findViewById(R.id.fragmentCreate_ETXT_fullName);
        fragmentCreate_ETXT_description =view.findViewById(R.id.fragmentCreate_ETXT_description);
        fragmentCreate_BTN_chooseSymbol = view.findViewById(R.id.fragmentCreate_BTN_chooseSymbol);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        fragmentSymbol_LST_teams = view.findViewById(R.id.fragmentSymbol_LST_teams);
        fragmentSymbol_LST_teams.setLayoutManager(layoutManager);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        Adapter_SymbolTeam adapter = new Adapter_SymbolTeam(getActivity(), nameSymbol);
        adapter.setClickListener(new Adapter_SymbolTeam.ItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                symbolChoice = nameSymbol.get(position);
                Toast.makeText(getActivity(), "A Symbol group is selected", Toast.LENGTH_SHORT).show();
                Log.d("bbbb", " " + symbolChoice);
            }
        });
        fragmentSymbol_LST_teams.setAdapter(adapter);


    }
    private void initTeam() {

        fragmentCreate_BTN_chooseSymbol.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String fullName = fragmentCreate_ETXT_fullName.getEditText().getText().toString();
                String city = fragmentCreate_ETXT_city.getEditText().getText().toString();
                String description = fragmentCreate_ETXT_description.getEditText().getText().toString();
                if (city.equals(null) || fullName.equals(null) || symbolChoice.equals(null))
                    Toast.makeText(getActivity(), "Please Fill All Fields", Toast.LENGTH_SHORT).show();


                else {
                    createNewTeam(fullName, city, symbolChoice, firebaseUser.getUid(), description);
                    Gson gson = new Gson();
                    String teamToString = gson.toJson(team);
                    SharedPreferences prefs = getActivity().getSharedPreferences(SP_FILE,getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("pressOnTeam" , teamToString);
                    editor.putBoolean("userIsManager",true);
                    editor.apply();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.CreateTeamFragment, new TeamFragment()).commit();
                }
            }

        });

    }



    private void getNameSymbol(View view) {
        nameSymbol = new ArrayList<>();
        for (int i = 1; i < 31; i++) {
            String name = "symbol_" + (i);
            nameSymbol.add(i - 1, name);

        }
    }

    private void createNewTeam(String name, String city, String idSymbol, String theManager, String description) {

        reference = FirebaseDatabase.getInstance().getReference("teams");

        HashMap<String, Object> hashMap = new HashMap<>();
        prefs  = getActivity().getSharedPreferences(SP_FILE, getActivity().MODE_PRIVATE);
        String nameManager = prefs.getString("nameOfUser",null);
        hashMap.put("name", name);
        hashMap.put("city", city);
        hashMap.put("nameSymbol", idSymbol);
        hashMap.put("theManager", theManager);
        hashMap.put("description", description);
        hashMap.put("nameManager",nameManager);
        hashMap.put("fullCadre",false);
        hashMap.put("cadre",nameManager);
        team = new Team(name,city,idSymbol,theManager,description,nameManager,false,theManager);
        reference.child(name).setValue(team);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();

        reference=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMapUsers = new HashMap<>();
        hashMapUsers.put("nameClub",name);
        hashMapUsers.put("isManager",true);
        reference.updateChildren(hashMapUsers);


    }


}
