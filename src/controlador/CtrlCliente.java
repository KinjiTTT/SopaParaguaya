/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import dao.ClienteDAO;
import interfaz.MenuGUI;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import modelo.Cliente;
import modelo.Conexion;

/**
 *
 * @author Notebook
 */
public class CtrlCliente implements ActionListener {
    private MenuGUI menu;
    private Conexion conexion = new Conexion();
    private DefaultTableModel model;
    ClienteDAO cliDAO = new ClienteDAO(); //de momento lo declaro asi
    
    CtrlCliente(MenuGUI menu)
    {
        this.menu = menu;
        
        this.menu.btnAgregarCliente.addActionListener(this);
        this.menu.btnModificarCliente.addActionListener(this);
        this.menu.btnEliminarCliente.addActionListener(this);
        //this.cliDAO = new ClienteDAO(); // asi lo declararia mas adelante (mas o menos) con una inyeccion de dependencias (Lo que hice con MenuGUI)
        //LlenarTabla(); //es mejor no hacer logica dentro del constructor
        this.menu.tblClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
            int fila = menu.tblClientes.getSelectedRow();
            if (fila != -1) {
                menu.txtNombreCliente.setText((String) menu.tblClientes.getValueAt(fila, 1));
                menu.txtTelefonoCliente.setText((String) menu.tblClientes.getValueAt(fila, 2));
            }
    }
});
    }
    public void CrearTabla(){
        model = new DefaultTableModel();
        model.addColumn("id");
        model.addColumn("Cliente");
        model.addColumn("Telefono");
        
        menu.tblClientes.setModel(model); // Usa la tabla creada por el diseñador
        //lo siguiente marca un tamaño minimo y maximo para la primera columna (la columna 0), al ponerle valor 0 a ambas permanece oculta
        menu.tblClientes.getColumnModel().getColumn(0).setMinWidth(0);
        menu.tblClientes.getColumnModel().getColumn(0).setMaxWidth(0);
    }
    
    public void LlenarTabla(){
        ArrayList<Cliente> lista = cliDAO.CargarClientes();
        CrearTabla();
        
        for(Cliente c: lista)
        {
            Object[] fila = new Object[3];
            fila[0] = c.getId();
            fila[1] = c.getNombre();
            fila[2] = c.getTelefono();
            model.addRow(fila);
        }
        
        
    }
    public void LimpiarCampos(){
        menu.txtNombreCliente.setText("");
        menu.txtTelefonoCliente.setText("");
    }
    
    
    @Override
    public void actionPerformed(ActionEvent a)
    {
        if(a.getSource() == menu.btnAgregarCliente)
        {
            Cliente cliente = new Cliente();
            
            cliente.setNombre(menu.txtNombreCliente.getText());
            cliente.setTelefono(menu.txtTelefonoCliente.getText());
            
            cliDAO.AgregarCliente(cliente);
            LlenarTabla();
            LimpiarCampos();
            menu.txtNombreCliente.requestFocusInWindow(); //para enfocar el teclado a ese campo
            
            System.err.println("bienbienbien");
        }
        else if(a.getSource() == menu.btnModificarCliente)
        {
            int fila = menu.tblClientes.getSelectedRow();
            if (fila != -1) {
                Cliente cliente = new Cliente();
                cliente.setId((int) menu.tblClientes.getValueAt(fila, 0)); // ID oculto
                cliente.setNombre(menu.txtNombreCliente.getText());
                cliente.setTelefono(menu.txtTelefonoCliente.getText());

                cliDAO.ModificarCliente(cliente);
                LlenarTabla();
                LimpiarCampos();
            }
        }
        else if(a.getSource() == menu.btnEliminarCliente)
        {
            int fila = menu.tblClientes.getSelectedRow();
            if (fila != -1) {
                int id = (int) menu.tblClientes.getValueAt(fila, 0);
                cliDAO.EliminarCliente(id);
                LlenarTabla();
                LimpiarCampos();
            }
            System.err.println("Cliente eliminado correctamente");
            
        }
    }
}
