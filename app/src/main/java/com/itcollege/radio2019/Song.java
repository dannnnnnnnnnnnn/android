package com.itcollege.radio2019;

public class Song {
    public int id;
    public String artist;
    public String title;
    public String radio;

    Song(String artist, String title, String radio) {
        this(0, artist, title, radio);
    }
    Song(int id, String artist, String title, String radioName) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.radio = radioName;
    }
}
