package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelo.Conexion;
import modelo.Login;

public class LoginDAO {
        Connection connection = null;
	Conexion conexion = null;
	PreparedStatement preStatement = null;
	ResultSet resultSet = null;
        
         // 👇 Este es el constructor
        public LoginDAO() {
            conexion = new Conexion(); // cada vez que crees un LoginDAO, también se crea su Conexion
        }
        
        public Boolean iniciarSesion(Login login)
        {
            connection = conexion.conectar();
            if(connection == null)
            {
                System.out.println("No se pudo conectar a la base de datos");
            }
            
            String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contraseña = ?";
            
            try {
                preStatement = connection.prepareStatement(sql);
                preStatement.setString(1,login.getUsuario());
                preStatement.setString(2, login.getPassword());
                
                resultSet = preStatement.executeQuery();
                
                if(resultSet.next())
                {
                    return true;
                }
                
            } catch (SQLException ex) {
                System.getLogger(LoginDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
           
            return false;
        }
        
}
