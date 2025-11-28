package Controller;

import View.Login;               // Import View Login kamu
import Controller.LoginController; // Import Controller Login
import javax.swing.SwingUtilities;

public class Main{
    
    public static void main(String[] args) {
        // Menjalankan aplikasi di dalam Event Dispatch Thread (Standard Swing)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // 1. Instansiasi View (Tampilan Login)
                    Login loginView = new Login();
                    
                    // 2. Instansiasi Controller
                    // Controller ini otomatis akan memasang Action Listener ke tombol di View
                    new LoginController(loginView);
                    
                    // 3. Tampilkan Aplikasi
                    loginView.setVisible(true);
                    loginView.setLocationRelativeTo(null); // Agar muncul di tengah layar laptop
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Terjadi kesalahan saat menjalankan aplikasi: " + e.getMessage());
                }
            }
        });
    }
}