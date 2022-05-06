package com.fatih.sixthify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlaylistBottomSheetDataAdapter extends RecyclerView.Adapter<PlaylistBottomSheetDataAdapter.ViewHolder> {


    private List<Sarki> sarkis;
    private int index;
    private OnItemClickListener<SarkiCal>  playSongListener;
    public interface OnItemClickListener<T> {
        void onItemClick(T data);
    }

    public PlaylistBottomSheetDataAdapter(
            List<Sarki> sarkis,
            OnItemClickListener<SarkiCal> playSongListener
    ){
        this.sarkis = sarkis;
        this.playSongListener=playSongListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context= parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);

        View dataView=inflater.inflate(R.layout.bottom_sheet_item,parent,false);
        PlaylistBottomSheetDataAdapter.ViewHolder viewHolder=new PlaylistBottomSheetDataAdapter.ViewHolder(dataView);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sarki sarki = sarkis.get(position);
        holder.name.setText(sarki.name);
        holder.duration.setText(ZamanCevirici.convertToMMSS(sarki.duration));

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSongListener.onItemClick(new SarkiCal(holder.getAdapterPosition(), sarkis));
            }
        });

    }

    @Override
    public int getItemCount() {
        return sarkis.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public TextView duration;
        public ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.bottomSheetItemName);
            duration=itemView.findViewById(R.id.bottomSheetItemDuration);
            constraintLayout=itemView.findViewById(R.id.bottomSheetItemLayout);

        }
    }

}
