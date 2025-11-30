package Controller;

import Model.Admin;
import Model.AdminDAO;
import Model.Mahasiswa;
import Model.Tagihan;
import View.Form_Tagihan;
import View.Dashboard_Admin;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FormTagihanController {
    
    private Form_Tagihan view;
    private AdminDAO dao;
    private Admin adminLog;
    
    // List data
    private List<Mahasiswa> listMahasiswa; 
    private List<Tagihan> listTagihan;
    
    // Penanda ID saat Edit
    private int idTagihanTerpilih = 0; 

    public FormTagihanController(Form_Tagihan view, Admin admin) {
        this.view = view;
        this.adminLog = admin;
        this.dao = new AdminDAO();
        
        // Hubungkan View dengan Controller
        view.setController(this);
        
        initForm();
        isiComboMahasiswa();
        isiTabel();
    }
    
    private void initForm() {
        // Setup Combo Status
        view.getComboStatus().removeAllItems();
        view.getComboStatus().addItem("Belum"); 
        view.getComboStatus().addItem("Lunas");
        
        // Setup Listener Tabel (Klik Baris) - Ini TETAP ADA karena MouseListener beda dengan Action
        view.getTabelTagihan().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                klikTabel();
            }
        });
        
        // Awal load: Matikan tombol edit
        view.getBtnEdit().setEnabled(false);
    }
    
    private void isiComboMahasiswa() {
        listMahasiswa = dao.getAllMahasiswa();
        view.getComboMahasiswa().removeAllItems();
        
        for (Mahasiswa m : listMahasiswa) {
            // Menampilkan Nama (NIM)
            view.getComboMahasiswa().addItem(m.getNama() + " (" + m.getNim() + ")");
        }
    }
    
    public void isiTabel() {
        listTagihan = dao.getAllTagihan();
        String[] kolom = {"Mahasiswa", "Jenis Tagihan", "Jumlah", "Jatuh Tempo", "Status"};
        DefaultTableModel model = new DefaultTableModel(null, kolom);
        
        for (Tagihan t : listTagihan) {
            Object[] row = {
                t.getNamaMahasiswa(),
                t.getJenisTagihan(),
                String.format("Rp %,.0f", t.getJumlah()), // Format angka
                t.getJatuhTempo(),
                t.getStatusBayar()
            };
            model.addRow(row);
        }
        view.getTabelTagihan().setModel(model);
    }
    
    public void klikSimpan() {
        try {
            if(cekInputKosong()) return;

            // Ambil ID Mahasiswa dari index combobox
            int idxMhs = view.getComboMahasiswa().getSelectedIndex();
            // Cek jika index valid
            if (idxMhs < 0 || listMahasiswa.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Data Mahasiswa kosong atau belum dipilih!");
                return;
            }
            
            int idMhs = listMahasiswa.get(idxMhs).getIdMahasiswa();
            
            Tagihan t = new Tagihan();
            t.setIdMahasiswa(idMhs);
            t.setJenisTagihan(view.getFieldJenis().getText());
            t.setJumlah(Double.parseDouble(view.getFieldJumlah().getText()));
            t.setJatuhTempo(view.getDateChooser().getDate());
            t.setStatusBayar(view.getComboStatus().getSelectedItem().toString());
            
            if (dao.tambahTagihan(t)) {
                JOptionPane.showMessageDialog(view, "Tagihan berhasil ditambahkan!");
                klikReset();
                isiTabel();
            } else {
                JOptionPane.showMessageDialog(view, "Gagal menambah tagihan.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Kolom 'Jumlah' harus berupa angka!");
        } catch (Exception e) {
             JOptionPane.showMessageDialog(view, "Terjadi kesalahan: " + e.getMessage());
        }
    }
    
    public void klikTabel() {
        int row = view.getTabelTagihan().getSelectedRow();
        if (row != -1) {
            Tagihan t = listTagihan.get(row);
            idTagihanTerpilih = t.getIdTagihan(); // Simpan ID untuk update
            
            // Isi Form dengan data terpilih
            view.getFieldJenis().setText(t.getJenisTagihan());
            view.getFieldJumlah().setText(String.valueOf((long)t.getJumlah()));
            view.getDateChooser().setDate(t.getJatuhTempo());
            view.getComboStatus().setSelectedItem(t.getStatusBayar());
            
            // Cari Mahasiswa di Combo Box agar terseleksi otomatis
            for(int i=0; i<listMahasiswa.size(); i++) {
                if(listMahasiswa.get(i).getIdMahasiswa() == t.getIdMahasiswa()) {
                    view.getComboMahasiswa().setSelectedIndex(i);
                    break;
                }
            }
            
            // Mode Edit: Aktifkan tombol Edit, Matikan Simpan
            view.getBtnSimpan().setEnabled(false);
            view.getBtnEdit().setEnabled(true);
        }
    }
    
    public void klikEdit() {
        if (idTagihanTerpilih == 0) {
            JOptionPane.showMessageDialog(view, "Pilih data tabel dulu!");
            return;
        }
        
        try {
            if(cekInputKosong()) return;

            int idxMhs = view.getComboMahasiswa().getSelectedIndex();
            int idMhs = listMahasiswa.get(idxMhs).getIdMahasiswa();
            
            Tagihan t = new Tagihan();
            t.setIdTagihan(idTagihanTerpilih); // ID untuk WHERE clause
            t.setIdMahasiswa(idMhs);
            t.setJenisTagihan(view.getFieldJenis().getText());
            t.setJumlah(Double.parseDouble(view.getFieldJumlah().getText()));
            t.setJatuhTempo(view.getDateChooser().getDate());
            t.setStatusBayar(view.getComboStatus().getSelectedItem().toString());
            
            if (dao.updateTagihan(t)) {
                JOptionPane.showMessageDialog(view, "Tagihan berhasil diupdate!");
                klikReset();
                isiTabel();
            } else {
                JOptionPane.showMessageDialog(view, "Gagal update data.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Jumlah harus angka!");
        }
    }
    
    public void klikReset() {
        view.getFieldJenis().setText("");
        view.getFieldJumlah().setText("");
        view.getDateChooser().setDate(null);
        view.getComboStatus().setSelectedIndex(0);
        // Cek dulu sebelum set index agar tidak error jika combo kosong
        if (view.getComboMahasiswa().getItemCount() > 0) {
            view.getComboMahasiswa().setSelectedIndex(0);
        }
        view.getTabelTagihan().clearSelection();
        
        idTagihanTerpilih = 0;
        view.getBtnSimpan().setEnabled(true);
        view.getBtnEdit().setEnabled(false);
    }

    public void klikKembali() {
        view.dispose();
        Dashboard_Admin dash = new Dashboard_Admin();
        new AdminController(dash, adminLog);
        dash.setVisible(true);
    }
    
    private boolean cekInputKosong() {
        if(view.getFieldJenis().getText().isEmpty() || 
           view.getFieldJumlah().getText().isEmpty() || 
           view.getDateChooser().getDate() == null) {
            JOptionPane.showMessageDialog(view, "Semua field harus diisi!");
            return true;
        }
        return false;
    }
}