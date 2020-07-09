package com.example.mediaplayer;

import android.os.Build;

import java.util.Objects;

public class SongInfo
{
    private String path;
    private String songName;
    private String albumName;
    private String artistName;

    public SongInfo(String path, String songName, String albumName, String artistName) {
        this.path = path;
        this.songName = songName;
        this.albumName = albumName;
        this.artistName = artistName;
    }

    @Override
    public String toString() {
        return "SongInfo{" +
                "path='" + path + '\'' +
                ", songName='" + songName + '\'' +
                ", albumName='" + albumName + '\'' +
                ", artistName='" + artistName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SongInfo)) return false;
        SongInfo songInfo = (SongInfo) o;
        return getPath().equals(songInfo.getPath()) &&
                getSongName().equals(songInfo.getSongName()) &&
                getAlbumName().equals(songInfo.getAlbumName()) &&
                getArtistName().equals(songInfo.getArtistName());
    }

    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(getPath(), getSongName(), getAlbumName(), getArtistName());
        }
        else
            return 0;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
