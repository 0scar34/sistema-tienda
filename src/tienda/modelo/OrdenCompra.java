package tienda.modelo;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class OrdenCompra implements Persistible {
    private final int id;
    private final Cliente cliente;
    private final List<LineaOrden> lineas;
    private final LocalDate fecha;
    private EstadoOrden estado;

    public OrdenCompra(int id, Cliente cliente, List<LineaOrden> lineas, LocalDate fecha, EstadoOrden estado) {
        this.id = id;
        this.cliente = cliente;
        this.lineas = lineas;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public List<LineaOrden> getLineas() { return lineas; }
    public LocalDate getFecha() { return fecha; }
    public EstadoOrden getEstado() { return estado; }
    public void setEstado(EstadoOrden estado) { this.estado = estado; }

    public double total() {
        return lineas.stream().mapToDouble(LineaOrden::subtotal).sum();
    }

    @Override
    public String aLinea() {
        String detalle = lineas.stream().map(LineaOrden::aLinea).collect(Collectors.joining(";"));
        return String.join("|", String.valueOf(id), cliente.getDpi(), fecha.toString(),
                String.valueOf(total()), estado.name(), detalle);
    }
}
