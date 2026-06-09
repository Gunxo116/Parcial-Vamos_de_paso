package parcial.vamos_de_paso;

public class Tramo {
    private Ciudad origen;
    private Ciudad destino;
    private double distancia;
    private int carriles;

    public Tramo(Ciudad origen, Ciudad destino, double distancia, int carriles) {
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
        this.carriles = carriles;
    }

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

    @Override
    public String toString() {
        return origen.getNombre() + " -> " + destino.getNombre() + " (Distancia: " + distancia + " km, Carriles: " + carriles + ")";
    }
}
