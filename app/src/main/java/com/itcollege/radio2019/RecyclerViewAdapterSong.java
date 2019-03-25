package com.itcollege.radio2019;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecyclerViewAdapterSong extends RecyclerView.Adapter<RecyclerViewAdapterSong.ViewHolder> {
    private String[] songs;
    private LayoutInflater inflater;
    private ItemClickListener itemClickListener;
    private Context context;


    public RecyclerViewAdapterSong(Context context, String[] songs) {
        this.songs = songs;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerViewAdapterSong.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rowView = inflater.inflate(R.layout.recycler_song_row, viewGroup, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.textViewSongTitle.setText(songs[i]);
    }

    @Override
    public int getItemCount() {
        int i = 0;
        for (String song: songs) {
            i++;
        }
        return i;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewSongTitle;
        TextView textViewSongArtist;
        TextView textViewSongRadio;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSongTitle = itemView.findViewById(R.id.textViewSongName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onRecyclerViewRowClick(v, getAdapterPosition());
            }
        }
    }
    void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onRecyclerViewRowClick(View view, int position);
    }
}
