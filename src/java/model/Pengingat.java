
package model;

import java.util.Date;

public class Pengingat implements Reminderable {
    private String id, userId, pesan;
    private Date tanggal;

    public Pengingat() {
    }

    public Pengingat(String id, String userId, String pesan, Date tanggal) {
        this.id = id;
        this.userId = userId;
        this.pesan = pesan;
        this.tanggal = tanggal;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getPesan() {
        return pesan;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    @Override
    public void setReminder() {
        System.out.println("Reminder diset: " + pesan + " pada " + tanggal);
    }

    @Override
    public void cancelReminder() {
        System.out.println("Reminder dibatalkan: " + id);
    }
}
