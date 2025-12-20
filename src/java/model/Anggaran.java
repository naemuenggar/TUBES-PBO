
package model;

public class Anggaran {
    private String id;
    private String userId;
    private String nama;
    private double jumlah;

    public Anggaran() {}

    public Anggaran(String id, String userId, String nama, double jumlah) {
        this.id = id;
        this.userId = userId;
        this.nama = nama;
        this.jumlah = jumlah;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public double getJumlah() { return jumlah; }
    public void setJumlah(double jumlah) { this.jumlah = jumlah; }
}
