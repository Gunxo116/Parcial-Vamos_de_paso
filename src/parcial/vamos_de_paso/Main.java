package parcial.vamos_de_paso;

public class Main {

    public static void main(String[] args) {
        // Creamos una instancia de nuestro Grafo
        Grafo grafo = new Grafo();

        System.out.println("--- Cargando ciudades y tramos de prueba ---");
        // Agregamos tramos (las ciudades se agregan automáticamente)
        // Usamos ciudades y distancias reales/aproximadas en Argentina para que tenga sentido
        grafo.agregarTramo("Buenos Aires", "Rosario", 300.0, 4);
        grafo.agregarTramo("Buenos Aires", "San Luis", 800.0, 2);
        grafo.agregarTramo("Rosario", "Cordoba", 400.0, 3);
        grafo.agregarTramo("Rosario", "Santa Fe", 170.0, 2);
        grafo.agregarTramo("Cordoba", "Mendoza", 600.0, 2);
        grafo.agregarTramo("San Luis", "Mendoza", 260.0, 2);
        grafo.agregarTramo("Santa Fe", "Cordoba", 350.0, 1);
        grafo.agregarTramo("Cordoba", "San Luis", 400.0, 2);

        // Mostramos cómo quedó estructurado el grafo
        grafo.mostrarGrafo();
        System.out.println();

        System.out.println("--- PRUEBA 1: Ruta más corta entre dos ciudades (Dijkstra Clásico) ---");
        // Ruta Buenos Aires -> Mendoza
        grafo.rutaMasCorta("Buenos Aires", "Mendoza");
        System.out.println();

        // Ruta Santa Fe -> San Luis
        grafo.rutaMasCorta("Santa Fe", "San Luis");
        System.out.println();

        System.out.println("--- PRUEBA 2: Ruta más corta desde un origen hacia todos los destinos ---");
        // Rutas desde Buenos Aires
        grafo.rutaMasCortaDesdeOrigen("Buenos Aires");
    }

}
