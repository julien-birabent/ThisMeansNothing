package com.julienbirabent.thismeansnothing.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.julienbirabent.thismeansnothing.R;
import com.julienbirabent.thismeansnothing.model.AudioTrack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    // La liste contenant nos pistes audio
    private ArrayList<AudioTrack> audioTracks;
    private ListView audioTracksView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        // toujours instancier un objet avant de pouvoir l'utiliser
        audioTracks = new ArrayList<AudioTrack>();

        getAudioTracksFromExternalContent();

        Collections.sort(audioTracks, new Comparator<AudioTrack>(){
            public int compare(AudioTrack a, AudioTrack b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });

    }

    /**
     * On trie nos tracks par ordre alphabétique par ce que c est ca qu est bon
     */
    private void sortTracks(){

        // On s évite des problemes si jamais la liste est pas initialiser
        if(audioTracks != null) {
            Collections.sort(audioTracks, new Comparator<AudioTrack>() {
                public int compare(AudioTrack a, AudioTrack b) {
                    return a.getTitle().compareTo(b.getTitle());
                }
            });
        }
    }

    /**
     * Méthode privée servant juste à encapsuler l'initialisation des views de l'activity
     */
    private void initializeViews() {

        // Ici on dit au programme que notre ListView (l'objet) est associé au layout ayant
        // l'id audio_tracks_list (layout = ce qu on voit à l'écran)
        audioTracksView = (ListView) findViewById(R.id.audio_tracks_list);


    }

    /**
     * Méthoce récupérant les tracks audio présent sur la mémoire externe
     */
    public void getAudioTracksFromExternalContent() {

        ContentResolver musicResolver = getContentResolver();
        // Ici on récupère l'uri, c est à dire l adresse de la ressource que l on veut avoir
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        // Ensuite en fesant une requête avec cet uri et le contentResolver, on récupère
        // les tracks audio présentes
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        //Si la sélection de tracks n est pas null et que le curseur est placé au début de la table
        // qui contient les tracks que l on vient de demander
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //On récupère les noms de chaque colonne qui nous intéresse
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int durationColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

            // Pour chaque ligne de la table, on récupère les données de chaque colonne qui nous
            // intéressent et après on stocke ces informations dans notre objet modèle AudioTrack
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                double thisDuration = musicCursor.getDouble(durationColumn);
                audioTracks.add(new AudioTrack(thisId,thisTitle,thisArtist,thisDuration));
            }
            // Tant qu'il reste du contenu dans la table, on bouge le curseur à la position suivante
            // et on répète le processus
            while (musicCursor.moveToNext());
        }

    }

}
