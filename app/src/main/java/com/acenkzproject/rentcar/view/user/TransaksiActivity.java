package com.acenkzproject.rentcar.view.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.acenkzproject.rentcar.R;
import com.acenkzproject.rentcar.databinding.ActivityTransaksiBinding;

public class TransaksiActivity extends AppCompatActivity {

    ActivityTransaksiBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransaksiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        binding.namaMobil.setText(intent.getStringExtra("namaMobil"));
        binding.name.setText(intent.getStringExtra("nama"));
        binding.tanggal.setText(intent.getStringExtra("tanggal"));
        binding.lamaWaktu.setText(intent.getStringExtra("waktu"));
        binding.total.setText(intent.getStringExtra("total"));

        binding.btnback.setOnClickListener(v -> {
            startActivity(new Intent(TransaksiActivity.this, ListRentalMobil.class));
            finish();
        });
    }
}