package com.fatih.sixthify;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fatih.sixthify.databinding.PlayListBottomSheetDialogBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;


public class PlaylistBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private SixthifyCalici mpViewModel;

    private PlayListBottomSheetDialogBinding binding;
    private PlaylistTut playlistTut;
    private List<PlaylistTut> playlistTuts;
    public PlaylistBottomSheetDialogFragment(PlaylistTut playlistTut) {
        // Required empty public constructor
        this.playlistTut = playlistTut;
    }

    RecyclerView recyclerView;
    PlaylistBottomSheetDataAdapter dataAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mpViewModel=new ViewModelProvider(getActivity()).get(SixthifyCalici.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=PlayListBottomSheetDialogBinding.inflate(inflater,container,false);


        recyclerView=binding.bottomSheetRecyclerView;
        dataAdapter=new PlaylistBottomSheetDataAdapter(this.playlistTut.sarkiList, new PlaylistBottomSheetDataAdapter.OnItemClickListener<SarkiCal>() {
            @Override
            public void onItemClick(SarkiCal data) {
                mpViewModel.setChosenSong(data);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(dataAdapter);

        // Inflate the layout for this fragment
        View root=binding.getRoot();

        return root;

    }
}