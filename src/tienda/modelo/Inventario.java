package tienda.modelo;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/** Un inventario guarda cuántas unidades (stock) hay de cada producto en una bodega. */
public class Inventario implements Persistible {
    private final String id;
    private final Bodega bodega;
    private final Map<Producto, Integer> stock = new LinkedHashMap<>();

    public Inventario(String id, Bodega bodega) {
        this.id = id;
        this.bodega = bodega;
    }

    public String getId() { return id; }
    public Bodega getBodega() { return bodega; }
    public Map<Producto, Integer> getStock() { return stock; }

    public int stockDe(Producto p) { return stock.getOrDefault(p, 0); }

    public void agregar(Producto p, int cantidad) { stock.merge(p, cantidad, Integer::sum); }

    public void descontar(Producto p, int cantidad) {
        if (stockDe(p) < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente de " + p.getNombre());
        }
        stock.put(p, stockDe(p) - cantidad);
    }

    public int totalUnidades() {
        return stock.values().stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public String aLinea() {
        String items = stock.entrySet().stream()
                .map(e -> e.getKey().getCodigo() + ":" + e.getValue())
                .collect(Collectors.joining(";"));
        return id + "|" + bodega.getId() + "|" + items;
    }

    @Override public String toString() { return "Inventario " + id + " (" + bodega + ")"; }
}
