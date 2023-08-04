package com.example.cuongdvph20635asm.ui.security;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.cuongdvph20635asm.databinding.FragmentSignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupFragment extends Fragment {

    private FragmentSignupBinding binding;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listentEvent();
    }

    private void listentEvent() {
        progressDialog = new ProgressDialog(requireContext());
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignup();
            }
        });
    }

    private void onClickSignup() {
        mAuth = FirebaseAuth.getInstance();
        String strEmail = binding.txtEmail.getText().toString().trim();
        String strPass = binding.txtPass.getText().toString().trim();
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(strEmail, strPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Log.d("token", "onComplete: "+ task.getResult().toString());
                    Navigation.findNavController(binding.btnSignUp).popBackStack();

                }else {
                    Toast.makeText(requireActivity(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}