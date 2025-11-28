package Controller;

import Model.Kelas;
import Model.Mahasiswa;
import Model.PendidikanDAO;
import View.Dashboard_Mahasiswa;
import View.Registrasi_Matakuliah;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class RMKController {
    
    private Registrasi_Matakuliah view;
    private Mahasiswa mhs;
    private PendidikanDAO dao;
    
    // List untuk menyimpan data kelas yang sedang tampil di tabel
    // Ini PENTING agar kita tahu ID Kelas saat baris diklik
    private List<Kelas> daftarKelasSedangTampil;

    public RMKController(Registrasi_Matakuliah view, Mahasiswa mhs) {
        this.view = view;
        this.mhs = mhs;
        this.dao = new PendidikanDAO();
        
        view.setController(this); // Sambungkan
        
        // Isi tabel default (Semester 1)
        isiTabelKatalog("1"); 
    }
    
    // --- 1. ISI TABEL ---
    public void isiTabelKatalog(String semesterPaket) {
        // Ambil data dari DAO
        daftarKelasSedangTampil = dao.getKelasTersedia(semesterPaket);
        
        String[] kolom = {"Kode MK", "Mata Kuliah", "SKS", "Dosen", "Hari", "Waktu"};
        DefaultTableModel model = new DefaultTableModel(null, kolom);
        
        if (daftarKelasSedangTampil != null) {
            for (Kelas k : daftarKelasSedangTampil) {
                Object[] row = new Object[] {
                    k.getKodeMk(),
                    k.getNamaMk(),
                    k.getSks(),
                    k.getNamaDosen(),
                    k.getHari(),
                    k.getJamMulai() + "-" + k.getJamSelesai()
                };
                model.addRow(row);
            }
        }
        view.setTabelRMK(model);
    }
    
    // --- 2. LOGIKA PILIH SEMESTER ---
    public void semesterBerubah() {
        // Ambil semester yang dipilih dari ComboBox view
        String smt = view.getSelectedSemester();
        // Refresh tabel sesuai semester
        isiTabelKatalog(smt);
    }

    // --- 3. LOGIKA TAMBAH MATAKULIAH ---
    public void tambahMatakuliah() {
        // 1. Cek Baris mana yang dipilih
        int row = view.getTableRMK().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Pilih matakuliah di tabel dulu!");
            return;
        }
        
        // 2. Ambil Object Kelas dari List berdasarkan baris
        Kelas kelasDipilih = daftarKelasSedangTampil.get(row);
        
        // 3. Simpan ke Database
        String semesterAktif = "20231"; // Semester Mahasiswa Saat Ini (Bisa dinamis)
        boolean sukses = dao.ambilMatakuliah(mhs.getIdMahasiswa(), kelasDipilih.getIdKelas(), semesterAktif);
        
        if (sukses) {
            JOptionPane.showMessageDialog(view, "Berhasil mengambil MK: " + kelasDipilih.getNamaMk());
        } else {
            JOptionPane.showMessageDialog(view, "Gagal! Mungkin MK sudah diambil sebelumnya.");
        }
    }

    // --- 4. KEMBALI ---
    public void kembali() {
        javax.swing.SwingUtilities.getWindowAncestor(view).dispose();
        Dashboard_Mahasiswa dashboard = new Dashboard_Mahasiswa();
        new MahasiswaController(dashboard, mhs);
        dashboard.setVisible(true);
    }
}