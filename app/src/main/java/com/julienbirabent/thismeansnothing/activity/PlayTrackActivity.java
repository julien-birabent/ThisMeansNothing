package com.julienbirabent.thismeansnothing.activity;

import android.app.Activity;
import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;

import com.julienbirabent.thismeansnothing.R;
import com.julienbirabent.thismeansnothing.controller.TrackController;
import com.julienbirabent.thismeansnothing.model.AudioTrack;


/**
 * Created by Julien on 2017-02-18.
 */

public class PlayTrackActivity extends Activity implements MediaController.MediaPlayerControl,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private TrackController controller;
    private AudioTrack currentTrack;
    private TextView trackInfoView;
    private static MediaPlayer player;
    private int chosenTrackId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_track);
        trackInfoView = (TextView) findViewById(R.id.playing_track_informations);
        setController();
        initMusicPlayer();


    }

    @Override
    protected void onStop() {
        super.onStop();
        player.reset();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();

        Bundle extras = getIntent().getExtras();
        // On récupère l'id de la track choisie.
        if (extras != null) {
            chosenTrackId = extras.getInt("chosenTrackId");

            // on récupère la track via l'id fourni
            currentTrack = MainActivity.getTrackList().get(chosenTrackId);
            trackInfoView.setText(currentTrack.getTitle() + ", " + currentTrack.getAuthor());
            playSong();
        }


    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        player.reset();

    }

    public void initMusicPlayer(){

        player = new MediaPlayer();
        //set player properties
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);

    }

    private void setController() {
        //set the controller up
        controller = new TrackController(this);

        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //playPrev();
            }
        });
        controller.setMediaPlayer(this);
        //controller.setAnchorView(findViewById(R.id.));
        controller.setEnabled(true);

    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();
    }

    public void playSong(){
        //play a song
        player.reset();

        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currentTrack.getId());
        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }

        player.prepareAsync();
    }
}
