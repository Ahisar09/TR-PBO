package Controller;

import Model.Admin;
import Model.AdminDAO;
import View.Dashboard_Admin;
import View.Login;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class AdminController {
    
    private Dashboard_Admin view;
    private Admin adminLog;
    private AdminDAO dao;

    public AdminController(Dashboard_Admin view, Admin admin) {
        this.view = view;
        this.adminLog = admin;
        this.dao = new AdminDAO();
        
        initView();
        initController();
    }
    
    // DATA DIRI ADMIN DI DASHBOARD
    private void initView() {
        if (adminLog != null) {
            view.setNamaAdmin(adminLog.getNama());
            view.setDataDiri(adminLog.getNama(), adminLog.getEmail());
        }
    }
    
    private void initController() {
        
        // TOMBOL DATA MAHASISWA
        view.getButtonMahasiswa().addActionListener(e -> {
             view.dispose();
             View.Data_Mahasiswa viewMhs = new View.Data_Mahasiswa();
             new Controller.DataMahasiswaController(viewMhs, adminLog);
             viewMhs.setLocationRelativeTo(null);
             viewMhs.setVisible(true);
        });
        
        // TOMBOL DATA DOSEN
        view.getButtonDosen().addActionListener(e -> {
            view.dispose(); 
            View.Data_Dosen viewDosen = new View.Data_Dosen();
            new Controller.DataDosenController(viewDosen, adminLog); 
            viewDosen.setLocationRelativeTo(null);
            viewDosen.setVisible(true);
        });
        
        //  TOMBOL TAGIHAN (UPDATED)
        view.getButtonTagihan().addActionListener(e -> {
            view.dispose();
            View.Form_Tagihan viewTagihan = new View.Form_Tagihan();
            new Controller.FormTagihanController(viewTagihan, adminLog);
            viewTagihan.setLocationRelativeTo(null);
            viewTagihan.setVisible(true);
        });
        
        // TOMBOL DASHBOARD (Refresh)
        view.getButtonDashboard().addActionListener(e -> {
            initView();
            JOptionPane.showMessageDialog(view, "Dashboard disegarkan.");
        });
        
        //  TOMBOL LOGOUT
        view.getButtonLogout().addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(view, 
                "Apakah Anda yakin ingin keluar?", 
                "Konfirmasi Logout", 
                JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                view.dispose();
                Login loginView = new Login();
                new LoginController(loginView);
                loginView.setVisible(true);
            }
        });
    }
}