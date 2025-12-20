
package model;

import java.util.Date;

public class Pemasukan extends Transaksi {
    public Pemasukan() {
    }

    public Pemasukan(String id, String userId, double jumlah, String deskripsi, Date tanggal, String kategoriId) {
        super(id, userId, jumlah, deskripsi, tanggal, kategoriId, "pemasukan");
    }
}
