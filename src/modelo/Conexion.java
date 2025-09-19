package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

	String url = "jdbc:postgresql://localhost:5432/sopaparaguaya";
	String usuario = "postgres";
	String contraseña = "98765";
	
	Connection conn = null;
	public Connection conectar()
	{
		try {
	         conn = DriverManager.getConnection(url, usuario, contraseña);
	         return conn;
	     } catch (SQLException e) {
                 System.out.println("Error de conexion a la base de datos");
	         e.printStackTrace();
                 return conn;
	     }
	}
	
}