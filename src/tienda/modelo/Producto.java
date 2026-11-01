package tienda.modelo;

public class Producto {
    // atributos dentro de nuestra clase producto
    private String codigo;
    private String nombre;
    private String descripcion;
    private String marca;
    private double precio;
    // private significa que solo la propia clase puede tocar esos dates
// aqui usamos un constructor, nuestro constructor se ejecuta cuando hacemos nueva producto
    public Producto(String codigo, String nombre, String descripcion, String marca, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.marca = marca;
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public double getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
    
}
