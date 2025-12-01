package Controller;

import Model.Dosen;
import Model.DosenDAO;
import Model.Kelas;
import Model.Nilai;
import View.Dashboard_Dosen;
import View.Input_Nilai;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class InputNilaiController {
    
    private Input_Nilai view;
    private Dosen dosenLog;
    private DosenDAO dao;
    
    private List<Kelas> listKelasAjar;
    private List<Nilai> listNilaiTampil;

    public InputNilaiController(Input_Nilai view, Dosen dosen) {
        this.view = view;
        this.dosenLog = dosen;
        this.dao = new DosenDAO();
        
        view.setController(this); 
        
        isiComboBoxKelas();
        view.setEditMode(false); 
    }
    
    public void isiComboBoxKelas() {
        listKelasAjar = dao.getKelasAjar(dosenLog.getIdDosen());
        view.getComboBoxKelas().removeAllItems();
        view.getComboBoxKelas().addItem("- Pilih Matakuliah -");
        
        for (Kelas k : listKelasAjar) {
            view.getComboBoxKelas().addItem(k.getKodeMk() + " - " + k.getNamaMk() + " (" + k.getHari() + ")");
        }
    }
    
    public void loadMahasiswa() {
        int index = view.getComboBoxKelas().getSelectedIndex();
        if (index <= 0) {
            view.setTabelNilai(new DefaultTableModel(null, new String[]{"NIM", "Nama", "Nilai Angka", "Nilai Huruf"}));
            return;
        }
        
        Kelas kelasDipilih = listKelasAjar.get(index - 1);
        listNilaiTampil = dao.getNilaiMahasiswaKelas(kelasDipilih.getIdKelas());
        
        DefaultTableModel model = new DefaultTableModel(null, new String[]{"NIM", "Nama", "Nilai Angka", "Nilai Huruf"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; 
            }
        };
        
        for (Nilai n : listNilaiTampil) {
            model.addRow(new Object[]{
                n.getKodeMk(), 
                n.getNamaMk(), 
                n.getNilaiAngka(),
                n.getNilaiHuruf()
            });
        }
        view.setTabelNilai(model);
    }
    
    //  LOGIKA TOMBOL
    public void klikEdit() {
        view.setEditMode(true); 
        JOptionPane.showMessageDialog(view, "Silakan input Nilai Angka (0-100) di tabel, lalu tekan Enter.");
    }
    
    public void klikSimpan() {
        // Stop editing cell jika masih aktif (biar nilai terakhir tersimpan)
        if (view.getTable().isEditing()) {
            view.getTable().getCellEditor().stopCellEditing();
        }
        
        int rows = view.getTable().getRowCount();
        int berhasil = 0;
        
        for (int i = 0; i < rows; i++) {
            Nilai n = listNilaiTampil.get(i);
            
            // Ambil inputan user dari tabel (Kolom 2 = Nilai Angka)
            Object val = view.getTable().getValueAt(i, 2);
            double angkaBaru = Double.parseDouble(val.toString());
            
            // Hitung Huruf Otomatis
            String hurufBaru = hitungHuruf(angkaBaru);
            
            // Update ke Database
            if (dao.updateNilaiMahasiswa(n.getIdNilai(), angkaBaru, hurufBaru)) {
                berhasil++;
            }
        }
        
        if (berhasil > 0) {
            JOptionPane.showMessageDialog(view, "Berhasil menyimpan " + berhasil + " data nilai!");
            view.setEditMode(false);
            loadMahasiswa(); // Refresh tabel 
        }
    }
    
    public void klikBatal() {
        view.setEditMode(false);
        loadMahasiswa(); 
    }
    
    public void kembali() {
        view.dispose();
        Dashboard_Dosen dash = new Dashboard_Dosen();
        new Controller.DosenController(dash, dosenLog);
        dash.setVisible(true);
    }
    
    // Konversi Nilai
    private String hitungHuruf(double angka) {
        if (angka >= 85) return "A";
        if (angka >= 80) return "A-";
        if (angka >= 75) return "B+";
        if (angka >= 70) return "B";
        if (angka >= 65) return "B-";
        if (angka >= 60) return "C+";
        if (angka >= 50) return "C";
        if (angka >= 40) return "D";
        return "E";
    }
}