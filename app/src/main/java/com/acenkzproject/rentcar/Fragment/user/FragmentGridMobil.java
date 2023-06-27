package com.acenkzproject.rentcar.Fragment.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.acenkzproject.rentcar.Adapter.user.MobilGridAdapter;
import com.acenkzproject.rentcar.databinding.FragmentGridRentalBinding;
import com.acenkzproject.rentcar.model.Mobil;
import com.acenkzproject.rentcar.view.admin.ActivityAddMobil;
import com.acenkzproject.rentcar.view.user.DetailRentalActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


public class FragmentGridMobil extends Fragment {

    private FragmentGridRentalBinding binding;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private MobilGridAdapter mobilGridAdapter;
    ArrayList<Mobil> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGridRentalBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mobilGridAdapter = new MobilGridAdapter(requireContext(), list);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        binding.rvMobil.setLayoutManager(layoutManager);
        binding.rvMobil.setAdapter(mobilGridAdapter);

        mobilGridAdapter.setOnitem(data -> {
            Intent intent = new Intent(requireContext(), DetailRentalActivity.class);
            intent.putExtra("id", data.getId_mobil());
            intent.putExtra("namaMobil", data.getNama_mobil());
            intent.putExtra("tahun", data.getTahun());
            intent.putExtra("warna", data.getWarna());
            intent.putExtra("harga", data.getHarga());
            intent.putExtra("image", data.getImage());
            intent.putExtra("deskripsi", data.getDeskripsi());
            startActivity(intent);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ProgressBar(true);
        getMobil();
    }

    private void getMobil() {
        database.collection("RentalMobil")
                .get()
                .addOnCompleteListener(task -> {
                    list.clear();
                    ProgressBar(false);
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            Mobil mobil = new Mobil();
                            mobil.setNama_mobil(documentSnapshot.getString("namaMobil"));
                            mobil.setImage(documentSnapshot.getString("image"));
                            mobil.setHarga(documentSnapshot.getString("harga"));
                            mobil.setWarna(documentSnapshot.getString("warna"));
                            mobil.setTahun(documentSnapshot.getString("tahun"));
                            mobil.setDeskripsi(documentSnapshot.getString("deskripsi"));
                            mobil.setId_mobil(documentSnapshot.getId());
                            list.add(mobil);
                        }
                        mobilGridAdapter.notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(requireContext(), "Data Gagal Diambil", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Error : "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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