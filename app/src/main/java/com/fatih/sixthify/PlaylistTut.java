package com.fatih.sixthify;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class PlaylistTut {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "isim")
    public String name;

    public PlaylistTut(String name){
        this.name=name;
    }

    /*
    @ColumnInfo(name = "songs")
    public String songs;
    */
    @Ignore
    public List<Sarki> sarkiList;


}
