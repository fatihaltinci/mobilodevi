package com.fatih.sixthify;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.fatih.sixthify.Kullanici;

@Dao
public interface UserDao {

    @Insert
    void insert(Kullanici kullanici);

    @Query("SELECT * FROM  Kullanici where Kullanici.email=:email")
    Kullanici getUserByEmail(String email);

}
