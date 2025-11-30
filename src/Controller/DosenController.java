package Controller;

import Model.Dosen;
import Model.DosenDAO;
import Model.Kelas;
import View.Dashboard_Dosen;
import View.Login;
import View.Presensi;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DosenController {
    
    private Dashboard_Dosen view;
    private Dosen dosenLog;
    private DosenDAO dao;

    public DosenController(Dashboard_Dosen view, Dosen dosen) {
        this.view = view;
        this.dosenLog = dosen;
        this.dao = new DosenDAO();
        
        // PENTING: Isi data saat pertama kali dibuka
        initView();
        
        initController();
    }
    
    private void initView() {
        if (dosenLog != null) {
            view.setNamaDosen(dosenLog.getNama());
        }
        isiTabelKelas(); // <--- Panggil fungsi isi tabel
    }
    
    // Update method ini di DosenController.java
    private void isiTabelKelas() {
        List<Kelas> listKelas = dao.getKelasAjar(dosenLog.getIdDosen());
        
        // Tambahkan Kolom "Ruang"
        String[] kolom = {"Mata Kuliah", "Id Kelas", "Hari", "Jam", "Ruang"};
        DefaultTableModel model = new DefaultTableModel(null, kolom);
        
        for (Kelas k : listKelas) {
            String formatJam = k.getJamMulai() + " - " + k.getJamSelesai();
            
            Object[] row = {
                k.getNamaMk(),
                k.getIdKelas(),
                k.getHari(),
                formatJam,
                k.getRuang() // Masukkan data ruang ke tabel
            };
            model.addRow(row);
        }
        
        view.setTabelKelas(model);
    }

    private void initController() {
        
        // --- LOGIKA TOMBOL PROFIL ---
        view.getButtonProfil().addActionListener(e -> {
            view.dispose(); // Tutup Dashboard
            
            // 1. Buat Object View LANGSUNG dengan tipe kelasnya (Bukan javax.swing.JFrame)
            View.Profile_Dosen frameProfil = new View.Profile_Dosen(); 
            
            // 2. Panggil Controller (Sekarang tidak perlu casting lagi, langsung masuk)
            new Controller.ProfilDosenController(frameProfil, dosenLog);
            
            // 3. Atur Posisi & Tampilkan
            frameProfil.pack(); // Agar ukuran pas
            frameProfil.setLocationRelativeTo(null);
            frameProfil.setResizable(false);
            frameProfil.setVisible(true);
        });
        
        // --- LOGIKA TOMBOL KELAS SAYA ---
        view.getButtonKelas().addActionListener(e -> {
            view.dispose(); // Tutup Dashboard
            
            // PERBAIKAN PENTING DI SINI:
            // Jangan gunakan 'javax.swing.JFrame frameKelas = ...'
            // Gunakan nama class view-nya secara spesifik:
            View.Kelas_Saya frameKelas = new View.Kelas_Saya(); 
            
            // Panggil Controller (Sekarang pasti cocok tipe datanya)
            new Controller.KelasSayaController(frameKelas, dosenLog);
            
            // Setting Tampilan
            frameKelas.setLocationRelativeTo(null);
            frameKelas.setResizable(false);
            frameKelas.setVisible(true);
        });
        
        // --- LOGIKA TOMBOL PRESENSI ---
        view.getButtonPresensi().addActionListener(e -> {
            view.dispose(); // Tutup Dashboard
            
            // Buka Jendela Presensi
            JFrame framePresensi = new JFrame("Presensi Mahasiswa");
            View.Presensi panelPresensi = new View.Presensi();
            
            framePresensi.setContentPane(panelPresensi);
            framePresensi.pack();
            framePresensi.setLocationRelativeTo(null);
            
            // Panggil Controller Presensi
            new PresensiController(panelPresensi, dosenLog);
            
            framePresensi.setVisible(true);
        });
        
        // --- LOGIKA TOMBOL INPUT NILAI ---
        view.getButtonNilai().addActionListener(e -> {
            view.dispose();
            
            View.Input_Nilai viewNilai = new View.Input_Nilai();
            
            // Panggil Controller
            new Controller.InputNilaiController(viewNilai, dosenLog);
            
            viewNilai.setLocationRelativeTo(null);
            viewNilai.setVisible(true);
        });
        
        
        // --- LOGIKA TOMBOL LOGOUT ---
        view.getButtonLogout().addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(view, "Yakin ingin keluar?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                view.dispose();
                Login loginView = new Login();
                new LoginController(loginView);
                loginView.setVisible(true);
            }
        });
    }
}