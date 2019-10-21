package com.example.minip;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mfragmentList = new ArrayList<>();
    private final List<String> mfragmentTitleList = new ArrayList<>();


    public void addfragment(Fragment fragment, String title)
    {
        mfragmentList.add(fragment);
        mfragmentTitleList.add(title);
    }


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);

    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mfragmentTitleList.get(position);
    }
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return mfragmentList.get(position);
    }



    @Override
    public int getCount() {
        //return number of tabs
        return mfragmentList.size();
    }
}