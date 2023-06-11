
package main;

import Controlador.Controlador;
import Vista.Salon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class ModuloSalon {

    public static void main(String[] args) {
        Salon panel = new Salon();
        panel.setSize(550,550);
        panel.setVisible(true);
        panel.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Controlador control = new Controlador(panel);
    }
    
}
