/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import modelo.Cliente;
import modelo.Conexion;

/**
 *
 * @author Notebook
 */
public class ClienteDAO {
    Connection conec;
    PreparedStatement sentencia;
    ResultSet resultSet;
    public ClienteDAO(){
        Conexion conexionBD = new Conexion();
        try {
            conec = conexionBD.conectar();
        } catch (Exception e) {
            System.out.println("error");
        }
    }
    public void AgregarCliente(Cliente cliente)
    {
        String sql = "INSERT INTO clientes (nombre,telefono) VALUES (?,?)";
        
        try {
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1,cliente.getNombre());
            sentencia.setString(2,cliente.getTelefono());
            sentencia.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Cliente Agregado exitosamente");
            
        } catch (SQLException ex) {
            System.getLogger(ClienteDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        
    }
    
    public ArrayList<Cliente> CargarClientes()
    {
        ArrayList<Cliente> listaClientes = new ArrayList<>();
        String sql = "SELECT id_cliente,nombre,telefono FROM clientes ORDER BY nombre";
        
        try {
            sentencia = conec.prepareStatement(sql);
            resultSet = sentencia.executeQuery();
            while(resultSet.next())
            {
                Cliente cliente = new Cliente();
                cliente.setId(resultSet.getInt("id_cliente"));
                cliente.setNombre(resultSet.getString("nombre"));
                cliente.setTelefono(resultSet.getString("telefono"));
                listaClientes.add(cliente);
            }
        } catch (SQLException ex) {
            System.getLogger(ClienteDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return listaClientes;
        
    }
    public void ModificarCliente(Cliente cliente)
    {
        String sql = "UPDATE clientes SET nombre = ?, telefono = ? WHERE id_cliente = ?";
        try {
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, cliente.getNombre());
            sentencia.setString(2, cliente.getTelefono());
            sentencia.setInt(3, cliente.getId());
            sentencia.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cliente modificado correctamente");
        } catch (SQLException ex) {
            System.getLogger(ClienteDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    public void EliminarCliente(int id)
    {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";
        try {
            sentencia = conec.prepareStatement(sql);
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cliente eliminado correctamente");
        } catch (SQLException ex) {
            System.getLogger(ClienteDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    
    
}
