package tienda.modelo;

/** Una línea de la orden: un producto, su cantidad y el precio al momento de la compra. */
public class LineaOrden {
    private final Producto producto;
    private final int cantidad;
    private final double precioUnitario;

    public LineaOrden(Producto producto, int cantidad) {
        this(producto, cantidad, producto.getPrecio());
    }

    public LineaOrden(Producto producto, int cantidad, double precioUnitario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public double subtotal() { return cantidad * precioUnitario; }

    public String aLinea() { return producto.getCodigo() + ":" + cantidad + ":" + precioUnitario; }
}
