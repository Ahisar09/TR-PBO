package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Mahasiswa;
import Model.Nilai;
import Model.Tagihan;
import Model.Kelas;
import Model.KoneksiDB;

public class PendidikanDAO {

    private Connection conn;

    public PendidikanDAO() {
        this.conn = KoneksiDB.getConnection();
    }

    // 1. LOGIN
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
    
    // LOGIN DOSEN 
    public Model.Dosen loginDosen(String nip, String password) {
        Model.Dosen d = null;
        // Query pakai 'nip' dan 'nama_dosen'
        String sql = "SELECT * FROM dosen WHERE nip = ? AND password = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nip);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                d = new Model.Dosen();
                d.setIdDosen(rs.getInt("id_dosen"));
                d.setNip(rs.getString("nip"));
                d.setNama(rs.getString("nama_dosen")); // Ambil dari nama_dosen
                d.setEmail(rs.getString("email"));
                d.setNoHp(rs.getString("no_hp"));
                d.setProdi(rs.getString("prodi"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return d;
    }

    // LOGIN ADMIN 
    public Model.Admin loginAdmin(String username, String password) {
        Model.Admin a = null;
        // Query pakai tabel 'super_admin'
        String sql = "SELECT * FROM super_admin WHERE username = ? AND password = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                a = new Model.Admin();
                a.setIdAdmin(rs.getInt("id_admin"));
                a.setUsername(rs.getString("username"));
                a.setNama(rs.getString("nama_admin")); 
                a.setEmail(rs.getString("email_admin")); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }
    
    
    // FITUR REGISTRASI MATAKULIAH (RMK)
    
    // 1. Ambil Semua Kelas yang Tersedia 
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

    //  AMBIL MATAKULIAH 
    public boolean ambilMatakuliah(int idMahasiswa, int idKelas, String semesterAktif) {
        
        // 1. Cek dulu di tabel KST
        if (cekSudahAmbil(idMahasiswa, idKelas)) {
            return false; 
        }

        try {
            // 2. Insert ke Tabel KST 
            String sqlKst = "INSERT INTO kst (id_mahasiswa, id_kelas, semester) VALUES (?, ?, ?)";
            PreparedStatement psKst = conn.prepareStatement(sqlKst);
            psKst.setInt(1, idMahasiswa);
            psKst.setInt(2, idKelas);
            psKst.setString(3, semesterAktif);
            psKst.executeUpdate();

            // 3. Insert ke Tabel NILAI (biar Transkrip & Presensi Dosen Tetap Jalan)
            // Gunakan INSERT IGNORE agar jika ada sisa data lama, tidak error
            String sqlNilai = "INSERT IGNORE INTO nilai (id_mahasiswa, id_kelas, id_mk, id_dosen, semester, status) "
                            + "SELECT ?, id_kelas, id_mk, id_dosen, ?, 'diambil' "
                            + "FROM kelas WHERE id_kelas = ?";
            
            PreparedStatement psNilai = conn.prepareStatement(sqlNilai);
            psNilai.setInt(1, idMahasiswa);
            psNilai.setString(2, semesterAktif);
            psNilai.setInt(3, idKelas);
            
            return psNilai.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method Bantu Cek Duplikat 
    private boolean cekSudahAmbil(int idMahasiswa, int idKelas) {
        String sql = "SELECT id_kst FROM kst WHERE id_mahasiswa = ? AND id_kelas = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMahasiswa);
            ps.setInt(2, idKelas);
            ResultSet rs = ps.executeQuery();
            return rs.next(); 
        } catch (SQLException e) {
            return false;
        }
    }

  
    // 2. GET KST 
    public List<Nilai> getKST(int idMahasiswa, String semester) {
        List<Nilai> list = new ArrayList<>();

        // Query Join: KST -> KELAS -> MATAKULIAH
        String sql = "SELECT kst.id_kst, kst.id_mahasiswa, kst.id_kelas, kst.semester, "
                   + "mk.nama_mk, mk.sks "
                   + "FROM kst " 
                   + "JOIN kelas k ON kst.id_kelas = k.id_kelas "
                   + "JOIN matakuliah mk ON k.id_mk = mk.id_mk "
                   + "WHERE kst.id_mahasiswa = ? AND kst.semester = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMahasiswa);
            ps.setString(2, semester);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Nilai n = new Nilai();
                // Kita pinjam model Nilai untuk menampilkan data KST
                n.setIdNilai(rs.getInt("id_kst"));
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
    
    // Method Hapus KST 
    public boolean hapusKstMatakuliah(int idMahasiswa, int idKelas) {
        try {
            // Hapus dari KST
            String sql1 = "DELETE FROM kst WHERE id_mahasiswa=? AND id_kelas=?";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setInt(1, idMahasiswa);
            ps1.setInt(2, idKelas);
            ps1.executeUpdate();
            
            // Hapus dari Nilai juga
            String sql2 = "DELETE FROM nilai WHERE id_mahasiswa=? AND id_kelas=?";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, idMahasiswa);
            ps2.setInt(2, idKelas);
            
            return ps2.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
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
    
    
   
    // UPDATE PROFIL 
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
    
    
    // FITUR TO-DO LIST
   
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
    
    // Method ubah status
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
    
    //  AMBIL TRANSKRIP NILAI
    public java.util.List<Model.Nilai> getTranskrip(int idMahasiswa) {
        java.util.List<Model.Nilai> list = new java.util.ArrayList<>();
        
        // Query: Ambil nilai yang sudah ada hurufnya (sudah dinilai dosen)
        String sql = "SELECT n.*, mk.kode_mk, mk.nama_mk, mk.sks "
                   + "FROM nilai n "
                   + "JOIN matakuliah mk ON n.id_mk = mk.id_mk "
                   + "WHERE n.id_mahasiswa = ? AND n.nilai_huruf IS NOT NULL "
                   + "ORDER BY n.semester ASC";
                   
        try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMahasiswa);
            java.sql.ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                Model.Nilai n = new Model.Nilai();
                
                // Data Nilai
                n.setIdNilai(rs.getInt("id_nilai"));
                n.setNilaiAngka(rs.getDouble("nilai_angka")); // NA
                n.setNilaiHuruf(rs.getString("nilai_huruf")); // NH
                n.setSemester(rs.getString("semester"));
                
                // Data Matakuliah (Helper)
                n.setKodeMk(rs.getString("kode_mk"));
                n.setNamaMk(rs.getString("nama_mk"));
                n.setSks(rs.getInt("sks"));
                
                list.add(n);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // TAGIHAN MAHASISWA
    
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

    // AMBIL JADWAL KULIAH (BERDASARKAN KST)
 
    public List<Kelas> getJadwalKuliah(int idMahasiswa, String semester) {
        List<Kelas> list = new ArrayList<>();
        
        // Query: Gabungkan KST -> KELAS -> MK -> DOSEN
        // Mengambil data jadwal untuk matakuliah yang diambil mahasiswa di KST
        String sql = "SELECT k.hari, k.jam_mulai, k.jam_selesai, k.ruang, "
                   + "mk.nama_mk, mk.sks, "
                   + "d.nama_dosen "
                   + "FROM kst "
                   + "JOIN kelas k ON kst.id_kelas = k.id_kelas "
                   + "JOIN matakuliah mk ON k.id_mk = mk.id_mk "
                   + "JOIN dosen d ON k.id_dosen = d.id_dosen "
                   + "WHERE kst.id_mahasiswa = ? AND kst.semester = ? "
                   + "ORDER BY FIELD(k.hari, 'Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat', 'Sabtu'), k.jam_mulai";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMahasiswa);
            ps.setString(2, semester);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Kelas k = new Kelas();
                k.setHari(rs.getString("hari"));
                k.setJamMulai(rs.getTime("jam_mulai"));
                k.setJamSelesai(rs.getTime("jam_selesai"));
                k.setRuang(rs.getString("ruang"));
                k.setNamaMk(rs.getString("nama_mk"));
                k.setSks(rs.getInt("sks"));
                k.setNamaDosen(rs.getString("nama_dosen"));
                
                list.add(k);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
}
