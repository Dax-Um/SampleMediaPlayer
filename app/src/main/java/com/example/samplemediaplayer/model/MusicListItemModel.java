package com.example.samplemediaplayer.model;

import android.graphics.Bitmap;

public class MusicListItemModel {
    String _id;
    String title;
    String artist;
    Bitmap albumArt;

    public MusicListItemModel(String _id, String title, String artist, Bitmap albumArt)
    {
        this._id = _id;
        this.title = title;
        this.artist = artist;
        this.albumArt = albumArt;
    }

    public String getId() {
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
}

