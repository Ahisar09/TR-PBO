package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    
    private Connection conn;

    public AdminDAO() {
        this.conn = KoneksiDB.getConnection(); 
    }

  
    //MANAJEMEN MAHASISWA (CRUD)
  
    
    // READ MAHASISWA
    public List<Mahasiswa> getAllMahasiswa() {
        List<Mahasiswa> list = new ArrayList<>();
        String sql = "SELECT * FROM mahasiswa ORDER BY nim ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Mahasiswa m = new Mahasiswa();
                m.setIdMahasiswa(rs.getInt("id_mahasiswa"));
                m.setNim(rs.getString("nim"));
                m.setNama(rs.getString("nama"));
                m.setProdi(rs.getString("prodi"));
                m.setSemesterMasuk(rs.getInt("semester_masuk"));
                m.setStatus(rs.getString("status")); 
                // Field lain opsional jika ingin ditampilkan
                list.add(m);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // UPDATE MAHASISWA
    public boolean updateMahasiswa(Mahasiswa m) {
        // Kita update Nama, Prodi, Semester, dan Status berdasarkan NIM
        String sql = "UPDATE mahasiswa SET nama=?, prodi=?, semester_masuk=?, status=? WHERE nim=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getNama());
            ps.setString(2, m.getProdi());
            ps.setInt(3, m.getSemesterMasuk());
            ps.setString(4, m.getStatus()); 
            ps.setString(5, m.getNim());    
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }

    // HAPUS MAHASISWA
    public boolean hapusMahasiswa(String nim) {
        String sql = "DELETE FROM mahasiswa WHERE nim=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nim);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }


    //  MANAJEMEN DOSEN 
    
    /// READ 
    public List<Dosen> getAllDosen() {
        List<Dosen> list = new ArrayList<>();
        String sql = "SELECT * FROM dosen ORDER BY nama_dosen ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Dosen d = new Dosen();
                d.setIdDosen(rs.getInt("id_dosen"));
                d.setNip(rs.getString("nip"));
                d.setNama(rs.getString("nama_dosen"));
                d.setProdi(rs.getString("prodi"));
                d.setEmail(rs.getString("email"));
            
                
                list.add(d);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

  
    public boolean updateDosen(Dosen d) {
        String sql = "UPDATE dosen SET nama_dosen=?, email=?, prodi=? WHERE nip=?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getNama());
            ps.setString(2, d.getEmail());
            ps.setString(3, d.getProdi()); 
            ps.setString(4, d.getNip());   
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // UPDATE & DELETE
    public boolean hapusDosen(String nip) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM dosen WHERE nip=?")) {
            ps.setString(1, nip);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
        
        
        
    }
    
    

   
    //  MANAJEMEN TAGIHAN (Buat Tagihan Baru)
    public boolean buatTagihan(Tagihan t) {
        String sql = "INSERT INTO tagihan (id_mahasiswa, jenis_tagihan, jumlah, jatuh_tempo, status_bayar) VALUES (?, ?, ?, ?, 'belum')";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, t.getIdMahasiswa());
            ps.setString(2, t.getJenisTagihan());
            ps.setDouble(3, t.getJumlah());
            ps.setDate(4, new java.sql.Date(t.getJatuhTempo().getTime()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    // Cari ID Mahasiswa berdasarkan NIM 
    public int getIdMahasiswaByNim(String nim) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT id_mahasiswa FROM mahasiswa WHERE nim=?")) {
            ps.setString(1, nim);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {}
        return 0; 
    }
    
    
    // 4. MANAJEMEN TAGIHAN 

    // READ (JOIN Tabel Tagihan & Mahasiswa)
    public List<Tagihan> getAllTagihan() {
        List<Tagihan> list = new ArrayList<>();
        // Query JOIN untuk mendapatkan Nama Mahasiswa berdasarkan ID
        String sql = "SELECT t.*, m.nama FROM tagihan t JOIN mahasiswa m ON t.id_mahasiswa = m.id_mahasiswa ORDER BY t.jatuh_tempo ASC";
        
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Tagihan t = new Tagihan();
                t.setIdTagihan(rs.getInt("id_tagihan"));
                t.setIdMahasiswa(rs.getInt("id_mahasiswa"));
                t.setNamaMahasiswa(rs.getString("nama")); 
                t.setJenisTagihan(rs.getString("jenis_tagihan"));
                t.setJumlah(rs.getDouble("jumlah"));
                t.setJatuhTempo(rs.getDate("jatuh_tempo"));
                t.setStatusBayar(rs.getString("status_bayar"));
                list.add(t);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // CREATE
    public boolean tambahTagihan(Tagihan t) {
        String sql = "INSERT INTO tagihan (id_mahasiswa, jenis_tagihan, jumlah, jatuh_tempo, status_bayar) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, t.getIdMahasiswa());
            ps.setString(2, t.getJenisTagihan());
            ps.setDouble(3, t.getJumlah());
            ps.setDate(4, new java.sql.Date(t.getJatuhTempo().getTime()));
            ps.setString(5, t.getStatusBayar());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // UPDATE
    public boolean updateTagihan(Tagihan t) {
        String sql = "UPDATE tagihan SET id_mahasiswa=?, jenis_tagihan=?, jumlah=?, jatuh_tempo=?, status_bayar=? WHERE id_tagihan=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, t.getIdMahasiswa());
            ps.setString(2, t.getJenisTagihan());
            ps.setDouble(3, t.getJumlah());
            ps.setDate(4, new java.sql.Date(t.getJatuhTempo().getTime()));
            ps.setString(5, t.getStatusBayar());
            ps.setInt(6, t.getIdTagihan()); 
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}