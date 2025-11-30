package Model;

public class Admin {
    private int idAdmin;
    private String username;
    private String password;
    private String nama; // Nanti diisi dari 'nama_admin'
    private String email; // Nanti diisi dari 'email_admin'

    public Admin() {}

    // Getter Setter
    public int getIdAdmin() { return idAdmin; }
    public void setIdAdmin(int idAdmin) { this.idAdmin = idAdmin; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}