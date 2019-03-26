package com.itcollege.radio2019;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SongActivity extends AppCompatActivity implements RecyclerViewAdapterSong.ItemClickListener, AdapterView.OnItemSelectedListener {
    private Spinner mSpinner;
    private RecyclerViewAdapterSong songAdapter;
    private List<Radio> radios;
    private SongRepository mSongRepository;
    private RadioRepository mRadioRepository;
    private TextView mUniqueSongs;
    private TextView mUniqueArtists;
    private String[] songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRadioRepository = new RadioRepository(this);
        mRadioRepository.open();
        radios = mRadioRepository.getAll();


        setContentView(R.layout.activity_songs);
        mSpinner = (Spinner) findViewById(R.id.spinnerRadio);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<Radio> adapter = new ArrayAdapter<Radio>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, radios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);
        mUniqueSongs = findViewById(R.id.textViewUniqueSongs);
        mUniqueArtists = findViewById(R.id.textViewUniqueArtists);

        mSongRepository = new SongRepository(this);
        mSongRepository.open();


    }

    @Override
    public void onRecyclerViewRowClick(View view, int position) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        songs = mSongRepository.getSongsWithCount(radios.get(position).streamName);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSongs);
        recyclerView.setLayoutManager(new LinearLayoutManager(SongActivity.this));
        songAdapter = new RecyclerViewAdapterSong(this, songs);
        songAdapter.setItemClickListener(this);
        recyclerView.setAdapter(songAdapter);
        int uniqueArtists = mSongRepository.getUniqueCount(SongDatabaseHelper.SONG_ARTIST, SongDatabaseHelper.SONG_RADIO, radios.get(position).streamName);
        int uniqueSongs = mSongRepository.getUniqueCount(SongDatabaseHelper.SONG_TITLE, SongDatabaseHelper.SONG_RADIO, radios.get(position).streamName);


        mUniqueSongs.setText(String.valueOf(uniqueSongs));
        mUniqueArtists.setText(String.valueOf(uniqueArtists));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
