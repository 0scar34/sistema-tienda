package tienda.modelo;

public abstract class Persona {
    protected String dpi;
    protected String nombre;
    protected String direccion;

    // nuestro constructor de Persona
    public Persona(String dpi, String nombre, String direccion) {
        this.dpi = dpi;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    // nuestros getters para los tres
    // protected es como private, pero deja que las clases hijas usen el atributo
    public String getDpi() {
        return dpi;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }
}
