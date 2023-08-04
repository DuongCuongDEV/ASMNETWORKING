package com.example.cuongdvph20635asm.ui.security.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private ArrayList<Fragment> fragmentList;

    public ViewPagerAdapter(ArrayList<Fragment> list, FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        fragmentList = list;
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }
}

