package com.acenkzproject.rentcar.view.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.acenkzproject.rentcar.R;
import com.acenkzproject.rentcar.databinding.ActivityDetailRentalBinding;
import com.acenkzproject.rentcar.model.Mobil;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.NumberFormat;
import java.util.Locale;

public class DetailRentalActivity extends AppCompatActivity {

    private ActivityDetailRentalBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailRentalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        int harga = Integer.parseInt(intent.getStringExtra("harga"));
        binding.namaMobil.setText(intent.getStringExtra("namaMobil"));
        binding.tahunMobil.setText(intent.getStringExtra("tahun"));
        binding.warnaMobil.setText(intent.getStringExtra("warna"));
        binding.jenisMobil.setText(intent.getStringExtra("namaMobil"));
        binding.harga.setText(formatUang(harga) + " / Hari");
        binding.deskripsi.setText(intent.getStringExtra("deskripsi"));

        Glide.with(getApplicationContext()).load(intent.getStringExtra("image"))
                .centerCrop()
                .into(binding.image);

        binding.btnRental.setOnClickListener(v -> {

            Intent intent1 = new Intent(getApplicationContext(), BookingMobilActivity.class);
            intent1.putExtra("id", intent.getStringExtra("id"));
            intent1.putExtra("namaMobil", intent.getStringExtra("namaMobil"));
            intent1.putExtra("tahun", intent.getStringExtra("tahun"));
            intent1.putExtra("warna", intent.getStringExtra("warna"));
            intent1.putExtra("harga", intent.getStringExtra("harga"));
            intent1.putExtra("image", intent.getStringExtra("image"));
            intent1.putExtra("deskripsi", intent.getStringExtra("deskripsi"));
            startActivity(intent1);
        });
    }

    private String formatUang(int number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }
}