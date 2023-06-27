package com.acenkzproject.rentcar.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Mobil implements Parcelable {
    private String id_mobil;
    private String nama_mobil;
    private String tahun;
    private String warna;
    private String harga;
    private String image;
    private String deskripsi;

    public Mobil() {

    }

    public Mobil(String namaMobil, String tahun, String warna, String harga, String image) {
    }

    protected Mobil(Parcel in) {
        id_mobil = in.readString();
        nama_mobil = in.readString();
        tahun = in.readString();
        warna = in.readString();
        harga = in.readString();
        image = in.readString();
        deskripsi = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_mobil);
        dest.writeString(nama_mobil);
        dest.writeString(tahun);
        dest.writeString(warna);
        dest.writeString(harga);
        dest.writeString(image);
        dest.writeString(deskripsi);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Mobil> CREATOR = new Creator<Mobil>() {
        @Override
        public Mobil createFromParcel(Parcel in) {
            return new Mobil(in);
        }

        @Override
        public Mobil[] newArray(int size) {
            return new Mobil[size];
        }
    };

    public String getId_mobil() {
        return id_mobil;
    }

    public void setId_mobil(String id_mobil) {
        this.id_mobil = id_mobil;
    }

    public String getNama_mobil() {
        return nama_mobil;
    }

    public void setNama_mobil(String nama_mobil) {
        this.nama_mobil = nama_mobil;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public String getWarna() {
        return warna;
    }

    public void setWarna(String warna) {
        this.warna = warna;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
