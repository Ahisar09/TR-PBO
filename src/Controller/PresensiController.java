package Controller;

import Model.Dosen;
import Model.DosenDAO;
import Model.Kelas;
import Model.Presensi; 

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PresensiController {

    private View.Presensi view; 
    private Dosen dosenLog;
    private DosenDAO dao;

    private List<Kelas> listKelasAjar;
    private List<Presensi> listMahasiswaTampil;
    private int pertemuanKe = 1; 

    public PresensiController(View.Presensi view, Dosen dosen) {
        this.view = view;
        this.dosenLog = dosen;
        this.dao = new DosenDAO();

        view.setController(this);
        isiComboBoxKelas();
    }

    public void isiComboBoxKelas() {
        listKelasAjar = dao.getKelasAjar(dosenLog.getIdDosen());

        view.getComboBoxMK().removeAllItems();
        view.getComboBoxMK().addItem("- Pilih Matakuliah -");

        for (Kelas k : listKelasAjar) {
            view.getComboBoxMK().addItem(k.getKodeMk() + " - " + k.getNamaMk() + " (" + k.getHari() + ")");
        }
    }

    public void loadMahasiswa() {
        int index = view.getComboBoxMK().getSelectedIndex();

        if (index <= 0) {
            view.setTabel(new DefaultTableModel(null, new String[]{"NIM", "Nama", "H", "I", "S", "A"}));
            return;
        }

        Kelas kelasDipilih = listKelasAjar.get(index - 1);
        listMahasiswaTampil = dao.getMahasiswaKelas(kelasDipilih.getIdKelas(), pertemuanKe);

        DefaultTableModel model = new DefaultTableModel(null,
                new String[]{"NIM", "Nama", "H", "I", "S", "A"}) {

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 2) return Boolean.class; // Checkbox
                return String.class;
            }
        };

        for (Presensi p : listMahasiswaTampil) {
            boolean h = false, i = false, s = false, a = false;

            if (p.getStatus() != null) {
                switch (p.getStatus()) {
                    case "Hadir": h = true; break;
                    case "Izin": i = true; break;
                    case "Sakit": s = true; break;
                    default: a = true;
                }
            } else {
                 a = true; 
            }

            model.addRow(new Object[]{
                p.getNimMahasiswa(),
                p.getNamaMahasiswa(),
                h, i, s, a
            });
        }

        view.setTabel(model);
    }

    public void simpanAbsen() {
        int index = view.getComboBoxMK().getSelectedIndex();

        if (index <= 0) {
            JOptionPane.showMessageDialog(view, "Pilih kelas terlebih dahulu!");
            return;
        }

        Kelas kelasDipilih = listKelasAjar.get(index - 1);
        int rows = view.getTable().getRowCount();
        int berhasil = 0;

        for (int row = 0; row < rows; row++) {
            int idMhs = listMahasiswaTampil.get(row).getIdMahasiswa();

            // Ambil Data Checkbox
            Boolean h = (Boolean) view.getTable().getValueAt(row, 2);
            Boolean i = (Boolean) view.getTable().getValueAt(row, 3);
            Boolean s = (Boolean) view.getTable().getValueAt(row, 4);

            String status = "Alfa"; // Default
            if (h != null && h) status = "Hadir";
            else if (i != null && i) status = "Izin";
            else if (s != null && s) status = "Sakit";
            if (dao.simpanPresensi(kelasDipilih.getIdKelas(), idMhs, pertemuanKe, status)) {
                berhasil++;
            }
        }

        if (berhasil > 0) {
            JOptionPane.showMessageDialog(view, "Presensi Pertemuan ke-" + pertemuanKe + " Berhasil Disimpan!");
            loadMahasiswa(); 
        } else {
            JOptionPane.showMessageDialog(view, "Gagal menyimpan.");
        }
    }

    public void batal() {
        view.getComboBoxMK().setSelectedIndex(0);
        view.setTabel(new DefaultTableModel(null, new String[]{"NIM", "Nama", "H", "I", "S", "A"}));
    }
    
  // METHOD  KEMBALI 
    public void kembali() {
        javax.swing.SwingUtilities.getWindowAncestor(view).dispose();
        View.Dashboard_Dosen dash = new View.Dashboard_Dosen();
        new Controller.DosenController(dash, dosenLog); 
        
        dash.setVisible(true);
    }

}