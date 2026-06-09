package parcial.vamos_de_paso;

import java.util.*;

public class Grafo {
    private Map<String, Ciudad> ciudades;
    private Map<Ciudad, List<Tramo>> adyacencias;

    public Grafo() {
        this.ciudades = new HashMap<>();
        this.adyacencias = new HashMap<>();
    }

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

    public void agregarTramo(String origen, String destino, double distancia, int carriles) {
        if (origen == null || destino == null || origen.equals(destino)) {
            System.out.println("Las ciudades origen y destino deben ser válidas y diferentes.");
            return;
        }

        agregarCiudad(origen);
        agregarCiudad(destino);

        Ciudad ciudadOrigen = ciudades.get(origen);
        Ciudad ciudadDestino = ciudades.get(destino);

        adyacencias.get(ciudadOrigen).add(new Tramo(ciudadOrigen, ciudadDestino, distancia, carriles));
        adyacencias.get(ciudadDestino).add(new Tramo(ciudadDestino, ciudadOrigen, distancia, carriles));
    }

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

    public Map<String, Ciudad> getCiudades() {
        return ciudades;
    }

    public Map<Ciudad, List<Tramo>> getAdyacencias() {
        return adyacencias;
    }

    private static class NodoDijkstra implements Comparable<NodoDijkstra> {
        Ciudad ciudad;
        double distanciaAcumulada;

        public NodoDijkstra(Ciudad ciudad, double distanciaAcumulada) {
            this.ciudad = ciudad;
            this.distanciaAcumulada = distanciaAcumulada;
        }

        @Override
        public int compareTo(NodoDijkstra otro) {
            return Double.compare(this.distanciaAcumulada, otro.distanciaAcumulada);
        }
    }

