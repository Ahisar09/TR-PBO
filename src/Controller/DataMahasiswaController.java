package Controller;

import Model.Admin;
import Model.AdminDAO;
import Model.Mahasiswa;
import View.Data_Mahasiswa;
import View.Dashboard_Admin;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class DataMahasiswaController {
    
    private Data_Mahasiswa view;
    private Admin adminLog;
    private AdminDAO dao;
    private List<Mahasiswa> listMahasiswa;

    public DataMahasiswaController(Data_Mahasiswa view, Admin admin) {
        this.view = view;
        this.adminLog = admin;
        this.dao = new AdminDAO();
        
        view.setController(this);
        isiTabel();
    }
    
    public void isiTabel() {
        listMahasiswa = dao.getAllMahasiswa();
        
        // Header
        String[] kolom = {"NIM", "Nama", "Program Studi", "Semester", "Status"};
        DefaultTableModel model = new DefaultTableModel(null, kolom);
        
        for (Mahasiswa m : listMahasiswa) {
            Object[] row = {
                m.getNim(),
                m.getNama(),
                m.getProdi(),
                m.getSemesterMasuk(),
                m.getStatus()
            };
            model.addRow(row);
        }
        view.getTabelMahasiswa().setModel(model);
    }

    public void klikKembali() {
        view.dispose();
        Dashboard_Admin dash = new Dashboard_Admin();
        new AdminController(dash, adminLog);
        dash.setVisible(true);
    }

    public void klikHapus() {
        int row = view.getTabelMahasiswa().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Pilih data mahasiswa yang akan dihapus!");
            return;
        }
        
        Mahasiswa m = listMahasiswa.get(row);
        int confirm = JOptionPane.showConfirmDialog(view, 
                "Yakin hapus mahasiswa: " + m.getNama() + "?", 
                "Konfirmasi Hapus", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.hapusMahasiswa(m.getNim())) {
                JOptionPane.showMessageDialog(view, "Data berhasil dihapus.");
                isiTabel();
            } else {
                JOptionPane.showMessageDialog(view, "Gagal menghapus data.");
            }
        }
    }

    //  LOGIKA EDIT 

    public void klikEdit() {
        int row = view.getTabelMahasiswa().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Pilih data mahasiswa yang ingin diedit!");
            return;
        }

        Mahasiswa mhsLama = listMahasiswa.get(row);

        // 1. Buat Komponen Input untuk Popup
        JTextField fieldNama = new JTextField(mhsLama.getNama());
        JTextField fieldProdi = new JTextField(mhsLama.getProdi());
        JTextField fieldSemester = new JTextField(String.valueOf(mhsLama.getSemesterMasuk()));
        
        // Combo Box untuk Status 
        String[] statusOpsi = {"aktif", "nonaktif"};
        JComboBox<String> comboStatus = new JComboBox<>(statusOpsi);
        comboStatus.setSelectedItem(mhsLama.getStatus());

        // 2. Panel Layout
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("NIM (Tidak bisa diubah): " + mhsLama.getNim()));
        
        panel.add(new JLabel("Nama Mahasiswa:"));
        panel.add(fieldNama);
        
        panel.add(new JLabel("Program Studi:"));
        panel.add(fieldProdi);
        
        panel.add(new JLabel("Semester:"));
        panel.add(fieldSemester);
        
        panel.add(new JLabel("Status:"));
        panel.add(comboStatus);

        // 3. Tampilkan Dialog
        int result = JOptionPane.showConfirmDialog(view, panel, 
                "Edit Data Mahasiswa", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.PLAIN_MESSAGE);

        // 4. Proses Simpan
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Update Object
                mhsLama.setNama(fieldNama.getText());
                mhsLama.setProdi(fieldProdi.getText());
                mhsLama.setSemesterMasuk(Integer.parseInt(fieldSemester.getText()));
                mhsLama.setStatus((String) comboStatus.getSelectedItem());
                
                // Update Database
                if (dao.updateMahasiswa(mhsLama)) {
                    JOptionPane.showMessageDialog(view, "Data berhasil diperbarui!");
                    isiTabel(); // Refresh tabel
                } else {
                    JOptionPane.showMessageDialog(view, "Gagal memperbarui data di database!");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(view, "Semester harus berupa angka!");
            }
        }
    }
}