package com.julienbirabent.thismeansnothing.model;

/**
 * Created by Julien on 2017-02-18.
 * Clqsse permettqnt de stocker des informations de base concernant une piste audio
 */

public class AudioTrack {

    private long id;
    private String title;
    private String author;
    private double duration;


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
    public AudioTrack(long id, String title, String author, double duration) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.duration = duration;
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

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
