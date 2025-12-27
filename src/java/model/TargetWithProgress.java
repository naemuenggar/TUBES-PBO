
package model;

public class TargetWithProgress {
    private String id;
    private String userId;
    private String nama;
    private double jumlahTarget;
    private double currentProgress;
    private double progressPercentage;

    public TargetWithProgress() {
    }

    public TargetWithProgress(String id, String userId, String nama, double jumlahTarget, double currentProgress) {
        this.id = id;
        this.userId = userId;
        this.nama = nama;
        this.jumlahTarget = jumlahTarget;
        this.currentProgress = currentProgress;
        this.progressPercentage = (jumlahTarget > 0) ? (currentProgress / jumlahTarget * 100) : 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public double getJumlahTarget() {
        return jumlahTarget;
    }

    public void setJumlahTarget(double jumlahTarget) {
        this.jumlahTarget = jumlahTarget;
        // Recalculate percentage when target changes
        this.progressPercentage = (jumlahTarget > 0) ? (currentProgress / jumlahTarget * 100) : 0;
    }

    public double getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(double currentProgress) {
        this.currentProgress = currentProgress;
        // Recalculate percentage when progress changes
        this.progressPercentage = (jumlahTarget > 0) ? (currentProgress / jumlahTarget * 100) : 0;
    }

    public double getProgressPercentage() {
        return progressPercentage;
    }
}
