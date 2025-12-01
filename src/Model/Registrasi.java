package Model;

import java.util.Date;

public class Registrasi {
    private int idRegistrasi;
    private int idMahasiswa;
    private Date tanggalRegistrasi;
    private String semester; 
    private String status;   
    private String keterangan;
    private int idAdmin; 

    public Registrasi() {}

    // Getter & Setter
    public int getIdRegistrasi() { return idRegistrasi; }
    public void setIdRegistrasi(int idRegistrasi) { this.idRegistrasi = idRegistrasi; }

    public int getIdMahasiswa() { return idMahasiswa; }
    public void setIdMahasiswa(int idMahasiswa) { this.idMahasiswa = idMahasiswa; }

    public Date getTanggalRegistrasi() { return tanggalRegistrasi; }
    public void setTanggalRegistrasi(Date tanggalRegistrasi) { this.tanggalRegistrasi = tanggalRegistrasi; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }

    public int getIdAdmin() { return idAdmin; }
    public void setIdAdmin(int idAdmin) { this.idAdmin = idAdmin; }
}