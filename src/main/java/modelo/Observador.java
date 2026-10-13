package modelo;

// Cambios del cajero (vista)
public interface Observador {

    // El cajero lo llama cada vez que sus datos cambian
    void actualizar();
}
