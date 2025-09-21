package controlador;

import dao.ClienteDAO;
import interfaz.MenuGUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import modelo.Cliente;

public class CtrlNuevoPedido implements ActionListener, KeyListener, DocumentListener {

    private MenuGUI menu;
    //private DefaultComboBoxModel<Cliente> model; //como se asigna dinamicamente, no tiene sentido declararla aqui
    private ClienteDAO cliDAO = new ClienteDAO();
    private JTextField editor;
    List<Cliente> listaDeClientes;

    public CtrlNuevoPedido(MenuGUI menu) {
        this.menu = menu;
        this.listaDeClientes = cliDAO.CargarClientes();
        DefaultComboBoxModel<Cliente> modelo = new DefaultComboBoxModel<>();
        for (Cliente c : listaDeClientes) {
            modelo.addElement(c);
        }
        menu.cmbCliente.setModel(modelo);
        editor = (JTextField) menu.cmbCliente.getEditor().getEditorComponent();
        
        editor.getDocument().addDocumentListener(this);
        editor.addKeyListener(this);
        menu.cmbCliente.addActionListener(this);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            // Seleccionar el primer elemento y ocultar el popup
            if (menu.cmbCliente.getItemCount() > 0) {
                menu.cmbCliente.setSelectedItem(menu.cmbCliente.getItemAt(0));
                menu.cmbCliente.setPopupVisible(false);
                // Puedes agregar aquí la lógica para usar el cliente seleccionado
                Cliente clienteSeleccionado = (Cliente) menu.cmbCliente.getSelectedItem();
                System.out.println("Cliente seleccionado: " + clienteSeleccionado.getNombre() + ", ID: " + clienteSeleccionado.getId());
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
        //no usamos
    }
        
    private void filtrar() {
        String texto = editor.getText().toLowerCase();
        DefaultComboBoxModel<Cliente> modelo = (DefaultComboBoxModel<Cliente>) menu.cmbCliente.getModel();
        modelo.removeAllElements(); // limpiamos

        if (texto.isEmpty()) {
            for (Cliente c : listaDeClientes) {
                modelo.addElement(c);
            }
        } else {
            for (Cliente c : listaDeClientes) {
                if (c.getNombre().toLowerCase().contains(texto)) {
                    modelo.addElement(c);
                }
            }
        }

        // Mantener el texto escrito en el editor
        editor.setText(texto);
        menu.cmbCliente.setPopupVisible(true);
    }

    
}