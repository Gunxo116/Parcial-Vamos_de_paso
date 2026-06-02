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

    // ==========================================
    // MÉTODOS DEL INTEGRANTE 2 (Dijkstra)
    // ==========================================

    /**
     * Clase auxiliar que sirve para ordenar los nodos en la Cola de Prioridad (PriorityQueue)
     * basándose en la distancia acumulada hasta el momento.
     */
    private static class NodoDijkstra implements Comparable<NodoDijkstra> {
        Ciudad ciudad;
        double distanciaAcumulada;

        public NodoDijkstra(Ciudad ciudad, double distanciaAcumulada) {
            this.ciudad = ciudad;
            this.distanciaAcumulada = distanciaAcumulada;
        }

        @Override
        public int compareTo(NodoDijkstra otro) {
            // Comparamos las distancias acumuladas para que el menor vaya primero
            return Double.compare(this.distanciaAcumulada, otro.distanciaAcumulada);
        }
    }

    /**
     * Encuentra la ruta más corta entre dos ciudades usando el algoritmo de Dijkstra clásico.
     * Suma los kilómetros de los tramos.
     * @param origen Nombre de la ciudad de partida.
     * @param destino Nombre de la ciudad de llegada.
     * @return Lista con los nombres de las ciudades del camino, o null si no hay camino.
     */
    public List<String> rutaMasCorta(String origen, String destino) {
        // Validamos que ambas ciudades existan en nuestro grafo
        if (!ciudades.containsKey(origen) || !ciudades.containsKey(destino)) {
            System.out.println("Error: Una o ambas ciudades no existen en el grafo.");
            return null;
        }

        Ciudad ciudadOrigen = ciudades.get(origen);
        Ciudad ciudadDestino = ciudades.get(destino);

        // Mapa para guardar la distancia más corta conocida desde el origen a cada ciudad
        Map<Ciudad, Double> distancias = new HashMap<>();
        // Mapa para guardar el "padre" de cada ciudad en el camino más corto (para reconstruir la ruta)
        Map<Ciudad, Ciudad> padres = new HashMap<>();
        // Cola de prioridad para procesar siempre el nodo con menor distancia acumulada
        PriorityQueue<NodoDijkstra> colaPrioridad = new PriorityQueue<>();

        // Inicializamos todas las distancias conocidas como "infinito" (Double.MAX_VALUE)
        for (Ciudad c : adyacencias.keySet()) {
            distancias.put(c, Double.MAX_VALUE);
        }

        // La distancia desde el origen a sí mismo es 0
        distancias.put(ciudadOrigen, 0.0);
        colaPrioridad.add(new NodoDijkstra(ciudadOrigen, 0.0));

        // Bucle principal de Dijkstra
        while (!colaPrioridad.isEmpty()) {
            NodoDijkstra actual = colaPrioridad.poll();
            Ciudad u = actual.ciudad;

            // Si ya encontramos un camino más corto a esta ciudad, ignoramos este registro viejo
            if (actual.distanciaAcumulada > distancias.get(u)) {
                continue;
            }

            // Si ya llegamos al destino, terminamos el algoritmo antes (optimización)
            if (u.equals(ciudadDestino)) {
                break;
            }

            // Revisamos todas las conexiones (tramos) que salen de la ciudad actual
            List<Tramo> tramosVecinos = adyacencias.get(u);
            if (tramosVecinos != null) {
                for (Tramo tramo : tramosVecinos) {
                    Ciudad v = tramo.getDestino();
                    // Distancia acumulada si viajamos de 'u' a 'v'
                    double nuevaDistancia = distancias.get(u) + tramo.getDistancia();

                    // Si encontramos un camino más corto hacia 'v', actualizamos
                    if (nuevaDistancia < distancias.get(v)) {
                        distancias.put(v, nuevaDistancia);
                        padres.put(v, u); // Guardamos que a 'v' se llega mejor por 'u'
                        colaPrioridad.add(new NodoDijkstra(v, nuevaDistancia));
                    }
                }
            }
        }

        // Si la distancia al destino sigue siendo infinito, significa que no hay conexión
        if (distancias.get(ciudadDestino) == Double.MAX_VALUE) {
            System.out.println("No se encontró ninguna ruta entre " + origen + " y " + destino);
            return null;
        }

        // Reconstruimos el camino desde el destino hacia atrás usando el mapa de padres
        List<String> camino = new ArrayList<>();
        Ciudad aux = ciudadDestino;
        while (aux != null) {
            camino.add(0, aux.getNombre()); // Lo insertamos al inicio para que quede en orden
            aux = padres.get(aux);
        }

        // Imprimimos el resultado de forma clara por pantalla
        System.out.println("Ruta más corta desde " + origen + " hasta " + destino + ":");
        System.out.println("Camino: " + String.join(" -> ", camino));
        System.out.println("Distancia total: " + distancias.get(ciudadDestino) + " km");

        return camino;
    }

    /**
     * Calcula la ruta más corta desde una ciudad origen hacia todos los demás destinos.
     * Imprime las distancias y caminos correspondientes en consola.
     * @param origen Nombre de la ciudad de partida.
     */
    public void rutaMasCortaDesdeOrigen(String origen) {
        if (!ciudades.containsKey(origen)) {
            System.out.println("Error: La ciudad origen \"" + origen + "\" no existe en el grafo.");
            return;
        }

        Ciudad ciudadOrigen = ciudades.get(origen);

        // Mapas para almacenar la distancia mínima y el camino (padre de cada nodo)
        Map<Ciudad, Double> distancias = new HashMap<>();
        Map<Ciudad, Ciudad> padres = new HashMap<>();
        PriorityQueue<NodoDijkstra> colaPrioridad = new PriorityQueue<>();

        // Inicializamos todas las distancias conocidas como "infinito" (Double.MAX_VALUE)
        for (Ciudad c : adyacencias.keySet()) {
            distancias.put(c, Double.MAX_VALUE);
        }

        // La distancia desde el origen a sí mismo es 0
        distancias.put(ciudadOrigen, 0.0);
        colaPrioridad.add(new NodoDijkstra(ciudadOrigen, 0.0));

        // Bucle de Dijkstra
        while (!colaPrioridad.isEmpty()) {
            NodoDijkstra actual = colaPrioridad.poll();
            Ciudad u = actual.ciudad;

            // Si ya hay un camino más corto, ignoramos
            if (actual.distanciaAcumulada > distancias.get(u)) {
                continue;
            }

            // Revisamos los vecinos de u
            List<Tramo> tramosVecinos = adyacencias.get(u);
            if (tramosVecinos != null) {
                for (Tramo tramo : tramosVecinos) {
                    Ciudad v = tramo.getDestino();
                    double nuevaDistancia = distancias.get(u) + tramo.getDistancia();

                    if (nuevaDistancia < distancias.get(v)) {
                        distancias.put(v, nuevaDistancia);
                        padres.put(v, u);
                        colaPrioridad.add(new NodoDijkstra(v, nuevaDistancia));
                    }
                }
            }
        }

        // Imprimimos los resultados para todas las ciudades
        System.out.println("=== RUTAS MÁS CORTAS DESDE: " + origen + " ===");
        for (Ciudad destino : adyacencias.keySet()) {
            if (destino.equals(ciudadOrigen)) {
                continue; // Omitimos mostrar el origen hacia sí mismo
            }

            double dist = distancias.get(destino);
            if (dist == Double.MAX_VALUE) {
                System.out.println("A " + destino.getNombre() + ": Inalcanzable");
            } else {
                // Reconstruimos el camino hacia atrás para el destino actual
                List<String> camino = new ArrayList<>();
                Ciudad aux = destino;
                while (aux != null) {
                    camino.add(0, aux.getNombre());
                    aux = padres.get(aux);
                }
                System.out.println("A " + destino.getNombre() + ":");
                System.out.println("  Camino: " + String.join(" -> ", camino));
                System.out.println("  Distancia total: " + dist + " km");
            }
        }
        System.out.println("=========================================");
    }
}

