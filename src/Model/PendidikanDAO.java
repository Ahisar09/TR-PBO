package Model;

// ===== TAMBAHKAN SEMUA IMPORT INI =====
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Mahasiswa;
import Model.Nilai;
import Model.Tagihan;
import Model.Kelas;
import Model.KoneksiDB;
// ======================================

public class PendidikanDAO {

    private Connection conn;

    public PendidikanDAO() {
        this.conn = KoneksiDB.getConnection();
    }

    // ================================
    // 1. LOGIN
    // ================================
    public Mahasiswa login(String nim, String password) {
        Mahasiswa mhs = null;
        String sql = "SELECT * FROM mahasiswa WHERE nim = ? AND password = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nim);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                mhs = mapRowToMahasiswa(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mhs;
    }

    private Mahasiswa mapRowToMahasiswa(ResultSet rs) throws SQLException {
        Mahasiswa m = new Mahasiswa();

        m.setIdMahasiswa(rs.getInt("id_mahasiswa"));
        m.setNim(rs.getString("nim"));
        m.setNama(rs.getString("nama"));
        m.setProdi(rs.getString("prodi"));
        m.setTempatLahir(rs.getString("tempat_lahir"));
        m.setTanggalLahir(rs.getDate("tanggal_lahir"));
        m.setJk(rs.getString("jk"));
        m.setAlamat(rs.getString("alamat"));
        m.setEmail(rs.getString("email"));
        m.setNoHp(rs.getString("no_hp"));
        m.setSemesterMasuk(rs.getInt("semester_masuk"));
        m.setStatus(rs.getString("status"));
        m.setPassword(rs.getString("password"));
        m.setNik(rs.getString("nik"));
        m.setKk(rs.getString("kk"));
        m.setBank(rs.getString("bank"));
        m.setNoRekening(rs.getString("no_rekening"));
        m.setNamaRekening(rs.getString("nama_rekening"));

        return m;
    }
    
    
    
    
    // =================================================================
    // FITUR REGISTRASI MATAKULIAH (RMK)
    // =================================================================
    
    // 1. Ambil Semua Kelas yang Tersedia (Katalog) berdasarkan Semester Paket
    public List<Kelas> getKelasTersedia(String semesterPaket) {
        List<Kelas> list = new ArrayList<>();
        // Query: Ambil kelas yang semester matakuliahnya sesuai pilihan combobox
        String sql = "SELECT k.id_kelas, k.hari, k.jam_mulai, k.jam_selesai, k.ruang, "
                   + "mk.id_mk, mk.kode_mk, mk.nama_mk, mk.sks, mk.semester, "
                   + "d.id_dosen, d.nama_dosen "
                   + "FROM kelas k "
                   + "JOIN matakuliah mk ON k.id_mk = mk.id_mk "
                   + "JOIN dosen d ON k.id_dosen = d.id_dosen "
                   + "WHERE mk.semester = ?"; 

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // Konversi String semester (misal "1") ke Integer
            ps.setInt(1, Integer.parseInt(semesterPaket)); 
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Kelas kls = new Kelas();
                kls.setIdKelas(rs.getInt("id_kelas"));
                kls.setHari(rs.getString("hari"));
                kls.setJamMulai(rs.getTime("jam_mulai"));
                kls.setJamSelesai(rs.getTime("jam_selesai"));
                
                kls.setIdMk(rs.getInt("id_mk"));
                kls.setKodeMk(rs.getString("kode_mk"));
                kls.setNamaMk(rs.getString("nama_mk"));
                kls.setSks(rs.getInt("sks"));
                
                kls.setIdDosen(rs.getInt("id_dosen"));
                kls.setNamaDosen(rs.getString("nama_dosen"));
                
                list.add(kls);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Simpan Matakuliah Pilihan ke KST (Insert ke tabel Nilai)
    public boolean ambilMatakuliah(int idMahasiswa, int idKelas, String semesterAktif) {
        // Logika: Kita insert ke tabel 'nilai' dengan status 'diambil'
        // Kita gunakan SELECT Subquery untuk otomatis mengisi id_mk dan id_dosen dari tabel kelas
        String sql = "INSERT INTO nilai (id_mahasiswa, id_kelas, id_mk, id_dosen, semester, status) "
                   + "SELECT ?, id_kelas, id_mk, id_dosen, ?, 'diambil' "
                   + "FROM kelas WHERE id_kelas = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMahasiswa);
            ps.setString(2, semesterAktif);
            ps.setInt(3, idKelas); // Ini kunci utamanya
            
            int affected = ps.executeUpdate();
            return affected > 0;
            
        } catch (SQLException e) {
            // Jika error (misal duplikat), return false
            System.out.println("Gagal ambil MK: " + e.getMessage());
            return false;
        }
    }

