package com.example.samplemediaplayer.models;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.samplemediaplayer.utils.DataUtil;

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
    @ColumnInfo(name = "data")
    public String file_path;

    public MusicListItemModel(Integer _id, String title, String artist, Long album_id, String file_path)
    {
        this._id = _id;
        this.title = title;
        this.artist = artist;
        this.album_id = album_id;
        this.file_path = file_path;
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

    public String getFile_path() {return file_path;}
}

