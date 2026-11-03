package tienda.modelo;

public class Bodega {
    private String id;
    private Empleado encargado; // una bodega TIENE un encargado
    private int capacidad;
    private String direccion;

    public Bodega(String id, Empleado encargado, int capacidad, String direccion) {
        this.id = id;
        this.encargado = encargado;
        this.capacidad = capacidad;
        this.direccion = direccion;
    }

    public String getId() {
        return id;
    }

    public Empleado getEncargado() {
        return encargado;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public String getDireccion() {
        return direccion;
    }

    @Override
    public String toString() {
        return "Bodega " + id;
    }
}
