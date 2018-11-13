package com.example.hudson.tadszap.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.hudson.tadszap.Fragments.Fragment1;
import com.example.hudson.tadszap.Fragments.Fragment2;
import com.example.hudson.tadszap.Fragments.Fragment3;
import com.example.hudson.tadszap.MapsActivity;

public class FragmentAdapter extends FragmentPagerAdapter {

    public FragmentAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                return new Fragment1();
            case 1:
                return new Fragment2();
            case 2:
                return new Fragment3();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch(position){
            case 0:
                return "Camera";
            case 1:
                return "Chat";
            case 2:
                return "Mapa";
            default:
                return null;
        }
    }
}
