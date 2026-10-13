package modelo;

// Cuenta bancaria del cliente
public class Cuenta {

    private String numero;
    private String titular;
    private double saldo;

    public Cuenta(String numero, String titular, double saldo) {
        this.numero = numero;
        this.titular = titular;
        this.saldo = saldo;
    }

    public String getNumero() {
        return numero;
    }

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    // Regresa los ultimos cuatro digitos del numero de cuenta
    public String getUltimosDigitos() {
        return numero.substring(numero.length() - 4);
    }

    // Indica si el saldo alcanza para cubrir la cantidad
    public boolean tieneSaldo(double cantidad) {
        return cantidad <= saldo;
    }

    // Descuenta la cantidad del saldo
    public void descontar(double cantidad) {
        saldo -= cantidad;
    }
}
