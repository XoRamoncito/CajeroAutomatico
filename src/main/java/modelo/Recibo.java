package modelo;

// Recibo que se genera cuando el retiro se realiza
public class Recibo {

    private String ultimosDigitos;
    private String nombre;
    private double cantidadRetirada;
    private double comision;

    public Recibo(String ultimosDigitos, String nombre, double cantidadRetirada, double comision) {
        this.ultimosDigitos = ultimosDigitos;
        this.nombre = nombre;
        this.cantidadRetirada = cantidadRetirada;
        this.comision = comision;
    }

    public String getUltimosDigitos() {
        return ultimosDigitos;
    }

    public String getNombre() {
        return nombre;
    }

    public double getCantidadRetirada() {
        return cantidadRetirada;
    }

    public double getComision() {
        return comision;
    }
}
