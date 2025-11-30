package Controller;

import Model.Mahasiswa;
import Model.Nilai;
import Model.RekapNilai; 
import Model.PendidikanDAO;
import View.Transkrip_Nilai;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class TranskripController {
    
    private Transkrip_Nilai view;
    private Mahasiswa mhs;
    private PendidikanDAO dao;

    public TranskripController(Transkrip_Nilai view, Mahasiswa mhs) {
        this.view = view;
        this.mhs = mhs;
        this.dao = new PendidikanDAO();
        
        view.setController(this);
        
        isiDataDiri();
        isiTabelDanHitungRekap();
    }
    
    private void isiDataDiri() {
        if (mhs != null) {
            view.setNama(mhs.getNama());
            view.setNim(mhs.getNim());
            view.setProdi(mhs.getProdi());
            view.setSemester(String.valueOf(mhs.getSemesterMasuk()));
        }
    }
    
    private void isiTabelDanHitungRekap() {
        // 1. Ambil Data Transkrip dari Database
        List<Nilai> listNilai = dao.getTranskrip(mhs.getIdMahasiswa());
        
        // 2. Siapkan Wadah Perhitungan (RekapNilai)
        RekapNilai rekap = new RekapNilai();
        rekap.setIdMahasiswa(mhs.getIdMahasiswa());
        rekap.setTotalSks(0);
        
        double totalBobotKaliSks = 0; // Variabel bantu hitung IPK
        
        // 3. Siapkan Header Tabel (Ada 6 Kolom)
        String[] kolom = {"Kode MK", "Nama Matakuliah", "SKS", "NA", "NH", "Semester"};
        DefaultTableModel model = new DefaultTableModel(null, kolom);
        
        // 4. Looping Data
        for (Nilai n : listNilai) {
            
            // --- A. MASUKKAN DATA KE TABEL ---
            // Pastikan urutan ini SAMA PERSIS dengan Header di atas
            Object[] row = {
                n.getKodeMk(),      // Kolom 1
                n.getNamaMk(),      // Kolom 2
                n.getSks(),         // Kolom 3
                n.getNilaiAngka(),  // Kolom 4 (NA)
                n.getNilaiHuruf(),  // Kolom 5 (NH)
                n.getSemester()     // Kolom 6
            };
            model.addRow(row);
            
            // --- B. HITUNG MATEMATIKA IPK (Di balik layar) ---
            double bobot = hitungBobot(n.getNilaiHuruf()); 
            int sksSaatIni = n.getSks();
            
            rekap.setTotalSks(rekap.getTotalSks() + sksSaatIni); // Jumlahkan SKS
            totalBobotKaliSks += (bobot * sksSaatIni);           // Jumlahkan Mutu
        }
        
        // 5. Hitung IPK Akhir
        if (rekap.getTotalSks() > 0) {
            double ipkHitung = totalBobotKaliSks / rekap.getTotalSks();
            rekap.setIpk(ipkHitung);
        } else {
            rekap.setIpk(0.0);
        }
        
        // 6. Update Tampilan View
        view.setTabelTranskrip(model); // Tabel terisi
        
        // Update Label Total SKS & IPK
        view.setTotalSks(String.valueOf(rekap.getTotalSks()));
        view.setIpk(String.format("%.2f", rekap.getIpk())); 
    }
    
    // Helper: Konversi Huruf ke Angka Bobot
    private double hitungBobot(String huruf) {
        if (huruf == null) return 0.0;
        switch (huruf.toUpperCase()) {
            case "A": return 4.0;
            case "A-": return 3.75; 
            case "B+": return 3.5; 
            case "B": return 3.0;
            case "B-": return 2.75;
            case "C+": return 2.5;
            case "C": return 2.0;
            case "D": return 1.0;
            default: return 0.0; // E
        }
    }
    
    public void kembali() {
        view.dispose();
    }
}