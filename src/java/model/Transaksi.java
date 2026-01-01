
package model;

import java.util.Date;

public class Transaksi {
    protected String id, userId, deskripsi, jenis;
    protected double jumlah;
    protected Date tanggal;
    protected String kategoriId;
    protected String kategoriNama; // Added for display purposes

    public Transaksi() {
    }

    public Transaksi(String id, String userId, double jumlah, String deskripsi, Date tanggal, String kategoriId,
            String jenis) {
        this.id = id;
        this.userId = userId;
        this.jumlah = jumlah;
        this.deskripsi = deskripsi;
        this.tanggal = tanggal;
        this.kategoriId = kategoriId;
        this.jenis = jenis;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public double getJumlah() {
        return jumlah;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public String getKategoriId() {
        return kategoriId;
    }

    public String getJenis() {
        return jenis;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public void setKategoriId(String kategoriId) {
        this.kategoriId = kategoriId;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getKategoriNama() {
        return kategoriNama;
    }

    public void setKategoriNama(String kategoriNama) {
        this.kategoriNama = kategoriNama;
    }
}
