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
import modelo.Login;

public class CtrlMenu {
    private MenuGUI menu;
    private CardLayout cardLayout;
    private CtrlCliente ctrlCliente;
    private CtrlNuevoPedido ctrlNuevoPedido;
    private CtrlPedidos ctrlPedidos;
    private Conexion conexion = new Conexion();
    private Login usuario;
    
    //private LoginDAO dao = new LoginDAO();
    
    public CtrlMenu(MenuGUI menu, Login usuario)
    {
        this.menu = menu;
        this.cardLayout = (CardLayout) menu.getPanelImagenes().getLayout();
        this.ctrlCliente = null;
        this.usuario = usuario;
        this.ctrlNuevoPedido = null;
        this.ctrlPedidos = null;
        
        
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
                        if (ctrlNuevoPedido == null) {
                            ctrlNuevoPedido = new CtrlNuevoPedido(menu, usuario);
                            System.out.println("Controlador de Nuevo Pedido inicializado.");
                        }
                        menu.txtCliente.requestFocusInWindow();
                        cardLayout.show(menu.getPanelImagenes(), "Nuevo Pedido CRUD");
                        
                        
                        break;
                    case "Pedidos":
                        if (ctrlPedidos == null) {
                            ctrlPedidos = new CtrlPedidos(menu, usuario);
                            System.out.println("Controlador de Pedidos inicializado.");
                        }
                        cardLayout.show(menu.getPanelImagenes(), "PedidosCRUD");
                        
                        break;
                    case "Pagos":
                        
                        break;
                    case "Clientes":
                        if (ctrlCliente == null) {
                            ctrlCliente = new CtrlCliente(menu);
                            System.out.println("Controlador de Clientes inicializado.");
                        }
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