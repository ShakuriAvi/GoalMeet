package com.example.goalmeet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.goalmeet.Class.Game;
import com.example.goalmeet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class Adapter_Games extends RecyclerView.Adapter<Adapter_Games.MyViewHolder> {


    private List<Game> games;
    private LayoutInflater mInflater;
    private com.example.goalmeet.Adapter.Adapter_Teams.ItemClickListener mClickListener;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;

    // data is passed into the constructor
    public Adapter_Games(Context context, List<Game> games) {
        this.mInflater = LayoutInflater.from(context);
        this.games = games;
    }

    // inflates the row layout from xml when needed
    @Override
    public Adapter_Games.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycleview_list_team, parent, false);
        return new MyViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Game game = games.get(position);
        if(!game.getResult().equals("")){
            holder.recycleTeams_TXT_result.setText(game.getResult());
            holder.recycleTeams_TXT_result.setTextSize(20);
        }

        if (firebaseUser.getUid().equals(game.getAwayClubManager()) || firebaseUser.getUid().equals(game.getHomeClubManager())&&game.getResult().equals("")) {
            holder.recycleTeams_TXT_editResult.setVisibility(View.VISIBLE);

        }
        holder.recycleTeams_TXT_editResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.recycleTeams_ETXT_score1.setVisibility(View.VISIBLE);
                holder.recycleTeams_ETXT_score2.setVisibility(View.VISIBLE);
                holder.recycleTeams_IMG_saveResult.setVisibility(View.VISIBLE);
            }
        });
            holder.recycleTeams_IMG_saveResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( holder.recycleTeams_ETXT_score1.getText().toString().equals("")|| holder.recycleTeams_ETXT_score2.getText().toString().equals(""))
                        Toast.makeText(mInflater.getContext(), "Please fill in the game result correctly ", Toast.LENGTH_SHORT).show();
                    reference = FirebaseDatabase.getInstance().getReference("games").child(game.getHomeTeam()+"vs"+game.getAwayTeam());
                    HashMap<String, Object> hashMap = new HashMap<>();
                    String updateResult = holder.recycleTeams_ETXT_score1.getText().toString()+ " : "+ holder.recycleTeams_ETXT_score2.getText().toString();
                    hashMap.put("result",updateResult );
                    reference.updateChildren(hashMap);
                    holder.recycleTeams_TXT_result.setText(updateResult);
                    holder.recycleTeams_TXT_result.setTextSize(20);
                    holder.recycleTeams_ETXT_score1.setVisibility(View.GONE);
                    holder.recycleTeams_ETXT_score2.setVisibility(View.GONE);
                    holder.recycleTeams_IMG_saveResult.setVisibility(View.GONE);
                }
            });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return games.size();
    }


    // stores and recycles views as they are scrolled off screen


    // convenience method for getting data at click position
    Game getItem(int id) {
        return games.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(com.example.goalmeet.Adapter.Adapter_Teams.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView recycleTeams_IMG_symbolteam1;
        ImageView recycleTeams_IMG_symbolteam2;
        ImageView recycleTeams_IMG_saveResult;
        TextView recycleTeams_TXT_team1;
        TextView recycleTeams_TXT_team2;
        EditText recycleTeams_ETXT_score1;
        EditText recycleTeams_ETXT_score2;
        EditText recycleTeams_TXT_result;
        EditText recycleTeams_TXT_editResult;


        MyViewHolder(View itemView) {
            super(itemView);
            recycleTeams_IMG_symbolteam1 = itemView.findViewById(R.id.recycleTeams_IMG_symbolteam1);
            recycleTeams_IMG_symbolteam2 = itemView.findViewById(R.id.recycleTeams_IMG_symbolteam2);
            recycleTeams_IMG_saveResult = itemView.findViewById(R.id.recycleTeams_IMG_saveResult);
            recycleTeams_TXT_team1 = itemView.findViewById(R.id.recycleTeams_TXT_team1);
            recycleTeams_TXT_team2 = itemView.findViewById(R.id.recycleTeams_TXT_team2);
            recycleTeams_TXT_result = itemView.findViewById(R.id.recycleTeams_TXT_result);
            recycleTeams_TXT_editResult = itemView.findViewById(R.id.recycleTeams_TXT_editResult);
            recycleTeams_ETXT_score1 = itemView.findViewById(R.id.recycleTeams_ETXT_score1);
            recycleTeams_ETXT_score2 = itemView.findViewById(R.id.recycleTeams_ETXT_score2);
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





