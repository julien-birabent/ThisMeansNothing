package com.julienbirabent.thismeansnothing.adapter;

import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.julienbirabent.thismeansnothing.R;
import com.julienbirabent.thismeansnothing.RandomColorGenerator;
import com.julienbirabent.thismeansnothing.model.AudioTrack;

/**
 * Created by Julien on 2017-02-18.
 */

public class AudioTrackAdapter extends BaseAdapter {

    private ArrayList<AudioTrack> audioTracks;
    private LayoutInflater audioTrackInflater;
    private Context context;
    private boolean modeLicorne = false;


    public AudioTrackAdapter(Context context, ArrayList<AudioTrack> audioTracks){
        this.audioTracks = audioTracks;
        this.context = context;
        audioTrackInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return audioTracks.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //map to song layout
        LinearLayout trackLayout = (LinearLayout)audioTrackInflater.inflate
                (R.layout.audio_track, parent, false);

        //get title and artist views
        TextView tittleView = (TextView)trackLayout.findViewById(R.id.track_title);
        TextView authorView = (TextView)trackLayout.findViewById(R.id.track_author);
        TextView durationView = (TextView) trackLayout.findViewById(R.id.track_duration);
        //get song using position
        AudioTrack currentTrack = audioTracks.get(position);

        if(modeLicorne) {
            trackLayout.setBackgroundColor(RandomColorGenerator.generate());
            tittleView.setTextColor(RandomColorGenerator.generate());
            authorView.setTextColor(RandomColorGenerator.generate());
            durationView.setTextColor(RandomColorGenerator.generate());
        }

        //get title and artist strings
        tittleView.setText(currentTrack.getTitle());
        authorView.setText(currentTrack.getAuthor());
        durationView.setText(currentTrack.getReadableDuration());
        //set position as tag
        trackLayout.setTag(position);
        return trackLayout;

    }

    public void setModeLicorne(boolean rainbow){
        modeLicorne = rainbow;
    }

    public boolean getModeLicorne(){
        return modeLicorne;
    }
}
