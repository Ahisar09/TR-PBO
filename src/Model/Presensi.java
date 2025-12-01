package Model;

import java.util.Date;

public class Presensi {
    
    private int idPresensi;
    private int idKelas;
    private int idMahasiswa;
    private Date tanggal;
    private int pertemuanKe;
    private String status;
    private String namaMahasiswa;
    private String nimMahasiswa;

    public Presensi() {
    }

    // ==========================================================
    // GETTER & SETTER LENGKAP
    // ==========================================================

    public int getIdPresensi() { return idPresensi; }
    public void setIdPresensi(int idPresensi) { this.idPresensi = idPresensi; }

    public int getIdKelas() { return idKelas; }
    public void setIdKelas(int idKelas) { this.idKelas = idKelas; }

    public int getIdMahasiswa() { return idMahasiswa; }
    public void setIdMahasiswa(int idMahasiswa) { this.idMahasiswa = idMahasiswa; }

    public Date getTanggal() { return tanggal; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }

    public int getPertemuanKe() { return pertemuanKe; }
    public void setPertemuanKe(int pertemuanKe) { this.pertemuanKe = pertemuanKe; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // --- Getter Setter Helper (PENTING UNTUK TABEL) ---
    
    public String getNamaMahasiswa() { return namaMahasiswa; }
    public void setNamaMahasiswa(String namaMahasiswa) { this.namaMahasiswa = namaMahasiswa; }

    public String getNimMahasiswa() { return nimMahasiswa; }
    public void setNimMahasiswa(String nimMahasiswa) { this.nimMahasiswa = nimMahasiswa; }
}