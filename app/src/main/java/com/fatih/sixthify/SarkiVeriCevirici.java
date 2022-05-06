package com.fatih.sixthify;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.fatih.sixthify.Sarki;
import com.fatih.sixthify.SarkiListesi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SarkiVeriCevirici {
    @TypeConverter
    public String fromSong(Sarki song){
        if (song==null) return (null);

        Gson gson=new Gson();
        Type type=new TypeToken<Sarki>() {}.getType();
        String json=gson.toJson(song,type);

        return json;
    }

    public Sarki toSong(String songJson){

        if (songJson==null) return null;

        Gson gson=new Gson();
        Type type = new TypeToken<Sarki>() {}.getType();
        Sarki song = gson.fromJson(songJson, type);
        return song;

    }
}
