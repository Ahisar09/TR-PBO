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
        
        view.setController(this);
        isiTabelJadwal("Semua"); 
    }
    
    // Method isi tabel dengan Filter Hari
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
                // LOGIKA FILTER
                // Jika filter = "Semua", masukkan semua data.
                // Jika filter = Hari tertentu (misal Senin)
                boolean isMatch = false;
                
                if (filterHari.equalsIgnoreCase("Semua") || filterHari.contains("Semua")) {
                    isMatch = true;
                } else if (k.getHari() != null && k.getHari().equalsIgnoreCase(filterHari)) {
                    isMatch = true;
                }
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
        
        // Update View
        view.setJadwalTable(model);
    }
    
    // METHOD COMBOBOX DIGANTI 
    public void filterHari() {
        // Ambil teks yang dipilih user di ComboBox (misal Senin)
        String selectedHari = view.getComboBoxHari().getSelectedItem().toString();
        
        // Isi ulang tabel dengan filter tersebut
        isiTabelJadwal(selectedHari);
    }
    
    //  CEK BENTROK 
    private void cekBentrok(List<Kelas> listJadwal) {
        if (listJadwal == null || listJadwal.isEmpty()) return;

        StringBuilder pesanBentrok = new StringBuilder();
        boolean adaBentrok = false;

        // Loop bersarang (Nested Loop) 
        for (int i = 0; i < listJadwal.size(); i++) {
            for (int j = i + 1; j < listJadwal.size(); j++) {
                
                Kelas A = listJadwal.get(i);
                Kelas B = listJadwal.get(j);

                // 1. Cek 
                if (A.getHari().equalsIgnoreCase(B.getHari())) {
                    
                    // 2. Cek Apakah Jamnya Beririsan (Overlap)?
                    // Rumus: (StartA < EndB) && (StartB < EndA)
                    if (A.getJamMulai().getTime() < B.getJamSelesai().getTime() &&
                        B.getJamMulai().getTime() < A.getJamSelesai().getTime()) {
                        
                        adaBentrok = true;
                        pesanBentrok.append("- ")
                                    .append(A.getNamaMk()).append(" & ").append(B.getNamaMk())
                                    .append(" (Hari ").append(A.getHari()).append(")\n");
                    }
                }
            }
        }

        // tampilkan peringatan
        if (adaBentrok) {
            javax.swing.JOptionPane.showMessageDialog(view, 
                "PERINGATAN: Ditemukan Jadwal Bentrok!\n\n" + pesanBentrok.toString(),
                "Jadwal Bentrok",
                javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }
    
    // METHOD KEMBALI 
    public void kembali() {
        java.awt.Window parent = javax.swing.SwingUtilities.getWindowAncestor(view);
        if (parent != null) {
            parent.dispose();
        }
        
        Dashboard_Mahasiswa dashboard = new Dashboard_Mahasiswa();
        new MahasiswaController(dashboard, mahasiswaLog);
        dashboard.setVisible(true);
    }
}