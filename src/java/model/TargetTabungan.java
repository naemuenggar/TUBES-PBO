
package model;

public class TargetTabungan {
    private String id;
    private String userId;
    private String nama;
    private double jumlahTarget;

    public TargetTabungan() {}

    public TargetTabungan(String id, String userId, String nama, double jumlahTarget) {
        this.id = id;
        this.userId = userId;
        this.nama = nama;
        this.jumlahTarget = jumlahTarget;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public double getJumlahTarget() { return jumlahTarget; }
    public void setJumlahTarget(double jumlahTarget) { this.jumlahTarget = jumlahTarget; }
}
