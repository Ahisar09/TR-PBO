package Controller;

import Model.PendidikanDAO;
import Model.Mahasiswa;
import Model.Dosen;
import Model.Admin;

import View.Login;
import View.Dashboard_Mahasiswa;
import View.Dashboard_Dosen;
import View.Dashboard_Admin; // SUDAH DIAKTIFKAN

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class LoginController {

    private Login view;
    private PendidikanDAO dao;

    public LoginController(Login view) {
        this.view = view;
        this.dao = new PendidikanDAO();
        initController();
    }

    private void initController() {
        // Klik tombol Login
        view.getBtnLogin().addActionListener(e -> prosesLogin());
        
        // Tekan Enter di Password Field juga akan memicu login
        view.getPasswordField().addActionListener(e -> prosesLogin());
    }

    private void prosesLogin() {
        String inputUser = view.getUsernameInput();
        String inputPass = view.getPasswordInput();

        // Validasi Input Kosong
        if (inputUser.trim().isEmpty() || inputPass.trim().isEmpty()) {
            view.showMessage("Username/NIM/NIP dan Password harus diisi!");
            return;
        }

        // 1. CEK MAHASISWA (Login pakai NIM)
        Mahasiswa mhs = dao.login(inputUser, inputPass); 
        if (mhs != null) {
            view.showMessage("Login Berhasil! Selamat Datang Mahasiswa, " + mhs.getNama());
            view.dispose(); 
            bukaDashboardMahasiswa(mhs);
            return; 
        }

        // 2. CEK DOSEN (Login pakai NIP)
        Dosen dsn = dao.loginDosen(inputUser, inputPass);
        if (dsn != null) {
            view.showMessage("Login Berhasil! Selamat Datang Dosen, " + dsn.getNama());
            view.dispose(); 
            bukaDashboardDosen(dsn);
            return; 
        }
        
        // 3. CEK SUPER ADMIN (Login pakai Username)
        Admin adm = dao.loginAdmin(inputUser, inputPass);
        if (adm != null) {
            view.showMessage("Login Berhasil! Selamat Datang Admin, " + adm.getNama());
            view.dispose(); 
            bukaDashboardAdmin(adm);
            return; 
        }

        // 4. JIKA SEMUA GAGAL
        view.showMessage("Login Gagal! Akun tidak ditemukan atau Password salah.");
    }

    // --- METHOD MEMBUKA DASHBOARD ---

    private void bukaDashboardMahasiswa(Mahasiswa mhs) {
        Dashboard_Mahasiswa dash = new Dashboard_Mahasiswa();
        new MahasiswaController(dash, mhs);
        dash.setVisible(true);
    }
    
    private void bukaDashboardDosen(Dosen dsn) {
        Dashboard_Dosen dash = new Dashboard_Dosen();
        new Controller.DosenController(dash, dsn); 
        dash.setVisible(true);
    }
    
    private void bukaDashboardAdmin(Admin adm) {
        // --- BAGIAN INI SUDAH DIPERBAIKI ---
        // Sekarang membuka Dashboard Admin yang sesungguhnya
        
        View.Dashboard_Admin dash = new View.Dashboard_Admin();
        new Controller.AdminController(dash, adm); 
        dash.setVisible(true);
    }
}