package tienda.modelo;

/** Clase base (herencia) para Cliente y Empleado. */
public abstract class Persona {
    protected final String dpi;
    protected final String nombre;
    protected final String direccion;

    protected Persona(String dpi, String nombre, String direccion) {
        this.dpi = dpi;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public String getDpi() { return dpi; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
}
