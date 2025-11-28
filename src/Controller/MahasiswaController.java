package Controller;

import Model.Mahasiswa;
import Model.Nilai;
import Model.PendidikanDAO; 
import Model.Tagihan;
import model.*;             
import View.Dashboard_Mahasiswa; // Sesuaikan dengan package View kamu
import View.Login;               // Untuk kembali ke Login saat logout

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;

public class MahasiswaController { 

    private Dashboard_Mahasiswa view;
    private PendidikanDAO dao;
    private Mahasiswa mahasiswaLog; 

    public MahasiswaController(Dashboard_Mahasiswa view, Mahasiswa mhs) {
        this.view = view;
        this.mahasiswaLog = mhs;
        this.dao = new PendidikanDAO();

        // 1. Tampilkan Data Ringkasan saat Dashboard dibuka
        initDashboard();

        // 2. Pasang Event Listener untuk Tombol
        initListeners();
    }

    private void initDashboard() {
        // A. Set Profil Singkat
        if (mahasiswaLog != null) {
            view.setNamaUser(mahasiswaLog.getNama());
            view.setProdiUser(mahasiswaLog.getProdi());
        }

        // B. Hitung Total SKS (Semester Aktif, misal "20231")
        // Di aplikasi nyata, semester ini bisa diambil dari config/database setting
        String semesterAktif = "20231"; 
        List<Nilai> listKst = dao.getKST(mahasiswaLog.getIdMahasiswa(), semesterAktif);
        
        int totalSks = 0;
        for (Nilai n : listKst) {
            totalSks += n.getSks();
        }
        view.setTotalSks(String.valueOf(totalSks));

        // C. Hitung Total Tagihan (Hanya yang statusnya 'belum')
        List<Tagihan> listTagihan = dao.getTagihan(mahasiswaLog.getIdMahasiswa());
        double totalHutang = 0;
        
        for (Tagihan t : listTagihan) {
            if ("belum".equalsIgnoreCase(t.getStatusBayar())) {
                totalHutang += t.getJumlah();
            }
        }
        // Format uang sederhana (bisa pakai NumberFormat jika mau lebih rapi)
        view.setTotalTagihan(String.format("%,.0f", totalHutang)); 
    }

    private void initListeners() {
        
        // --- LOGIKA LOGOUT ---
        view.getButtonLogout().addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(view, "Yakin ingin keluar?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                view.dispose(); // Tutup Dashboard
                
                // Kembali ke Login
                Login loginView = new Login();
                new LoginController(loginView);
                loginView.setVisible(true);
            }
        });

        // --- NAVIGASI MENU LAIN ---
        // Karena kamu belum mengirimkan View untuk Jadwal, KST, dll, 
        // saya buat pesan placeholder dulu.
        
        view.getButtonJadwal().addActionListener(e -> {
            // Nanti diganti dengan: new JadwalController(new JadwalView(), mahasiswaLog);
            view.showMessage("Membuka Menu Jadwal..."); 
        });

        view.getButtonKst().addActionListener(e -> {
             view.showMessage("Membuka Menu KST...");
        });
        
        view.getButtonNilai().addActionListener(e -> {
             view.showMessage("Membuka Transkrip Nilai...");
        });

        view.getButtonTagihan().addActionListener(e -> {
             view.showMessage("Membuka Rincian Tagihan...");
        });
        
        view.getButtonTodoList().addActionListener(e -> {
             view.showMessage("Membuka To-Do List...");
        });
    }
}