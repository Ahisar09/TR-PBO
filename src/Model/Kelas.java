package Model;

import java.sql.Time;

public class Kelas {
    private int idKelas;
    private int idMk;
    private int idDosen;
    private String hari;       // Senin, Selasa, dll
    private Time jamMulai;
    private Time jamSelesai;
    private String ruang;
    private int kuota;

    // --- Helper Attributes (Tidak ada di tabel kelas, tapi berguna untuk Join) ---
    private String namaMk;     // Untuk menyimpan nama matakuliah
    private String namaDosen;  // Untuk menyimpan nama dosen
    private String kodeMk;     // Untuk menyimpan kode mk

    public Kelas() {}

    // Getter & Setter Utama
    public int getIdKelas() { return idKelas; }
    public void setIdKelas(int idKelas) { this.idKelas = idKelas; }

    public int getIdMk() { return idMk; }
    public void setIdMk(int idMk) { this.idMk = idMk; }

    public int getIdDosen() { return idDosen; }
    public void setIdDosen(int idDosen) { this.idDosen = idDosen; }

    public String getHari() { return hari; }
    public void setHari(String hari) { this.hari = hari; }

    public Time getJamMulai() { return jamMulai; }
    public void setJamMulai(Time jamMulai) { this.jamMulai = jamMulai; }

    public Time getJamSelesai() { return jamSelesai; }
    public void setJamSelesai(Time jamSelesai) { this.jamSelesai = jamSelesai; }

    public String getRuang() { return ruang; }
    public void setRuang(String ruang) { this.ruang = ruang; }

    public int getKuota() { return kuota; }
    public void setKuota(int kuota) { this.kuota = kuota; }

    // Getter & Setter Helper
    public String getNamaMk() { return namaMk; }
    public void setNamaMk(String namaMk) { this.namaMk = namaMk; }

    public String getNamaDosen() { return namaDosen; }
    public void setNamaDosen(String namaDosen) { this.namaDosen = namaDosen; }
    
    public String getKodeMk() { return kodeMk; }
    public void setKodeMk(String kodeMk) { this.kodeMk = kodeMk; }
}