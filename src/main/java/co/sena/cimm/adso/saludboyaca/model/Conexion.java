package co.sena.cimm.adso.saludboyaca.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexion {
    private static final String URL = "jdbc:mysql://tramway.proxy.rlwy.net:47360/railway";
    private static final String USER = "root";
    private static final String PASS = "QXraEbjQHkLGeQJxnuzhkuTIuvtedjhN";
    
   
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Error al cargar el driver de MySQL", ex);
        }
    }
    
   
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error al cerrar la conexión: " + ex.getMessage());
        }
    }
}
