package com.example.samplemediaplayer.model;

import android.graphics.Bitmap;

public class MiniPlayerModel {
    String _id;
    Bitmap albumArt;

    public MiniPlayerModel(String _id, Bitmap albumArt)
    {
        this._id = _id;
        this.albumArt = albumArt;
    }

    public String getId() {
        return _id;
    }

    public Bitmap getAlbumArt() {
        return albumArt;
    }
}
