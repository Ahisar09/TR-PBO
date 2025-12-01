package Model;

import java.util.Date; 

public class Mahasiswa {
    
    private int idMahasiswa;
    private String nim;
    private String nama;
    private String tempatLahir;
    private Date tanggalLahir;
    private String jk; 
    private String alamat;
    private String email;
    private String noHp;
    private String nik;
    private String kk;
    private String bank;
    private String noRekening;
    private String namaRekening;
    private String status; 
    private int semesterMasuk;
    private String prodi;
    private String password;

    // --- CONSTRUCTOR ---
    public Mahasiswa() {
    }

    public Mahasiswa(int idMahasiswa, String nim, String nama, String tempatLahir, Date tanggalLahir, 
                     String jk, String alamat, String email, String noHp, String nik, String kk, 
                     String bank, String noRekening, String namaRekening, String status, 
                     int semesterMasuk, String prodi, String password) {
        this.idMahasiswa = idMahasiswa;
        this.nim = nim;
        this.nama = nama;
        this.tempatLahir = tempatLahir;
        this.tanggalLahir = tanggalLahir;
        this.jk = jk;
        this.alamat = alamat;
        this.email = email;
        this.noHp = noHp;
        this.nik = nik;
        this.kk = kk;
        this.bank = bank;
        this.noRekening = noRekening;
        this.namaRekening = namaRekening;
        this.status = status;
        this.semesterMasuk = semesterMasuk;
        this.prodi = prodi;
        this.password = password;
    }

    // --- GETTER & SETTER ---

    public int getIdMahasiswa() {
        return idMahasiswa;
    }

    public void setIdMahasiswa(int idMahasiswa) {
        this.idMahasiswa = idMahasiswa;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTempatLahir() {
        return tempatLahir;
    }

    public void setTempatLahir(String tempatLahir) {
        this.tempatLahir = tempatLahir;
    }

    public Date getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(Date tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getJk() {
        return jk;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getKk() {
        return kk;
    }

    public void setKk(String kk) {
        this.kk = kk;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getNoRekening() {
        return noRekening;
    }

    public void setNoRekening(String noRekening) {
        this.noRekening = noRekening;
    }

    public String getNamaRekening() {
        return namaRekening;
    }

    public void setNamaRekening(String namaRekening) {
        this.namaRekening = namaRekening;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSemesterMasuk() {
        return semesterMasuk;
    }

    public void setSemesterMasuk(int semesterMasuk) {
        this.semesterMasuk = semesterMasuk;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}