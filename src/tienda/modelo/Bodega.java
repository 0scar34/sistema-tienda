package tienda.modelo;

public class Bodega implements Persistible {
    private final String id;
    private final Empleado encargado;
    private final int capacidad;
    private final String direccion;

    public Bodega(String id, Empleado encargado, int capacidad, String direccion) {
        this.id = id;
        this.encargado = encargado;
        this.capacidad = capacidad;
        this.direccion = direccion;
    }

    public String getId() { return id; }
    public Empleado getEncargado() { return encargado; }
    public int getCapacidad() { return capacidad; }
    public String getDireccion() { return direccion; }

    @Override
    public String aLinea() {
        return String.join("|", id, encargado.getId(), String.valueOf(capacidad), direccion);
    }

    @Override public String toString() { return "Bodega " + id; }
}
