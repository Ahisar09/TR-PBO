package Controller;

import Model.Dosen;
import Model.DosenDAO;
import Model.Kelas;
import View.Dashboard_Dosen;
import View.Kelas_Saya;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class KelasSayaController {
    
    private Kelas_Saya view;
    private Dosen dosenLog;
    private DosenDAO dao;

    public KelasSayaController(Kelas_Saya view, Dosen dosen) {
        this.view = view;
        this.dosenLog = dosen;
        this.dao = new DosenDAO();
        
        view.setController(this);
        
        isiDataDosen();
        isiTabelKelas();
    }
    
    private void isiDataDosen() {
        if (dosenLog != null) {
            view.setNamaDosen(dosenLog.getNama());
        }
    }
    
    private void isiTabelKelas() {
        // Ambil data kelas yang diajar
        List<Kelas> listKelas = dao.getKelasAjar(dosenLog.getIdDosen());
        
        String[] kolom = {"Kode MK", "Nama Matakuliah", "Hari", "Jam", "Ruang"};
        DefaultTableModel model = new DefaultTableModel(null, kolom);
        
        for (Kelas k : listKelas) {
            String jam = k.getJamMulai() + " - " + k.getJamSelesai();
            
            Object[] row = {
                k.getKodeMk(),
                k.getNamaMk(),
                k.getHari(),
                jam,
                k.getRuang()
            };
            model.addRow(row);
        }
        
        view.setTabelKelas(model);
    }
    
    public void kembali() {
        view.dispose();
        Dashboard_Dosen dash = new Dashboard_Dosen();
        new Controller.DosenController(dash, dosenLog);
        dash.setVisible(true);
    }
}