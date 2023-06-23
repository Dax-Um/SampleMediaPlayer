package com.example.samplemediaplayer.Models;

public class MiniPlayerModel {
    Integer _id;
    String albumArt;

    public MiniPlayerModel(Integer _id, String albumArt)
    {
        this._id = _id;
        this.albumArt = albumArt;
    }

    public Integer getId() {
        return _id;
    }

    public String getAlbumArt() {
        return albumArt;
    }
}
