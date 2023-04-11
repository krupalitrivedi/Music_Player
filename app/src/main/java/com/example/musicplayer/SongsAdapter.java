package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SongsAdapter extends ArrayAdapter<Song> {




    public SongsAdapter(@NonNull Context context, @NonNull List<Song> objects) {
        super(context, 0, objects);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, null);
        TextView songTitle = convertView.findViewById(R.id.songTitle);
        TextView songArtist = convertView.findViewById(R.id.songArtist);

        Song song = getItem(position);
        songTitle.setText(song.getTitle());
        songArtist.setText(song.getArtist());


        return convertView;

    }
}