package com.fatih.sixthify;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.fatih.sixthify.AppDatabase;
import com.fatih.sixthify.PlayListDao;
import com.google.android.material.tabs.TabLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.viewpager2.widget.ViewPager2;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fatih.sixthify.SectionsPagerAdapter;
import com.fatih.sixthify.databinding.ActivityAnaBinding;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnaActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    private final MediaPlayer mediaPlayer= SixthifyCaliciKontroller.getInstance();

    int currentSongIndex;
    List<Sarki> currentSarkiList;


    private ActivityAnaBinding binding;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private final int[] tabLayouts=new int[]{R.string.tab_song,R.string.tab_play_list,R.string.tab_player};

    private SarkiGosterimModeli sarkiGosterimModeli;
    private PlaylistGosterim playlistGosterim;
    private SixthifyCalici currentPlayerViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String[] mPermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.MANAGE_MEDIA};


        sectionsPagerAdapter= new SectionsPagerAdapter(this);
        ViewPager2 viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = binding.tabs;

        new  TabLayoutMediator(tabs,viewPager,
                (tab,position)->tab.setText(tabLayouts[position])
        ).attach();

        sharedPreferences=getApplicationContext().getSharedPreferences(UygulamaAyarlari.APP_SHARED_PREFS, Context.MODE_PRIVATE);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //mediaPlayer.pause();
                Log.v("Sixthify","Sarki Bitti");
                next();
            }

        });

        sarkiGosterimModeli =new ViewModelProvider(this).get(SarkiGosterimModeli.class);
        if(checkPermissions()){
            sarkiGosterimModeli.setSongs(getSongs(this));
        }else {
            try {
                if (ActivityCompat.checkSelfPermission(this, mPermission[0])
                        != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(this, mPermission[1])
                                != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(this, mPermission[2])
                                != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(this, mPermission[3])
                                != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            mPermission, 101);

                    // If any permission aboe not allowed by user, this condition will execute every tim, else your else part will work
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        currentPlayerViewModel=new ViewModelProvider(this).get(SixthifyCalici.class);
        currentPlayerViewModel.getChosenSong().observe(this, updatePlayerSong);

        playlistGosterim =new ViewModelProvider(this).get(PlaylistGosterim.class);

        playlistGosterim.setPlayLists(PlaylistKayitSistemi.getPlayLists(sharedPreferences));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.v("Req Code", "" + requestCode);
        if (requestCode == 101) {
            if (grantResults.length == 3 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] == PackageManager.PERMISSION_GRANTED ||
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {


                Log.v("Izinler","izinler saglandi");
                sarkiGosterimModeli.setSongs(getSongs(getApplicationContext()));

            }
        }



    }

    private Observer<SarkiCal> updatePlayerSong= new Observer<SarkiCal>() {
        @Override
        public void onChanged(SarkiCal sarkiCal) {

            if (currentSarkiList ==null){
                currentSarkiList = sarkiCal.sarkis;
                currentSongIndex= sarkiCal.index;
                play();

            }else{

                if(currentSarkiList.equals(sarkiCal.sarkis))
                {
                    //same or next item pressed

                    if(currentSongIndex== sarkiCal.index){

                        if(mediaPlayer.isPlaying()){

                            mediaPlayer.pause();

                        }else{

                            mediaPlayer.start();

                        }

                    }else{
                        currentSongIndex= sarkiCal.index;
                        play();
                    }

                }else{
                    currentSarkiList = sarkiCal.sarkis;
                    currentSongIndex= sarkiCal.index;

                    play();
                }
            }
        }
    };

    private void next(){
        if (currentSarkiList ==null) return;

        if (currentSongIndex+1< currentSarkiList.size()){
            currentSongIndex++;

            SarkiCal sarkiCal =new SarkiCal(currentSongIndex, currentSarkiList);

            currentPlayerViewModel.setChosenSong(sarkiCal);

            play();
        }
    }
    private void prev(){
        if (currentSongIndex-1>=0){
            currentSongIndex--;

            SarkiCal sarkiCal =new SarkiCal(currentSongIndex, currentSarkiList);
            currentPlayerViewModel.setChosenSong(sarkiCal);

            play();
        }
    }

    private void play(){
        if(currentSarkiList ==null) return;

        mediaPlayer.reset();
        Sarki chosen= currentSarkiList.get(currentSongIndex);
        try {

            File f=new File(chosen.path);

            if(!f.exists()) {
                Toast.makeText(getApplicationContext(), chosen.name+" silindi", Toast.LENGTH_SHORT).show();
                return;
            }

            mediaPlayer.setDataSource(chosen.path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (IOException e){

            e.printStackTrace();
        }
    }
    private void pause(){
        mediaPlayer.pause();
    }

    public boolean checkPermissions(){
        if(
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.MANAGE_MEDIA)!= PackageManager.PERMISSION_GRANTED

                        || ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
                        ||
                        ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
        ) {
            return false;
        }

        return true;
    }

    public ArrayList<Sarki> getSongs(final Context context){



        ArrayList<Sarki> list = new ArrayList<Sarki>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Log.v("Uri",uri.toString());
        String[] projection = {MediaStore.Audio.AudioColumns.DATA,MediaStore.Audio.AudioColumns.TITLE,MediaStore.Audio.AudioColumns.DURATION, MediaStore.Audio.ArtistColumns.ARTIST,};
        String selection = MediaStore.Audio.Media.IS_MUSIC +" != 0";
        //MediaStore.Audio.AudioColumns.DATA + " like ? "
        Cursor c = context.getContentResolver().query(uri, projection, selection, null, null);

        if (c != null) {

            while (c.moveToNext()) {


                String path = c.getString(0);
                String name = c.getString(1);
                String duration = c.getString(2);
                String artist = c.getString(3);

                Sarki sarki = new Sarki(name,path,duration,artist);

                list.add(sarki);
            }
            c.close();
        }
        Log.v("Sarkilar","Boyut:"+list.size());
        return list;
    }

    public void playPauseButton(View v){
        if (mediaPlayer.isPlaying()){
            pause();
        }else{
            mediaPlayer.start();
        }
    }

    public void nextButton(View v){

        next();
    }
    public void prevButton(View v)
    {
        prev();
    }



}