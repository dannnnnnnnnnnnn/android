package com.itcollege.radio2019;

public final class C {
    static private final String prefix = "com.itcollege.radio2019";
    static public final String SERVICE_MEDIASOURCE_KEY = prefix + "SERVICE_MEDIASOURCE_KEY";
    static public final String SERVICE_MEDIASOURCE_JSON = prefix + "SERVICE_MEDIASOURCE_JSON";
    //mediaplayer states
    static public final int MUSICSERVICE_STOPPED = 0;
    static public final int MUSICSERVICE_BUFFERING = 1;
    static public final int MUSICSERVICE_PLAYING = 2;

    //mediaplayer to activity broadcast intent messages
    static public final String MUSICSERVICE_INTENT_PLAYING = prefix + "MUSICSERVICE_INTENT_PLAYING";
    static public final String MUSICSERVICE_INTENT_BUFFERING = prefix + "MUSICSERVICE_INTENT_BUFFERING";
    static public final String MUSICSERVICE_INTENT_STOPPED = prefix + "MUSICSERVICE_INTENT_STOPED";

    static public final String MUSICSERVICE_INTENT_SONGINFO = prefix + "MUSICSERVICE_INTENT_SONGINFO";

    static public final String MUSICSERVICE_ARTIST = prefix + "MUSICSERVICE_ARTIST";
    static public final String MUSICSERVICE_TRACKTITLE = prefix + "MUSICSERVICE_TRACKTITLE";


    //activity to mediaplayer broadcast intent messages
    static public final String ACTIVITY_INTENT_STARTMUSIC = prefix + "ACTIVITY_INTENT_STARTMUSIC";
    static public final String ACTIVITY_INTENT_STOPPMUSIC = prefix + "ACTIVITY_INTENT_STOPPMUSIC";



    //mediaplayer tag
    static public final String MUSICSERVICE_VOLLEYTAG = prefix + "volleyTAG";
}
