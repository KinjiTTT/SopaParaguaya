package controlador;

import dao.ClienteDAO;
import interfaz.MenuGUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import modelo.Cliente;

public class CtrlNuevoPedido implements ActionListener, KeyListener, DocumentListener {

    private MenuGUI menu;
    private ClienteDAO cliDAO = new ClienteDAO();
    private JTextField editor;
    List<Cliente> listaDeClientes;
    private DefaultTableModel model = new DefaultTableModel();

    public CtrlNuevoPedido(MenuGUI menu) {
        this.menu = menu;
        this.listaDeClientes = cliDAO.CargarClientes();
        //model = new DefaultTableModel(new Object[]{"ID", "Cliente", "Teléfono"}, 0);
        //this.menu.tblCliente.setModel(model);
        CrearTabla();
        this.menu.tblCliente.setVisible(false);
        this.menu.txtCliente.getDocument().addDocumentListener(this);
        this.menu.txtCliente.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int row = menu.tblCliente.getSelectedRow();
        if (row != -1) {
            
            if (e.getKeyCode() == KeyEvent.VK_ENTER)
            {
            /*
            Esta es otra forma de hacer, sin usar un cast (int) en la declaracion, sino que transformamos a int el objeto seleccionado
            Object valor = menu.tblCliente.getValueAt(row, 0);
            int id = Integer.parseInt(valor.toString()); // obs: todo objeto tiene toString, por lo que de esta manera transformamos las cosas sin problemas
            */
            
            int id = (int) menu.tblCliente.getValueAt(row, 0); // con el parentesis (int) le damos la orden de que trate ese dato como int, si no lo es no funciona, osea asume
            String nombre = (String) menu.tblCliente.getValueAt(row, 1); // lo mismo aca, asumimos que es String con el cast para tratar ese dato como tal
            String telefono = (String) menu.tblCliente.getValueAt(row, 2);

            System.out.println("Cliente seleccionado: " + nombre + ", ID: " + id);
            menu.txtCliente.setText(nombre);
            menu.tblCliente.setVisible(false);
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
        menu.tblCliente.setTableHeader(null);
    }
    
}