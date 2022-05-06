package com.fatih.sixthify;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Kullanici.class, PlaylistTut.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract PlayListDao playListDao();

}
