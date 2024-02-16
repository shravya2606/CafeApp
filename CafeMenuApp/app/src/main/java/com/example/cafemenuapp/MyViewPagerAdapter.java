package com.example.cafemenuapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyViewPagerAdapter  extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull CafeMenu fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new MaincourseFragment();
            case 1:
                return new DessertsFragment();
            case 2:
                return new BeveragesFragment();

        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
