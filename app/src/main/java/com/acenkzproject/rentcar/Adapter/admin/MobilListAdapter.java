package com.acenkzproject.rentcar.Adapter.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acenkzproject.rentcar.R;
import com.acenkzproject.rentcar.model.Mobil;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MobilListAdapter extends RecyclerView.Adapter<MobilListAdapter.ViewHolder> {

    private ArrayList<Mobil> listMobil;
    private Context context;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;

    private OnItemDelete onItemDelete;

    public MobilListAdapter(Context context, ArrayList<Mobil> list) {
        this.context = context;
        this.listMobil = list;
        this.firestore = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance();
    }

    public void setOnitemDelete(OnItemDelete onItemDelete) {
        this.onItemDelete = onItemDelete;
    }

    @NonNull
    @Override
    public MobilListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_mobil, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MobilListAdapter.ViewHolder holder, int position) {
        Mobil mobil = listMobil.get(position);
        holder.nama.setText(mobil.getNama_mobil());

        int harga = Integer.parseInt(mobil.getHarga());

        holder.harga.setText(formatUang(harga));

        Glide.with(context)
                .load(mobil.getImage())
                .circleCrop()
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            onItemDelete.onItemClicked(listMobil.get(holder.getAdapterPosition()));
        });

        holder.delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder
                    .setPositiveButton("Ya", (dialog, which) -> {
                        firestore.collection("RentalMobil").document(mobil.getId_mobil())
                                .delete()
                                .addOnCompleteListener(task -> {
                                    Toast.makeText(context, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                    storage.getReferenceFromUrl(mobil.getImage())
                                            .delete()
                                            .addOnCompleteListener(task1 -> {
                                                Toast.makeText(context, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                                listMobil.remove(holder.getAdapterPosition());
                                                notifyItemRemoved(holder.getAdapterPosition());
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(context, "Data Gagal Dihapus " + e, Toast.LENGTH_SHORT).show());
                                })
                                .addOnFailureListener(e -> Toast.makeText(context, "Error " + e, Toast.LENGTH_SHORT).show());
                    })
                    .setNegativeButton("Tidak", (dialog, which) -> {

                    })
                    .setMessage("Apakah Yakin Menghapus Data ini ?");
            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return listMobil.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image, delete;
        TextView nama, harga;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            nama = itemView.findViewById(R.id.namaMobil);
            harga = itemView.findViewById(R.id.harga_mobil);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    public interface OnItemDelete {
        void onItemClicked(Mobil mobil);
    }

    private String formatUang(int number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }
}
