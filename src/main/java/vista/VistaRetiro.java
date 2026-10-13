package vista;

import modelo.Cajero;
import modelo.EstadoCuenta;
import modelo.Observador;
import modelo.Recibo;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.List;

// Vista: ventana del cajero para retirar efectivo. Observa al cajero para refrescarse sola
public class VistaRetiro extends JFrame implements Observador {

    private Cajero cajero;
    private DefaultListModel<Double> modeloLista;
    private JList<Double> listaMontos;
    private JTextArea areaDetalle;
    private TitledBorder bordeDetalle;
    private JPanel panelDerecho;
    private JButton btnRetirar;

    public VistaRetiro(Cajero cajero) {
        this.cajero = cajero;
        setTitle("Cajero automático - Retirar efectivo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(680, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelIzquierdo(), BorderLayout.WEST);
        add(crearPanelDerecho(), BorderLayout.CENTER);
    }

    // Zona izquierda: lista de montos y boton de retirar
    private JPanel crearPanelIzquierdo() {
        modeloLista = new DefaultListModel<>();
        listaMontos = new JList<>(modeloLista);
        listaMontos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaMontos.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        // Muestra cada monto con formato de dinero
        listaMontos.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
                                                          boolean seleccionado, boolean conFoco) {
                JLabel celda = (JLabel) super.getListCellRendererComponent(
                        lista, valor, indice, seleccionado, conFoco);
                celda.setText(dinero((Double) valor));
                return celda;
            }
        });

        btnRetirar = new JButton("Retirar efectivo");
        btnRetirar.setEnabled(false);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Cantidades disponibles"));
        panel.setPreferredSize(new Dimension(220, 0));
        panel.add(new JScrollPane(listaMontos), BorderLayout.CENTER);
        panel.add(btnRetirar, BorderLayout.SOUTH);
        return panel;
    }

    // Zona derecha: aqui aparece el estado de cuenta y despues el recibo
    private JPanel crearPanelDerecho() {
        areaDetalle = new JTextArea("Seleccione un monto de la lista.");
        areaDetalle.setEditable(false);
        areaDetalle.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        areaDetalle.setMargin(new Insets(10, 10, 10, 10));

        bordeDetalle = BorderFactory.createTitledBorder("Estado de cuenta");
        panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBorder(bordeDetalle);
        panelDerecho.add(new JScrollPane(areaDetalle), BorderLayout.CENTER);
        return panelDerecho;
    }

    // El cajero avisa que algo cambio: la vista le pide los datos y se vuelve a dibujar
    @Override
    public void actualizar() {
        EstadoCuenta estado = cajero.getEstadoCuenta();
        if (estado != null) {
            mostrarEstadoCuenta(estado);
            return;
        }
        mostrarMontos(cajero.obtenerMontosDisponibles());
        Recibo recibo = cajero.getRecibo();
        if (recibo != null) {
            mostrarRecibo(recibo);
        }
    }

    // Llena la lista con los montos que se pueden retirar
    public void mostrarMontos(List<Double> montos) {
        modeloLista.clear();
        for (Double monto : montos) {
            modeloLista.addElement(monto);
        }
        btnRetirar.setEnabled(false);
    }

    // Muestra en la zona derecha el estado de cuenta del monto elegido
    public void mostrarEstadoCuenta(EstadoCuenta estado) {
        cambiarTitulo("Estado de cuenta");
        areaDetalle.setText(
                "Nombre:\n  " + estado.getNombre() + "\n\n"
                + renglon("Monto actual:", estado.getMontoActual())
                + renglon("Cantidad a retirar:", estado.getCantidadRetirar())
                + renglon("Comisión por retiro:", estado.getComision())
                + "------------------------------------\n"
                + renglon("Monto final:", estado.getMontoFinal()));
        btnRetirar.setEnabled(true);
    }

    // Muestra en la zona derecha el recibo del retiro realizado
    public void mostrarRecibo(Recibo recibo) {
        cambiarTitulo("Recibo de retiro");
        areaDetalle.setText(
                "         RECIBO DE RETIRO\n"
                + "------------------------------------\n"
                + "Cuenta:  **** **** **** " + recibo.getUltimosDigitos() + "\n"
                + "Nombre:\n  " + recibo.getNombre() + "\n\n"
                + renglon("Cantidad retirada:", recibo.getCantidadRetirada())
                + renglon("Cobro por comisión:", recibo.getComision())
                + "------------------------------------\n"
                + "       Retire su efectivo");
        btnRetirar.setEnabled(false);
    }

    // Muestra un aviso al cliente
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    // Regresa el monto elegido en la lista, o null si no hay ninguno
    public Double getMontoSeleccionado() {
        return listaMontos.getSelectedValue();
    }

    // Permite al controlador enterarse cuando se elige un monto
    public void agregarListenerMonto(ListSelectionListener listener) {
        listaMontos.addListSelectionListener(listener);
    }

    // Permite al controlador enterarse cuando se presiona "Retirar efectivo"
    public void agregarListenerRetirar(ActionListener listener) {
        btnRetirar.addActionListener(listener);
    }

    // Cambia el titulo de la zona derecha
    private void cambiarTitulo(String titulo) {
        bordeDetalle.setTitle(titulo);
        panelDerecho.repaint();
    }

    // Arma un renglon con la etiqueta a la izquierda y la cantidad alineada a la derecha
    private String renglon(String etiqueta, double cantidad) {
        return String.format("%-21s%15s%n", etiqueta, dinero(cantidad));
    }

    // Da formato de dinero a una cantidad
    private String dinero(double cantidad) {
        return String.format("$%,.2f", cantidad);
    }
}
