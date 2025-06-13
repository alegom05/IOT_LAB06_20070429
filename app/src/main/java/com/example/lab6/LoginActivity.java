package com.example.lab6;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab6.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString();
            String password = binding.etPassword.getText().toString();
            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> startMain())
                    .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        binding.btnRegister.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString();
            String password = binding.etPassword.getText().toString();
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> startMain())
                    .addOnFailureListener(e -> Toast.makeText(this, "Registro fallido: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }

    void startMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}