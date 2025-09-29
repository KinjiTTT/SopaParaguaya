package controlador;

import dao.PedidosDAO;
import interfaz.MenuGUI;
import java.awt.CardLayout;
import modelo.Login;

public class CtrlPedidos {
    private MenuGUI menu;
    private PedidosDAO pedDAO = new PedidosDAO();
    private Login usuario;
    private CardLayout cardLayout;
    
    public CtrlPedidos(MenuGUI menu, Login usuario){
        this.menu = menu;
        this.usuario = usuario;
        this.cardLayout = (CardLayout) menu.getPanelImagenes().getLayout();
    }
}
