package com.example.cuongdvph20635asm.ui.security;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.cuongdvph20635asm.R;
import com.example.cuongdvph20635asm.databinding.FragmentLoginBinding;
import com.example.cuongdvph20635asm.ui.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;

//    private ActivityMainBinding binding;
    private static final String IP = "10.0.2.16";
    private static final int PORT = 8080;
    private ServerThread serverThread;
    private Handler handler = new Handler(Looper.getMainLooper());
    private static final int PERMISSION_REQUEST_CODE = 2;
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
                            initUI();
                        } else {
                            Toast.makeText(requireActivity(), "this is bug ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

//    private void showRequestPermissions() {
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
//            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
//        }
//    }

    private void initUI() {

        serverThread = new ServerThread();
        serverThread.startServer();
//            String message = binding.edtInput.getText().toString();
            if (serverThread!=null){
                serverThread.sendMessageToClient("message");
//                Toast.makeText(this, "Server Thanh cong", Toast.LENGTH_SHORT).show();
                Log.e("aaa", "thanhcong: " );
//                binding.edtInput.setText("");
            }else {
//                Toast.makeText(this, "Server Thread null", Toast.LENGTH_SHORT).show();
                Log.e("aaa", "looix: " );
            }
    }


//    private void showNotification(String message) {
//        Notification notification = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
//                .setContentTitle("A hava message from client")
//                .setContentText(message)
//                .setSmallIcon(R.drawable.i8)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.i8))
//                .build();
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(getNotificationId(), notification);
//
//    }

    private int getNotificationId(){
        return (int) new Date().getTime();
    }


    public class ServerThread extends Thread {

        private ServerSocket serverSocket;

        public void startServer() {
            start();
        }

        private List<ClientThread> clients = new ArrayList<>();

        public void sendMessageToClient(String message) {
            if (serverSocket != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(ClientThread client: clients){
                            client.sendMessage(message);
                        }
                    }
                }).start();
            }
        }

        @Override
        public void run() {
            super.run();
            try {
                serverSocket = new ServerSocket(PORT);
                handler.post(() -> {
                    Toast.makeText(requireContext(), "Waiting for Clients", Toast.LENGTH_SHORT).show();

                });

                Socket socket = serverSocket.accept();
                ClientThread clientThread = new ClientThread(socket);
                clientThread.start();
                clients.add(clientThread);
                handler.post(() -> {
                    Toast.makeText(requireContext(), "Connected to: " + socket.getInetAddress() + " : " + socket.getLocalPort() +" success!", Toast.LENGTH_SHORT).show();
                });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public class ClientThread extends Thread {

        private final Socket clientSocket;
        private BufferedReader input;
        private PrintWriter output;

        private ClientThread(Socket socket) {
            this.clientSocket = socket;
            try {
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                output = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String message) {
            output.println(message);
        }

        @Override
        public void run() {
            super.run();
            try {
                String messageFormClient;
                while ((messageFormClient = input.readLine()) != null) {
                    String finalMessage = messageFormClient;
                    handler.post(() -> {
                        Toast.makeText(requireContext(), "message from client: " + finalMessage, Toast.LENGTH_SHORT).show();
//                        showNotification(finalMessage);
                    });

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}