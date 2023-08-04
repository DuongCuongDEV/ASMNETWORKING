package com.example.cuongdvph20635asm.ui.security.guide;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cuongdvph20635asm.R;

public class ThirdFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        view.findViewById(R.id.textView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ThirdFragment.this)
                        .navigate(R.id.action_viewPagerFragment_to_loginFragment);
                onBoardingFinished();
            }
        });

        view.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ThirdFragment.this)
                        .navigate(R.id.action_viewPagerFragment_to_loginFragment);
                onBoardingFinished();
            }
        });

        return view;
    }

    private void onBoardingFinished() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Finished", true);
        editor.apply();
    }
}
