package tienda.modelo;

public class LineaOrden {
    private Producto producto;
    private int cantidad;
    private double precioUnitario;

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public LineaOrden(Producto producto, int cantidad){
        this.producto= producto;
        this.cantidad= cantidad;
        this.precioUnitario= producto.getPrecio();;//No llega por parámetro: lo copia del producto en este momento

    }

}
