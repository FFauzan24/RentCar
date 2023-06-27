package com.acenkzproject.rentcar.view.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.acenkzproject.rentcar.databinding.ActivityBookingMobilBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BookingMobilActivity extends AppCompatActivity {


    int total, harga, waktu;
    String id;
    ActivityBookingMobilBinding binding;

    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingMobilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ProgressBar(false);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        binding.nama.setText(firebaseUser.getDisplayName());
        binding.nama.setEnabled(false);

        Glide.with(getApplicationContext()).load(intent.getStringExtra("image"))
                .centerCrop()
                .into(binding.image);
        binding.namaMobil.setText(intent.getStringExtra("namaMobil"));
        binding.warna.setText(intent.getStringExtra("warna"));

        binding.tanggal.setOnClickListener(v -> {
            tanggal();
        });


        harga = Integer.parseInt(intent.getStringExtra("harga"));
        binding.totalBiaya.setText(formatUang(harga));

        binding.lamaRental.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hitung();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnRental.setOnClickListener(v -> {
            inputData(intent.getStringExtra("image"));
        });
    }

    private void inputData(String image) {
        if (binding.tanggal.getText().length() < 1) {
            binding.tanggal.setError("Data Harus Diisi Dengan Benar");
        }
        if (waktu == 0) {
            binding.lamaRental.setError("Data Harus Diisi Dengan Benar");
        } else {
            ProgressBar(true);
            UploadData(binding.namaMobil.getText().toString(), binding.nama.getText().toString(), binding.tanggal.getText().toString(), String.valueOf(waktu), String.valueOf(total), image);
        }
    }

    private void UploadData(String namaMobil, String nama, String tanggal, String waktu, String total, String image) {
        Map<String, Object> transaction = new HashMap<>();
        transaction.put("namaMobil", namaMobil);
        transaction.put("nama", nama);
        transaction.put("tanggal", tanggal);
        transaction.put("lamawaktu", waktu);
        transaction.put("total", total);
        transaction.put("images", image);

        database.collection("transaksi")
                .add(transaction)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Transaksi Berhasil", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(this, TransaksiActivity.class);

                    intent1.putExtra("nama", nama);
                    intent1.putExtra("namaMobil", namaMobil);
                    intent1.putExtra("tanggal", tanggal);
                    intent1.putExtra("waktu", waktu);
                    intent1.putExtra("total", formatUang(Integer.valueOf(total)));

                    Toast.makeText(this, "Transaksi Berhasil", Toast.LENGTH_SHORT).show();
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                    finish();
                    ProgressBar(false);
                });
    }

    private void hitung() {
        if (binding.lamaRental.getText().length() > 0) {
            waktu = Integer.parseInt(binding.lamaRental.getText().toString());
            total = waktu * harga;
            binding.totalBiaya.setText(formatUang(total));
        } else {
            binding.totalBiaya.setText("0");
        }

    }

    private void tanggal() {
        final Calendar c = Calendar.getInstance();
        int tahun = c.get(Calendar.YEAR);
        int bulan = c.get(Calendar.MONTH);
        int tgl = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(BookingMobilActivity.this,
                (datePicker, thn, bln, tggl) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(thn, bln, tggl);

                    Calendar currentDate = Calendar.getInstance();
                    if (selectedDate.before(currentDate)) {
                        Toast.makeText(BookingMobilActivity.this, "Tidak dapat memilih waktu lampau", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    binding.tanggal.setText(tggl + "/" + (bln + 1) + "/" + thn);
                },
                tahun, bulan, tgl);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private String formatUang(int number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
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