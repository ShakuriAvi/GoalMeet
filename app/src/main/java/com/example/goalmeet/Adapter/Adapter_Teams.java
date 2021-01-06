                                   package com.example.goalmeet.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goalmeet.Class.Team;
import com.example.goalmeet.Class.User;
import com.example.goalmeet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


    public class Adapter_Teams extends RecyclerView.Adapter<Adapter_Teams.MyViewHolder> {

        private List<Team> teams;
        private LayoutInflater mInflater;
        private ItemClickListener mClickListener;
        private DatabaseReference reference;
        private FirebaseUser firebaseUser;

        // data is passed into the constructor
        public Adapter_Teams(Context context, List<Team> teams) {
            this.mInflater = LayoutInflater.from(context);
            this.teams = teams;
        }

        // inflates the row layout from xml when needed
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.recycleview_list_team, parent, false);
            return new MyViewHolder(view);
        }

        // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Team team = teams.get(position);
            holder.listTeam_TXT_name.setText("Name team: " + team.getName());
            holder.listTeam_TXT_city.setText("City: " + team.getCity());
            holder.listTeam_TXT_description.setText("About Team" + team.getDescription());
            int id = (mInflater.getContext().getResources().getIdentifier(team.getNameSymbol(), "drawable", (mInflater.getContext().getPackageName())));
            Glide.with(mInflater.getContext()).load(id).into(holder.listTeam_IMG_symbol);

            Log.d("bbbb", "444444 " + team.getTheManager());
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("users").child(team.getTheManager());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    Log.d("bbbb", "444444 " + user.getUserName());
                    holder.listTeam_TXT_nameManager.setText("Manager Team: " + user.getUserName());
                    team.setNameManager(user.getUserName());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        // total number of rows
        @Override
        public int getItemCount() {
            return teams.size();
        }


        // stores and recycles views as they are scrolled off screen


        // convenience method for getting data at click position
        Team getItem(int id) {
            return teams.get(id);
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


            ImageView listTeam_IMG_symbol;
            TextView listTeam_TXT_city;
            TextView listTeam_TXT_name;
            TextView listTeam_TXT_description;
            TextView listTeam_TXT_nameManager;


            MyViewHolder(View itemView) {
                super(itemView);
                listTeam_IMG_symbol = itemView.findViewById(R.id.recyclelistTeam_IMG_symbol);
                listTeam_TXT_city = itemView.findViewById(R.id.recyclelistTeam_TXT_city);
                listTeam_TXT_name = itemView.findViewById(R.id.recyclelistTeam_TXT_name);
                listTeam_TXT_description = itemView.findViewById(R.id.recyclelistTeam_TXT_description);
                listTeam_TXT_nameManager = itemView.findViewById(R.id.recyclelistTeam_TXT_nameManager);
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



