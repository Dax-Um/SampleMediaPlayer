package com.example.samplemediaplayer.Models;

import android.graphics.Bitmap;

public class MusicPlayerModel {
    Integer _id;  //intent로 받음(playlist로 부터)
    String title; //intent로 받음(playlist로 부터)
    String artist; //intent로 받음(playlist로 부터)
    Long album_id; //intent로 받음(playlist로 부터)
    Long duration; // 신규 query

    //data: file path // 신규 query

    public MusicPlayerModel(Integer _id, String title, String artist,
                            Long album_id, Long duration)
    {
        this._id = _id;
        this.title = title;
        this.artist = artist;
        this.album_id = album_id;
        this.duration = duration;
    }

    public Integer getId() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public Long getAlbumId() {
        return album_id;
    }

    public Long getDuration() {return duration;}
}
