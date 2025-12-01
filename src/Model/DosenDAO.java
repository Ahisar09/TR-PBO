package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DosenDAO {

    private Connection conn;

    public DosenDAO() {
        try {
            String url = "jdbc:mysql://localhost:3306/siasat_db"; 
            String user = "root";
            String password = "";
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Kelas> getKelasAjar(int idDosen) {
        List<Kelas> list = new ArrayList<>();

        String sql = "SELECT k.id_kelas, mk.nama_mk, mk.kode_mk, k.hari, k.jam_mulai, k.jam_selesai, k.ruang "
                   + "FROM kelas k "
                   + "JOIN matakuliah mk ON k.id_mk = mk.id_mk "
                   + "WHERE k.id_dosen = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDosen);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                Kelas k = new Kelas();
                k.setIdKelas(rs.getInt("id_kelas"));
                k.setNamaMk(rs.getString("nama_mk"));
                k.setKodeMk(rs.getString("kode_mk"));
                
                k.setHari(rs.getString("hari")); 
                k.setJamMulai(rs.getTime("jam_mulai"));
                k.setJamSelesai(rs.getTime("jam_selesai"));
                
                k.setRuang(rs.getString("ruang")); 
         
                
                list.add(k);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }


  
    // AMBIL MAHASISWA DARI TABEL Kst
    public List<Presensi> getMahasiswaKelas(int idKelas, int pertemuanKe) {
        List<Presensi> list = new ArrayList<>();

        // Ubah FROM nilai n 
        String sql = "SELECT m.id_mahasiswa, m.nim, m.nama, p.status "
                   + "FROM kst k " 
                   + "JOIN mahasiswa m ON k.id_mahasiswa = m.id_mahasiswa "
                   + "LEFT JOIN presensi p ON (m.id_mahasiswa = p.id_mahasiswa " 
                   + "   AND p.id_kelas = ? AND p.pertemuan_ke = ?) "
                   + "WHERE k.id_kelas = ? "
                   + "ORDER BY m.nim ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idKelas);     // Untuk Join Presensi
            ps.setInt(2, pertemuanKe); // Untuk Join Presensi
            ps.setInt(3, idKelas);     // Untuk WHERE k.id_kelas
            
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Presensi p = new Presensi();
                p.setIdMahasiswa(rs.getInt("id_mahasiswa"));
                p.setNimMahasiswa(rs.getString("nim"));
                p.setNamaMahasiswa(rs.getString("nama"));
                p.setStatus(rs.getString("status"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    // SIMPAN PRESENSI 
    public boolean simpanPresensi(int idKelas, int idMahasiswa, int pertemuanKe, String status) {
        try {
            // Hapus dulu presensi per pertemuan
            String sqlDel = "DELETE FROM presensi WHERE id_kelas=? AND id_mahasiswa=? AND pertemuan_ke=?";
            PreparedStatement psDel = conn.prepareStatement(sqlDel);
            psDel.setInt(1, idKelas);
            psDel.setInt(2, idMahasiswa);
            psDel.setInt(3, pertemuanKe);
            psDel.executeUpdate();

            // Insert baru
            String sqlInsert = "INSERT INTO presensi (id_kelas, id_mahasiswa, tanggal, pertemuan_ke, status) "
                              + "VALUES (?, ?, CURDATE(), ?, ?)";

            PreparedStatement psIns = conn.prepareStatement(sqlInsert);
            psIns.setInt(1, idKelas);
            psIns.setInt(2, idMahasiswa);
            psIns.setInt(3, pertemuanKe);
            psIns.setString(4, status);

            return psIns.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // UPDATE PROFIL DOSEN 
    public boolean updateProfilDosen(Model.Dosen d) {
        String sql = "UPDATE dosen SET nama_dosen=?, email=?, no_hp=?, password=? WHERE id_dosen=?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getNama());
            ps.setString(2, d.getEmail());
            ps.setString(3, d.getNoHp());
            ps.setString(4, d.getPassword()); 
            ps.setInt(5, d.getIdDosen());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    // INPUT NILAI DOSEN 
    
    // 1. Ambil Mahasiswa + Nilai saat ini di kelas tersebut
    public List<Model.Nilai> getNilaiMahasiswaKelas(int idKelas) {
        List<Model.Nilai> list = new ArrayList<>();
        
        // Ambil dari tabel NILAI (karena nilai disimpan di situ)
        String sql = "SELECT n.id_nilai, n.id_mahasiswa, m.nim, m.nama, "
                   + "n.nilai_angka, n.nilai_huruf "
                   + "FROM nilai n "
                   + "JOIN mahasiswa m ON n.id_mahasiswa = m.id_mahasiswa "
                   + "WHERE n.id_kelas = ? AND n.status = 'diambil' "
                   + "ORDER BY m.nim ASC";
                   
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idKelas);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                Model.Nilai n = new Model.Nilai();
                n.setIdNilai(rs.getInt("id_nilai"));
                n.setIdMahasiswa(rs.getInt("id_mahasiswa"));
                n.setNilaiAngka(rs.getDouble("nilai_angka"));
                n.setNilaiHuruf(rs.getString("nilai_huruf"));
                n.setKodeMk(rs.getString("nim")); 
                n.setNamaMk(rs.getString("nama")); 
                
                list.add(n);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 2. Simpan Nilai (Update per mahasiswa)
    public boolean updateNilaiMahasiswa(int idNilai, double angka, String huruf) {
        String sql = "UPDATE nilai SET nilai_angka = ?, nilai_huruf = ? WHERE id_nilai = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, angka);
            ps.setString(2, huruf);
            ps.setInt(3, idNilai);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
