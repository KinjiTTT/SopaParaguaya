package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import modelo.Conexion;
import modelo.Detalle_Pedido;
import modelo.Sopa;
import modelo.Pedido;

public class PedidosDAO {
    Connection conec;
    PreparedStatement sentencia;
    ResultSet resultSet;
    public PedidosDAO(){
        Conexion conexionBD = new Conexion();
        try {
            conec = conexionBD.conectar();
        } catch (Exception e) {
            System.out.println("error");
        }
    }
    public ArrayList<Sopa> CargarSopas(){
        ArrayList<Sopa> listasopas = new ArrayList<>();
        String sql = "SELECT id_sopa,tamaño,precio FROM sopas ORDER BY precio";
        
        try {
            sentencia = conec.prepareStatement(sql);
            resultSet = sentencia.executeQuery();
            while(resultSet.next())
            {
                Sopa sopa = new Sopa();
                sopa.setId(resultSet.getInt("id_sopa"));
                sopa.setTamaño(resultSet.getString("tamaño"));
                sopa.setPrecio(resultSet.getBigDecimal("precio"));
                listasopas.add(sopa);
            }
        } catch (SQLException ex) {
            System.getLogger(ClienteDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return listasopas;
    }
    
    public void AgregarDetalle(Detalle_Pedido detalle){
        String sql = "INSERT INTO detalles_pedido(id_pedido, id_sopa, cantidad) VALUES (?,?,?)";
        
        try {
            sentencia = conec.prepareStatement(sql);
            sentencia.setInt(1, detalle.getId_pedido());
            sentencia.setInt(2, detalle.getId_sopa());
            sentencia.setInt(3, detalle.getCantidad());
            sentencia.executeUpdate();
            
        } catch (SQLException ex) {
            System.getLogger(ClienteDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    public int AgregarPedido(Pedido pedido){
        int id = -1;
        String sql = "INSERT INTO pedidos(id_cliente, id_usuario) VALUES (?,?)";
        
        try {
            sentencia = conec.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            sentencia.setInt(1, pedido.getId_cliente());
            sentencia.setInt(2, pedido.getId_usuario());
            sentencia.executeUpdate();
            resultSet = sentencia.getGeneratedKeys();
            if (resultSet.next()) {
            id = resultSet.getInt(1);
        }
            
        } catch (SQLException ex) {
            System.getLogger(ClienteDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return id;
    }
}
