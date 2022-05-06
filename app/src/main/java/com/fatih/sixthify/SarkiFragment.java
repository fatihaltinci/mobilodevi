package com.fatih.sixthify;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fatih.sixthify.databinding.FragmentSongBinding;

import java.io.File;
import java.util.List;

public class SarkiFragment extends Fragment {

    private FragmentSongBinding binding;

    private SarkiGosterimModeli viewModel;
    private SixthifyCalici mpViewModel;
    private PlaylistGosterim playlistGosterim;
    RecyclerView songRecyclerView;
    SarkiVeri sarkiVeri;

    SharedPreferences sharedPreferences;
    private List<Sarki> sarkis;
    private List<PlaylistTut> playlistTuts;

    public static SarkiFragment newInstance() {

        SarkiFragment fragment = new SarkiFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel=new ViewModelProvider(getActivity()).get(SarkiGosterimModeli.class);
        mpViewModel=new ViewModelProvider(getActivity()).get(SixthifyCalici.class);
        playlistGosterim =new ViewModelProvider(getActivity()).get(PlaylistGosterim.class);
        sharedPreferences=getActivity().getSharedPreferences(UygulamaAyarlari.APP_SHARED_PREFS,Context.MODE_PRIVATE);
    }

    private SarkiVeri.OnItemClickListener<SarkiCal> playHandler=new SarkiVeri.OnItemClickListener<SarkiCal>() {
        @Override
        public void onItemClick(SarkiCal data) {
            mpViewModel.setChosenSong(data);
        }
    };

    private SarkiVeri.OnItemClickListener<Sarki> shareHandler=new SarkiVeri.OnItemClickListener<Sarki>() {
        @Override
        public void onItemClick(Sarki data) {

            Uri uri= Uri.parse(data.path);
            Intent shareIntent=new Intent(Intent.ACTION_SEND);
            shareIntent.setType("ses/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
            startActivity(Intent.createChooser(shareIntent,"Paylas"));

        }
    };

    private SarkiVeri.OnItemClickListener<Sarki> deleteHandler=new SarkiVeri.OnItemClickListener<Sarki>() {
        @Override
        public void onItemClick(Sarki data) {

            File file = new File(data.path);
            file.delete();

            MediaScannerConnection.scanFile(getContext(),
                    new String[]{file.toString()},
                    new String[]{file.getName()},null);

            if(sarkis.remove(data)){
                viewModel.setSongs(sarkis);
            }
        }
    };

    private SarkiVeri.OnItemClickListener<Sarki> addtoPlayListHandler=new SarkiVeri.OnItemClickListener<Sarki>() {
        @Override
        public void onItemClick(Sarki data) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

            alertDialog.setTitle("Liste seç");

            String[] arr=new String[playlistTuts.size()];
            for (int i=0;i<arr.length;i++){
                arr[i]= playlistTuts.get(i).name;
            }

            alertDialog.setSingleChoiceItems(arr ,-1, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                     PlaylistTut chosen= playlistTuts.get(i);
                     chosen.sarkiList.add(data);

                     PlaylistKayitSistemi.savePlayList(sharedPreferences, playlistTuts);

                     playlistGosterim.setPlayLists(playlistTuts);

                    Toast.makeText(getContext(), data.name+" sarkisi "+chosen.name+" listeye eklendi", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            });

            alertDialog.setNegativeButton("Iptal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            alertDialog.show();

        }
    };

    private Observer<List<PlaylistTut>> updatePlayLists=new Observer<List<PlaylistTut>>(){

        @Override
        public void onChanged(List<PlaylistTut> list) {
            playlistTuts =list;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentSongBinding.inflate(inflater,container,false);

        songRecyclerView=binding.songRecyclerView;

        sarkiVeri =new SarkiVeri(playHandler,shareHandler,deleteHandler,addtoPlayListHandler);

        //songDataAdapter.setSongs(getSongs(getContext()));

        songRecyclerView.setAdapter(sarkiVeri);

        viewModel.getSongs().observe(getViewLifecycleOwner(),updateSongList);

        playlistGosterim.getPlayLists().observe(getViewLifecycleOwner(),updatePlayLists);

        View root=binding.getRoot();


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    private Observer<List<Sarki>> updateSongList= new Observer<List<Sarki>>() {

        @Override
        public void onChanged(List<Sarki> sarkis) {

            SarkiFragment.this.sarkis = sarkis;

            sarkiVeri.setSongs(sarkis);
        }

    };

}