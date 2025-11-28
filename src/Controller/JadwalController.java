package Controller;

import Model.Kelas;
import Model.Mahasiswa;
import Model.PendidikanDAO;
import View.Dashboard_Mahasiswa;
import View.Jadwal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class JadwalController {
    
    private Jadwal view;
    private PendidikanDAO dao;
    private Mahasiswa mahasiswaLog;

    public JadwalController(Jadwal view, Mahasiswa mhs) {
        this.view = view;
        this.mahasiswaLog = mhs;
        this.dao = new PendidikanDAO();
        
        // PENTING: Sambungkan Controller ke View
        view.setController(this);
        
        // Tampilkan semua jadwal saat pertama kali dibuka
        isiTabelJadwal("Semua"); 
    }
    
    // Method untuk mengisi tabel dengan Filter Hari
    public void isiTabelJadwal(String filterHari) {
        // 1. Siapkan Kolom
        String[] kolom = {"Hari", "Mata Kuliah", "Jam", "Ruang", "Dosen", "SKS"};
        DefaultTableModel model = new DefaultTableModel(null, kolom);
        
        // 2. Ambil Semua Data dari Database
        String semesterAktif = "20231"; 
        List<Kelas> listJadwal = dao.getJadwalKuliah(mahasiswaLog.getIdMahasiswa(), semesterAktif);
        
        // 3. Filter Data di sini (Looping)
        if (listJadwal != null) {
            for (Kelas k : listJadwal) {
                // LOGIKA FILTER:
                // Jika filter = "Semua", masukkan semua data.
                // Jika filter = Hari tertentu (misal "Senin"), hanya masukkan yang harinya sama.
                
                boolean isMatch = false;
                
                if (filterHari.equalsIgnoreCase("Semua") || filterHari.contains("Semua")) {
                    isMatch = true;
                } else if (k.getHari() != null && k.getHari().equalsIgnoreCase(filterHari)) {
                    isMatch = true;
                }
                
                // Jika cocok, masukkan ke tabel
                if (isMatch) {
                    Object[] row = new Object[] {
                        k.getHari(),
                        k.getNamaMk(),
                        k.getJamMulai() + "-" + k.getJamSelesai(),
                        k.getRuang(),
                        k.getNamaDosen(),
                        k.getSks()
                    };
                    model.addRow(row);
                }
            }
        }
        
        // 4. Update View
        view.setJadwalTable(model);
    }
    
    // --- METHOD YANG DIPANGGIL SAAT COMBOBOX DIGANTI ---
    public void filterHari() {
        // Ambil teks yang dipilih user di ComboBox (misal: "Senin")
        String selectedHari = view.getComboBoxHari().getSelectedItem().toString();
        
        // Isi ulang tabel dengan filter tersebut
        isiTabelJadwal(selectedHari);
    }
    
    // --- METHOD KEMBALI ---
    public void kembali() {
        // Tutup Frame Pembungkus Panel Jadwal
        java.awt.Window parent = javax.swing.SwingUtilities.getWindowAncestor(view);
        if (parent != null) {
            parent.dispose();
        }
        
        // Buka Dashboard
        Dashboard_Mahasiswa dashboard = new Dashboard_Mahasiswa();
        new MahasiswaController(dashboard, mahasiswaLog);
        dashboard.setVisible(true);
    }
}