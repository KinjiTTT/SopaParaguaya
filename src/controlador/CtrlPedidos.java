package controlador;

import dao.PedidosDAO;
import interfaz.MenuGUI;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.swing.JSplitPane;
import javax.swing.table.DefaultTableModel;
import modelo.Detalle_Pedido;
import modelo.Login;
import modelo.Pedido;

public class CtrlPedidos extends KeyAdapter implements ActionListener {
    private MenuGUI menu;
    private PedidosDAO pedDAO = new PedidosDAO();
    private Login usuario;
    private CardLayout cardLayout;
    private DefaultTableModel modelPedidos = new DefaultTableModel();
    private DefaultTableModel modelDetallePedido = new DefaultTableModel();
    //private JSplitPane panelDividido = new JSplitPane();
    
    public CtrlPedidos(MenuGUI menu, Login usuario){
        this.menu = menu;
        this.usuario = usuario;
        this.cardLayout = (CardLayout) menu.getPanelImagenes().getLayout();
        LlenarTablaPedidos();
        CrearTablaDetallesPedido();
        //LlenarTablaDetalles();
        menu.tblPedidos.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
        int filaSeleccionada = menu.tblPedidos.getSelectedRow();
            if (filaSeleccionada != -1) {
                // Obtener el ID del pedido seleccionado
                int id_pedido = (int) menu.tblPedidos.getValueAt(filaSeleccionada, 0);
                // Cargar los detalles del pedido en la otra tabla
                LlenarTablaDetalles(id_pedido);
                InformacionDePedido(id_pedido);
            }
        }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
    public void CrearTablaPedidos(){
        modelPedidos = new DefaultTableModel();
        modelPedidos.addColumn("Numero de Pedido");
        modelPedidos.addColumn("Cliente");
        modelPedidos.addColumn("Vendedor");
        modelPedidos.addColumn("Solicitud");
        modelPedidos.addColumn("estado");
        modelPedidos.addColumn("Entrega");
        menu.tblPedidos.setModel(modelPedidos);
        System.out.println(menu.tblPedidos.getRowCount());


        /*
        menu.tblDetalles.getColumnModel().getColumn(0).setMinWidth(0);
        menu.tblDetalles.getColumnModel().getColumn(0).setMaxWidth(0);
        */
    }
    public void CrearTablaDetallesPedido(){
        modelDetallePedido = new DefaultTableModel();
        modelDetallePedido.addColumn("id_detalle");
        modelDetallePedido.addColumn("id_pedido");
        modelDetallePedido.addColumn("id_sopa");
        modelDetallePedido.addColumn("cantidad");
        menu.tblDetallesPedido.setModel(modelDetallePedido);
        System.out.println(menu.tblDetallesPedido.getRowCount());
        /*
        menu.tblDetalles.getColumnModel().getColumn(0).setMinWidth(0);
        menu.tblDetalles.getColumnModel().getColumn(0).setMaxWidth(0);
        */
    }
    
    public void LlenarTablaPedidos(){
        ArrayList<Pedido> lista = pedDAO.CargarPedidos();
        CrearTablaPedidos();
        
        for(Pedido p: lista)
        {
            Object[] fila = new Object[6];
            fila[0] = p.getId_pedido();
            fila[1] = p.getId_cliente();
            fila[2] = p.getId_usuario();
            fila[3] = p.getFecha_pedido_formateada();
            fila[4] = p.getEstado();
            fila[5] = p.getFecha_entrega_formateada();
            modelPedidos.addRow(fila);
        }
        
        
    }
    public void LlenarTablaDetalles(int id_pedido){
        ArrayList<Detalle_Pedido> detalles = pedDAO.CargarDetallesPedidoTabla(id_pedido);
        modelDetallePedido.setRowCount(0); // Limpia las filas
        for(Detalle_Pedido d: detalles)
        {
            Object[] fila = new Object[4];
            fila[0] = d.getId_detalle();
            fila[1] = d.getId_pedido();
            fila[2] = d.getId_sopa();
            fila[3] = d.getCantidad();
            modelDetallePedido.addRow(fila);
        }
        
    }
    public void InformacionDePedido(int id_pedido){
        ArrayList<Object[]> detalles = pedDAO.CargarDetallesPedidoTexto(id_pedido);
        menu.TextAreaInfoPedido.setText(""); // Limpia el Texto
        if (detalles.isEmpty()) return;

            StringBuilder texto = new StringBuilder();

            // El nombre del cliente está en la primera fila
            String cliente = (String) detalles.get(0)[1];
            texto.append("Pedido N°: ").append(id_pedido)
                 .append("\nCliente: ").append(cliente)
                 .append("\n-----------------------------\n");

            // Recorremos los productos del pedido
            for (Object[] d : detalles) {
                String sopa = (String) d[2];
                int cantidad = (int) d[3];
                BigDecimal subtotal = (BigDecimal) d[4];

                texto.append("Sopa ").append(sopa)
                     .append(" x").append(cantidad)
                     .append(" → ").append(subtotal)
                     .append("\n");
            }

            texto.append("-----------------------------\n")
                 .append("Total a pagar: ")
                 .append(detalles.get(0)[5]); // mismo total en todas las filas

            menu.TextAreaInfoPedido.setText(texto.toString());
    }
}