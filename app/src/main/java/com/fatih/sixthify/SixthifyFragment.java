package com.fatih.sixthify;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fatih.sixthify.databinding.FragmentPlayerBinding;

public class SixthifyFragment extends Fragment {

    private FragmentPlayerBinding binding;

    private SixthifyCalici mpViewModel;
    private SarkiCal sarkiCal;
    MediaPlayer mediaPlayer= SixthifyCaliciKontroller.getInstance();

    TextView currentTime,totalTime,musicName,artistName;
    SeekBar seekBar;
    ImageButton playPause,next,prev;
    public static SixthifyFragment newInstance() {
        SixthifyFragment fragment = new SixthifyFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mpViewModel=new ViewModelProvider(getActivity()).get(SixthifyCalici.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentPlayerBinding.inflate(inflater,container,false);

        currentTime=binding.playerCurrentTime;
        totalTime=binding.playerTotalTime;
        artistName=binding.playerArtist;
        musicName=binding.playerSong;
        currentTime.setText("0:00");
        seekBar=binding.playerSeekBar;

        View root = binding.getRoot();


        mpViewModel.getChosenSong().observe(getViewLifecycleOwner(), new Observer<SarkiCal>() {

            @Override
            public void onChanged(SarkiCal sarkiCal) {
                SixthifyFragment.this.sarkiCal = sarkiCal;
                Sarki sarki = sarkiCal.sarkis.get(sarkiCal.index);
                totalTime.setText(ZamanCevirici.convertToMMSS(sarki.duration));
                musicName.setText(sarki.name);
                artistName.setText(sarki.artist);
                currentTime.setText("0:00");
                seekBar.setMax(Integer.parseInt(sarki.duration));

            }

        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //change player time
                if (mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);

                    currentTime.setText(ZamanCevirici.convertToMMSS(Integer.toString(mediaPlayer.getCurrentPosition())));
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        playPause=binding.playerPlayPause;
        next=binding.playerNext;
        prev=binding.playerPrevious;

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer==null) return;

                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }else {
                    mediaPlayer.start();
                }

            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null && sarkiCal !=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTime.setText(ZamanCevirici.convertToMMSS(mediaPlayer.getCurrentPosition()+""));

                    if(mediaPlayer.isPlaying()){
                        playPause.setImageResource(R.drawable.ic_baseline_pause_24);
                    }else{
                        playPause.setImageResource(R.drawable.ic_baseline_play_arrow);

                    }

                }
                new Handler().postDelayed(this,100);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }


}