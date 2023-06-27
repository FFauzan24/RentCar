package com.acenkzproject.rentcar.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Penyewaan implements Parcelable {
    private String id_penyewaan;
    private String id_user;
    private String id_mobil;
    private String tanggal;
    private String lama_waktu;
    private String total_biaya;

    private String image;

    public Penyewaan() {

    }

    protected Penyewaan(Parcel in) {
        id_penyewaan = in.readString();
        id_user = in.readString();
        id_mobil = in.readString();
        tanggal = in.readString();
        lama_waktu = in.readString();
        total_biaya = in.readString();
        image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_penyewaan);
        dest.writeString(id_user);
        dest.writeString(id_mobil);
        dest.writeString(tanggal);
        dest.writeString(lama_waktu);
        dest.writeString(total_biaya);
        dest.writeString(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Penyewaan> CREATOR = new Creator<Penyewaan>() {
        @Override
        public Penyewaan createFromParcel(Parcel in) {
            return new Penyewaan(in);
        }

        @Override
        public Penyewaan[] newArray(int size) {
            return new Penyewaan[size];
        }
    };

    public String getTotal_biaya() {
        return total_biaya;
    }

    public void setTotal_biaya(String total_biaya) {
        this.total_biaya = total_biaya;
    }

    public String getLama_waktu() {
        return lama_waktu;
    }

    public void setLama_waktu(String lama_waktu) {
        this.lama_waktu = lama_waktu;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getId_mobil() {
        return id_mobil;
    }

    public void setId_mobil(String id_mobil) {
        this.id_mobil = id_mobil;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_penyewaan() {
        return id_penyewaan;
    }

    public void setId_penyewaan(String id_penyewaan) {
        this.id_penyewaan = id_penyewaan;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
