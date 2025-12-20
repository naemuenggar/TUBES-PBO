
package model;

import java.util.Date;

public class Pengeluaran extends Transaksi {
    public Pengeluaran() {
    }

    public Pengeluaran(String id, String userId, double jumlah, String deskripsi, Date tanggal, String kategoriId) {
        super(id, userId, jumlah, deskripsi, tanggal, kategoriId, "pengeluaran");
    }
}