package Model;

public class Dosen {
    
    private int idDosen;
    private String nip; // GANTI NIDN JADI NIP
    private String nama; // Nanti diisi dari kolom 'nama_dosen'
    private String email;
    private String noHp;
    private String prodi; // TAMBAHAN BARU
    private String password;

    public Dosen() {}

    // Getter Setter
    public int getIdDosen() { return idDosen; }
    public void setIdDosen(int idDosen) { this.idDosen = idDosen; }

    public String getNip() { return nip; }
    public void setNip(String nip) { this.nip = nip; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNoHp() { return noHp; }
    public void setNoHp(String noHp) { this.noHp = noHp; }

    public String getProdi() { return prodi; }
    public void setProdi(String prodi) { this.prodi = prodi; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}