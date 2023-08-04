package com.example.cuongdvph20635asm.ui.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cuongdvph20635asm.R;

public class SplashFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (onBoardingFinished()) {
                    NavHostFragment.findNavController(SplashFragment.this)
                            .navigate(R.id.action_splashFragment_to_loginFragment);
                } else {
                    NavHostFragment.findNavController(SplashFragment.this)
                            .navigate(R.id.action_splashFragment_to_viewPagerFragment);
                }
            }
        }, 3000);

        return view;
    }

    private boolean onBoardingFinished() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("Finished", false);
    }
}
