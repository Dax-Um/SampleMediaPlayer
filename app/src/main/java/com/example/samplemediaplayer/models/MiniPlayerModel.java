package com.example.samplemediaplayer.models;

import android.graphics.Bitmap;

import com.example.samplemediaplayer.R;

public class MiniPlayerModel {
    int _id;  //intent로 받음(playlist로 부터)
    String title;
    String artist;
    Bitmap album_art;
    String file_path;
    int res_id;

    public MiniPlayerModel(int _id, String title, String artist,
                           Bitmap album_art, String file_path)
    {
        this._id = _id;
        this.title = title;
        this.artist = artist;
        this.album_art = album_art;
        this.file_path = file_path;
    }

    public int getId() {
        return this._id;
    }
    public String getTitle() {
        return this.title;
    }
    public String getArtist() {return this.artist;}
    public Bitmap getAlbumArt() {
        return this.album_art;
    }
    public String getFile_path() {return file_path;}
    public int getRes(){return res_id = R.drawable.default_album_art;}
}