    public List<String> rutaMasCorta(String origen, String destino) {
        if (!ciudades.containsKey(origen) || !ciudades.containsKey(destino)) {
            System.out.println("Error: Una o ambas ciudades no existen en el grafo.");
            return null;
        }

        Ciudad ciudadOrigen = ciudades.get(origen);
        Ciudad ciudadDestino = ciudades.get(destino);

        Map<Ciudad, Double> distancias = new HashMap<>();
        Map<Ciudad, Ciudad> padres = new HashMap<>();
        PriorityQueue<NodoDijkstra> colaPrioridad = new PriorityQueue<>();

        for (Ciudad c : adyacencias.keySet()) {
            distancias.put(c, Double.MAX_VALUE);
        }

        distancias.put(ciudadOrigen, 0.0);
        colaPrioridad.add(new NodoDijkstra(ciudadOrigen, 0.0));

        while (!colaPrioridad.isEmpty()) {
            NodoDijkstra actual = colaPrioridad.poll();
            Ciudad u = actual.ciudad;

            if (actual.distanciaAcumulada > distancias.get(u)) {
                continue;
            }

            if (u.equals(ciudadDestino)) {
                break;
            }

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

        if (distancias.get(ciudadDestino) == Double.MAX_VALUE) {
            System.out.println("No se encontró ninguna ruta entre " + origen + " y " + destino);
            return null;
        }

        List<String> camino = new ArrayList<>();
        Ciudad aux = ciudadDestino;
        while (aux != null) {
            camino.add(0, aux.getNombre());
            aux = padres.get(aux);
        }

        System.out.println("Ruta más corta desde " + origen + " hasta " + destino + ":");
        System.out.println("Camino: " + String.join(" -> ", camino));
        System.out.println("Distancia total: " + distancias.get(ciudadDestino) + " km");

        return camino;
    }

    public void rutaMasCortaDesdeOrigen(String origen) {
        if (!ciudades.containsKey(origen)) {
            System.out.println("Error: La ciudad origen \"" + origen + "\" no existe en el grafo.");
            return;
        }

        Ciudad ciudadOrigen = ciudades.get(origen);

        Map<Ciudad, Double> distancias = new HashMap<>();
        Map<Ciudad, Ciudad> padres = new HashMap<>();
        PriorityQueue<NodoDijkstra> colaPrioridad = new PriorityQueue<>();

        for (Ciudad c : adyacencias.keySet()) {
            distancias.put(c, Double.MAX_VALUE);
        }

        distancias.put(ciudadOrigen, 0.0);
        colaPrioridad.add(new NodoDijkstra(ciudadOrigen, 0.0));

        while (!colaPrioridad.isEmpty()) {
            NodoDijkstra actual = colaPrioridad.poll();
            Ciudad u = actual.ciudad;

            if (actual.distanciaAcumulada > distancias.get(u)) {
                continue;
            }

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

        System.out.println("=== RUTAS MÁS CORTAS DESDE: " + origen + " ===");
        for (Ciudad destino : adyacencias.keySet()) {
            if (destino.equals(ciudadOrigen)) {
                continue;
            }

            double dist = distancias.get(destino);
            if (dist == Double.MAX_VALUE) {
                System.out.println("A " + destino.getNombre() + ": Inalcanzable");
            } else {
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

    private static class NodoFlujo implements Comparable<NodoFlujo> {
        Ciudad ciudad;
        double capacidadCamino;

        public NodoFlujo(Ciudad ciudad, double capacidadCamino) {
            this.ciudad = ciudad;
            this.capacidadCamino = capacidadCamino;
        }

        @Override
        public int compareTo(NodoFlujo otro) {
            return Double.compare(otro.capacidadCamino, this.capacidadCamino);
        }
    }

    public List<String> rutaMayorFlujo(String origen, String destino) {
        if (!ciudades.containsKey(origen) || !ciudades.containsKey(destino)) {
            System.out.println("Error: Una o ambas ciudades no existen en el grafo.");
            return null;
        }

        if (origen.equals(destino)) {
            System.out.println("Ruta con mayor flujo desde " + origen + " hasta " + destino + ":");
            System.out.println("Camino: " + origen);
            System.out.println("Capacidad máxima de flujo: Sin límite (origen y destino son iguales)");
            List<String> camino = new ArrayList<>();
            camino.add(origen);
            return camino;
        }

        Ciudad ciudadOrigen = ciudades.get(origen);
        Ciudad ciudadDestino = ciudades.get(destino);

        Map<Ciudad, Double> capacidades = new HashMap<>();
        Map<Ciudad, Ciudad> padres = new HashMap<>();
        PriorityQueue<NodoFlujo> colaPrioridad = new PriorityQueue<>();

        for (Ciudad c : adyacencias.keySet()) {
            capacidades.put(c, 0.0);
        }

        capacidades.put(ciudadOrigen, Double.MAX_VALUE);
        colaPrioridad.add(new NodoFlujo(ciudadOrigen, Double.MAX_VALUE));

        while (!colaPrioridad.isEmpty()) {
            NodoFlujo actual = colaPrioridad.poll();
            Ciudad u = actual.ciudad;

            if (actual.capacidadCamino < capacidades.get(u)) {
                continue;
            }

            if (u.equals(ciudadDestino)) {
                break;
            }

            List<Tramo> tramosVecinos = adyacencias.get(u);
            if (tramosVecinos != null) {
                for (Tramo tramo : tramosVecinos) {
                    Ciudad v = tramo.getDestino();
                    double capacidadTentativa = Math.min(capacidades.get(u), tramo.getCarriles());

                    if (capacidadTentativa > capacidades.get(v)) {
                        capacidades.put(v, capacidadTentativa);
                        padres.put(v, u);
                        colaPrioridad.add(new NodoFlujo(v, capacidadTentativa));
                    }
                }
            }
        }

        if (capacidades.get(ciudadDestino) == 0.0) {
            System.out.println("No se encontró ninguna ruta entre " + origen + " y " + destino);
            return null;
        }

        List<String> camino = new ArrayList<>();
        Ciudad aux = ciudadDestino;
        while (aux != null) {
            camino.add(0, aux.getNombre());
            aux = padres.get(aux);
        }

        System.out.println("Ruta con mayor capacidad de flujo desde " + origen + " hasta " + destino + ":");
        System.out.println("Camino: " + String.join(" -> ", camino));
        System.out.println("Capacidad máxima de flujo (cuello de botella): " + (int) (double) capacidades.get(ciudadDestino) + " carriles");

        return camino;
    }

    public void rutaMayorFlujoDesdeOrigen(String origen) {
        if (!ciudades.containsKey(origen)) {
            System.out.println("Error: La ciudad origen \"" + origen + "\" no existe en el grafo.");
            return;
        }

        Ciudad ciudadOrigen = ciudades.get(origen);

        Map<Ciudad, Double> capacidades = new HashMap<>();
        Map<Ciudad, Ciudad> padres = new HashMap<>();
        PriorityQueue<NodoFlujo> colaPrioridad = new PriorityQueue<>();

        for (Ciudad c : adyacencias.keySet()) {
            capacidades.put(c, 0.0);
        }

        capacidades.put(ciudadOrigen, Double.MAX_VALUE);
        colaPrioridad.add(new NodoFlujo(ciudadOrigen, Double.MAX_VALUE));

        while (!colaPrioridad.isEmpty()) {
            NodoFlujo actual = colaPrioridad.poll();
            Ciudad u = actual.ciudad;

            if (actual.capacidadCamino < capacidades.get(u)) {
                continue;
            }

            List<Tramo> tramosVecinos = adyacencias.get(u);
            if (tramosVecinos != null) {
                for (Tramo tramo : tramosVecinos) {
                    Ciudad v = tramo.getDestino();
                    double capacidadTentativa = Math.min(capacidades.get(u), tramo.getCarriles());

                    if (capacidadTentativa > capacidades.get(v)) {
                        capacidades.put(v, capacidadTentativa);
                        padres.put(v, u);
                        colaPrioridad.add(new NodoFlujo(v, capacidadTentativa));
                    }
                }
            }
        }

        System.out.println("=== RUTAS CON MAYOR CAPACIDAD DE FLUJO DESDE: " + origen + " ===");
        for (Ciudad destino : adyacencias.keySet()) {
            if (destino.equals(ciudadOrigen)) {
                continue;
            }

            double cap = capacidades.get(destino);
            if (cap == 0.0) {
                System.out.println("A " + destino.getNombre() + ": Inalcanzable");
            } else {
                List<String> camino = new ArrayList<>();
                Ciudad aux = destino;
                while (aux != null) {
                    camino.add(0, aux.getNombre());
                    aux = padres.get(aux);
                }
                System.out.println("A " + destino.getNombre() + ":");
                System.out.println("  Camino: " + String.join(" -> ", camino));
                System.out.println("  Capacidad máxima (cuello de botella): " + (int) cap + " carriles");
            }
        }
        System.out.println("=========================================================");
    }
}
