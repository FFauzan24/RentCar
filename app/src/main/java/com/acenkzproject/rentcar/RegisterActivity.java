package com.acenkzproject.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.acenkzproject.rentcar.databinding.ActivityRegisterBinding;
import com.acenkzproject.rentcar.view.user.ListRentalMobil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        ProgressBar(false);

        binding.btnRegister.setOnClickListener(v -> {
            ProgressBar(true);
            if (binding.email.getText().toString().equals("")) {
                binding.email.setError("Data Harus Terisi Semua");
            }
            if (binding.nama.getText().toString().equals("")) {
                binding.nama.setError("Data Harus Terisi Semua");
            }
            if (binding.password.getText().toString().equals("")) {
                binding.password.setError("Data Harus Terisi Semua");
            }
            if (binding.password.getText().length() < 6) {
                binding.password.setError("Password harus Lebih dari 6 Karakter");
            }
            if(!isValidEmail()){
                binding.email.setError("Format Email Salah");
            }
            else {
                Register(binding.email.getText().toString(), binding.nama.getText().toString(), binding.password.getText().toString());
            }

        });
    }

    private boolean isValidEmail() {
        String email = binding.email.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private void Register(String email, String nama, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        if (firebaseUser != null) {
                            ProgressBar(false);
                            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(nama)
                                    .build();
                            firebaseUser.updateProfile(request).addOnCompleteListener(task1 -> {
                                startActivity(new Intent(getApplicationContext(), ListRentalMobil.class));
                            });
                        } else {
                            Toast.makeText(this, "Register Gagal " + task.getResult(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Register Gagal : " + task.getResult(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Error " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("error", e.getLocalizedMessage());
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