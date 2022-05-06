package com.fatih.sixthify;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class SectionsPagerAdapter extends FragmentStateAdapter {


    public SectionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position==0){
            return  SarkiFragment.newInstance();

        }else if(position==1){
            return PlaylistFragment.newInstance();
        }
        return SixthifyFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}