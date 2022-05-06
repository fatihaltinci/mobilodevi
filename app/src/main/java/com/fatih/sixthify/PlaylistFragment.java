package com.fatih.sixthify;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fatih.sixthify.databinding.FragmentPlayListBinding;

import java.util.ArrayList;
import java.util.List;


public class PlaylistFragment extends Fragment {

    private AppDatabase db;
    private PlayListDao playListDao;
    private FragmentPlayListBinding binding;

    private PlaylistGosterim playlistGosterim;
    RecyclerView playListRecyclerView;
    PlaylistVeri playlistVeri;

    private SarkiGosterimModeli sarkiGosterimModeli;
    private List<Sarki> sarkis;
    private SharedPreferences sharedPreferences;
    private List<PlaylistTut> playlistTuts =new ArrayList<>();

    public static PlaylistFragment newInstance() {
        PlaylistFragment fragment = new PlaylistFragment();

        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playlistGosterim =new ViewModelProvider(getActivity()).get(PlaylistGosterim.class);
        sarkiGosterimModeli =new ViewModelProvider(getActivity()).get(SarkiGosterimModeli.class);
        db= Room.databaseBuilder(getActivity(), AppDatabase.class,"app-db").allowMainThreadQueries().build();
        playListDao=db.playListDao();
        sharedPreferences=getActivity().getSharedPreferences(UygulamaAyarlari.APP_SHARED_PREFS, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentPlayListBinding.inflate(inflater,container,false);

        playlistGosterim.getPlayLists().observe(getViewLifecycleOwner(), new Observer<List<PlaylistTut>>() {
            @Override
            public void onChanged(List<PlaylistTut> playlistTuts) {

                PlaylistFragment.this.playlistTuts = playlistTuts;

                playlistVeri.setPlayLists(playlistTuts);
                playListRecyclerView.setAdapter(playlistVeri);
            }
        });

        sarkiGosterimModeli.getSongs().observe(getViewLifecycleOwner(), new Observer<List<Sarki>>() {
            @Override
            public void onChanged(List<Sarki> sarkis) {
                PlaylistFragment.this.sarkis = sarkis;
            }
        });

        playListRecyclerView=binding.playlistRecyclerView;
        playlistVeri =new PlaylistVeri(getActivity(),deleteList);

        //set playlist here
        playListRecyclerView.setAdapter(playlistVeri);

        playListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        View root=binding.getRoot();

        binding.playListAddNewButton.setOnClickListener(addNewPlayListListener);

        return root;
    }

    private PlaylistVeri.OnItemClickListener<PlaylistTut> deleteList=new PlaylistVeri.OnItemClickListener<PlaylistTut>() {
        @Override
        public void onItemClick(PlaylistTut data) {

            //show approval
            AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
            alert.setTitle("Listeyi Sil");
            alert.setMessage(""+data.name+" silinecek");
            alert.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    playlistTuts.remove(data);
                    PlaylistKayitSistemi.savePlayList(sharedPreferences, playlistTuts);
                    playlistGosterim.setPlayLists(playlistTuts);
                    dialogInterface.dismiss();
                }
            });

            //
            alert.setNegativeButton("Hayir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            alert.show();
        }
    };



    private View.OnClickListener addNewPlayListListener=new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

            builder.setTitle("Yeni Playlist");

            final EditText input=new EditText(getContext());

            builder.setView(input);

            positiveButtonHandler(builder,input);

            builder.setNegativeButton("Iptal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            builder.show();
        }
    };

    private void positiveButtonHandler(AlertDialog.Builder builder,EditText input){

        builder.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int a) {

                String playListName= input.getText().toString();
                boolean[] checkedItems=new boolean[sarkis.size()];
                String[] s=new String[sarkis.size()];

                for (int k=0;k<s.length;k++) {
                    s[k]=(sarkis.get(k).name);
                }

                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setMultiChoiceItems(s, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int choiceIndex, boolean b) {
                        checkedItems[choiceIndex]=b;
                    }
                });

                builder.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ArrayList<Sarki> chosens=new ArrayList<>();
                        for (int j=0;j<checkedItems.length;j++){
                            if(checkedItems[j]){
                                chosens.add(PlaylistFragment.this.sarkis.get(j));
                            }
                        }
                        addChosensToList(chosens,playListName);
                    }
                });

                builder.setNegativeButton("Iptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    private void addChosensToList(List<Sarki> sarkis, String listName){

            PlaylistTut list= new PlaylistTut(listName);
            list.sarkiList = sarkis;
            this.playlistTuts.add(list);

            PlaylistKayitSistemi.savePlayList(sharedPreferences, playlistTuts);
            playlistGosterim.setPlayLists(this.playlistTuts);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }
}