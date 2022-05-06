package com.fatih.sixthify;

import android.media.MediaPlayer;
import android.util.Log;

import com.fatih.sixthify.Sarki;

import java.io.IOException;
import java.util.List;

public class SixthifyCaliciKontroller {

    static MediaPlayer instance;

    public static MediaPlayer getInstance() {
        if (instance == null)
            instance = new MediaPlayer();

        return instance;
    }

}
