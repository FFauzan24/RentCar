package com.acenkzproject.rentcar.Fragment.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.acenkzproject.rentcar.Adapter.admin.MobilListAdapter;
import com.acenkzproject.rentcar.Adapter.admin.PenyewaanAdapter;
import com.acenkzproject.rentcar.R;
import com.acenkzproject.rentcar.databinding.FragmentAllListBinding;
import com.acenkzproject.rentcar.model.Penyewaan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class FragmentAllList extends Fragment {

    FragmentAllListBinding binding;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private PenyewaanAdapter penyewaanAdapter;
    ArrayList<Penyewaan> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAllListBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        penyewaanAdapter = new PenyewaanAdapter(requireContext(), list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        binding.rvTransaksi.setLayoutManager(layoutManager);
        binding.rvTransaksi.addItemDecoration(itemDecoration);
        binding.rvTransaksi.setAdapter(penyewaanAdapter);

        penyewaanAdapter.setOnItemClickData(data -> {
        });

        getTransaksi();
    }

    private void getTransaksi() {
        database.collection("transaksi")
                .get()
                .addOnCompleteListener(task -> {
                    list.clear();
                    ProgressBar(false);
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            ProgressBar(false);
                            Penyewaan penyewaan = new Penyewaan();
                            penyewaan.setId_penyewaan(documentSnapshot.getId());
                            penyewaan.setId_user(documentSnapshot.getString("nama"));
                            penyewaan.setImage(documentSnapshot.getString("images"));
                            penyewaan.setLama_waktu(documentSnapshot.getString("lamawaktu"));
                            penyewaan.setTanggal(documentSnapshot.getString("tanggal"));
                            penyewaan.setTotal_biaya(documentSnapshot.getString("total"));
                            penyewaan.setId_mobil(documentSnapshot.getString("namaMobil"));
                            list.add(penyewaan);
                        }
                        penyewaanAdapter.notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(requireContext(), "Data Gagal Diambil", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Error ", e.getLocalizedMessage());
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        ProgressBar(true);
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