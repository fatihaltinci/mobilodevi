package com.fatih.sixthify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.fatih.sixthify.R;
import com.fatih.sixthify.PlaylistTut;

import java.util.ArrayList;
import java.util.List;

public class PlaylistVeri extends RecyclerView.Adapter<PlaylistVeri.ViewHolder>  {

    private List<PlaylistTut> playlistTuts;

    private Context context;

    private OnItemClickListener<PlaylistTut> playListOnLongItemClickListener;
    public PlaylistVeri(
            Context context,
             OnItemClickListener<PlaylistTut> playListOnLongItemClickListener){

        this.playListOnLongItemClickListener=playListOnLongItemClickListener;
        this.context=context;
        this.playlistTuts =new ArrayList<>();

    }

    public interface OnItemClickListener<T> {
        void onItemClick(T data);
    }

    public void setPlayLists(List<PlaylistTut> playlistTuts){

        this.playlistTuts = playlistTuts;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public PlaylistVeri.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context= parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);

        View dataView=inflater.inflate(R.layout.play_list_item,parent,false);
        PlaylistVeri.ViewHolder viewHolder=new ViewHolder(dataView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlaylistTut playlistTut = playlistTuts.get(position);

        holder.playListName.setText(playlistTut.name);

        holder.playListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlaylistBottomSheetDialogFragment dialogFragment=new PlaylistBottomSheetDialogFragment(playlistTut);
                dialogFragment.show(((AppCompatActivity)context).getSupportFragmentManager(),dialogFragment.getTag());

            }
        });

        holder.playListLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                playListOnLongItemClickListener.onItemClick(playlistTut);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        if (playlistTuts ==null) return 0;

        return playlistTuts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ConstraintLayout playListLayout;
        public TextView playListName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            playListLayout=itemView.findViewById(R.id.playListItemLayout);
            playListName=itemView.findViewById(R.id.playListItemName);

        }
    }

}
