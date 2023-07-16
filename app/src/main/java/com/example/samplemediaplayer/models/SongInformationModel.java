package com.example.samplemediaplayer.models;

public class SongInformationModel {
    Integer _id;
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

    public SongInformationModel(Integer _id, String directory, String title, String artist,
                              String album, String genre, String year, Long duration,
                              String track, String mime_type, Long size)
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
    }

    public Integer getId() {
        return _id;
    }

    public String getDirectory() {
        return directory;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getGenre() {
        return genre;
    }

    public String getYear() {
        return year;
    }

    public Long getDuration() {return duration;}

    public String getTrack() {
        return track;
    }

    public String getMimeType() {
        return mime_type;
    }

    public Long getSize() {return size;}
}
