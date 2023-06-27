package com.acenkzproject.rentcar.Adapter.user;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acenkzproject.rentcar.Adapter.admin.MobilListAdapter;
import com.acenkzproject.rentcar.R;
import com.acenkzproject.rentcar.model.Mobil;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MobilGridAdapter extends RecyclerView.Adapter<MobilGridAdapter.ViewHolder> {
    private ArrayList<Mobil> listMobil;
    private Context context;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;

    private OnItemClickData onItemClickData;

    public MobilGridAdapter(Context context, ArrayList<Mobil> list) {
        this.context = context;
        this.listMobil = list;
        this.firestore = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance();
    }

    public void setOnitem(OnItemClickData onItemClickData) {
        this.onItemClickData = onItemClickData;
    }

    @NonNull
    @Override
    public MobilGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_mobil, parent, false);
        return new MobilGridAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MobilGridAdapter.ViewHolder holder, int position) {
        Mobil mobil = listMobil.get(position);
        holder.nama.setText(mobil.getNama_mobil());
        int harga = Integer.parseInt(mobil.getHarga());
        holder.harga.setText(formatUang(harga));

        Glide.with(context)
                .load(mobil.getImage())
                .centerCrop()
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            onItemClickData.onItemClicked(listMobil.get(holder.getAdapterPosition()));
        });
    }

    @Override
    public int getItemCount() {
        return listMobil.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView nama, harga;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            nama = itemView.findViewById(R.id.namaMobil);
            harga = itemView.findViewById(R.id.harga_mobil);
        }
    }

    public interface OnItemClickData {
        void onItemClicked(Mobil mobil);
    }

    private String formatUang(int number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }
}
