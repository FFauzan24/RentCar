package com.acenkzproject.rentcar.Adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acenkzproject.rentcar.Adapter.user.MobilGridAdapter;
import com.acenkzproject.rentcar.R;
import com.acenkzproject.rentcar.model.Mobil;
import com.acenkzproject.rentcar.model.Penyewaan;
import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PenyewaanAdapter extends RecyclerView.Adapter<PenyewaanAdapter.ViewHolder> {

    private ArrayList<Penyewaan> sewaList;

    private Context context;

    private OnItemClickData onItemClickData;

    public PenyewaanAdapter(Context context, ArrayList<Penyewaan> list){
        this.context = context;
        this.sewaList = list;
    }

    public void setOnItemClickData(OnItemClickData onItemClickData) {
        this.onItemClickData = onItemClickData;
    }

    @NonNull
    @Override
    public PenyewaanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_transaksi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PenyewaanAdapter.ViewHolder holder, int position) {
        Penyewaan penyewaan = sewaList.get(position);

        int total = Integer.parseInt(penyewaan.getTotal_biaya());
        Glide.with(context)
                .load(penyewaan.getImage())
                .centerCrop()
                .into(holder.image);

        holder.waktu.setText(penyewaan.getLama_waktu() + " hari");
        holder.nama.setText(penyewaan.getId_user());
        holder.namaMobil.setText(penyewaan.getId_mobil());
        holder.total.setText(formatUang(total));

        holder.itemView.setOnClickListener(v -> {
            onItemClickData.onItemClicked(sewaList.get(holder.getAdapterPosition()));
        });
    }

    @Override
    public int getItemCount() {
        return sewaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView nama, namaMobil, tanggal, waktu, total;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            nama = itemView.findViewById(R.id.namaUser);
            total = itemView.findViewById(R.id.total);
            namaMobil = itemView.findViewById(R.id.namaMobil);
            tanggal = itemView.findViewById(R.id.tanggal);
            waktu = itemView.findViewById(R.id.lamaWaktu);
        }
    }

    private String formatUang(int number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    public interface OnItemClickData{
        void onItemClicked(Penyewaan penyewaan);
    }
}
