package com.acenkzproject.rentcar.Fragment.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.acenkzproject.rentcar.databinding.FragmentListMobilBinding;
import com.acenkzproject.rentcar.view.admin.ActivityAddMobil;
import com.acenkzproject.rentcar.Adapter.admin.MobilListAdapter;
import com.acenkzproject.rentcar.model.Mobil;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class FragmentListMobil extends Fragment {

    private FragmentListMobilBinding binding;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private MobilListAdapter mobilListAdapter;

    ArrayList<Mobil> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListMobilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mobilListAdapter = new MobilListAdapter(requireContext(), list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        binding.rvMobil.setLayoutManager(layoutManager);
        binding.rvMobil.addItemDecoration(decoration);
        binding.rvMobil.setAdapter(mobilListAdapter);

        binding.addMobil.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ActivityAddMobil.class));
        });

        mobilListAdapter.setOnitemDelete(data -> {
            Intent intent = new Intent(requireContext(), ActivityAddMobil.class);
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
                    ProgressBar(false);
                    list.clear();
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
                        mobilListAdapter.notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(requireContext(), "Data Gagal Diambil", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Error : "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });

    }

    private ArrayList<Mobil> getDummyMobil(){
        ArrayList<Mobil> list1 = new ArrayList<>();
        for (int i = 0; i<= 20; i++){
            Mobil mobil = new Mobil();
            mobil.setNama_mobil("mobil "+i);
            list1.add(mobil);
        }
        return list1;
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