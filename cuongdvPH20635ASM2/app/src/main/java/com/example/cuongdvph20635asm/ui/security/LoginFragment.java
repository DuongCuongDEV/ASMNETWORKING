package com.example.cuongdvph20635asm.ui.security;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.cuongdvph20635asm.R;
import com.example.cuongdvph20635asm.databinding.FragmentLoginBinding;
import com.example.cuongdvph20635asm.ui.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listentEvent();
    }

    private void listentEvent() {
        progressDialog = new ProgressDialog(requireContext());
        binding.layoutSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_loginFragment_to_signUpFragment);
            }
        });
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogin();
            }
        });
    }

    private void onClickLogin() {
        mAuth = FirebaseAuth.getInstance();
        String strEmail = binding.txtEmailSignIn.getText().toString().trim();
        String strPass = binding.txtPassSignIn.getText().toString().trim();
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(strEmail, strPass)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            startActivity(new Intent(requireActivity(), MainActivity.class));
                            requireActivity().finishAffinity();

                        } else {
                            Toast.makeText(requireActivity(), "this is bug ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}