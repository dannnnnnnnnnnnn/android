package com.itcollege.radio2019;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {
    private MediaPlayer mMediaPlayer = new MediaPlayer();
    static final String TAG = MusicService.class.getSimpleName();
    private String mMediaSource = "";
    private ScheduledExecutorService mScheduledExecutorService;
    private String endPoint;
    private Song mLastSong;
    private SongRepository mSongRepository;
    private String mCurrentRadioName;
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.reset();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); mSongRepository = new SongRepository(this);
        mSongRepository.open();

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        if (intent == null) {
            Log.e(TAG, "Intent was null");
            return Service.START_NOT_STICKY;
        }
        mMediaPlayer.reset();
        //no magic strings!

        endPoint = intent.getExtras().getString(C.SERVICE_MEDIASOURCE_JSON);
        mMediaSource = "http://sky.babahhcdn.com/" + intent.getExtras().getString(C.SERVICE_MEDIASOURCE_KEY);
        mCurrentRadioName = intent.getStringExtra(C.SERVICE_MEDIASOURCE_KEY);

         try {
             mMediaPlayer.setDataSource(mMediaSource);
             //cancel all current requests
             WebApiSingletonServiceHandler.getInstance(getApplicationContext()).cancelRequestQueue(C.MUSICSERVICE_VOLLEYTAG);
             mMediaPlayer.prepareAsync();
             Intent intentInformActivity = new Intent(C.MUSICSERVICE_INTENT_BUFFERING);
             LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intentInformActivity);
         }catch (IOException e) {
             e.printStackTrace();
         }
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    // ======================== MEDIAPLAYER LIFECYCLE ==================
    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "onCompletion");
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "onError");
        return false;
    }
    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "onPrepared");
        mMediaPlayer.start();
        Intent intentInformActivity = new Intent(C.MUSICSERVICE_INTENT_PLAYING);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intentInformActivity);
        StartMediaInfoGathering();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
        mScheduledExecutorService.shutdown();
        Intent intentInformActivity = new Intent(C.MUSICSERVICE_INTENT_STOPPED);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intentInformActivity);
        mScheduledExecutorService.shutdown();
        mSongRepository.close();
    }

    private void StartMediaInfoGathering() {
        mScheduledExecutorService = Executors.newScheduledThreadPool(5);
        mScheduledExecutorService.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        new GetSongInfo().execute();
                    }
                }, 0, 15, TimeUnit.SECONDS
        );
    }

    private class GetSongInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "http://dad.akaver.com/api/SongTitles/" + endPoint;
            StringRequest stringRequest = new StringRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse" + response);

                            //parse json

                            try {
                                JSONObject jsonObjectStationInfo = new JSONObject(response);
                                JSONArray jsonArraySongHistory = jsonObjectStationInfo.getJSONArray("SongHistoryList");
                                JSONObject jsonArraySongInfo = jsonArraySongHistory.getJSONObject(0);

                                String artist = jsonArraySongInfo.getString("Artist");
                                String trackTitle = jsonArraySongInfo.getString("Title");

                                Intent sendSongInfoIntent = new Intent(C.MUSICSERVICE_INTENT_SONGINFO);
                                sendSongInfoIntent.putExtra(C.MUSICSERVICE_ARTIST, artist);
                                sendSongInfoIntent.putExtra(C.MUSICSERVICE_TRACKTITLE, trackTitle);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(sendSongInfoIntent);

                                Song currentSong = new Song(artist, trackTitle, mCurrentRadioName);
                                if (mLastSong == null) {
                                    mLastSong = currentSong;
                                    mSongRepository.add(mLastSong);
                                }
                                if (!currentSong.artist.equals(mLastSong.artist) && !currentSong.title.equals(mLastSong.title)) {
                                    mSongRepository.add(currentSong);
                                    mLastSong = currentSong;
                                }
                            } catch (JSONException e) {
                                Log.d(TAG, e.getMessage());
                                e.printStackTrace();
                            }
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onErrorResponse" + error.getMessage());
                        }
                    }
            );
                stringRequest.addMarker(C.MUSICSERVICE_VOLLEYTAG);
                //add the request to volley queue
                WebApiSingletonServiceHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

                return null;
        }
    }
}
