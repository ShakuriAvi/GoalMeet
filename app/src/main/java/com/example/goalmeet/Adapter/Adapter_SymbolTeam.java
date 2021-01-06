package com.example.goalmeet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goalmeet.R;

import java.util.List;

public class Adapter_SymbolTeam extends RecyclerView.Adapter<Adapter_SymbolTeam.MyViewHolderSymbol>  {

    private List<String> nameSymbols;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public Adapter_SymbolTeam(Context context, List<String> nameSymbol) {
        this.mInflater = LayoutInflater.from(context);
        this.nameSymbols = nameSymbol;
    }

    // inflates the row layout from xml when needed
    @Override
    public MyViewHolderSymbol onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycleview_symbol_team, parent, false);

        return new MyViewHolderSymbol(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderSymbol holder, int position) {
        String symbol = nameSymbols.get(position);
        int id = (mInflater.getContext().getResources().getIdentifier(symbol, "drawable", (mInflater.getContext().getPackageName())));
        Glide.with(mInflater.getContext()).load(id).into(holder.symbolTeams_IMG_symbol);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return nameSymbols.size();
    }


    // stores and recycles views as they are scrolled off screen


    // convenience method for getting data at click position
    String getItem(int id) {
        return nameSymbols.get(id);
    }

    // allows clicks events to be caught
   public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolderSymbol extends RecyclerView.ViewHolder {



        ImageView symbolTeams_IMG_symbol;



        MyViewHolderSymbol(View itemView) {
            super(itemView);

            symbolTeams_IMG_symbol = itemView.findViewById(R.id.symbolTeams_IMG_symbol);
            itemView.setOnClickListener(new View.OnClickListener(){
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
