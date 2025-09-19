package sopaparaguaya;

import controlador.CtrlLogin;
import interfaz.LoginGUI;

public class SopaParaguaya {
    public static void main(String[] args) {
        LoginGUI loginGUI = new LoginGUI();
        CtrlLogin ctrlLogin = new CtrlLogin(loginGUI); //Enviamos la interfaz Login al controlador del mismo
        
        loginGUI.setVisible(true);
        
    }
}