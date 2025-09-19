package controlador;

import dao.ClienteDAO;
import interfaz.MenuGUI;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import modelo.Cliente;

public class CtrlNuevoPedido {

    private MenuGUI menu;
    private DefaultComboBoxModel<String> model;
    private ClienteDAO cliDAO = new ClienteDAO();

    public CtrlNuevoPedido(MenuGUI menu) {
        this.menu = menu;
        this.model = new DefaultComboBoxModel<>();
        
        menu.cmbCliente.setModel(model);
        
    }
    
    public void LlenarComboBox(){
        // Limpia el modelo antes de cargar nuevos datos
        model.removeAllElements();
        
        // Carga los clientes desde la base de datos
        List<Cliente> clientes = cliDAO.CargarClientes();
        for (Cliente c : clientes) {
            model.addElement(c.getNombre());
        }
        
    
        
    
    }
}