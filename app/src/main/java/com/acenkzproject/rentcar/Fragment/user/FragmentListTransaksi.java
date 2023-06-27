package com.acenkzproject.rentcar.Fragment.user;

import android.content.Intent;
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

import com.acenkzproject.rentcar.Adapter.admin.PenyewaanAdapter;
import com.acenkzproject.rentcar.databinding.FragmentListTransaksiBinding;
import com.acenkzproject.rentcar.model.Penyewaan;
import com.acenkzproject.rentcar.view.user.TransaksiActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class FragmentListTransaksi extends Fragment {

    private FragmentListTransaksiBinding binding;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private String username;
    private PenyewaanAdapter penyewaanAdapter;
    private ArrayList<Penyewaan> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListTransaksiBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username = firebaseUser.getDisplayName();

        penyewaanAdapter = new PenyewaanAdapter(requireContext(), list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        binding.listTransaksi.setLayoutManager(layoutManager);
        binding.listTransaksi.addItemDecoration(itemDecoration);
        binding.listTransaksi.setAdapter(penyewaanAdapter);

        getTransaksi(username);

        penyewaanAdapter.setOnItemClickData(data ->{
            Intent intent1 = new Intent(getContext(), TransaksiActivity.class);

            intent1.putExtra("nama", data.getId_user());
            intent1.putExtra("namaMobil", data.getId_mobil());
            intent1.putExtra("tanggal", data.getTanggal());
            intent1.putExtra("waktu", data.getLama_waktu());
            intent1.putExtra("total", formatUang(Integer.parseInt(data.getTotal_biaya())));
            startActivity(intent1);
        });

    }

    private void getTransaksi(String username) {
        database.collection("transaksi")
                .whereEqualTo("nama", username)
                .get()
                .addOnCompleteListener(task -> {
                    list.clear();
                    ProgressBar(false);
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

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
                    } else {
                        Toast.makeText(requireContext(), "Data Gagal Diambil", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Error " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Error", e.getLocalizedMessage());
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

    private String formatUang(int number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }
}