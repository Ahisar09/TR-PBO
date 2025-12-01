package Model;

import java.util.Date;

public class Tagihan {
    private int idTagihan;
    private int idMahasiswa;
    private String namaMahasiswa; 
    private String jenisTagihan;
    private double jumlah;
    private Date jatuhTempo;
    private String statusBayar; 

    public Tagihan() {}

    // Getter & Setter 
    public String getNamaMahasiswa() { return namaMahasiswa; }
    public void setNamaMahasiswa(String namaMahasiswa) { this.namaMahasiswa = namaMahasiswa; }

    public int getIdTagihan() { return idTagihan; }
    public void setIdTagihan(int idTagihan) { this.idTagihan = idTagihan; }

    public int getIdMahasiswa() { return idMahasiswa; }
    public void setIdMahasiswa(int idMahasiswa) { this.idMahasiswa = idMahasiswa; }

    public String getJenisTagihan() { return jenisTagihan; }
    public void setJenisTagihan(String jenisTagihan) { this.jenisTagihan = jenisTagihan; }

    public double getJumlah() { return jumlah; }
    public void setJumlah(double jumlah) { this.jumlah = jumlah; }

    public Date getJatuhTempo() { return jatuhTempo; }
    public void setJatuhTempo(Date jatuhTempo) { this.jatuhTempo = jatuhTempo; }

    public String getStatusBayar() { return statusBayar; }
    public void setStatusBayar(String statusBayar) { this.statusBayar = statusBayar; }
}