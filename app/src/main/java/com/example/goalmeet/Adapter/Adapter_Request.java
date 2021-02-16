package com.example.goalmeet.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goalmeet.Class.Game;
import com.example.goalmeet.Class.Request;
import com.example.goalmeet.Class.Team;
import com.example.goalmeet.Class.User;
import com.example.goalmeet.R;
import com.example.goalmeet.fragment.ProfilFragment;
import com.example.goalmeet.fragment.TeamFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.lang.ref.Reference;
import java.util.HashMap;
import java.util.List;

import static com.example.goalmeet.Other.Constants.SP_FILE;


public class Adapter_Request extends RecyclerView.Adapter<Adapter_Request.MyViewHolder> {
    private User user;
    private List<Request> requests;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private DatabaseReference reference;
    private FragmentManager fragmentManager;
    private Team team;;
    // data is passed into the constructor
    public Adapter_Request(Context context, List<Request> requests, User user, FragmentManager fragmentManager) {
        this.mInflater = LayoutInflater.from(context);
        this.requests = requests;
        this.user = user;
        this.fragmentManager = fragmentManager;

    }

    // inflates the row layout from xml when needed
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycleview_requests, parent, false);
        return new MyViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Request request = requests.get(position);
        if(request.getFromTeam().equals("")) {//it's request from player that want to join the club
            holder.recycleViewRequest_IMG_picture.setImageResource(R.drawable.add_user);
            holder.recycleViewRequest_TXT_data.setText(" You have a new request to join from " + (request).getNameSender() + ". Do you approve? ");
            holder.recycleViewRequest_BTN_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveToProfileFragment(position);
                }
            });

        }else{//it's request from club that want to meet for play
            reference=  FirebaseDatabase.getInstance().getReference("teams").child(request.getFromTeam());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    team = snapshot.getValue(Team.class);

                    holder.recycleViewRequest_TXT_data.setText("You have a new request to play front "+request.getFromTeam()+" team. Would you like to confirm the meeting?");
                    holder.recycleViewRequest_IMG_picture.setImageResource(R.drawable.support__1_);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            holder.recycleViewRequest_BTN_show.setOnClickListener(new View.OnClickListener() {//if the user clickOn show team
                @Override
                public void onClick(View v) {
                    moveToTeamFragment();
                }
            });




        }
        clickOnButton(request,holder.recycleViewRequest_BTN_confirm, true, true, position, holder.recycleViewRequest_LAY_cover);
        clickOnButton(request,holder.recycleViewRequest_BTN_remove,true,false,position,holder.recycleViewRequest_LAY_cover);

    }


    private void clickOnButton(Request request,Button button,boolean seen, boolean confirmApplication,int position,LinearLayout layCover ){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFromManagerAnswer(seen,confirmApplication,position);
                layCover.setVisibility(View.GONE);
                if(confirmApplication == true&&request.getFromTeam().equals(""))
                    addUserToTeam(position);
                else if(confirmApplication == true&&!request.getFromTeam().equals("")){
                    addNewGameToListOfGames(request);
                }
            }
        });

    }
    private void addUserToTeam(int position) {
        reference = FirebaseDatabase.getInstance().getReference("users").child(requests.get(position).getSender());
        HashMap<String, Object> map = new HashMap<>();
        map.put("nameClub", team.getName());
        reference.updateChildren(map);
    }
    private void addNewGameToListOfGames(Request request) {

        reference = FirebaseDatabase.getInstance().getReference("games");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("homeClub", request.getFromTeam());
        hashMap.put("homeClubManager", request.getSender());
        hashMap.put("awayClub", request.getToTeam());
        hashMap.put("awayClubManager", request.getReceiver());
        hashMap.put("result", "");
        Game game = new Game( request.getFromTeam(),request.getToTeam(),"",request.getReceiver(), request.getSender());
        reference.child( request.getFromTeam()+"vs"+request.getToTeam()).setValue(game);
    }

    private void moveToTeamFragment() {
        SharedPreferences prefs = mInflater.getContext().getSharedPreferences(SP_FILE, mInflater.getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String teamToString = gson.toJson(team);
        String userToString = gson.toJson(user);
        editor.putString("pressOnTeam", teamToString);
        editor.putString("theUser",userToString);
        editor.apply();
        fragmentManager.beginTransaction().replace(R.id.requestFragment, new TeamFragment()).commit();
    }
    private void moveToProfileFragment(int position){
        SharedPreferences prefs = mInflater.getContext().getSharedPreferences(SP_FILE, mInflater.getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userId" , requests.get(position).getSender());
        editor.apply();
        fragmentManager.beginTransaction().replace(R.id.requestFragment, new ProfilFragment()).commit();
    }


    private void updateFromManagerAnswer( boolean seen, boolean confirmApplication,int position) {
        String key = requests.get(position).getReceiver()+requests.get(position).getSender();
        reference = FirebaseDatabase.getInstance().getReference("request").child(key);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("seen", seen);
        hashMap.put("confirmApplication", confirmApplication);
        reference.updateChildren(hashMap);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return requests.size();
    }


    // stores and recycles views as they are scrolled off screen


    // convenience method for getting data at click position
    Object getItem(int id) {
        return requests.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView recycleViewRequest_IMG_picture;
        TextView recycleViewRequest_TXT_data;
        Button recycleViewRequest_BTN_confirm;
        Button recycleViewRequest_BTN_show;
        Button recycleViewRequest_BTN_remove;
        LinearLayout recycleViewRequest_LAY_cover;


        MyViewHolder(View itemView) {
            super(itemView);
            recycleViewRequest_IMG_picture = itemView.findViewById(R.id.recycleViewRequest_IMG_picture);
            recycleViewRequest_TXT_data = itemView.findViewById(R.id.recycleViewRequest_TXT_data);
            recycleViewRequest_BTN_remove = itemView.findViewById(R.id.recycleViewRequest_BTN_remove);
            recycleViewRequest_BTN_confirm = itemView.findViewById(R.id.recycleViewRequest_BTN_confirm);
            recycleViewRequest_BTN_show = itemView.findViewById(R.id.recycleViewRequest_BTN_show);
            recycleViewRequest_LAY_cover = itemView.findViewById(R.id.recycleViewRequest_LAY_cover);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mClickListener != null) {
                        mClickListener.onItemClick(view, getAdapterPosition());
                    }

                }
            });
        }
    }
}



