package Model;

public class Matakuliah {
    private int idMk;
    private String kodeMk;
    private String namaMk;
    private int sks;
    private int semester; 
    private String prodi;

    public Matakuliah() {}

    // Getter & Setter
    public int getIdMk() { return idMk; }
    public void setIdMk(int idMk) { this.idMk = idMk; }

    public String getKodeMk() { return kodeMk; }
    public void setKodeMk(String kodeMk) { this.kodeMk = kodeMk; }

    public String getNamaMk() { return namaMk; }
    public void setNamaMk(String namaMk) { this.namaMk = namaMk; }

    public int getSks() { return sks; }
    public void setSks(int sks) { this.sks = sks; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public String getProdi() { return prodi; }
    public void setProdi(String prodi) { this.prodi = prodi; }
}