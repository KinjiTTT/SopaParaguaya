package controlador;
import interfaz.LoginGUI;
import modelo.Login;
import dao.LoginDAO;
import modelo.Conexion;
import interfaz.MenuGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;



public class CtrlLogin implements ActionListener {
    private LoginGUI login;
    //private Conexion conexion = new Conexion();
    private LoginDAO dao = new LoginDAO();
    
    public CtrlLogin(LoginGUI login)
    {
        this.login = login;
        this.login.btnIngresar.addActionListener(this);
        /*
        Hay dos maneras de usar ActionListener con los textfields, esta es una:
        // Escuchar Enter en usuario → pasar a contraseña
        this.vista.txtUsuario.addActionListener(e -> vista.txtContraseña.requestFocus());

        // Escuchar Enter en contraseña → simular click del botón
        this.vista.txtContraseña.addActionListener(e -> vista.btnIngresar.doClick());
        
        A esta manera se la conoce como la manera modular, que separa cada componente con su propio listener.
        La otra manera es como se esta utilizando en este programa, registrando los listeners pero en lugar de
        tener las acciones de los componentes separados, se pone todo dentro del ActionPerformed con los "if's"
        */
        this.login.txtUsuario.addActionListener(this);// Escuchar Enter en usuario → pasar a contraseña
        this.login.txtContraseña.addActionListener(this); // Escuchar Enter en contraseña → simular click del botón
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == login.btnIngresar)
        {
            if(login.txtUsuario.getText().isEmpty() || login.txtContraseña.getText().isEmpty())
            {
                JOptionPane.showMessageDialog(null,"Llene todos los campos");
            }
            Login usuario = new Login();
            usuario.setUsuario(login.txtUsuario.getText());
            usuario.setPassword(login.txtContraseña.getText());
            if(dao.iniciarSesion(usuario))
            {
                JOptionPane.showMessageDialog(null, "Bien hecho caballero");
                MenuGUI mainGUI = new MenuGUI();
                CtrlMenu ctrlMenu = new CtrlMenu(mainGUI); //Mandamos como argumento la vista al controlador
                mainGUI.setVisible(true);
                login.dispose();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Usuario no encontrado");
            }
            
            
        }
        else if (e.getSource() == login.txtUsuario) {
            login.txtContraseña.requestFocus(); //Tras presionar ENTER va al campo de la contraseña
        } else if (e.getSource() == login.txtContraseña) {
            login.btnIngresar.doClick(); //Tras presionar ENTER hace un click en el boton Ingresar
        }
        
    }
}
