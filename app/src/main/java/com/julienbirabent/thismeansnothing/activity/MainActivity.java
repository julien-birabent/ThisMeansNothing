package com.julienbirabent.thismeansnothing.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.julienbirabent.thismeansnothing.R;
import com.julienbirabent.thismeansnothing.adapter.AudioTrackAdapter;
import com.julienbirabent.thismeansnothing.model.AudioTrack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    // La liste contenant nos pistes audio
    private static ArrayList<AudioTrack> audioTracks;
    private  ListView audioTracksView;

    private final static int EXTERNAL_STORAGE_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        // toujours instancier un objet avant de pouvoir l'utiliser
        audioTracks = new ArrayList<AudioTrack>();
        // Il nous faut les permissions avant sinon ca crash
        requestPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    // On a besoin de pouvoir récupérer cette liste dans d'autre context
    public static ArrayList<AudioTrack>  getTrackList(){

        if(audioTracks == null){
            audioTracks = new ArrayList<AudioTrack>();
        }

        return audioTracks;
    }

    /**
     * Dans cette méthode, on affiche un message à l utilisateur lui demandant si oui ou non
     * il nous accorde les permissions demandées. onRequestPermissionsResult est appelée quand cette
     * méthode ce termine.
     */
    private void requestPermissions(){

        // Demander la permission de lire le storage externe si elle n est pas déja attribuée
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        EXTERNAL_STORAGE_CODE);

        }else{
            // all greavy baby on peut initialiser nos SHIT
            getAudioTracksFromExternalContent();
            sortTracks();
            // En gros l'adapteur custom qu on a créé contient la liste des tracks que l on veut
            // afficher. C est cet objet qui va se charger d aller mettre les informations des tracks
            // dans la liste qui s affiche à l'écran.
            AudioTrackAdapter audioTrackAdapter = new AudioTrackAdapter(this, audioTracks);
            audioTracksView.setAdapter(audioTrackAdapter);
        }

    }

    /**
     * Cette méthode est appelée quand un évnement concernant l attribution de permission
     * pendant l execution de l'application est détecté.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case EXTERNAL_STORAGE_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // all greavy baby on peut initialiser nos SHIT
                    getAudioTracksFromExternalContent();
                    sortTracks();
                    // En gros l'adapteur custom qu on a créé contient la liste des tracks que l on veut
                    // afficher. C est cet objet qui va se charger d aller mettre les informations des tracks
                    // dans la liste qui s affiche à l'écran.
                    AudioTrackAdapter audioTrackAdapter = new AudioTrackAdapter(this, audioTracks);
                    audioTracksView.setAdapter(audioTrackAdapter);

                } else {

                    // Permission DENIED = t'es NIKEEEEEEE retour à la case départ
                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory(Intent.CATEGORY_HOME);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                }
                return;
            }


        }
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

        // Quand on clique sur une track, on va lancer l'activité chargé d afficher le temps
        // et de gérer la lecture de la track
        audioTracksView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), PlayTrackActivity.class);
                // On passe en extra le id correspondant à la place de la track passée en extra
                // dans la liste de tracks
                i.putExtra("chosenTrackId",(int) view.getTag());
                startActivity(i);
            }
        });

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
                long thisDuration = musicCursor.getLong(durationColumn);
                audioTracks.add(new AudioTrack(thisId,thisTitle,thisArtist,thisDuration));
            }
            // Tant qu'il reste du contenu dans la table, on bouge le curseur à la position suivante
            // et on répète le processus
            while (musicCursor.moveToNext());
        }

    }

}
