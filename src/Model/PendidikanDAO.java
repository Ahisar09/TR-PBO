package Model; 


import Model.KoneksiDB; 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PendidikanDAO {

    private Connection conn;

    public PendidikanDAO() {
        // Memanggil koneksi dari class KoneksiDB
        this.conn = KoneksiDB.getConnection(); 
    }

    // ========================================================================
    // 1. BAGIAN AUTH & PROFIL (Mahasiswa)
    // ========================================================================

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

    public boolean updateProfil(Mahasiswa mhs) {
        String sql = "UPDATE mahasiswa SET alamat=?, no_hp=?, email=?, password=? WHERE id_mahasiswa=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mhs.getAlamat());
            ps.setString(2, mhs.getNoHp());
            ps.setString(3, mhs.getEmail());
            ps.setString(4, mhs.getPassword());
            ps.setInt(5, mhs.getIdMahasiswa());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Mahasiswa mapRowToMahasiswa(ResultSet rs) throws SQLException {
        Mahasiswa mhs = new Mahasiswa();
        mhs.setIdMahasiswa(rs.getInt("id_mahasiswa"));
        mhs.setNim(rs.getString("nim"));
        mhs.setNama(rs.getString("nama"));
        mhs.setProdi(rs.getString("prodi"));
        mhs.setTempatLahir(rs.getString("tempat_lahir"));
        mhs.setTanggalLahir(rs.getDate("tanggal_lahir"));
        mhs.setJk(rs.getString("jk"));
        mhs.setAlamat(rs.getString("alamat"));
        mhs.setEmail(rs.getString("email"));
        mhs.setNoHp(rs.getString("no_hp"));
        mhs.setSemesterMasuk(rs.getInt("semester_masuk"));
        mhs.setStatus(rs.getString("status"));
        mhs.setPassword(rs.getString("password"));
        return mhs;
    }

    // ========================================================================
    // 2. BAGIAN REGISTRASI & MATAKULIAH
    // ========================================================================

    public Registrasi getRegistrasiSemester(int idMahasiswa, String semester) {
        Registrasi reg = null;
        String sql = "SELECT * FROM registrasi WHERE id_mahasiswa = ? AND semester = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMahasiswa);
            ps.setString(2, semester);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                reg = new Registrasi();
                reg.setIdRegistrasi(rs.getInt("id_registrasi"));
                reg.setIdMahasiswa(rs.getInt("id_mahasiswa"));
                reg.setSemester(rs.getString("semester"));
                reg.setStatus(rs.getString("status"));
                reg.setTanggalRegistrasi(rs.getDate("tgl_registrasi"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reg;
    }

    public List<Matakuliah> getAllMatakuliah(String prodi) {
        List<Matakuliah> list = new ArrayList<>();
        String sql = "SELECT * FROM matakuliah WHERE prodi = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prodi);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Matakuliah mk = new Matakuliah();
                mk.setIdMk(rs.getInt("id_mk"));
                mk.setKodeMk(rs.getString("kode_mk"));
                mk.setNamaMk(rs.getString("nama_mk"));
                mk.setSks(rs.getInt("sks"));
                mk.setSemester(rs.getInt("semester"));
                mk.setProdi(rs.getString("prodi"));
                list.add(mk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ========================================================================
    // 3. BAGIAN AKADEMIK (KST, JADWAL, TRANSKRIP)
    // ========================================================================

    public List<Nilai> getKST(int idMahasiswa, String semester) {
        List<Nilai> listKst = new ArrayList<>();
        String sql = "SELECT n.*, mk.kode_mk, mk.nama_mk, mk.sks, d.nama_dosen, k.hari, k.jam_mulai, k.jam_selesai, k.ruang " +
                     "FROM nilai n " +
                     "JOIN matakuliah mk ON n.id_mk = mk.id_mk " +
                     "JOIN dosen d ON n.id_dosen = d.id_dosen " +
                     "JOIN kelas k ON n.id_kelas = k.id_kelas " +
                     "WHERE n.id_mahasiswa = ? AND n.semester = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMahasiswa);
            ps.setString(2, semester);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Nilai nilai = new Nilai();
                nilai.setIdNilai(rs.getInt("id_nilai"));
                nilai.setSemester(rs.getString("semester"));
                nilai.setStatus(rs.getString("status"));
                
                nilai.setKodeMk(rs.getString("kode_mk"));
                nilai.setNamaMk(rs.getString("nama_mk"));
                nilai.setSks(rs.getInt("sks"));
                
                String jadwalInfo = rs.getString("hari") + ", " + rs.getTime("jam_mulai") + "-" + rs.getTime("jam_selesai") + " (" + rs.getString("ruang") + ")";
                nilai.setKeterangan(jadwalInfo); 
                
                listKst.add(nilai);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listKst;
    }

    public List<Nilai> getTranskrip(int idMahasiswa) {
        List<Nilai> listTranskrip = new ArrayList<>();
        String sql = "SELECT n.*, mk.kode_mk, mk.nama_mk, mk.sks " +
                     "FROM nilai n " +
                     "JOIN matakuliah mk ON n.id_mk = mk.id_mk " +
                     "WHERE n.id_mahasiswa = ? AND n.nilai_huruf IS NOT NULL " +
                     "ORDER BY n.semester ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Nilai nilai = new Nilai();
                nilai.setSemester(rs.getString("semester"));
                nilai.setKodeMk(rs.getString("kode_mk"));
                nilai.setNamaMk(rs.getString("nama_mk"));
                nilai.setSks(rs.getInt("sks"));
                nilai.setNilaiAngka(rs.getDouble("nilai_angka"));
                nilai.setNilaiHuruf(rs.getString("nilai_huruf"));
                listTranskrip.add(nilai);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listTranskrip;
    }

    public List<Kelas> getJadwalKuliah(int idMahasiswa, String semester) {
        List<Kelas> listJadwal = new ArrayList<>();
        String sql = "SELECT k.*, mk.nama_mk, mk.kode_mk, d.nama_dosen " +
                     "FROM nilai n " +
                     "JOIN kelas k ON n.id_kelas = k.id_kelas " +
                     "JOIN matakuliah mk ON k.id_mk = mk.id_mk " +
                     "JOIN dosen d ON k.id_dosen = d.id_dosen " +
                     "WHERE n.id_mahasiswa = ? AND n.semester = ? " +
                     "ORDER BY FIELD(k.hari, 'Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat', 'Sabtu')";

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
                k.setKodeMk(rs.getString("kode_mk"));
                k.setNamaDosen(rs.getString("nama_dosen"));
                listJadwal.add(k);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listJadwal;
    }

    // ========================================================================
    // 4. BAGIAN TODO LIST
    // ========================================================================

    public List<TodoList> getTodoList(int idMahasiswa) {
        List<TodoList> list = new ArrayList<>();
        String sql = "SELECT * FROM todo_list WHERE id_mahasiswa = ? ORDER BY deadline ASC";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TodoList todo = new TodoList();
                todo.setIdTodo(rs.getInt("id_todo"));
                todo.setJudul(rs.getString("judul"));
                todo.setDeskripsi(rs.getString("deskripsi"));
                todo.setPrioritas(rs.getString("prioritas"));
                todo.setDeadline(rs.getTimestamp("deadline"));
                todo.setStatus(rs.getString("status"));
                list.add(todo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addTodo(TodoList todo) {
        String sql = "INSERT INTO todo_list (id_mahasiswa, judul, deskripsi, prioritas, deadline, status, created_at) VALUES (?, ?, ?, ?, ?, ?, NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, todo.getIdMahasiswa());
            ps.setString(2, todo.getJudul());
            ps.setString(3, todo.getDeskripsi());
            ps.setString(4, todo.getPrioritas());
            ps.setTimestamp(5, new java.sql.Timestamp(todo.getDeadline().getTime()));
            ps.setString(6, "pending");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStatusTodo(int idTodo, String statusBaru) {
        String sql = "UPDATE todo_list SET status = ? WHERE id_todo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statusBaru);
            ps.setInt(2, idTodo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteTodo(int idTodo) {
        String sql = "DELETE FROM todo_list WHERE id_todo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTodo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ========================================================================
    // 5. BAGIAN TAGIHAN
    // ========================================================================

    public List<Tagihan> getTagihan(int idMahasiswa) {
        List<Tagihan> list = new ArrayList<>();
        String sql = "SELECT * FROM tagihan WHERE id_mahasiswa = ? ORDER BY jatuh_tempo ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMahasiswa);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Tagihan t = new Tagihan();
                t.setIdTagihan(rs.getInt("id_tagihan"));
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
}