package controlador;

import dao.ClienteDAO;
import dao.PedidosDAO;
import interfaz.MenuGUI;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

public class CtrlNuevoPedido implements ActionListener, KeyListener, DocumentListener, MouseListener {

    private MenuGUI menu;
    private ClienteDAO cliDAO = new ClienteDAO();
    private PedidosDAO pedDAO = new PedidosDAO();
    private List<Sopa> listaDeSopas;
    private DefaultComboBoxModel<Sopa> modelComboBox= new DefaultComboBoxModel<>();
    private DefaultTableModel modelBuscador = new DefaultTableModel();
    private DefaultTableModel modelDetalles = new DefaultTableModel();
    private Cliente clienteSeleccionado = new Cliente();
    private Login usuario;
    private CardLayout cardLayout;

    public CtrlNuevoPedido(MenuGUI menu, Login usuario) {
        this.menu = menu;
        this.usuario = usuario;
        this.cardLayout = (CardLayout) menu.getPanelImagenes().getLayout();
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
        this.menu.cmbTamañoSopa.addKeyListener(this);
        this.menu.spnCantidad.addKeyListener(this);
        this.menu.btnAgregarDetalle.addActionListener(this);
        this.menu.btnConfirmarPedido.addActionListener(this);
        
        this.menu.spnCantidad.setValue(1);
        
        this.menu.btnCancelarPedido.addActionListener(this);
        this.menu.tblCliente.addKeyListener(this);
    }
    //---------------------------------------------Listeners---------------------------------------------//
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == menu.btnAgregarDetalle)
        {
           AgregarDetalle();
        }
        if(e.getSource() == menu.btnConfirmarPedido){
            AgregarPedido();
        }
        if(e.getSource() == menu.btnCancelarPedido){
            menu.txtCliente.setEditable(true); //Hacemos editable el textfield del cliente
            menu.txtCliente.setText(""); //Vaciamos el campo
            menu.spnCantidad.setValue(1);//cantidad predeterminada 1
            menu.cmbTamañoSopa.setSelectedIndex(0); //seleccion predeterminada (la primera)
            CrearTablaDetalles(); //limpiamos la tabla (añadiendo una nuevo)
            clienteSeleccionado = new Cliente(); //ya no hay cliente seleccionado
            menu.lblTotalAPagar.setText("0 GS");
            menu.txtCliente.requestFocusInWindow();
            
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            menu.ListaDeOpciones.requestFocusInWindow();
            cardLayout.show(menu.getPanelImagenes(), "Nuevo Pedido");
        }
        
        if(e.getSource() == menu.txtCliente)
        {
            int filaSeleccionada = 0;
            if (e.getKeyCode() == KeyEvent.VK_ENTER && menu.tblCliente.getRowCount() > 0)
            {
                SeleccionDeCliente(filaSeleccionada);
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
            if (e.getKeyCode() == KeyEvent.VK_DOWN && menu.tblCliente.getRowCount() > 0) 
                {
                    menu.tblCliente.requestFocusInWindow();
                    menu.tblCliente.setRowSelectionInterval(0, 0);
                    return;
                }
           
        }
        if (e.getSource() == menu.tblCliente)
        {
            int filaSeleccionada = menu.tblCliente.getSelectedRow();
            if(filaSeleccionada != -1 && e.getKeyCode() == KeyEvent.VK_ENTER)
            {
                SeleccionDeCliente(filaSeleccionada);
                
            }
            if(filaSeleccionada == 0 && e.getKeyCode() == KeyEvent.VK_UP){
                //e.consume(); // evita que JTable procese la tecla
                menu.txtCliente.requestFocusInWindow();
                menu.tblCliente.clearSelection();
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
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void insertUpdate(DocumentEvent e) {
        filtrar();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        filtrar();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {}
    
    @Override
    public void mouseClicked(MouseEvent e) {
        int filaSeleccionada = menu.tblCliente.getSelectedRow();
        if(e.getClickCount() == 2 && e.getSource() == menu.tblCliente)
        {
            SeleccionDeCliente(filaSeleccionada); //no funciona
        }
        
  
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
    
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
        modelDetalles.addColumn("sopa"); //Guardara todo el objeto Sopa al igual que el comboBox del tamaño
        modelDetalles.addColumn("cantidad");
        modelDetalles.addColumn("precio");
        menu.tblDetalles.setModel(modelDetalles);
        /*
        menu.tblDetalles.getColumnModel().getColumn(0).setMinWidth(0);
        menu.tblDetalles.getColumnModel().getColumn(0).setMaxWidth(0);
        */
}
    public void AgregarDetalle(){
        
        Detalle_Pedido detalle = new Detalle_Pedido();
        Sopa sopaSeleccionada = new Sopa();
        sopaSeleccionada = (Sopa) menu.cmbTamañoSopa.getSelectedItem();
        System.out.println(sopaSeleccionada.getPrecio());
        int cantidad = (int) menu.spnCantidad.getValue();
        if(cantidad > 0 || menu.txtCliente.getText().isEmpty())
        {
            detalle.setId_sopa(sopaSeleccionada.getId());
            detalle.setCantidad(cantidad);

            Object[] fila = new Object[3];
            fila[0] = sopaSeleccionada;
            fila[1] = detalle.getCantidad();
            BigDecimal subtotal = sopaSeleccionada.getPrecio().multiply(BigDecimal.valueOf(detalle.getCantidad()));

            fila[2] = subtotal;
            modelDetalles.addRow(fila);
            
            BigDecimal total = BigDecimal.ZERO;
            for(int i = 0; i < menu.tblDetalles.getRowCount(); i++){
                BigDecimal sub = (BigDecimal) menu.tblDetalles.getValueAt(i, 2); // columna del subtotal
                total = total.add(sub);
            }
            menu.lblTotalAPagar.setText(total.toString() + " GS");
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Cantidad no valida");
        }
    }
    public void AgregarPedido(){
        Pedido pedido = new Pedido();
        pedido.setId_cliente(clienteSeleccionado.getId()); //Guardamos el id del cliente seleccionado en el objeto que ira al DAO
        pedido.setId_usuario(usuario.getId_usuario()); //Lo mismo con el usuario logueado
        int filasDetalles = menu.tblDetalles.getRowCount();
        if(menu.tblDetalles.getRowCount() > 0){
            int id_pedido = pedDAO.AgregarPedido(pedido); // Se guarda el Pedido y el metodo retorna el id generado en el pedido agregado
            System.out.println("ID generado: " + id_pedido);
            if (id_pedido > 0){ //si no funciona retornara -1, para ello esta la condicion
                
                int cantidad;
                for(int i = 0; i < filasDetalles; i++){
                    Sopa sopaDetalle = new Sopa();
                    Detalle_Pedido detalle = new Detalle_Pedido();
                    sopaDetalle = (Sopa) menu.tblDetalles.getValueAt(i, 0);
                    cantidad = (int) menu.tblDetalles.getValueAt(i,1);
                    detalle.setId_pedido(id_pedido);
                    detalle.setId_sopa(sopaDetalle.getId());
                    detalle.setCantidad(cantidad);

                    pedDAO.AgregarDetalle(detalle);
            }
            JOptionPane.showMessageDialog(null, "Pedido y detalles guardados exitosamente");
            
            menu.txtCliente.setEditable(true); //Hacemos editable el textfield del Cliente
            menu.txtCliente.setText(""); //Vaciamos el campo
            menu.spnCantidad.setValue(1);//cantidad predeterminada 1
            menu.cmbTamañoSopa.setSelectedIndex(0); //seleccion predeterminada (la primera)
            menu.lblTotalAPagar.setText("0 GS");
            CrearTablaDetalles(); //limpiamos la tabla detalles
            clienteSeleccionado = new Cliente(); //ya no hay cliente seleccionado
            menu.txtCliente.requestFocusInWindow();
            
            
            
            }
            else{
                JOptionPane.showMessageDialog(null, "Error al guardar el pedido\nPosible razon: Error en el campo del cliente\n(Vacio o mal seleccionado)");
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "No se pudo guardar el pedido\nRazon: No hay detalles");
        }
        
        
        
        
    }
    public void SeleccionDeCliente(int filaSeleccionada){
        clienteSeleccionado.setId((int) menu.tblCliente.getValueAt(filaSeleccionada, 0));
        clienteSeleccionado.setNombre((String) menu.tblCliente.getValueAt(filaSeleccionada, 1));
        clienteSeleccionado.setTelefono((String) menu.tblCliente.getValueAt(filaSeleccionada, 2));
        menu.txtCliente.setText(clienteSeleccionado.getNombre());
        menu.txtCliente.setEditable(false);
        menu.tblCliente.setVisible(false);
        menu.cmbTamañoSopa.requestFocus();
    }
}