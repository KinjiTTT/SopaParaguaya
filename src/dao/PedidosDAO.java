/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 *
 * @author Notebook
 */
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
        
    }
    
    public void AgregarPedido(Pedido pedido){
        String sql = "INSERT INTO pedidos(id_cliente, id_usuario, id_detalle, estado) VALUES (?,?,?,?)";
        
        try {
            sentencia = conec.prepareStatement(sql);
            pedido.setId_cliente(resultSet.getInt("id_cliente"));
            pedido.setId_usuario(resultSet.getInt("id_cliente"));
            pedido.setId_detalle(resultSet.getInt("id_detalle"));
            sentencia.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Pedido agregado exitosamente");
            
        } catch (SQLException ex) {
            System.getLogger(ClienteDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
