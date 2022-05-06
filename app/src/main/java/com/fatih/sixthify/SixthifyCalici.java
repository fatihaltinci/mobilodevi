package com.fatih.sixthify;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fatih.sixthify.Sarki;

public class SixthifyCalici extends ViewModel {

    private MutableLiveData<SarkiCal> chosenSong=new MutableLiveData<SarkiCal>();

    public MutableLiveData<SarkiCal> getChosenSong() {
        return chosenSong;
    }


    public void setChosenSong(SarkiCal chosenSong) {
        this.chosenSong.setValue(chosenSong);
    }
}
