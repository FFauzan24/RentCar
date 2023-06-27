package com.acenkzproject.rentcar.view.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.acenkzproject.rentcar.R;
import com.acenkzproject.rentcar.databinding.ActivityAddmobilBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActivityAddMobil extends AppCompatActivity {

    ActivityAddmobilBinding binding;
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddmobilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ProgressBar(false);
        Intent intent = getIntent();
        String mobil = intent.getStringExtra("namaMobil");
        if (mobil != null) {
            id = intent.getStringExtra("id");
            binding.namaMobil.setText(intent.getStringExtra("namaMobil"));
            binding.tahun.setText(intent.getStringExtra("tahun"));
            binding.warnaMobil.setText(intent.getStringExtra("warna"));
            binding.hargaMobil.setText(intent.getStringExtra("harga"));
            binding.deskripsi.setText(intent.getStringExtra("deskripsi"));
            Glide.with(getApplicationContext()).load(intent.getStringExtra("image"))
                    .centerCrop()
                    .into(binding.image);
            binding.judul.setText("Update Mobil");
            binding.addMobil.setText("update Data");
        }

        binding.image.setOnClickListener(v -> {
            UploadImage();
        });

        binding.addMobil.setOnClickListener(v -> {
            ProgressBar(true);
            if (binding.namaMobil.getText().toString().equals("")) {
                binding.namaMobil.setError("Data Harus Diisi");
            }
            if (binding.tahun.getText().toString().equals("")) {
                binding.tahun.setError("Data Harus Diisi");
            }
            if (binding.warnaMobil.getText().toString().equals("")) {
                binding.warnaMobil.setError("Data Harus Diisi");
            }
            if (binding.hargaMobil.getText().toString().equals("")) {
                binding.hargaMobil.setError("Data Harus Diisi");
            }
            if (binding.deskripsi.getText().toString().equals("")) {
                binding.deskripsi.setError("Data Harus Diisi");
            } else {
                SimpanToImg(binding.namaMobil.getText().toString(), binding.tahun.getText().toString(), binding.warnaMobil.getText().toString(), binding.hargaMobil.getText().toString(), binding.deskripsi.getText().toString());
            }
        });
    }

    private void UploadImage() {
        final CharSequence[] item = {"Ambil Gambar", "Pilih Dari File", "Batal"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rental Mobil");
        builder.setIcon(R.drawable.baseline_image_24);
        builder.setItems(item, (dialog, which) -> {
            if (item[which].equals("Ambil Gambar")) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 10);
            } else if (item[which].equals("Pilih Dari File")) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/");
                startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), 20);
            } else if (item[which].equals("Batal")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Thread thread = new Thread(() -> {
                Bitmap bitmap = (Bitmap) extras.get("data");
                binding.image.post(() -> binding.image.setImageBitmap(bitmap));
            });
            thread.start();
        }
        if (requestCode == 20 && resultCode == RESULT_OK && data != null) {
            final Uri path = data.getData();
            Thread thread = new Thread(() -> {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(path);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    binding.image.post(() -> binding.image.setImageBitmap(bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }
    }

    private void SimpanToImg(String namaMobil, String tahun, String warna, String harga, String deskripsi) {
        binding.image.setDrawingCacheEnabled(true);
        binding.image.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) binding.image.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        StorageReference storageReference = storage.getReference("images").child("Img " + new Date().getTime() + " .jpeg");

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask
                .addOnSuccessListener(taskSnapshot -> {
                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            taskSnapshot.getMetadata().getReference().getDownloadUrl()
                                    .addOnCompleteListener(task -> {
                                        if (task.getResult() != null) {
                                            UploadData(namaMobil, tahun, warna, harga, task.getResult().toString(), deskripsi);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Upload Data Gagal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "Upload Data Gagal", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Upload Data Gagal", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }

    private void UploadData(String namaMobil, String tahun, String warna, String harga, String image, String deskripsi) {
        ProgressBar(false);
        Map<String, Object> user = new HashMap<>();
        user.put("namaMobil", namaMobil);
        user.put("tahun", tahun);
        user.put("warna", warna);
        user.put("harga", harga);
        user.put("image", image);
        user.put("deskripsi", deskripsi);

        if (id != null) {
            database.collection("RentalMobil")
                    .document(id)
                    .set(user)
                    .addOnCompleteListener(task -> {
                        Toast.makeText(getApplicationContext(), "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Data Gagal Disimpan", Toast.LENGTH_SHORT).show();
                    });

        } else {
            database.collection("RentalMobil")
                    .add(user)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getApplicationContext(), "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Data Gagal Disimpan", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void ProgressBar(Boolean progressbar) {
        if (progressbar) {
            binding.pgBar.setVisibility(View.VISIBLE);
        } else {
            binding.pgBar.setVisibility(View.GONE);
        }
    }

}