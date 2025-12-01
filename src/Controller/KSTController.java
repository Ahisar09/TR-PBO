package Controller;

import Model.Mahasiswa;
import Model.Kelas;
import Model.PendidikanDAO;
import View.Dashboard_Mahasiswa;
import View.KST;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class KSTController {
    
    private KST view;
    private PendidikanDAO dao;
    private Mahasiswa mahasiswaLog;

    public KSTController(KST view, Mahasiswa mhs) {
        this.view = view;
        this.mahasiswaLog = mhs;
        this.dao = new PendidikanDAO();
        
        view.setController(this); 
        
        isiDataDiri();
        isiTabelKst();
    }
    

    public void isiDataDiri() {
         if (mahasiswaLog != null) {
            view.setNamaMahasiswa(mahasiswaLog.getNama());
            view.setNimMahasiswa(mahasiswaLog.getNim());
        }
    }
    
    public void isiTabelKst() {
     
         String[] kolom = {"Kode MK", "Matakuliah", "SKS", "Hari", "Waktu"};
         DefaultTableModel model = new DefaultTableModel(null, kolom);
         //  logika ambil data
         String semesterAktif = "20231";
         List<Kelas> listKst = dao.getJadwalKuliah(mahasiswaLog.getIdMahasiswa(), semesterAktif);
         if (listKst != null) {
            for (Kelas k : listKst) {
                Object[] row = new Object[] {
                    k.getKodeMk(), k.getNamaMk(), k.getSks(), k.getHari(), k.getJamMulai() + "-" + k.getJamSelesai()
                };
                model.addRow(row);
            }
         }
         view.setTableKST(model);
    }

    // LOGIKA HAPUS 
    public void hapusMatakuliah(String kodeMk) {
        int confirm = JOptionPane.showConfirmDialog(view, 
                "Yakin ingin menghapus matakuliah " + kodeMk + "?", 
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean sukses = dao.hapusKstMatakuliah(mahasiswaLog.getIdMahasiswa(), kodeMk);
            
            if (sukses) {
                JOptionPane.showMessageDialog(view, "Matakuliah berhasil dihapus!");
                isiTabelKst(); 
            } else {
                JOptionPane.showMessageDialog(view, "Gagal menghapus. Coba lagi.");
            }
        }
    }
    
    
    // METHOD KEMBALI 
    public void kembali() {
        java.awt.Window parentWindow = javax.swing.SwingUtilities.getWindowAncestor(view);
        if (parentWindow != null) {
            parentWindow.dispose();
        }
        Dashboard_Mahasiswa dashboard = new Dashboard_Mahasiswa();
        new MahasiswaController(dashboard, mahasiswaLog); 
        dashboard.setVisible(true);
    }
}