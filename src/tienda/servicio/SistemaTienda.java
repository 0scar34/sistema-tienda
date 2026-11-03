package tienda.servicio;

import tienda.modelo.*;
import tienda.persistencia.ArchivoDatos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase central: guarda todas las listas, aplica las reglas del negocio
 * y se encarga de cargar/guardar los archivos.
 */
public class SistemaTienda {
    private static final String DIR = "datos/";

    private final List<Producto> productos = new ArrayList<>();
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Empleado> empleados = new ArrayList<>();
    private final List<Bodega> bodegas = new ArrayList<>();
    private final List<Inventario> inventarios = new ArrayList<>();
    private final List<OrdenCompra> ordenes = new ArrayList<>();

    public SistemaTienda() {
        cargar();
    }

    public List<Producto> getProductos() { return productos; }
    public List<Cliente> getClientes() { return clientes; }
    public List<Empleado> getEmpleados() { return empleados; }
    public List<Bodega> getBodegas() { return bodegas; }
    public List<Inventario> getInventarios() { return inventarios; }
    public List<OrdenCompra> getOrdenes() { return ordenes; }

    // ------------------------------------------------------------------ búsquedas
    public Producto buscarProducto(String codigo) {
        return productos.stream().filter(p -> p.getCodigo().equals(codigo)).findFirst().orElse(null);
    }
    public Cliente buscarCliente(String dpi) {
        return clientes.stream().filter(c -> c.getDpi().equals(dpi)).findFirst().orElse(null);
    }
    public Empleado buscarEmpleado(String id) {
        return empleados.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }
    public Bodega buscarBodega(String id) {
        return bodegas.stream().filter(b -> b.getId().equals(id)).findFirst().orElse(null);
    }
    public Inventario buscarInventario(String id) {
        return inventarios.stream().filter(i -> i.getId().equals(id)).findFirst().orElse(null);
    }
    public OrdenCompra buscarOrden(int id) {
        return ordenes.stream().filter(o -> o.getId() == id).findFirst().orElse(null);
    }

