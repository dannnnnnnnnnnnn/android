package com.itcollege.radio2019;

public class Radio {
    public int id;
    public String name;
    public String streamName;
    public String jsonName;

    Radio(String name, String streamName, String jsonName) {
        this(0, name, streamName, jsonName);
    }
    Radio(int id, String name, String streamName, String jsonName) {
        this.id = id;
        this.name = name;
        this.streamName = streamName;
        this.jsonName = jsonName;
    }

    @Override
    public String toString() {
        return name;
    }
}
