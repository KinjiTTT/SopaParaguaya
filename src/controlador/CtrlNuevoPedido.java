package controlador;

import dao.ClienteDAO;
import dao.PedidosDAO;
import interfaz.MenuGUI;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import modelo.Cliente;
import modelo.Sopa;

public class CtrlNuevoPedido implements ActionListener, KeyListener, DocumentListener {

    private MenuGUI menu;
    private ClienteDAO cliDAO = new ClienteDAO();
    private PedidosDAO pedDAO = new PedidosDAO();
    private JTextField editor;
    List<Cliente> listaDeClientes;
    List<Sopa> listaDeSopas;
    private DefaultComboBoxModel<Sopa> modelComboBox= new DefaultComboBoxModel<>();
    private DefaultTableModel model = new DefaultTableModel();
    private Cliente clienteSeleccionado = new Cliente();
    private Sopa tamañoSeleccionado = new Sopa();

    public CtrlNuevoPedido(MenuGUI menu) {
        this.menu = menu;
        this.listaDeClientes = cliDAO.CargarClientes();
        //model = new DefaultTableModel(new Object[]{"ID", "Cliente", "Teléfono"}, 0);
        //this.menu.tblCliente.setModel(model);
        CrearTabla();
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == menu.txtCliente)
        {
           // menu.cmbTamañoSopa.requestFocus();
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
                model.addRow(fila);
                //modelo.addRow(new Object[]{c.getId(), c.getNombre(), c.getTelefono()});
            }
        menu.tblCliente.setVisible(modelo.getRowCount() > 0);
    }
}
    public void CrearTabla(){
        model = new DefaultTableModel();
        model.addColumn("id");
        model.addColumn("Cliente");
        model.addColumn("Telefono");
        
        menu.tblCliente.setModel(model);
        //lo siguiente marca un tamaño minimo y maximo para la primera columna (la columna 0), al ponerle valor 0 a ambas permanece oculta
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
}