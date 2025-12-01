package Controller;

import Model.Mahasiswa;
import Model.Nilai;
import Model.PendidikanDAO;
import Model.Tagihan;
import View.Dashboard_Mahasiswa;
import View.Login;
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

       
        isiDataDashboard();
        
        
        initController();
    }

    private void initController() {
        
        // LOGIKA TOMBOL PROFILE 
        view.getButtonProfile().addActionListener(e -> {
            view.dispose(); 
            View.Profile_Mahasiswa profileView = new View.Profile_Mahasiswa();
            new Controller.ProfileController(profileView, mahasiswaLog);
            profileView.pack(); 
            profileView.setLocationRelativeTo(null);
            profileView.setResizable(false); 
            profileView.setVisible(true);
        });
        
        // LOGIKA TOMBOL REGISTRASI MATAKULIAH 
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
        
        //  LOGIKA TOMBOL TAGIHAN 
        view.getButtonTagihan().addActionListener(e -> {
             view.dispose(); 
             
          
             View.Tagihan_Mahasiswa tagihanView = new View.Tagihan_Mahasiswa();
             tagihanView.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
             tagihanView.pack(); 
             tagihanView.setLocationRelativeTo(null); 
             new Controller.TagihanController(tagihanView, mahasiswaLog);
             tagihanView.setVisible(true);
        });
        
        // LOGIKA TOMBOL LOGOUt
        view.getButtonLogout().addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(view, "Yakin ingin keluar?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                view.dispose();
                Login loginView = new Login();
                new LoginController(loginView);
                loginView.setVisible(true);
            }
        });

        // LOGIKA TOMBOL JADWAL
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

        // LOGIKA TOMBOL KST 
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
        
        // LOGIKA TOMBOL TODO LIST 
        view.getButtonTodoList().addActionListener(e -> {
            view.dispose();  
            javax.swing.JFrame frameTodo = new javax.swing.JFrame("To-Do List Mahasiswa");
            View.ToDo_List panelTodo = new View.ToDo_List();
            frameTodo.setContentPane(panelTodo);
            frameTodo.pack();
            frameTodo.setLocationRelativeTo(null);
            new Controller.TodoListController(panelTodo, mahasiswaLog);
          
            frameTodo.setVisible(true);
        });
        
        // LOGIKA TOMBOL NILAI 
        view.getButtonNilai().addActionListener(e -> {
             View.Transkrip_Nilai viewTranskrip = new View.Transkrip_Nilai();         
             viewTranskrip.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
             viewTranskrip.setLocationRelativeTo(null);     
             new Controller.TranskripController(viewTranskrip, mahasiswaLog);       
             viewTranskrip.setVisible(true);
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