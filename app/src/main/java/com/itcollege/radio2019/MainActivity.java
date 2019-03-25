package com.itcollege.radio2019;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, RecyclerViewAdapterRadio.ItemClickListener {
    static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mBroadCastReceiver;
    ImageButton mButtonControlMusic;
    private int mMusicPlayerStatus = C.MUSICSERVICE_STOPPED;
    private String mArtist;
    private String mTrackTitle;
    private String mRadioName;
    private String streamName;
    private String jsonName;
    private TextView mTextViewArist;
    private TextView mTextViewTitle;
    private TextView mTextViewRadioName;
    private SettingsContentObserver mSettingsContentObserver;
    private SeekBar mSeekBarAudioVolume;
    private RadioRepository mRadioRepository;
    private SongRepository mSongRepository;
    private RecyclerViewAdapterRadio radioAdapter;
    private List<Radio> radios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreateEvent");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonControlMusic =  findViewById(R.id.buttonControlMusic);
        mTextViewArist = (TextView) findViewById(R.id.textViewArtist);
        mTextViewTitle = (TextView) findViewById(R.id.textViewTitle);
        mSeekBarAudioVolume = (SeekBar) findViewById(R.id.seekBarAudioVolume);
        mTextViewRadioName = (TextView)findViewById(R.id.textViewRadio);


        AudioManager audio = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mSeekBarAudioVolume.setMax(audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC));

        mSeekBarAudioVolume.setProgress(audio.getStreamVolume(AudioManager.STREAM_MUSIC));
        mSeekBarAudioVolume.setOnSeekBarChangeListener(this);
        //2.05:36

        //Register intents we intend to receive
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(C.MUSICSERVICE_INTENT_BUFFERING);
        intentFilter.addAction(C.MUSICSERVICE_INTENT_PLAYING);
        intentFilter.addAction(C.MUSICSERVICE_INTENT_STOPPED);
        intentFilter.addAction(C.MUSICSERVICE_INTENT_SONGINFO);

        mBroadCastReceiver = new MainActivityBroadCastReceiver();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadCastReceiver, intentFilter);

        mRadioRepository = new RadioRepository(this);
        mRadioRepository.open();
        if(mRadioRepository.getAll().isEmpty()) {
            mRadioRepository.add(new Radio("NRJ", "NRJ", "NRJ"));
            mRadioRepository.add(new Radio("NRJDNB", "NRJdnb", "SPDNB"));
            mRadioRepository.add(new Radio("Skyplus", "SKYPLUS", "SP"));
            mRadioRepository.add(new Radio("SKYDance", "SKYdance", "SPDANCE"));
            mRadioRepository.add(new Radio("RetroFM", "RETRO", "RETRO"));
        }



        radios = mRadioRepository.getAll();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewRadios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        radioAdapter = new RecyclerViewAdapterRadio(this, radios);
        radioAdapter.setItemClickListener(this);
        recyclerView.setAdapter(radioAdapter);

    }
    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart");
        super.onRestart();
    }
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        mSettingsContentObserver = new SettingsContentObserver(new Handler());
        this.getApplicationContext().getContentResolver().registerContentObserver(
               android.provider.Settings.System.CONTENT_URI,
               true,
               mSettingsContentObserver
        );
    }
    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }
    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mBroadCastReceiver);
    }

    public void controlMusic(View view) {
        Log.d(TAG, "buttonControlMusicOnClick");

        if (mMusicPlayerStatus == C.MUSICSERVICE_PLAYING) {
            Intent intentStopService = new Intent(this, MusicService.class);
            this.stopService(intentStopService);
        } else if (streamName != null && jsonName != null){
            Intent intentStartService = new Intent(this, MusicService.class);
            intentStartService.putExtra(C.SERVICE_MEDIASOURCE_KEY, streamName);
            intentStartService.putExtra(C.SERVICE_MEDIASOURCE_JSON, jsonName);
            this.startService(intentStartService);
        } else {
            Toast.makeText(this, "Select radio!", Toast.LENGTH_LONG).show();
        }
        UpdateUI();
    }
    public void buttonSongHistoryOnClick (View view) {
        Intent changeActivity = new Intent(this, SongActivity.class);
        startActivity(changeActivity);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        AudioManager audio = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_SHOW_UI);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onRecyclerViewRowClick(View view, int position) {
        Intent intentStopService = new Intent(this, MusicService.class);
        this.stopService(intentStopService);
        Radio radio = radios.get(position);
        Intent intentStartService = new Intent(this, MusicService.class);
        streamName = radio.streamName;
        jsonName = radio.jsonName;
        //intentStartService.putExtra(C.SERVICE_MEDIASOURCE_KEY, radio.streamName);
        //intentStartService.putExtra(C.SERVICE_MEDIASOURCE_JSON, radio.jsonName);
        mRadioName = radio.name;
        //this.startService(intentStartService);
        UpdateUI();
    }

    public class MainActivityBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive" + intent.getAction());
            switch(intent.getAction()) {
                case C.MUSICSERVICE_INTENT_BUFFERING:
                    mMusicPlayerStatus = C.MUSICSERVICE_BUFFERING;
                    break;
                case C.MUSICSERVICE_INTENT_PLAYING:
                    mMusicPlayerStatus = C.MUSICSERVICE_PLAYING;
                    break;
                case C.MUSICSERVICE_INTENT_STOPPED:
                    mMusicPlayerStatus = C.MUSICSERVICE_STOPPED;
                    mArtist = "";
                    mTrackTitle = "";
                    break;
                case C.MUSICSERVICE_INTENT_SONGINFO:
                    mArtist = intent.getStringExtra(C.MUSICSERVICE_ARTIST);
                    mTrackTitle = intent.getStringExtra(C.MUSICSERVICE_TRACKTITLE);
                    break;
            }
            UpdateUI();
        }
    }
    public void UpdateUI() {
        switch (mMusicPlayerStatus) {
            case C.MUSICSERVICE_STOPPED:
                mButtonControlMusic.setImageResource(R.drawable.play);
                break;
            case C.MUSICSERVICE_PLAYING:
                mButtonControlMusic.setImageResource(R.drawable.stop);
                break;
            case C.MUSICSERVICE_BUFFERING:
                mButtonControlMusic.setImageResource(R.drawable.buffering);
                break;
        }
        mTextViewArist.setText(mArtist);
        mTextViewTitle.setText(mTrackTitle);
        mTextViewRadioName.setText(mRadioName);
    }
    public class SettingsContentObserver extends ContentObserver {
        private int previousVolume;
        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public SettingsContentObserver(Handler handler) {
            super(handler);
        }
        @Override
        public void onChange(boolean selfChange) {
            Log.d(TAG, "SettingsContentObserver.onChange selfChange" + selfChange);

            AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            super.onChange(selfChange);
            Log.d(TAG, "Volume: " + currentVolume);

            if (currentVolume != previousVolume) {
                previousVolume = currentVolume;
                mSeekBarAudioVolume.setProgress(currentVolume);
            }
        }
    }
}
