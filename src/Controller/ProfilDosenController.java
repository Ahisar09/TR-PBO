package Controller;

import Model.Dosen;
import Model.DosenDAO;
import View.Dashboard_Dosen;
import View.Profile_Dosen;
import javax.swing.JOptionPane;

public class ProfilDosenController {
    
    private Profile_Dosen view;
    private Dosen dosenLog;
    private DosenDAO dao;

    public ProfilDosenController(Profile_Dosen view, Dosen dosen) {
        this.view = view;
        this.dosenLog = dosen;
        this.dao = new DosenDAO();
        
        view.setController(this);
        
        isiFormulir();
        view.setEditMode(false); // Default terkunci
    }
    
    public void isiFormulir() {
        if (dosenLog == null) return;
        
        view.setNama(dosenLog.getNama());
        view.setNip(dosenLog.getNip());
        view.setNoHp(dosenLog.getNoHp());
        view.setEmail(dosenLog.getEmail());
        
    }
    
    // --- LOGIKA TOMBOL ---
    
    public void klikEdit() {
        view.setEditMode(true);
        JOptionPane.showMessageDialog(view, "Silakan ubah data pribadi Anda.");
    }
    
    public void klikBatal() {
        view.setEditMode(false);
        isiFormulir(); // Reset data
    }
    
    public void klikSimpan() {
        // Ambil data baru
        dosenLog.setNama(view.getInputNama());
        dosenLog.setEmail(view.getInputEmail());
        dosenLog.setNoHp(view.getInputNoHp());
        
        // Simpan ke DB
        if (dao.updateProfilDosen(dosenLog)) {
            JOptionPane.showMessageDialog(view, "Profil Berhasil Diperbarui!");
            view.setEditMode(false);
        } else {
            JOptionPane.showMessageDialog(view, "Gagal menyimpan data.");
        }
    }
    
    public void kembali() {
        view.dispose();
        View.Dashboard_Dosen dash = new View.Dashboard_Dosen();
        new Controller.DosenController(dash, dosenLog);
        dash.setVisible(true);
    }
}