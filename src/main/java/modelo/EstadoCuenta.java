package modelo;

// Datos que se muestran al cliente antes de confirmar el retiro
public class EstadoCuenta {

    private String nombre;
    private double montoActual;
    private double cantidadRetirar;
    private double comision;
    private double montoFinal;

    public EstadoCuenta(String nombre, double montoActual, double cantidadRetirar,
                        double comision, double montoFinal) {
        this.nombre = nombre;
        this.montoActual = montoActual;
        this.cantidadRetirar = cantidadRetirar;
        this.comision = comision;
        this.montoFinal = montoFinal;
    }

    public String getNombre() {
        return nombre;
    }

    public double getMontoActual() {
        return montoActual;
    }

    public double getCantidadRetirar() {
        return cantidadRetirar;
    }

    public double getComision() {
        return comision;
    }

    public double getMontoFinal() {
        return montoFinal;
    }
}
