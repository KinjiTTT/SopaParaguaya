package controlador;

import dao.PedidosDAO;
import interfaz.MenuGUI;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.swing.JOptionPane;
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
    int caso = -1;
    //private JSplitPane panelDividido = new JSplitPane();
    
    public CtrlPedidos(MenuGUI menu, Login usuario){
        this.menu = menu;
        this.usuario = usuario;
        this.cardLayout = (CardLayout) menu.getPanelImagenes().getLayout();
        LlenarTablaPedidos(-1);
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
        menu.tblPedidos.addKeyListener(this);
        menu.TextAreaInfoPedido.addKeyListener(this);
        menu.btnModificarPedido.addActionListener(this);
        menu.btnEntregado.addActionListener(this);
        menu.btnCancelado.addActionListener(this);
        menu.btnVerTodos.addActionListener(this);
        menu.btnVerCancelados.addActionListener(this);
        menu.btnVerEntregados.addActionListener(this);
        menu.btnVerPendientes.addActionListener(this);
    }
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            menu.ListaDeOpciones.requestFocusInWindow();
            cardLayout.show(menu.getPanelImagenes(), "Pedidos");
            System.err.println("Se presiono Escape desde Pedidos");
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == menu.btnCancelado)
        {
            int filaSeleccionada = menu.tblPedidos.getSelectedRow();
            String estado = (String) menu.tblPedidos.getValueAt(filaSeleccionada, 4);

            if ("pendiente".equalsIgnoreCase(estado)) {
                int estadoPedido = 0;
                pedDAO.MarcarComoEntregado((int) menu.tblPedidos.getValueAt(filaSeleccionada, 0), estadoPedido);
                LlenarTablaPedidos(caso);
            } else {
                JOptionPane.showMessageDialog(null, "Solo se pueden marcar pedidos 'pendientes'");
            }
            
        }
        if(e.getSource() == menu.btnEntregado)
        {
            int filaSeleccionada = menu.tblPedidos.getSelectedRow();
            String estado = (String) menu.tblPedidos.getValueAt(filaSeleccionada, 4);

            if ("pendiente".equalsIgnoreCase(estado)) {
                int estadoPedido = 1;
                pedDAO.MarcarComoEntregado((int) menu.tblPedidos.getValueAt(filaSeleccionada, 0), estadoPedido);
                LlenarTablaPedidos(caso);
            } else {
                JOptionPane.showMessageDialog(null, "Solo se pueden marcar pedidos 'pendientes'");
            }
            
        }
        if(e.getSource() == menu.btnModificarPedido)
        {
            
        }
        if(e.getSource() == menu.btnVerTodos){
            caso = -1;
            LlenarTablaPedidos(caso);
        }
        if(e.getSource() == menu.btnVerCancelados){
            caso = 0;
            LlenarTablaPedidos(caso);
        }
        if(e.getSource() == menu.btnVerEntregados){
            caso = 1;
            LlenarTablaPedidos(caso);
        }
        if(e.getSource() == menu.btnVerPendientes){
            caso = 2;
            LlenarTablaPedidos(caso);
        }
        
        
        
    }
    //---------------------------------------------Metodos---------------------------------------------//
    /*
    public void filtrar() {
        DefaultTableModel modelo = (DefaultTableModel) menu.tblCliente.getModel();
        modelo.setRowCount(0); // limpiar antes de volver a cargar
        String texto = menu.txtCliente.getText().trim();
        List<Cliente> resultados = cliDAO.buscarClientes(texto);
        
        if (texto.isEmpty()) {
            menu.tblCliente.setVisible(false);
        } else {
            for (Cliente c : resultados) {
               
                Object[] fila = new Object[3];
                fila[0] = c.getId();
                fila[1] = c.getNombre();
                fila[2] = c.getTelefono();
                modelBuscador.addRow(fila);
                //modelo.addRow(new Object[]{c.getId(), c.getNombre(), c.getTelefono()});
            }
        menu.tblCliente.setVisible(modelo.getRowCount() > 0);
    }*/
    
    
    public void CrearTablaPedidos(){
        modelPedidos = new DefaultTableModel();
        modelPedidos.addColumn("Numero de Pedido");
        modelPedidos.addColumn("Cliente");
        modelPedidos.addColumn("Vendedor");
        modelPedidos.addColumn("Solicitud");
        modelPedidos.addColumn("estado");
        modelPedidos.addColumn("Entrega");
        menu.tblPedidos.setModel(modelPedidos);


        /*
        menu.tblDetalles.getColumnModel().getColumn(0).setMinWidth(0);
        menu.tblDetalles.getColumnModel().getColumn(0).setMaxWidth(0);
        */
    }
    
    public void LlenarTablaPedidos(int caso){
        ArrayList<Object[]> lista = pedDAO.CargarPedidos(caso);
        CrearTablaPedidos();
        
        for(Object[] p: lista)
        {
            Object[] fila = new Object[6];
            fila[0] = p[0];
            fila[1] = p[1];
            fila[2] = p[2];
            fila[3] = p[3];
            fila[4] = p[4];
            fila[5] = p[5];
            modelPedidos.addRow(fila);
        }
        System.out.println(menu.tblPedidos.getRowCount());
        
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