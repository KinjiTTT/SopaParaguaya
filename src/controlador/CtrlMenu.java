package controlador;

import interfaz.MenuGUI;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modelo.Conexion;

public class CtrlMenu {
    private MenuGUI menu;
    private CardLayout cardLayout;
    private CtrlCliente ctrlCliente;
    private CtrlNuevoPedido ctrlNuevoPedido;
    private Conexion conexion = new Conexion();
    
    //private LoginDAO dao = new LoginDAO();
    
    public CtrlMenu(MenuGUI menu)
    {
        this.menu = menu;
        this.cardLayout = (CardLayout) menu.getPanelImagenes().getLayout();
        this.ctrlCliente = new CtrlCliente(menu);
        
        menu.getListaDeOpciones().addListSelectionListener(new ListSelectionListener() { //Es parecido a cuando usas un ActionListener predeterminado
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String seleccion = menu.getListaDeOpciones().getSelectedValue();
                    // Mostramos el panel correspondiente
                    cardLayout.show(menu.getPanelImagenes(), seleccion);
                    
                }

            }
        });
        
        menu.getListaDeOpciones().getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "accionEnter");
        menu.getListaDeOpciones().getActionMap().put("accionEnter", new AbstractAction(){
            
            public void actionPerformed(ActionEvent e) {
                String seleccion = menu.getListaDeOpciones().getSelectedValue();
                System.out.println("Enter en: " + seleccion);
                // acá podés abrir ventana, mostrar un panel, etc.

                JOptionPane.showMessageDialog(menu, "Entraste a: " + seleccion);
                switch(seleccion)
                {
                    case "Nuevo Pedido":
                        
                        cardLayout.show(menu.getPanelImagenes(), "Nuevo Pedido CRUD");
                        
                        break;
                    case "Pedidos":
                        
                        break;
                    case "Pagos":
                        
                        break;
                    case "Clientes":
                        ctrlCliente.LlenarTabla();
                        cardLayout.show(menu.getPanelImagenes(), "ClientesCRUD");
                        menu.txtNombreCliente.requestFocusInWindow(); // foco directo en el campo
                        break;
                    case "Historial":
                        
                        break;
                    case "Ganancias":
                        
                        break;
                   
                        
                }
            }
        });
    }
}