package tienda.modelo;

import java.util.LinkedHashMap;
import java.util.Map;

public class Inventario {
    private String id;
    private Bodega bodega;

    private Map<Producto, Integer> stock = new LinkedHashMap<>();

    public Inventario(String id, Bodega bodega) {
        this.id = id;
        this.bodega = bodega;
    }

    public String getId() {
        return id;
    }

    public Bodega getBodega() {
        return bodega;
    }

    public Map<Producto, Integer> getStock() {
        return stock;
    }

    // cuantas unidades hay dentro de un producto
    public int stockDe(Producto p) {
        return stock.getOrDefault(p, 0);
    }

    // suma de unidades a nuestro stock
    public void agregar(Producto p, int cantidad) {
        stock.put(p, stockDe(p) + cantidad);
    }

    // resta de unidades (esto lo usamos al completar una venta
    public void descontar(Producto p, int cantidad) {
        if (stockDe(p) < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente de: " + p.getNombre());
        }
        stock.put(p, stockDe(p) - cantidad);
    }

    //suma de unidades, esto nos ayuda a evaluar la capacidad de la bodega
    public int totalUnidades() {
        int total = 0;
        for (int cantidad : stock.values()) {
            total += cantidad;

        }
        return total;
    }


    @Override
    public String toString() {
        return "Inventario " + id + " (" + bodega + ")";
    }
}
