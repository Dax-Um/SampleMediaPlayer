package com.example.samplemediaplayer.Utils;

import android.graphics.Bitmap;
import com.example.samplemediaplayer.Models.MiniPlayerModel;
import com.example.samplemediaplayer.Models.MusicListItemModel;
import com.example.samplemediaplayer.Models.MusicPlayerModel;
import com.example.samplemediaplayer.Models.SongInformationModel;

public class MusicUtil {
    MiniPlayerModel miniPlayerModel;
    MusicListItemModel musicListItemModel;
    MusicPlayerModel musicPlayerModel;
    SongInformationModel songInformationModel;
    Long _id;
    String directory;
    String title;
    String artist;
    String album;
    String genre;
    String year;
    Long duration;
    String track;
    String mime_type;
    Long size;
    Bitmap albumArt;

    public MusicUtil(Long _id, String directory, String title, String artist,
                     String album, String genre, String year, Long duration,
                     String track, String mime_type, Long size, Bitmap albumArt)
    {
        this._id = _id;
        this.directory = directory;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.year = year;
        this.duration = duration;
        this.track = track;
        this.mime_type = mime_type;
        this.size = size;
        this.albumArt = albumArt;

        miniPlayerModel = new MiniPlayerModel(_id, albumArt);
        musicListItemModel = new MusicListItemModel(_id, title, artist, albumArt);
        musicPlayerModel = new MusicPlayerModel(_id, title, artist, albumArt, duration);
        songInformationModel = new SongInformationModel(_id, directory, title, artist, album, genre,
                year, duration, track, mime_type, size);
    }

    public MiniPlayerModel getMiniPlayerModel(){return miniPlayerModel;}

    public MusicListItemModel getMusicListItemModel(){return musicListItemModel;}

    public MusicPlayerModel getMusicPlayerModel(){return musicPlayerModel;}

    public SongInformationModel getSongInformationModel(){return songInformationModel;}
}