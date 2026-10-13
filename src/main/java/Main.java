import controlador.ControlRetiro;
import modelo.Cajero;
import modelo.Cuenta;
import vista.VistaRetiro;

import javax.swing.SwingUtilities;

// Punto de entrada: arma el modelo, la vista y el controlador
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Cuenta de ejemplo del cliente que ya eligio la opcion "Retirar"
            Cuenta cuenta = new Cuenta("4152313800457821", "Ramon Montoya Manjarrez", 5300.00);

            Cajero cajero = new Cajero(cuenta);
            VistaRetiro vista = new VistaRetiro(cajero);
            ControlRetiro control = new ControlRetiro(cajero, vista);

            control.iniciar();
        });
    }
}
