package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KoneksiDB {
    
    // Konfigurasi Database
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DB_NAME = "siasat_db";
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;
    
    // Username & Password default XAMPP biasanya 'root' dan kosong
    private static final String USER = "root";
    private static final String PASSWORD = ""; 

    private static Connection connection;

    public static Connection getConnection() {
        // Cek apakah koneksi belum ada atau sudah tertutup
        try {
            if (connection == null || connection.isClosed()) {
                // Memuat driver MySQL (Opsional untuk JDBC versi baru, tapi aman disertakan)
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    System.err.println("Driver MySQL tidak ditemukan!");
                    e.printStackTrace();
                    return null;
                }

                // Membuka koneksi
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Sukses terhubung ke database: " + DB_NAME);
            }
        } catch (SQLException e) {
            System.err.println("Gagal terhubung ke database!");
            System.err.println("Pesan Error: " + e.getMessage());
        }
        return connection;
    }
}