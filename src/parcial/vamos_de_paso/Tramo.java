package parcial.vamos_de_paso;

/**
 * Clase que representa un Tramo (arco o arista entre dos ciudades).
 * Contiene el destino, la distancia en km y la cantidad de carriles.
 */
public class Tramo {
    private Ciudad origen;
    private Ciudad destino;
    private double distancia; // Peso 1: menor distancia (km)
    private int carriles;     // Peso 2: mayor capacidad de flujo (carriles)

    // Constructor para inicializar el tramo
    public Tramo(Ciudad origen, Ciudad destino, double distancia, int carriles) {
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
        this.carriles = carriles;
    }

    // Getters y Setters
    public Ciudad getOrigen() {
        return origen;
    }

    public void setOrigen(Ciudad origen) {
        this.origen = origen;
    }

    public Ciudad getDestino() {
        return destino;
    }

    public void setDestino(Ciudad destino) {
        this.destino = destino;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public int getCarriles() {
        return carriles;
    }

    public void setCarriles(int carriles) {
        this.carriles = carriles;
    }

    // Método toString para depurar e imprimir el tramo
    @Override
    public String toString() {
        return origen.getNombre() + " -> " + destino.getNombre() + " (Distancia: " + distancia + " km, Carriles: " + carriles + ")";
    }
}
