package com.fatih.sixthify;


import androidx.room.TypeConverter;

import com.fatih.sixthify.Sarki;
import com.fatih.sixthify.SarkiListesi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SarkiListesiVeriCevirici {

    @TypeConverter
    public String fromSongList(SarkiListesi songs){
        if (songs==null) return (null);

        Gson gson=new Gson();
        Type type=new TypeToken<List<Sarki>>() {}.getType();
        String json=gson.toJson(songs.sarkis,type);

        return json;
    }

    public SarkiListesi toSongList(String songListJson){

        if (songListJson==null) return null;

        Gson gson=new Gson();
        Type type = new TypeToken<SarkiListesi>() {}.getType();
        List<Sarki> songList = gson.fromJson(songListJson, type);
        return new SarkiListesi(songList);

    }
}
