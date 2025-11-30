package Controller;

import Model.Mahasiswa;
import Model.PendidikanDAO;
import Model.Tagihan;
import View.Dashboard_Mahasiswa;
import View.Tagihan_Mahasiswa;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;

public class TagihanController {
    
    private Tagihan_Mahasiswa view;
    private Mahasiswa mhs;
    private PendidikanDAO dao;

    public TagihanController(Tagihan_Mahasiswa view, Mahasiswa mhs) {
        this.view = view;
        this.mhs = mhs;
        this.dao = new PendidikanDAO();
        
        // Sambungkan View ke Controller
        view.setController(this);
        
        // Isi Data saat dibuka
        isiDataDiri();
        isiTabelTagihan();
    }
    
    private void isiDataDiri() {
        if (mhs != null) {
            view.setNama(mhs.getNama());
            view.setNim(mhs.getNim());
            view.setProdi(mhs.getProdi());
            view.setSemester(String.valueOf(mhs.getSemesterMasuk()));
        }
    }
    
    private void isiTabelTagihan() {
        // 1. Ambil data dari DAO
        List<Tagihan> listTagihan = dao.getTagihan(mhs.getIdMahasiswa());
        
        // 2. Siapkan Tabel Model
        String[] kolom = {"No", "Jenis Tagihan", "Jumlah", "Jatuh Tempo", "Status"};
        DefaultTableModel model = new DefaultTableModel(null, kolom);
        
        // Formatter untuk Rupiah
        NumberFormat rupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        
        int no = 1;
        boolean adaTunggakan = false;
        
        for (Tagihan t : listTagihan) {
            // Cek status untuk logika warna nanti
            if ("belum".equalsIgnoreCase(t.getStatusBayar())) {
                adaTunggakan = true;
            }
            
            Object[] row = {
                no++,
                t.getJenisTagihan(),
                rupiah.format(t.getJumlah()), // Tampil sebagai Rupiah
                t.getJatuhTempo(),
                t.getStatusBayar().toUpperCase() // LUNAS / BELUM
            };
            model.addRow(row);
        }
        
        view.setTabelTagihan(model);
        
        // 3. Update Status Umum (Header)
        if (adaTunggakan) {
            view.setStatusHeader("BELUM LUNAS", java.awt.Color.RED);
        } else {
            view.setStatusHeader("LUNAS", java.awt.Color.BLUE);
        }
    }
    
    // Logika Tombol Kembali
    public void kembali() {
        view.dispose();
        Dashboard_Mahasiswa dashboard = new Dashboard_Mahasiswa();
        new MahasiswaController(dashboard, mhs);
        dashboard.setVisible(true);
    }
}