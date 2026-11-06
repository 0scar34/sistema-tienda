package tienda.modelo;

import java.util.Objects;

public class Producto implements Persistible {
    private final String codigo;
    private final String nombre;
    private final String descripcion;
    private final String marca;
    private final double precio;

    public Producto(String codigo, String nombre, String descripcion, String marca, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.marca = marca;
        this.precio = precio;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getMarca() { return marca; }
    public double getPrecio() { return precio; }

    @Override
    public String aLinea() {
        return String.join("|", codigo, nombre, descripcion, marca, String.valueOf(precio));
    }

    public static Producto deLinea(String linea) {
        String[] p = linea.split("\\|", -1);
        return new Producto(p[0], p[1], p[2], p[3], Double.parseDouble(p[4]));
    }

    @Override public boolean equals(Object o) {
        return o instanceof Producto && ((Producto) o).codigo.equals(codigo);
    }
    @Override public int hashCode() { return Objects.hash(codigo); }
    @Override public String toString() { return codigo + " - " + nombre; }
}
