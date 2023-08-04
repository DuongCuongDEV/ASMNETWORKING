package com.example.cuongdvph20635asm.ui.security.onbroading;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cuongdvph20635asm.R;
import com.example.cuongdvph20635asm.ui.security.adapter.ViewPagerAdapter;
import com.example.cuongdvph20635asm.ui.security.guide.FirstFragment;
import com.example.cuongdvph20635asm.ui.security.guide.SecondFragment;
import com.example.cuongdvph20635asm.ui.security.guide.ThirdFragment;

import java.util.ArrayList;

public class ViewPagerFragment extends Fragment {

    private ViewPager2 viewPager2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        viewPager2 = view.findViewById(R.id.view_pager);
        // Create a list of Fragments
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new FirstFragment());
        fragmentArrayList.add(new SecondFragment());
        fragmentArrayList.add(new ThirdFragment());

        // Create and set the adapter for the ViewPager2
        ViewPagerAdapter adapter = new ViewPagerAdapter(
                fragmentArrayList,
                requireActivity().getSupportFragmentManager(),
                getLifecycle()
        );
        viewPager2.setAdapter(adapter);

        return view;
    }
}
