package Controller;

import View.Login;               
import Controller.LoginController; 
import javax.swing.SwingUtilities;

public class Main{
    
    public static void main(String[] args) {
     
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // View (Tampilan Login)
                    Login loginView = new Login();
                    //   Controller
                    
                    new LoginController(loginView);
                    //  Tampilkan Aplikasi
                    loginView.setVisible(true);
                    loginView.setLocationRelativeTo(null); 
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Terjadi kesalahan saat menjalankan aplikasi: " + e.getMessage());
                }
            }
        });
    }
}