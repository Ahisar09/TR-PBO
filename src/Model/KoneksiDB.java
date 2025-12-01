package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KoneksiDB {
    
    // Konfig Database
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DB_NAME = "siasat_db";
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;
    
    private static final String USER = "root";
    private static final String PASSWORD = ""; 

    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    System.err.println("Driver MySQL tidak ditemukan!");
                    e.printStackTrace();
                    return null;
                }

               
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