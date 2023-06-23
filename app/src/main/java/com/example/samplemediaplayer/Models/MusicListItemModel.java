package com.example.samplemediaplayer.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class MusicListItemModel {
    @PrimaryKey
    public int _id;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "artist")
    public String artist;
    @ColumnInfo(name = "album_id")
    public Long album_id;

    public MusicListItemModel(Integer _id, String title, String artist, Long album_id)
    {
        this._id = _id;
        this.title = title;
        this.artist = artist;
        this.album_id = album_id;
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
}

