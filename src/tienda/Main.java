package tienda;

import tienda.servicio.SistemaTienda;
import tienda.vista.VentanaPrincipal;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal(new SistemaTienda()).setVisible(true));
    }
}
