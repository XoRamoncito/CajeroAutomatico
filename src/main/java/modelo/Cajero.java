package modelo;

import java.util.ArrayList;
import java.util.List;

// Modelo principal: contiene las reglas del retiro de efectivo y avisa a sus observadores
public class Cajero {

    // Los montos se ofrecen en multiplos de esta cantidad
    public static final double MULTIPLO = 200.00;
    // Porcentaje que se cobra de comision sobre el monto retirado
    public static final double PORCENTAJE_COMISION = 0.02;

    private Cuenta cuenta;
    // Estado de cuenta del monto elegido (null si no hay monto elegido)
    private EstadoCuenta estadoCuenta;
    // Recibo del ultimo retiro (null si todavia no se retira)
    private Recibo recibo;
    private List<Observador> observadores = new ArrayList<>();

    public Cajero(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    // Registra a alguien que quiere enterarse de los cambios
    public void agregarObservador(Observador observador) {
        observadores.add(observador);
    }

    // Avisa a todos los observadores que los datos cambiaron
    private void notificarObservadores() {
        for (Observador observador : observadores) {
            observador.actualizar();
        }
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public EstadoCuenta getEstadoCuenta() {
        return estadoCuenta;
    }

    public Recibo getRecibo() {
        return recibo;
    }

    // Regresa los montos en multiplos de 200 que el saldo de la cuenta permite retirar
    public List<Double> obtenerMontosDisponibles() {
        List<Double> montos = new ArrayList<>();
        double monto = MULTIPLO;
        while (cuenta.tieneSaldo(monto + calcularComision(monto))) {
            montos.add(monto);
            monto += MULTIPLO;
        }
        return montos;
    }

    // Calcula la comision que se cobra por retirar el monto
    public double calcularComision(double monto) {
        return monto * PORCENTAJE_COMISION;
    }

    // Arma el estado de cuenta del monto elegido (todavia no retira nada) y avisa
    public void seleccionarMonto(double monto) {
        double comision = calcularComision(monto);
        double montoFinal = cuenta.getSaldo() - monto - comision;
        estadoCuenta = new EstadoCuenta(cuenta.getTitular(), cuenta.getSaldo(), monto, comision, montoFinal);
        recibo = null;
        notificarObservadores();
    }

    // Realiza el retiro del monto elegido: descuenta monto y comision, genera el recibo y avisa
    public void retirarEfectivo() {
        if (estadoCuenta == null) {
            throw new IllegalStateException("Seleccione un monto de la lista.");
        }
        double monto = estadoCuenta.getCantidadRetirar();
        double comision = estadoCuenta.getComision();
        double total = monto + comision;

        if (!cuenta.tieneSaldo(total)) {
            throw new IllegalStateException("Saldo insuficiente para retirar ese monto.");
        }

        cuenta.descontar(total);
        recibo = new Recibo(cuenta.getUltimosDigitos(), cuenta.getTitular(), monto, comision);
        estadoCuenta = null;
        notificarObservadores();
    }
}
