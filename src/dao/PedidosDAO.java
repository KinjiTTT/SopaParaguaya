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
import modelo.Conexion;
import modelo.Sopa;

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
                sopa.setPrecio(resultSet.getString("precio"));
                listasopas.add(sopa);
            }
        } catch (SQLException ex) {
            System.getLogger(ClienteDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return listasopas;
    }
    
    
    
}
