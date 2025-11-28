package model;

public class RekapNilai {
    private int idRekap;
    private int idMahasiswa;
    private String semester;
    private int totalSks;
    private double ipk;
    private double ipkKumulatif;

    public RekapNilai() {}

    // Getter & Setter
    public int getIdRekap() { return idRekap; }
    public void setIdRekap(int idRekap) { this.idRekap = idRekap; }

    public int getIdMahasiswa() { return idMahasiswa; }
    public void setIdMahasiswa(int idMahasiswa) { this.idMahasiswa = idMahasiswa; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public int getTotalSks() { return totalSks; }
    public void setTotalSks(int totalSks) { this.totalSks = totalSks; }

    public double getIpk() { return ipk; }
    public void setIpk(double ipk) { this.ipk = ipk; }

    public double getIpkKumulatif() { return ipkKumulatif; }
    public void setIpkKumulatif(double ipkKumulatif) { this.ipkKumulatif = ipkKumulatif; }
}