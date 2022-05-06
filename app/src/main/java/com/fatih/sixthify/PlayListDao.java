package com.fatih.sixthify;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fatih.sixthify.PlaylistTut;

import java.util.List;

@Dao
public interface PlayListDao {

    @Insert
    void insert(PlaylistTut list);

    @Query("SELECT * FROM PlaylistTut")
    List<PlaylistTut> getAll();

    @Query("SELECT * FROM PlaylistTut WHERE PlaylistTut.id=:id")
    PlaylistTut getById(int id);

    @Update
    void update(PlaylistTut entity);

}
