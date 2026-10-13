package controlador;

import modelo.Cajero;
import vista.VistaRetiro;

// Controlador: recibe lo que hace el cliente en la vista y le pide el trabajo al modelo
public class ControlRetiro {

    private Cajero cajero;
    private VistaRetiro vista;

    public ControlRetiro(Cajero cajero, VistaRetiro vista) {
        this.cajero = cajero;
        this.vista = vista;
    }

    // Registra la vista como observador del cajero, conecta los eventos y muestra la ventana
    public void iniciar() {
        cajero.agregarObservador(vista);

        vista.agregarListenerMonto(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarMonto();
            }
        });
        vista.agregarListenerRetirar(e -> retirarEfectivo());

        // Primera carga de la pantalla con los datos del cajero
        vista.actualizar();
        vista.setVisible(true);
    }

    // Se ejecuta cuando el cliente elige un monto de la lista
    public void seleccionarMonto() {
        Double monto = vista.getMontoSeleccionado();
        if (monto == null) {
            return;
        }
        // El cajero avisa a la vista cuando el estado de cuenta esta listo
        cajero.seleccionarMonto(monto);
    }

    // Se ejecuta cuando el cliente presiona "Retirar efectivo"
    public void retirarEfectivo() {
        try {
            // El cajero avisa a la vista cuando el recibo esta listo
            cajero.retirarEfectivo();
        } catch (IllegalStateException ex) {
            vista.mostrarMensaje(ex.getMessage());
        }
    }
}
