package parcial.vamos_de_paso;

import java.util.*;

/**
 * Clase que representa el Grafo de ciudades y tramos.
 * Utiliza una lista de adyacencia para representar los caminos.
 */
public class Grafo {
    // Mapa para buscar rápidamente una Ciudad por su nombre
    private Map<String, Ciudad> ciudades;
    // Lista de adyacencia: cada Ciudad se asocia con su lista de tramos salientes
    private Map<Ciudad, List<Tramo>> adyacencias;

    // Constructor del grafo
    public Grafo() {
        this.ciudades = new HashMap<>();
        this.adyacencias = new HashMap<>();
    }

    /**
     * Agrega una nueva ciudad al grafo si no existe.
     * @param nombre Nombre de la ciudad.
     */
    public void agregarCiudad(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("El nombre de la ciudad no puede estar vacío.");
            return;
        }
        if (!ciudades.containsKey(nombre)) {
            Ciudad nuevaCiudad = new Ciudad(nombre);
            ciudades.put(nombre, nuevaCiudad);
            adyacencias.put(nuevaCiudad, new ArrayList<>());
        }
    }

    /**
     * Agrega un tramo entre dos ciudades (no dirigido).
     * Si las ciudades no existen en el grafo, las agrega automáticamente.
     * @param origen Nombre de la ciudad de origen.
     * @param destino Nombre de la ciudad de destino.
     * @param distancia Distancia en kilómetros (peso 1).
     * @param carriles Cantidad de carriles (peso 2).
     */
    public void agregarTramo(String origen, String destino, double distancia, int carriles) {
        if (origen == null || destino == null || origen.equals(destino)) {
            System.out.println("Las ciudades origen y destino deben ser válidas y diferentes.");
            return;
        }

        // Nos aseguramos de que ambas ciudades estén en el grafo
        agregarCiudad(origen);
        agregarCiudad(destino);

        Ciudad ciudadOrigen = ciudades.get(origen);
        Ciudad ciudadDestino = ciudades.get(destino);

        // Como el grafo es no dirigido, agregamos el tramo en ambos sentidos
        adyacencias.get(ciudadOrigen).add(new Tramo(ciudadDestino, distancia, carriles));
        adyacencias.get(ciudadDestino).add(new Tramo(ciudadOrigen, distancia, carriles));
    }

    /**
     * Muestra la estructura del grafo en consola de forma visual.
     */
    public void mostrarGrafo() {
        System.out.println("=== ESTRUCTURA DEL GRAFO ===");
        for (Map.Entry<Ciudad, List<Tramo>> entry : adyacencias.entrySet()) {
            Ciudad ciudad = entry.getKey();
            List<Tramo> tramos = entry.getValue();
            System.out.print(ciudad.getNombre() + " conecta con:");
            if (tramos.isEmpty()) {
                System.out.println(" (Ninguna ciudad)");
            } else {
                System.out.println();
                for (Tramo t : tramos) {
                    System.out.println("  " + t);
                }
            }
        }
        System.out.println("=============================");
    }

    // Getters para que el resto del programa o algoritmos puedan acceder
    public Map<String, Ciudad> getCiudades() {
        return ciudades;
    }

    public Map<Ciudad, List<Tramo>> getAdyacencias() {
        return adyacencias;
    }
}
