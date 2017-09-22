package com.arny.aircraftrefueling.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.arny.aircraftrefueling.R;
import com.arny.aircraftrefueling.fragments.KilogrammFragment;
import com.arny.aircraftrefueling.fragments.LitreFragment;

import java.util.ArrayList;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;
    private ArrayList<String> tabsFragments = new ArrayList<>();

    public TabPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        tabsFragments.add(context.getString(R.string.str_kilofragment));
        tabsFragments.add(context.getString(R.string.str_litrefragment));
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new KilogrammFragment();
            case 1:
                return new LitreFragment();
        }
        return null;

    }

    @Override
    public int getCount() {
        return tabsFragments.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return tabsFragments.get(position);
    }

}