package parcial.vamos_de_paso;

import java.util.Objects;

/**
 * Clase que representa a una Ciudad (vértice del grafo).
 */
public class Ciudad {
    private String nombre;

    // Constructor para crear una ciudad con su nombre
    public Ciudad(String nombre) {
        this.nombre = nombre;
    }

    // Getter para obtener el nombre
    public String getNombre() {
        return nombre;
    }

    // Setter para cambiar el nombre si es necesario
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Sobrescribimos equals y hashCode para comparar ciudades por su nombre
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ciudad ciudad = (Ciudad) o;
        return Objects.equals(nombre, ciudad.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    // Para poder mostrar la ciudad de forma bonita por consola
    @Override
    public String toString() {
        return nombre;
    }
}
