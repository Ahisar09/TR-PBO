package Model;

public class Nilai {
    private int idNilai;
    private int idMahasiswa;
    private int idMk;
    private int idDosen;
    private int idKelas;
    private String semester;
    private double nilaiAngka; // decimal(5,2)
    private String nilaiHuruf; // A, B, C, dll
    private String status;     // 'diambil', 'hapus'
    private String keterangan;

    // --- Helper Attributes (Untuk Tampilan Dashboard) ---
    private String namaMk;
    private String kodeMk;
    private int sks; // Penting untuk perhitungan IPK

    public Nilai() {}

    // Getter & Setter Utama
    public int getIdNilai() { return idNilai; }
    public void setIdNilai(int idNilai) { this.idNilai = idNilai; }

    public int getIdMahasiswa() { return idMahasiswa; }
    public void setIdMahasiswa(int idMahasiswa) { this.idMahasiswa = idMahasiswa; }

    public int getIdMk() { return idMk; }
    public void setIdMk(int idMk) { this.idMk = idMk; }

    public int getIdDosen() { return idDosen; }
    public void setIdDosen(int idDosen) { this.idDosen = idDosen; }

    public int getIdKelas() { return idKelas; }
    public void setIdKelas(int idKelas) { this.idKelas = idKelas; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public double getNilaiAngka() { return nilaiAngka; }
    public void setNilaiAngka(double nilaiAngka) { this.nilaiAngka = nilaiAngka; }

    public String getNilaiHuruf() { return nilaiHuruf; }
    public void setNilaiHuruf(String nilaiHuruf) { this.nilaiHuruf = nilaiHuruf; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }

    // Getter & Setter Helper
    public String getNamaMk() { return namaMk; }
    public void setNamaMk(String namaMk) { this.namaMk = namaMk; }

    public String getKodeMk() { return kodeMk; }
    public void setKodeMk(String kodeMk) { this.kodeMk = kodeMk; }
    
    public int getSks() { return sks; }
    public void setSks(int sks) { this.sks = sks; }
}