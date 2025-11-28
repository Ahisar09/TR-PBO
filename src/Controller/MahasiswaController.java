package Controller;

import Model.Mahasiswa;
import Model.Nilai;
import Model.PendidikanDAO;
import Model.Tagihan;
import View.Dashboard_Mahasiswa;
import View.Login;

// Import View lain
import View.Jadwal;
import View.KST;
import View.Transkrip_Nilai;
import View.Tagihan_Mahasiswa;

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

        // 1. Tampilkan Data Awal
        isiDataDashboard();
        
        // 2. Pasang Event Listener (Agar tombol berfungsi)
        initController();
    }

    private void initController() {
        
        // --- LOGIKA TOMBOL PROFILE ---
        view.getButtonProfile().addActionListener(e -> {
            view.dispose(); 
            View.Profile_Mahasiswa profileView = new View.Profile_Mahasiswa();
            new Controller.ProfileController(profileView, mahasiswaLog);
            profileView.pack(); 
            profileView.setLocationRelativeTo(null);
            profileView.setResizable(false); 
            profileView.setVisible(true);
        });
        
        // --- LOGIKA TOMBOL REGISTRASI MATAKULIAH (RMK) ---
        view.getButtonRMK().addActionListener(e -> {
            view.dispose(); 
            javax.swing.JFrame frameRmk = new javax.swing.JFrame("Registrasi Matakuliah");
            View.Registrasi_Matakuliah panelRmk = new View.Registrasi_Matakuliah();
            frameRmk.setContentPane(panelRmk);
            frameRmk.pack();
            frameRmk.setLocationRelativeTo(null);
            new Controller.RMKController(panelRmk, mahasiswaLog);
            frameRmk.setVisible(true);
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

        // --- LOGIKA TOMBOL JADWAL ---
        view.getButtonJadwal().addActionListener(e -> {
            view.dispose(); 
            javax.swing.JFrame frameWindow = new javax.swing.JFrame("Jadwal Kuliah");
            Jadwal panelJadwal = new Jadwal();
            frameWindow.setContentPane(panelJadwal);
            frameWindow.pack(); 
            frameWindow.setLocationRelativeTo(null); 
            new JadwalController(panelJadwal, mahasiswaLog); 
            frameWindow.setVisible(true);
        });

        // --- LOGIKA TOMBOL KST ---
        view.getButtonKst().addActionListener(e -> {
            view.dispose(); 
            javax.swing.JFrame frameKst = new javax.swing.JFrame("Kartu Studi Tetap");
            KST panelKst = new KST();
            frameKst.setContentPane(panelKst);
            frameKst.pack();
            frameKst.setLocationRelativeTo(null); 
            new KSTController(panelKst, mahasiswaLog);
            frameKst.setVisible(true);
        });
        
        // --- LOGIKA TOMBOL TO-DO LIST ---
        view.getButtonTodoList().addActionListener(e -> {
            view.dispose();
            
            // 1. Buat Frame
            javax.swing.JFrame frameTodo = new javax.swing.JFrame("To-Do List Mahasiswa");
            
            // 2. Buat Panel ToDo
            View.ToDo_List panelTodo = new View.ToDo_List();
            
            // 3. Masukkan Panel ke Frame
            frameTodo.setContentPane(panelTodo);
            frameTodo.pack();
            frameTodo.setLocationRelativeTo(null);
            
            // 4. Panggil Controller
            new Controller.TodoListController(panelTodo, mahasiswaLog);
            
            // 5. Tampilkan
            frameTodo.setVisible(true);
        });
        
        // --- LOGIKA TOMBOL NILAI ---
        view.getButtonNilai().addActionListener(e -> {
            JOptionPane.showMessageDialog(view, "Menu Nilai akan segera hadir!");
        });
        
        // --- LOGIKA TOMBOL TAGIHAN ---
        view.getButtonTagihan().addActionListener(e -> {
             JOptionPane.showMessageDialog(view, "Menu Tagihan akan segera hadir!");
        });
    }

    private void isiDataDashboard() {
        if (mahasiswaLog != null) {
            view.setNamaUser(mahasiswaLog.getNama() != null ? mahasiswaLog.getNama() : "-");
            view.setProdiUser(mahasiswaLog.getProdi() != null ? mahasiswaLog.getProdi() : "-");
        } 

        try {
             String semesterAktif = "20231"; 
             List<Nilai> listKst = dao.getKST(mahasiswaLog.getIdMahasiswa(), semesterAktif);
             int totalSks = 0;
             if (listKst != null) {
                 for (Nilai n : listKst) totalSks += n.getSks();
             }
             view.setTotalSks(String.valueOf(totalSks));

             List<Tagihan> listTagihan = dao.getTagihan(mahasiswaLog.getIdMahasiswa());
             double totalHutang = 0;
             if (listTagihan != null) {
                 for (Tagihan t : listTagihan) {
                     if ("belum".equalsIgnoreCase(t.getStatusBayar())) totalHutang += t.getJumlah();
                 }
             }
             view.setTotalTagihan(String.valueOf(totalHutang));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}