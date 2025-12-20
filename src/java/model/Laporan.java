
package model;
import java.util.Date;

public class Laporan {
    private String id;
    private double totalPemasukan, totalPengeluaran;
    private Date dari, sampai;

    public Laporan(String id, double pemasukan, double pengeluaran, Date dari, Date sampai) {
        this.id = id;
        this.totalPemasukan = pemasukan;
        this.totalPengeluaran = pengeluaran;
        this.dari = dari;
        this.sampai = sampai;
    }

    public String getId() { return id; }
    public double getTotalPemasukan() { return totalPemasukan; }
    public double getTotalPengeluaran() { return totalPengeluaran; }
    public Date getDari() { return dari; }
    public Date getSampai() { return sampai; }

    public double getSaldo() {
        return totalPemasukan - totalPengeluaran;
    }
}
