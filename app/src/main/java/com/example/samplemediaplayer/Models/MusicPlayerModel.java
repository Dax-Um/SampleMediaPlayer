package com.example.samplemediaplayer.Models;

import android.graphics.Bitmap;

public class MusicPlayerModel {
    Long _id;
    String title;
    String artist;
    Bitmap albumArt;
    Long duration;

    public MusicPlayerModel(Long _id, String title, String artist,
                              Bitmap albumArt, Long duration)
    {
        this._id = _id;
        this.title = title;
        this.artist = artist;
        this.albumArt = albumArt;
        this.duration = duration;
    }

    public Long getId() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public Bitmap getAlbumArt() {
        return albumArt;
    }

    public Long getDuration() {return duration;}
}
