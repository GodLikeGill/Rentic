package com.group9.project.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.group9.project.fragments.AllListings;
import com.group9.project.fragments.MyListings;

public class PageViewerAdapter extends FragmentPagerAdapter {

    public PageViewerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AllListings();
            case 1:
                return new MyListings();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}