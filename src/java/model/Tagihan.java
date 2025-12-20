
package model;

import java.util.Date;

public class Tagihan {
    private String id, nama, userId;
    private double jumlah;
    private Date tanggalJatuhTempo;

    public Tagihan() {
    }

    public Tagihan(String id, String userId, String nama, double jumlah, Date tanggalJatuhTempo) {
        this.id = id;
        this.userId = userId;
        this.nama = nama;
        this.jumlah = jumlah;
        this.tanggalJatuhTempo = tanggalJatuhTempo;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getNama() {
        return nama;
    }

    public double getJumlah() {
        return jumlah;
    }

    public Date getTanggalJatuhTempo() {
        return tanggalJatuhTempo;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public void setTanggalJatuhTempo(Date tanggalJatuhTempo) {
        this.tanggalJatuhTempo = tanggalJatuhTempo;
    }
}