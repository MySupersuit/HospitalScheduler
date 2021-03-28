package com.example.hospitalscheduler.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class OTPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_TABS = 3;
    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> fragmentTitles = new ArrayList<>();

    public OTPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public void add(Fragment fragment, String title) {
        fragments.add(fragment);
        fragmentTitles.add(title);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }
}