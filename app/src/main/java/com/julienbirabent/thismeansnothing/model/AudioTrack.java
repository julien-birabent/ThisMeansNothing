package com.julienbirabent.thismeansnothing.model;

import java.util.concurrent.TimeUnit;

/**
 * Created by Julien on 2017-02-18.
 * Clqsse permettqnt de stocker des informations de base concernant une piste audio
 */

public class AudioTrack {

    private long id;
    private String title;
    private String author;
    private long duration;
    private String readableDuration;


    /**
     * contructeur par défaut
     */
    public AudioTrack() {

    }

    /**
     * constructeur par copie d'attributs
     * @param id
     * @param title
     * @param author
     * @param duration
     */
    public AudioTrack(long id, String title, String author, long duration) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.duration = duration;
    }

    /**
     * fonction permettant d'avoir accès à la durée d une piste écrite dans le format hh:mm:ss
     * @return
     */
    public String getReadableDuration(){

        if(readableDuration == null){
            readableDuration = String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes(getDuration()),
                    TimeUnit.MILLISECONDS.toSeconds(getDuration()) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(getDuration()))
            );
        }
            return readableDuration;
    }

    public void setReadableDuration(String hhmmss){
        readableDuration = hhmmss;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
