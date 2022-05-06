package com.fatih.sixthify;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fatih.sixthify.PlaylistTut;

import java.util.List;

public class PlaylistGosterim extends ViewModel {

    private MutableLiveData<List<PlaylistTut>> playLists=new MutableLiveData<List<PlaylistTut>>();

    public void setPlayLists(List<PlaylistTut> playlistTuts){
        this.playLists.setValue(playlistTuts);
    }

    public LiveData<List<PlaylistTut>> getPlayLists(){
        return playLists;
    }

    public void addNewList(PlaylistTut list){
        List<PlaylistTut> l=this.playLists.getValue();
        Log.v("Liste",""+l.size());
        l.add(list);
        setPlayLists(l);
    }

}
