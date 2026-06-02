package parcial.vamos_de_paso;

import java.util.Scanner;

/**
 * Clase principal que contiene el menú interactivo para interactuar con el
 * grafo de ciudades.
 * Implementación correspondiente al Integrante 1.
 */
public class Main {

    public static void main(String[] args) {
        Grafo grafo = new Grafo();
        Scanner scanner = new Scanner(System.in);

        // Carga de datos de prueba predefinidos para facilitar la demostración
        cargarDatosDePrueba(grafo);

        boolean continuar = true;
        while (continuar) {
            System.out.println("\n=============================================");
            System.out.println("          MENÚ PRINCIPAL - VAMOS DE PASEO    ");
            System.out.println("=============================================");
            System.out.println("1. Agregar ciudad");
            System.out.println("2. Agregar conexión (tramo)");
            System.out.println("3. Mostrar grafo");
            System.out.println("4. Consultar ruta más corta (Dijkstra)");
            System.out.println("5. Consultar ruta con mayor flujo (Widest Path)");
            System.out.println("6. Consultar rutas desde origen (distancia y flujo)");
            System.out.println("7. Salir");
            System.out.println("=============================================");
            System.out.print("Seleccione una opción: ");

            String opcionStr = scanner.nextLine().trim();
            switch (opcionStr) {
                case "1":
                    agregarCiudadMenu(grafo, scanner);
                    break;
                case "2":
                    agregarConexionMenu(grafo, scanner);
                    break;
                case "3":
                    grafo.mostrarGrafo();
                    break;
                case "4":
                    consultarRutaMasCortaMenu(grafo, scanner);
                    break;
                case "5":
                    consultarRutaMayorFlujoMenu(grafo, scanner);
                    break;
                case "6":
                    consultarDesdeCiudadHaciaTodas(grafo, scanner);
                    break;
                case "7":
                    System.out.println("\n¡Gracias por utilizar Vamos de Paseo! ¡Buen viaje!");
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
            }
        }
        scanner.close();
    }

    /**
     * Carga ciudades y conexiones viales reales en el grafo de prueba.
     */
    private static void cargarDatosDePrueba(Grafo grafo) {
        System.out.println("Cargando ciudades y tramos de prueba (Red Vial Argentina)...");
        grafo.agregarTramo("Buenos Aires", "Rosario", 300.0, 4);
        grafo.agregarTramo("Buenos Aires", "San Luis", 800.0, 2);
        grafo.agregarTramo("Rosario", "Cordoba", 400.0, 3);
        grafo.agregarTramo("Rosario", "Santa Fe", 170.0, 2);
        grafo.agregarTramo("Cordoba", "Mendoza", 600.0, 2);
        grafo.agregarTramo("San Luis", "Mendoza", 260.0, 2);
        grafo.agregarTramo("Santa Fe", "Cordoba", 350.0, 1);
        grafo.agregarTramo("Cordoba", "San Luis", 400.0, 2);
        System.out.println("Datos cargados con éxito.");
    }

    /**
     * Menú para agregar una ciudad al grafo.
     */
    private static void agregarCiudadMenu(Grafo grafo, Scanner scanner) {
        System.out.println("\n--- AGREGAR CIUDAD ---");
        System.out.print("Ingrese el nombre de la ciudad: ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("El nombre de la ciudad no puede estar vacío.");
            return;
        }
        if (grafo.getCiudades().containsKey(nombre)) {
            System.out.println("La ciudad \"" + nombre + "\" ya existe en el grafo.");
        } else {
            grafo.agregarCiudad(nombre);
            System.out.println("Ciudad \"" + nombre + "\" agregada con éxito.");
        }
    }

    /**
     * Menú para agregar una conexión (tramo) entre dos ciudades.
     */
    private static void agregarConexionMenu(Grafo grafo, Scanner scanner) {
        System.out.println("\n--- AGREGAR CONEXIÓN ---");
        System.out.print("Ingrese ciudad origen: ");
        String origen = scanner.nextLine().trim();
        System.out.print("Ingrese ciudad destino: ");
        String destino = scanner.nextLine().trim();

        if (origen.isEmpty() || destino.isEmpty()) {
            System.out.println("Las ciudades no pueden estar vacías.");
            return;
        }
        if (origen.equalsIgnoreCase(destino)) {
            System.out.println("La ciudad origen y destino deben ser distintas.");
            return;
        }

        double distancia = leerDouble(scanner, "Ingrese la distancia en km: ");
        if (distancia <= 0) {
            System.out.println("La distancia debe ser un número positivo.");
            return;
        }

        int carriles = leerEntero(scanner, "Ingrese la cantidad de carriles: ");
        if (carriles <= 0) {
            System.out.println("La cantidad de carriles debe ser un número entero positivo.");
            return;
        }

        grafo.agregarTramo(origen, destino, distancia, carriles);
        System.out.println("Conexión entre \"" + origen + "\" y \"" + destino + "\" agregada con éxito.");
    }

    /**
     * Menú para buscar y mostrar la ruta más corta entre dos ciudades.
     */
    private static void consultarRutaMasCortaMenu(Grafo grafo, Scanner scanner) {
        System.out.println("\n--- CONSULTAR RUTA MÁS CORTA ---");
        System.out.print("Ingrese ciudad de origen: ");
        String origen = scanner.nextLine().trim();
        System.out.print("Ingrese ciudad de destino: ");
        String destino = scanner.nextLine().trim();

        if (origen.isEmpty() || destino.isEmpty()) {
            System.out.println("Las ciudades origen y destino no pueden estar vacías.");
            return;
        }

        grafo.rutaMasCorta(origen, destino);
    }

    /**
     * Menú para buscar y mostrar la ruta con mayor capacidad de flujo (carriles)
     * entre dos ciudades.
     */
    private static void consultarRutaMayorFlujoMenu(Grafo grafo, Scanner scanner) {
        System.out.println("\n--- CONSULTAR RUTA CON MAYOR FLUJO ---");
        System.out.print("Ingrese ciudad de origen: ");
        String origen = scanner.nextLine().trim();
        System.out.print("Ingrese ciudad de destino: ");
        String destino = scanner.nextLine().trim();

        if (origen.isEmpty() || destino.isEmpty()) {
            System.out.println("Las ciudades origen y destino no pueden estar vacías.");
            return;
        }

        grafo.rutaMayorFlujo(origen, destino);
    }

    /**
     * Menú para calcular las rutas más cortas y con mayor capacidad de flujo
     * desde una ciudad origen a todas las demás del grafo.
     */
    private static void consultarDesdeCiudadHaciaTodas(Grafo grafo, Scanner scanner) {
        System.out.println("\n--- CONSULTAR RUTAS DESDE ORIGEN (HACIA TODOS LOS DESTINOS) ---");
        System.out.print("Ingrese la ciudad origen: ");
        String origen = scanner.nextLine().trim();

        if (origen.isEmpty()) {
            System.out.println("La ciudad de origen no puede estar vacía.");
            return;
        }

        // 1. Mostrar rutas más cortas (distancias mínimas)
        grafo.rutaMasCortaDesdeOrigen(origen);
        System.out.println();
        // 2. Mostrar rutas con mayor flujo (capacidades máximas)
        grafo.rutaMayorFlujoDesdeOrigen(origen);
    }

    /**
     * Lee un decimal de la consola de forma robusta.
     */
    private static double leerDouble(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, ingrese un número decimal válido.");
            }
        }
    }

    /**
     * Lee un entero de la consola de forma robusta.
     */
    private static int leerEntero(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, ingrese un número entero válido.");
            }
        }
    }
}
