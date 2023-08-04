package com.example.cuongdvph20635asm.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.cuongdvph20635asm.R;
import com.example.cuongdvph20635asm.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    //khai bao viewBinding
    private ActivityMainBinding binding;
    //khai bao view bottomNavigationView
    BottomNavigationView bottomNavigationView;
    //khai bao navController (doi tuong dieu huong)
    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpBottomNav();
    }

    private void setUpBottomNav() {
        navController= Navigation.findNavController(this, R.id.fragmentContainerView);
        bottomNavigationView = binding.bottomNavigationView;
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

}