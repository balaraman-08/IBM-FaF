package com.example.friendaffinityfinder;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class FriendsPagerAdapter extends FragmentStatePagerAdapter {

    FriendsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AffinityFragment();
            case 1:
                return new Big5Fragment();
            default:
                return new AffinityFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        if (position == 0) {
            title = "Affinity";
        } else if (position == 1) {
            title = "Big5";
        }
        return title;
    }

}
