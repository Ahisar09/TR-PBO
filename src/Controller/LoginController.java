package Controller;

import Model.PendidikanDAO; 
import Model.Mahasiswa;     
import View.Login;              // <--- Update: Sesuaikan dengan nama class View kamu
import View.Dashboard_Mahasiswa; // Pastikan nama file Dashboard kamu benar

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class LoginController {

    private Login view; // Menggunakan class 'Login' dari View kamu
    private PendidikanDAO dao;

    public LoginController(Login view) {
        this.view = view;
        this.dao = new PendidikanDAO();

        initController();
    }

    private void initController() {
        // Logika saat tombol Login diklik
        // Kita ambil tombol via getter yang baru kita buat di Login.java
        view.getBtnLogin().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosesLogin();
            }
        });
        
        // Agar bisa login dengan tekan ENTER di kolom password
        view.getPasswordField().addActionListener(e -> prosesLogin());
    }

    private void prosesLogin() {
        // 1. Ambil input menggunakan Getter dari View
        String username = view.getUsernameInput(); 
        String password = view.getPasswordInput();

        // 2. Validasi Input Kosong
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            view.showMessage("Username dan Password tidak boleh kosong!");
            return;
        }

        // 3. Cek Login ke Database
        Mahasiswa mhs = dao.login(username, password);

        if (mhs != null) {
            // === LOGIN BERHASIL ===
            view.showMessage("Login Berhasil! Selamat Datang, " + mhs.getNama());
            
            view.dispose(); // Tutup jendela login
            
            // Buka Dashboard (Nanti kita sambungkan ke Dashboard kamu)
            bukaDashboardMahasiswa(mhs);
            
        } else {
            // === LOGIN GAGAL ===
            view.showMessage("Login Gagal! Username atau Password salah.");
        }
    }

    private void bukaDashboardMahasiswa(Mahasiswa mhs) {
        // Pastikan class DashboardMahasiswa sudah ada di package view
        Dashboard_Mahasiswa dashboardView = new Dashboard_Mahasiswa();
        new MahasiswaController(dashboardView, mhs);
        dashboardView.setVisible(true);
    }
}