package com.fatih.sixthify;

import android.os.Parcel;
import android.os.Parcelable;

public class Sarki {

    public String name;
    public String path;
    public String duration;
    public String artist;

    public Sarki(String name, String path, String duration, String artist){
        this.name=name;
        this.duration=duration;
        this.path=path;
        this.artist=artist;
    }

}