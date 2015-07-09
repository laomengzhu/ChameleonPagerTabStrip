package com.fan.chameleonpagertabstrip.demo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fan.chameleonpagertabstrip.ChameleonPagerTabStrip;

public class MyAdapter extends FragmentPagerAdapter implements ChameleonPagerTabStrip.IconTabProvider {

    private static final int[] ICONS = new int[] {R.drawable.icon_document, R.drawable.icon_music, R.drawable.icon_movie};

    public MyAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int arg0) {
        Fragment fragment = new PagerFragment();
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "pager" + position;
    }

    @Override
    public int getPageIconResId(int position) {
        return ICONS[position];
    }
}
