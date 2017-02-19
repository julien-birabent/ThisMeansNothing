package com.julienbirabent.thismeansnothing.activity;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
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

    // L'objet qui va permettre de controller le media player en fesant le lien entre l'interface
    // et le media player
    private TrackController controller;
    // La track choisie
    private AudioTrack currentTrack;
    // La view contenant les info de la track jouée
    private TextView trackInfoView;
    // L objet permettant de lire l ressource audio
    private static MediaPlayer player;
    // la position de la track choisie dans la liste contenant toutes les tracks
    private int chosenTrackId;
    private View mMediaControllerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play_track);
        trackInfoView = (TextView) findViewById(R.id.playing_track_informations);

        mMediaControllerView = (View)findViewById(R.id.media_controller);

        initMusicPlayer();

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PlayTrackActivity.this, MainActivity.class);
        startActivity(intent);

        finish();
    }


    @Override
    protected void onStop() {
        super.onStop();
        player.reset();
        player.release();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();

        //setController();

        Bundle extras = getIntent().getExtras();
        // On récupère l'id de la track choisie.
        if (extras != null) {
            chosenTrackId = extras.getInt("chosenTrackId");
            actualiserCeQuiJoue();

        }


    }

    private void actualiserCeQuiJoue(){
        // on récupère la track via l'id fourni
        currentTrack = MainActivity.getTrackList().get(chosenTrackId);
        // On affiche la track en cours
        trackInfoView.setText(currentTrack.getTitle() + ", " + currentTrack.getAuthor());
        // On joue le fichier audio
        playSong();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    public void initMusicPlayer(){

        player = new MediaPlayer();
        //set player properties
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);

    }

    private void setController() {
        //set the controller up
        controller = new TrackController(this);

        controller.setMediaPlayer(this);
        controller.setAnchorView(mMediaControllerView);
        controller.setEnabled(true);
        controller.show(0);

        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();

            }
        });



    }

    public void playPrev(){
        //Si la track audio choisie n est pas la premiere de la liste
        if(chosenTrackId != 0){
            // on décrémente notre indice de liste
            chosenTrackId--;
        }else{
            chosenTrackId = MainActivity.getTrackList().size()-1;
        }
        actualiserCeQuiJoue();

    }

    public void playNext(){
        // si la track choisie n est pas en bout de liste
        if(chosenTrackId != MainActivity.getTrackList().size()-1){
            chosenTrackId++;
        }else{
            chosenTrackId = 0;
        }
        actualiserCeQuiJoue();
    }

    @Override
    public void start() {
        if(!isPlaying()) {
            player.start();
        }
    }

    @Override
    public void pause() {
        if(player.isPlaying()) {
            player.pause();
        }
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return 0;// player.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        player.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.reset();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        mp.reset();
        mp.release();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        setController();
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