    // ================================
    // 2. GET KST
    // ================================
    public List<Nilai> getKST(int idMahasiswa, String semester) {
        List<Nilai> list = new ArrayList<>();

        String sql = "SELECT n.id_nilai, n.id_mahasiswa, n.id_kelas, n.semester, "
                + "mk.nama_mk, mk.sks "
                + "FROM nilai n "
                + "JOIN kelas k ON n.id_kelas = k.id_kelas "
                + "JOIN matakuliah mk ON k.id_mk = mk.id_mk "
                + "WHERE n.id_mahasiswa = ? AND n.semester = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMahasiswa);
            ps.setString(2, semester);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Nilai n = new Nilai();
                n.setIdNilai(rs.getInt("id_nilai"));
                n.setIdMahasiswa(rs.getInt("id_mahasiswa"));
                n.setIdKelas(rs.getInt("id_kelas"));
                n.setSemester(rs.getString("semester"));
                n.setNamaMk(rs.getString("nama_mk"));
                n.setSks(rs.getInt("sks"));
                list.add(n);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    
    // --- TAMBAHKAN INI DI Model/PendidikanDAO.java ---
    public boolean hapusKstMatakuliah(int idMahasiswa, String kodeMk) {
        // Kita hapus dari tabel 'nilai' berdasarkan id_mahasiswa dan kode_mk
        String sql = "DELETE FROM nilai WHERE id_mahasiswa = ? AND id_mk = (SELECT id_mk FROM matakuliah WHERE kode_mk = ? LIMIT 1)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMahasiswa);
            ps.setString(2, kodeMk);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0; // Mengembalikan true jika ada data yang terhapus
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    // =================================================================
    // UPDATE PROFIL LENGKAP (Termasuk JK & Tanggal Lahir)
    // =================================================================
    public boolean updateProfilMahasiswa(Mahasiswa mhs) {
        String sql = "UPDATE mahasiswa SET email = ?, no_hp = ?, alamat = ?, "
                   + "tempat_lahir = ?, jk = ?, tanggal_lahir = ?, "
                   + "bank = ?, no_rekening = ?, nama_rekening = ? "
                   + "WHERE id_mahasiswa = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // 1. Data Kontak
            ps.setString(1, mhs.getEmail());
            ps.setString(2, mhs.getNoHp());
            ps.setString(3, mhs.getAlamat());
            
            // 2. Data Lahir & JK
            ps.setString(4, mhs.getTempatLahir());
            ps.setString(5, mhs.getJk()); // 'L' atau 'P'
            
            if (mhs.getTanggalLahir() != null) {
                ps.setDate(6, new java.sql.Date(mhs.getTanggalLahir().getTime()));
            } else {
                ps.setNull(6, java.sql.Types.DATE);
            }
            
            // 3. Data Bank
            ps.setString(7, mhs.getBank());
            ps.setString(8, mhs.getNoRekening());
            ps.setString(9, mhs.getNamaRekening());
            
            // 4. ID (Kunci Update)
            ps.setInt(10, mhs.getIdMahasiswa());
            
            int affected = ps.executeUpdate();
            return affected > 0; // True jika berhasil
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    // =================================================================
    // FITUR TO-DO LIST
    // =================================================================
    
    // 1. Ambil Semua Todo List Mahasiswa
    public List<Model.TodoList> getTodoList(int idMahasiswa) {
        List<Model.TodoList> list = new ArrayList<>();
        String sql = "SELECT * FROM todo_list WHERE id_mahasiswa = ? ORDER BY deadline ASC";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                Model.TodoList todo = new Model.TodoList();
                todo.setIdTodo(rs.getInt("id_todo"));
                todo.setJudul(rs.getString("judul"));
                todo.setDeskripsi(rs.getString("deskripsi"));
                todo.setPrioritas(rs.getString("prioritas"));
                todo.setDeadline(rs.getDate("deadline"));
                todo.setStatus(rs.getString("status"));
                list.add(todo);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    // 2. Tambah Todo Baru
    public boolean tambahTodo(Model.TodoList todo) {
        String sql = "INSERT INTO todo_list (id_mahasiswa, judul, deskripsi, prioritas, deadline, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, todo.getIdMahasiswa());
            ps.setString(2, todo.getJudul());
            ps.setString(3, todo.getDeskripsi());
            ps.setString(4, todo.getPrioritas());
            ps.setDate(5, new java.sql.Date(todo.getDeadline().getTime()));
            ps.setString(6, todo.getStatus());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
    
    // 3. Update Todo
    public boolean updateTodo(Model.TodoList todo) {
        String sql = "UPDATE todo_list SET judul=?, deskripsi=?, prioritas=?, deadline=?, status=? WHERE id_todo=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, todo.getJudul());
            ps.setString(2, todo.getDeskripsi());
            ps.setString(3, todo.getPrioritas());
            ps.setDate(4, new java.sql.Date(todo.getDeadline().getTime()));
            ps.setString(5, todo.getStatus());
            ps.setInt(6, todo.getIdTodo());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
    
    // 4. Hapus Todo
    public boolean hapusTodo(int idTodo) {
        String sql = "DELETE FROM todo_list WHERE id_todo=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTodo);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
    
    // Method khusus untuk mengubah status saja (Lebih cepat daripada update semua)
    public boolean updateStatusTodo(int idTodo, String statusBaru) {
        String sql = "UPDATE todo_list SET status = ? WHERE id_todo = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statusBaru);
            ps.setInt(2, idTodo);
            
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================================
    // 3. TAGIHAN MAHASISWA
    // ================================
    public List<Tagihan> getTagihan(int idMahasiswa) {
        List<Tagihan> list = new ArrayList<>();

        String sql = "SELECT * FROM tagihan WHERE id_mahasiswa = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Tagihan t = new Tagihan();
                t.setIdTagihan(rs.getInt("id_tagihan"));
                t.setIdMahasiswa(rs.getInt("id_mahasiswa"));
                t.setJenisTagihan(rs.getString("jenis_tagihan"));
                t.setJumlah(rs.getDouble("jumlah"));
                t.setJatuhTempo(rs.getDate("jatuh_tempo"));
                t.setStatusBayar(rs.getString("status_bayar"));

                list.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================================
    // 4. JADWAL KULIAH
    // ================================
    public List<Kelas> getJadwalKuliah(int idMahasiswa, String semester) {
        List<Kelas> listJadwal = new ArrayList<>();

        String sql = "SELECT k.id_kelas, k.hari, k.jam_mulai, k.jam_selesai, k.ruang, "
                + "mk.id_mk, mk.kode_mk, mk.nama_mk, mk.sks, "
                + "d.id_dosen, d.nama_dosen "
                + "FROM nilai n "
                + "JOIN kelas k ON n.id_kelas = k.id_kelas "
                + "JOIN matakuliah mk ON k.id_mk = mk.id_mk "
                + "JOIN dosen d ON k.id_dosen = d.id_dosen "
                + "WHERE n.id_mahasiswa = ? AND n.semester = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMahasiswa);
            ps.setString(2, semester);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Kelas kls = new Kelas();
                kls.setIdKelas(rs.getInt("id_kelas"));
                kls.setHari(rs.getString("hari"));
                kls.setJamMulai(rs.getTime("jam_mulai"));
                kls.setJamSelesai(rs.getTime("jam_selesai"));
                kls.setRuang(rs.getString("ruang"));

                kls.setIdMk(rs.getInt("id_mk"));
                kls.setKodeMk(rs.getString("kode_mk"));
                kls.setNamaMk(rs.getString("nama_mk"));
                kls.setSks(rs.getInt("sks"));

                kls.setIdDosen(rs.getInt("id_dosen"));
                kls.setNamaDosen(rs.getString("nama_dosen"));

                listJadwal.add(kls);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listJadwal;
    }
}