    // ------------------------------------------------------------------ registros
    public void registrarProducto(Producto p) {
        if (!p.getCodigo().matches("[A-Za-z0-9_-]+")) {
            throw new IllegalArgumentException("El código solo puede tener letras, números, guion y guion bajo.");
        }
        if (buscarProducto(p.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un producto con el código " + p.getCodigo());
        }
        if (p.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }
        productos.add(p);
        guardar("productos.txt", productos);
    }

    public void registrarCliente(Cliente c) {
        if (buscarCliente(c.getDpi()) != null) {
            throw new IllegalArgumentException("Ya existe un cliente con el DPI " + c.getDpi());
        }
        if (c.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
        }
        clientes.add(c);
        guardar("clientes.txt", clientes);
    }

    public void registrarEmpleado(Empleado e) {
        if (buscarEmpleado(e.getId()) != null) {
            throw new IllegalArgumentException("Ya existe un empleado con el ID " + e.getId());
        }
        if (e.getSalario() <= 0) {
            throw new IllegalArgumentException("El salario debe ser mayor que cero.");
        }
        empleados.add(e);
        guardar("empleados.txt", empleados);
    }

    public void registrarBodega(Bodega b) {
        if (buscarBodega(b.getId()) != null) {
            throw new IllegalArgumentException("Ya existe una bodega con el ID " + b.getId());
        }
        if (b.getEncargado() == null) {
            throw new IllegalArgumentException("Debe seleccionar un empleado encargado (registre uno primero).");
        }
        if (b.getCapacidad() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero.");
        }
        bodegas.add(b);
        guardar("bodegas.txt", bodegas);
    }

    public void registrarInventario(Inventario inv) {
        if (buscarInventario(inv.getId()) != null) {
            throw new IllegalArgumentException("Ya existe un inventario con el ID " + inv.getId());
        }
        if (inv.getBodega() == null) {
            throw new IllegalArgumentException("Debe seleccionar una bodega (registre una primero).");
        }
        inventarios.add(inv);
        guardar("inventarios.txt", inventarios);
    }

    /** Suma unidades de un producto a un inventario, respetando la capacidad de la bodega. */
    public void agregarStock(Inventario inv, Producto p, int cantidad) {
        if (inv == null || p == null) {
            throw new IllegalArgumentException("Seleccione un inventario y un producto.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        Bodega bodega = inv.getBodega();
        if (unidadesEnBodega(bodega) + cantidad > bodega.getCapacidad()) {
            throw new IllegalArgumentException("La bodega " + bodega.getId() + " no tiene capacidad suficiente. "
                    + "Ocupado: " + unidadesEnBodega(bodega) + " de " + bodega.getCapacidad());
        }
        inv.agregar(p, cantidad);
        guardar("inventarios.txt", inventarios);
    }

    public int unidadesEnBodega(Bodega b) {
        return inventarios.stream()
                .filter(i -> i.getBodega().getId().equals(b.getId()))
                .mapToInt(Inventario::totalUnidades).sum();
    }

    public OrdenCompra registrarOrden(Cliente cliente, List<LineaOrden> lineas) {
        if (cliente == null) {
            throw new IllegalArgumentException("Seleccione un cliente (registre uno primero).");
        }
        if (lineas.isEmpty()) {
            throw new IllegalArgumentException("La orden debe tener al menos un producto.");
        }
        int id = ordenes.stream().mapToInt(OrdenCompra::getId).max().orElse(0) + 1;
        OrdenCompra orden = new OrdenCompra(id, cliente, new ArrayList<>(lineas), LocalDate.now(), EstadoOrden.PENDIENTE);
        ordenes.add(orden);
        guardar("ordenes.txt", ordenes);
        return orden;
    }

    /** Pasa la orden a COMPLETADA y resta del inventario cada producto vendido. */
    public void completarOrden(OrdenCompra orden, Inventario inv) {
        if (orden.getEstado() != EstadoOrden.PENDIENTE) {
            throw new IllegalArgumentException("Solo se puede completar una orden PENDIENTE.");
        }
        if (inv == null) {
            throw new IllegalArgumentException("Seleccione el inventario del que se descontará el stock.");
        }
        // 1) validar TODO antes de modificar nada
        for (LineaOrden l : orden.getLineas()) {
            int disponible = inv.stockDe(l.getProducto());
            if (disponible < l.getCantidad()) {
                throw new IllegalArgumentException("Stock insuficiente de " + l.getProducto().getNombre()
                        + " (pedido: " + l.getCantidad() + ", disponible: " + disponible + ")");
            }
        }
        // 2) descontar
        for (LineaOrden l : orden.getLineas()) {
            inv.descontar(l.getProducto(), l.getCantidad());
        }
        orden.setEstado(EstadoOrden.COMPLETADA);
        guardar("inventarios.txt", inventarios);
        guardar("ordenes.txt", ordenes);
    }

    public void cancelarOrden(OrdenCompra orden) {
        if (orden.getEstado() != EstadoOrden.PENDIENTE) {
            throw new IllegalArgumentException("Solo se puede cancelar una orden PENDIENTE.");
        }
        orden.setEstado(EstadoOrden.CANCELADA);
        guardar("ordenes.txt", ordenes);
    }

    // ------------------------------------------------------------------ archivos
    private void guardar(String archivo, List<? extends Persistible> datos) {
        List<String> lineas = datos.stream().map(Persistible::aLinea).collect(Collectors.toList());
        ArchivoDatos.escribir(DIR + archivo, lineas);
    }

    /** Se cargan en orden porque unos datos dependen de otros (bodega -> empleado, etc.). */
    private void cargar() {
        for (String l : ArchivoDatos.leer(DIR + "productos.txt")) productos.add(Producto.deLinea(l));
        for (String l : ArchivoDatos.leer(DIR + "clientes.txt")) clientes.add(Cliente.deLinea(l));
        for (String l : ArchivoDatos.leer(DIR + "empleados.txt")) empleados.add(Empleado.deLinea(l));

        for (String l : ArchivoDatos.leer(DIR + "bodegas.txt")) {
            String[] p = l.split("\\|", -1);
            bodegas.add(new Bodega(p[0], buscarEmpleado(p[1]), Integer.parseInt(p[2]), p[3]));
        }
        for (String l : ArchivoDatos.leer(DIR + "inventarios.txt")) {
            String[] p = l.split("\\|", -1);
            Inventario inv = new Inventario(p[0], buscarBodega(p[1]));
            if (!p[2].isEmpty()) {
                for (String par : p[2].split(";")) {
                    String[] c = par.split(":");
                    inv.agregar(buscarProducto(c[0]), Integer.parseInt(c[1]));
                }
            }
            inventarios.add(inv);
        }
        for (String l : ArchivoDatos.leer(DIR + "ordenes.txt")) {
            String[] p = l.split("\\|", -1);
            List<LineaOrden> lineas = new ArrayList<>();
            for (String par : p[5].split(";")) {
                String[] c = par.split(":");
                lineas.add(new LineaOrden(buscarProducto(c[0]), Integer.parseInt(c[1]), Double.parseDouble(c[2])));
            }
            ordenes.add(new OrdenCompra(Integer.parseInt(p[0]), buscarCliente(p[1]), lineas,
                    LocalDate.parse(p[2]), EstadoOrden.valueOf(p[4])));
        }
    }
}
