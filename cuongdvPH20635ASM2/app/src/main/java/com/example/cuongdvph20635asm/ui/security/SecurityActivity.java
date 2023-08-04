package com.example.cuongdvph20635asm.ui.security;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cuongdvph20635asm.databinding.ActivitySecurityBinding;

public class SecurityActivity extends AppCompatActivity {
    private ActivitySecurityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecurityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}