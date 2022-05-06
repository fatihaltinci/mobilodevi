package com.fatih.sixthify;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlaylistKayitSistemi {

    public static void savePlayList(SharedPreferences sharedPreferences, List<PlaylistTut> playLists){

        Gson gson=new Gson();
        Type type=new TypeToken<List<PlaylistTut>>() {}.getType();
        String json=gson.toJson(playLists,type);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(UygulamaAyarlari.PLAY_LISTS,json).apply();
    }

    public static List<PlaylistTut> getPlayLists(SharedPreferences sharedPreferences){
        String playListsJson="";

        playListsJson=sharedPreferences.getString(UygulamaAyarlari.PLAY_LISTS,null);

        if (playListsJson==null) return new ArrayList<>();

        Gson gson=new Gson();

        Type type = new TypeToken<List<PlaylistTut>>() {}.getType();

        ArrayList<PlaylistTut> result = gson.fromJson(playListsJson, type);

        return result;
    }
}
