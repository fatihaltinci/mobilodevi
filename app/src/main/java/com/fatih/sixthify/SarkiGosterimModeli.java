package com.fatih.sixthify;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fatih.sixthify.Sarki;

import java.util.List;

public class SarkiGosterimModeli extends ViewModel {

    private MutableLiveData<List<Sarki>> songs=new MutableLiveData<List<Sarki>>();

    public void setSongs(List<Sarki> sarkis){
        Log.v("Sarki","sarkilarin yeni boyutu:"+ sarkis.size());
        this.songs.setValue(sarkis);
    }


    public LiveData<List<Sarki>> getSongs(){
        Log.v("sarki","sarkiyi getir");
        return songs;
    }

}
