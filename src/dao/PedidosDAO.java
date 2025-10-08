package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
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
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
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
    
    public ArrayList<Pedido> CargarPedidos() {
        ArrayList<Pedido> listaPedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos ORDER BY fecha_pedido DESC";

        try {
            sentencia = conec.prepareStatement(sql);
            resultSet = sentencia.executeQuery();

            while (resultSet.next()) {
                Pedido pedido = new Pedido();

                pedido.setId_pedido(resultSet.getInt("id_pedido")); // ← corregido, antes usabas id_cliente
                pedido.setId_cliente(resultSet.getInt("id_cliente"));
                pedido.setId_usuario(resultSet.getInt("id_usuario"));

                // 🔹 convertir SQL Timestamp → LocalDateTime
                java.sql.Timestamp tsPedido = resultSet.getTimestamp("fecha_pedido");
                if (tsPedido != null) {
                    pedido.setFecha_pedido(tsPedido.toLocalDateTime());
                    pedido.setFecha_pedido_formateada(pedido.getFecha_pedido().format(formato));
                }

                java.sql.Timestamp tsEntrega = resultSet.getTimestamp("fecha_entrega");
                if (tsEntrega != null) {
                    pedido.setFecha_entrega(tsEntrega.toLocalDateTime());
                    pedido.setFecha_entrega_formateada(pedido.getFecha_entrega().format(formato));
                }

                pedido.setEstado(resultSet.getString("estado"));

                listaPedidos.add(pedido);
            }
        } catch (SQLException ex) {
            System.getLogger(ClienteDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        return listaPedidos;
    }
    
    public ArrayList<Detalle_Pedido> CargarDetallesPedido(int id_pedido){
        ArrayList<Detalle_Pedido> listaDetalles = new ArrayList<>();
        String sql = "SELECT * FROM detalles_pedido WHERE id_pedido = ?";

        try {
            sentencia = conec.prepareStatement(sql);
            sentencia.setInt(1, id_pedido);
            resultSet = sentencia.executeQuery();

            while (resultSet.next()) {
                Detalle_Pedido detalle = new Detalle_Pedido();

                detalle.setId_detalle(resultSet.getInt("id_detalle"));
                detalle.setId_pedido(resultSet.getInt("id_pedido"));
                detalle.setId_sopa(resultSet.getInt("id_sopa"));
                detalle.setCantidad(resultSet.getInt("cantidad"));

                listaDetalles.add(detalle);
            }
        } catch (SQLException ex) {
            System.getLogger(ClienteDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return listaDetalles;
    }

}
