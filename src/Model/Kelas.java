package Model;

import java.sql.Time;

public class Kelas {

    // --- ATRIBUT DATABASE ---
    private int idKelas;
    private int idMk;
    private int idDosen;
    private String hari;
    private Time jamMulai;
    private Time jamSelesai;
    private String ruang;
    private int kuota;

    // --- ATRIBUT TAMBAHAN (HELPER) UNTUK JOIN ---
    private String kodeMk;
    private String namaMk;
    private int sks;
    private String namaDosen;

    public Kelas() {}

    // ================= GETTER & SETTER LENGKAP =================

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

    // --- Helper Getters Setters ---
    public String getKodeMk() { return kodeMk; }
    public void setKodeMk(String kodeMk) { this.kodeMk = kodeMk; }

    public String getNamaMk() { return namaMk; }
    public void setNamaMk(String namaMk) { this.namaMk = namaMk; }

    public int getSks() { return sks; }
    public void setSks(int sks) { this.sks = sks; }

    public String getNamaDosen() { return namaDosen; }
    public void setNamaDosen(String namaDosen) { this.namaDosen = namaDosen; }
}