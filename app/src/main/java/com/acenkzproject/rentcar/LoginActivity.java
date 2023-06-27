package com.acenkzproject.rentcar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.acenkzproject.rentcar.databinding.ActivityLoginBinding;
import com.acenkzproject.rentcar.view.admin.AdminActivity;
import com.acenkzproject.rentcar.view.user.ListRentalMobil;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        ProgressBar(false);

        binding.register.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        });

        binding.btnSignIn.setOnClickListener(v -> {
            ProgressBar(true);
            if (binding.email.getText().toString().equals("")) {
                binding.email.setError("Data Harus Diisi Dengan Benar");
            }
            if (binding.password.getText().toString().equals("")) {
                binding.password.setError("Data Harus Diisi Dengan Benar");
            } else {
                if (binding.email.getText().toString().equals("admin") && binding.password.getText().toString().equals("admin")) {
                    startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                } else {
                    Login(binding.email.getText().toString(), binding.password.getText().toString());
                }
            }

        });

    }

    private void Login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        if (task.getResult().getUser() != null) {
                            ProgressBar(false);
                            Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), ListRentalMobil.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Login Gagal", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void ProgressBar(Boolean progressbar){
        if (progressbar){
            binding.pgBar.setVisibility(View.VISIBLE);
        }
        else {
            binding.pgBar.setVisibility(View.GONE);
        }
    }
}