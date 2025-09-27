package controlador;

import dao.ClienteDAO;
import dao.PedidosDAO;
import interfaz.MenuGUI;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import modelo.Cliente;
import modelo.Sopa;
import modelo.Detalle_Pedido;
import modelo.Pedido;
import modelo.Login;

public class CtrlNuevoPedido implements ActionListener, KeyListener, DocumentListener {

    private MenuGUI menu;
    private ClienteDAO cliDAO = new ClienteDAO();
    private PedidosDAO pedDAO = new PedidosDAO();
    List<Sopa> listaDeSopas;
    private DefaultComboBoxModel<Sopa> modelComboBox= new DefaultComboBoxModel<>();
    private DefaultTableModel modelBuscador = new DefaultTableModel();
    private DefaultTableModel modelDetalles = new DefaultTableModel();
    private Cliente clienteSeleccionado = new Cliente();
    private Login usuario;

    public CtrlNuevoPedido(MenuGUI menu, Login usuario) {
        this.menu = menu;
        this.usuario = usuario;
        CrearTablaBuscador();
        CrearTablaDetalles();
        this.menu.tblCliente.setVisible(false);
        this.menu.txtCliente.getDocument().addDocumentListener(this);
        this.menu.txtCliente.addKeyListener(this);
        this.menu.txtCliente.addActionListener(this);
        
        
        this.listaDeSopas = pedDAO.CargarSopas();
        for(Sopa s : listaDeSopas)
        {
            modelComboBox.addElement(s);
        }
        this.menu.cmbTamañoSopa.setModel(modelComboBox);
        
        this.menu.cmbTamañoSopa.addActionListener(this);
        this.menu.spnCantidad.addKeyListener(this);
        this.menu.btnAgregarDetalle.addActionListener(this);
        this.menu.btnConfirmarPedido.addActionListener(this);
        
        this.menu.spnCantidad.setValue(1);
        
        this.menu.btnCancelarPedido.addActionListener(this);
    }
    //---------------------------------------------Listeners---------------------------------------------//
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == menu.btnAgregarDetalle)
        {
           AgregarDetalle();
        }
        if(e.getSource() == menu.cmbTamañoSopa){
            //menu.spnCantidad.requestFocus();
        }
        if(e.getSource() == menu.btnConfirmarPedido){
            menu.txtCliente.setEditable(true);
            menu.txtCliente.setText("");
            
        }
        if(e.getSource() == menu.btnCancelarPedido){
            menu.txtCliente.setEditable(true);
            menu.txtCliente.setText("");
            menu.spnCantidad.setValue(1);
            CrearTablaDetalles();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
        if(e.getSource() == menu.txtCliente)
        {
            
            if (e.getKeyCode() == KeyEvent.VK_ENTER)
            {
                if (menu.tblCliente.getRowCount() > 0)
                {
                    clienteSeleccionado.setId((int) menu.tblCliente.getValueAt(0, 0));
                    clienteSeleccionado.setNombre((String) menu.tblCliente.getValueAt(0, 1));
                    clienteSeleccionado.setTelefono((String) menu.tblCliente.getValueAt(0, 2));
                    menu.txtCliente.setText(clienteSeleccionado.getNombre());
                    menu.txtCliente.setEditable(false);
                    menu.tblCliente.setVisible(false);
                    menu.cmbTamañoSopa.requestFocus();
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN && menu.tblCliente.getRowCount() > 0) 
                {
                    menu.tblCliente.requestFocusInWindow();
                    menu.tblCliente.setRowSelectionInterval(0, 0);
                    return;
                }
            
            /*
            Esta es otra forma de hacer, sin usar un cast (int) en la declaracion, sino que transformamos a int el objeto seleccionado
            Object valor = menu.tblCliente.getValueAt(row, 0);
            int id = Integer.parseInt(valor.toString()); // obs: todo objeto tiene toString, por lo que de esta manera transformamos las cosas sin problemas
            */
            /*
            int id = (int) menu.tblCliente.getValueAt(row, 0); // con el parentesis (int) le damos la orden de que trate ese dato como int, si no lo es no funciona, osea asume
            String nombre = (String) menu.tblCliente.getValueAt(row, 1); // lo mismo aca, asumimos que es String con el cast para tratar ese dato como tal
            String telefono = (String) menu.tblCliente.getValueAt(row, 2);

            System.out.println("Cliente seleccionado: " + nombre + ", ID: " + id);
            menu.txtCliente.setText(nombre);
            menu.tblCliente.setVisible(false);
            */
            }
           
        }
        if (e.getSource() == menu.tblCliente)
        {
            int row = menu.tblCliente.getSelectedRow();
            if(row != -1 && e.getKeyCode() == KeyEvent.VK_ENTER)
            {
                clienteSeleccionado.setId((int) menu.tblCliente.getValueAt(0, 0));
                clienteSeleccionado.setNombre((String) menu.tblCliente.getValueAt(0, 1));
                clienteSeleccionado.setTelefono((String) menu.tblCliente.getValueAt(0, 2));
                menu.txtCliente.setText(clienteSeleccionado.getNombre());
                menu.tblCliente.setVisible(false);
                menu.cmbTamañoSopa.requestFocus();
            }
        }
        if (e.getSource() == menu.cmbTamañoSopa)
        {
            if (e.getKeyCode() == KeyEvent.VK_ENTER)
            {
                menu.spnCantidad.requestFocus();
            }
        }
        if (e.getSource() == menu.spnCantidad)
        {
            if (e.getKeyCode() == KeyEvent.VK_ENTER)
            {
                menu.btnAgregarDetalle.doClick();
            }
        }
            
            
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        filtrar();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        filtrar();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        
    }
    //---------------------------------------------Metodos---------------------------------------------//
    private void filtrar() {
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
    }
}
    public void CrearTablaBuscador(){
        modelBuscador = new DefaultTableModel();
        modelBuscador.addColumn("id");
        modelBuscador.addColumn("Cliente");
        modelBuscador.addColumn("Telefono");
        
        menu.tblCliente.setModel(modelBuscador);
        //lo siguiente marca un tamaño minimo y maximo para las columnas, en este caso lo hacemos para que no sean visibles
        menu.tblCliente.getColumnModel().getColumn(0).setMinWidth(0);
        menu.tblCliente.getColumnModel().getColumn(0).setMaxWidth(0);
        menu.tblCliente.getColumnModel().getColumn(2).setMinWidth(0);
        menu.tblCliente.getColumnModel().getColumn(2).setMaxWidth(0);
        /* FORMA CORTA DE OCULTAR CABECERAS
        menu.tblCliente.setTableHeader(null);
    ((javax.swing.JScrollPane) menu.tblCliente.getParent().getParent()).setColumnHeaderView(null);
        */
        
        //FORMA "SEGURA"
        
        //ocultamos el header de la tabla
        if (menu.tblCliente.getTableHeader() != null) {
            menu.tblCliente.setTableHeader(null);
        }
        // Como la tabla está dentro de un JScrollPane, limpiamos su columnHeaderView
        Container parent = menu.tblCliente.getParent();
        if (parent instanceof JViewport) {
            Container grand = parent.getParent();
            if (grand instanceof javax.swing.JScrollPane) {
                ((javax.swing.JScrollPane) grand).setColumnHeaderView(null);
            }
        }
    }
    public void CrearTablaDetalles(){
        modelDetalles = new DefaultTableModel();
        modelDetalles.addColumn("id");
        modelDetalles.addColumn("sopa");
        modelDetalles.addColumn("cantidad");
        modelDetalles.addColumn("precio");
        menu.tblDetalles.setModel(modelDetalles);
        
        menu.tblDetalles.getColumnModel().getColumn(0).setMinWidth(0);
        menu.tblDetalles.getColumnModel().getColumn(0).setMaxWidth(0);
    }
    public void AgregarDetalle(){
        
        Detalle_Pedido detalle = new Detalle_Pedido();
        Sopa sopaSeleccionada = new Sopa();
        sopaSeleccionada = (Sopa) menu.cmbTamañoSopa.getSelectedItem();
        int cantidad = (int) menu.spnCantidad.getValue();
        if(cantidad > 0)
        {
            detalle.setId_sopa(sopaSeleccionada.getId());
            detalle.setCantidad(cantidad);

            Object[] fila = new Object[4];
            fila[0] = detalle;
            fila[1] = sopaSeleccionada.getTamaño();
            fila[2] = detalle.getCantidad();
            BigDecimal subtotal = sopaSeleccionada.getPrecio().multiply(BigDecimal.valueOf(detalle.getCantidad()));

            fila[3] = subtotal;
            modelDetalles.addRow(fila);
            
            //menu.lblTotalAPagar.setText();
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Cantidad no valida");
        }
    }
    public void AgregarPedido(){
        Pedido pedido = new Pedido();
        pedido.setId_cliente(clienteSeleccionado.getId());
        pedido.setId_usuario(usuario.getId_usuario());
        pedido.setEstado("fldsmdfr");
    }
    
    
}