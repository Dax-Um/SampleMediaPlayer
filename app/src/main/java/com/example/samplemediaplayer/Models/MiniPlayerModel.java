package com.example.samplemediaplayer.Models;

import android.graphics.Bitmap;

public class MiniPlayerModel {
    Long _id;
    Bitmap albumArt;

    public MiniPlayerModel(Long _id, Bitmap albumArt)
    {
        this._id = _id;
        this.albumArt = albumArt;
    }

    public Long getId() {
        return _id;
    }

    public Bitmap getAlbumArt() {
        return albumArt;
    }
}
