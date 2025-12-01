package Controller;

import Model.Admin;
import Model.AdminDAO;
import Model.Dosen;
import View.Data_Dosen;
import View.Dashboard_Admin;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.table.DefaultTableModel;

public class DataDosenController {
    
    private Data_Dosen view;
    private Admin adminLog;
    private AdminDAO dao;
    private List<Dosen> listDosen;

    public DataDosenController(Data_Dosen view, Admin admin) {
        this.view = view;
        this.adminLog = admin;
        this.dao = new AdminDAO();
        
        view.setController(this);
        isiTabel();
    }
    
    public void isiTabel() {
        listDosen = dao.getAllDosen();
        String[] kolom = {"NIP", "Nama Dosen", "Email", "Prodi"};
        DefaultTableModel model = new DefaultTableModel(null, kolom);
        
        for (Dosen d : listDosen) {
            Object[] row = { 
                d.getNip(), 
                d.getNama(), 
                d.getEmail(),
                d.getProdi()
         
            };
            model.addRow(row);
        }
        view.setTabelDosen(model);
    }
    
    // LOGIKA TOMBOL 

    public void klikKembali() {
        view.dispose();
        Dashboard_Admin dash = new Dashboard_Admin();
        new Controller.AdminController(dash, adminLog);
        dash.setVisible(true);
    }

    public void klikHapus() {
        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Pilih dosen yang akan dihapus!");
            return;
        }
        
        Dosen d = listDosen.get(row);
        int confirm = JOptionPane.showConfirmDialog(view, 
            "Yakin hapus dosen: " + d.getNama() + "?", 
            "Konfirmasi", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.hapusDosen(d.getNip())) {
                JOptionPane.showMessageDialog(view, "Dosen berhasil dihapus.");
                isiTabel();
            } else {
                JOptionPane.showMessageDialog(view, "Gagal menghapus dosen.");
            }
        }
    }

    public void klikEdit() {
        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Pilih data dosen yang ingin diedit terlebih dahulu!");
            return;
        }

        Dosen dosenLama = listDosen.get(row);

        // Form Input Popup 
        JTextField fieldNama = new JTextField(dosenLama.getNama());
        JTextField fieldEmail = new JTextField(dosenLama.getEmail());
        JTextField fieldProdi = new JTextField(dosenLama.getProdi());
        
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("NIP (Tidak bisa diubah): " + dosenLama.getNip()));
        panel.add(new JLabel("Nama Dosen:"));
        panel.add(fieldNama);
        panel.add(new JLabel("Email:"));
        panel.add(fieldEmail);
        panel.add(new JLabel("Prodi:"));
        panel.add(fieldProdi);

        int result = JOptionPane.showConfirmDialog(view, panel, 
                "Edit Data Dosen", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Update object
            dosenLama.setNama(fieldNama.getText());
            dosenLama.setEmail(fieldEmail.getText());
            dosenLama.setProdi(fieldProdi.getText());
            
            // Simpan ke database
            boolean sukses = dao.updateDosen(dosenLama);
            
            if (sukses) {
                JOptionPane.showMessageDialog(view, "Data berhasil diperbarui!");
                isiTabel(); // Refresh tabel
            } else {
                JOptionPane.showMessageDialog(view, "Gagal memperbarui data!");
            }
        }
    }
}