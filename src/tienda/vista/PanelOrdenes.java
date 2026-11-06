package tienda.vista;

import tienda.modelo.*;
import tienda.servicio.SistemaTienda;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PanelOrdenes extends PanelBase {
    private final JComboBox<Cliente> cmbCliente = new JComboBox<>();
    private final JComboBox<Producto> cmbProducto = new JComboBox<>();
    private final JTextField txtCantidad = new JTextField();

    private final List<LineaOrden> lineasActuales = new ArrayList<>();
    private final DefaultTableModel modeloLineas = modeloTabla("Producto", "Cantidad", "Precio", "Subtotal");
    private final DefaultTableModel modeloOrdenes = modeloTabla("ID", "Cliente", "Fecha", "Total (Q)", "Estado", "Productos");
    private final JTable tablaOrdenes = new JTable(modeloOrdenes);

    public PanelOrdenes(SistemaTienda sistema) {
        super(sistema);

        JButton btnAgregar = new JButton("Agregar producto a la orden");
        btnAgregar.addActionListener(e -> agregarLinea());
        JButton btnRegistrar = new JButton("Registrar orden");
        btnRegistrar.addActionListener(e -> registrarOrden());

        JPanel nueva = new JPanel(new BorderLayout(5, 5));
        nueva.add(formulario(new String[]{"Cliente:", "Producto:", "Cantidad:"},
                new JComponent[]{cmbCliente, cmbProducto, txtCantidad}), BorderLayout.NORTH);
        JScrollPane scrollLineas = new JScrollPane(new JTable(modeloLineas));
        scrollLineas.setPreferredSize(new Dimension(100, 110));
        nueva.add(scrollLineas, BorderLayout.CENTER);
        nueva.add(botonera(btnAgregar, btnRegistrar), BorderLayout.SOUTH);
        nueva.setBorder(BorderFactory.createTitledBorder("Nueva orden"));

        JButton btnCompletar = new JButton("Completar orden seleccionada");
        btnCompletar.addActionListener(e -> completar());
        JButton btnCancelar = new JButton("Cancelar orden seleccionada");
        btnCancelar.addActionListener(e -> cancelar());

        add(nueva, BorderLayout.NORTH);
        add(new JScrollPane(tablaOrdenes), BorderLayout.CENTER);
        add(botonera(btnCompletar, btnCancelar), BorderLayout.SOUTH);
        refrescar();
    }

    private void agregarLinea() {
        try {
            Producto p = (Producto) cmbProducto.getSelectedItem();
            if (p == null) throw new IllegalArgumentException("Seleccione un producto (registre uno primero).");
            int cantidad = entero(txtCantidad, "Cantidad");
            if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
            lineasActuales.add(new LineaOrden(p, cantidad));
            limpiar(txtCantidad);
            mostrarLineas();
        } catch (RuntimeException ex) {
            error(ex.getMessage());
        }
    }

    private void registrarOrden() {
        try {
            OrdenCompra o = sistema.registrarOrden((Cliente) cmbCliente.getSelectedItem(), lineasActuales);
            lineasActuales.clear();
            mostrarLineas();
            refrescar();
            info("Orden #" + o.getId() + " registrada como PENDIENTE.");
        } catch (RuntimeException ex) {
            error(ex.getMessage());
        }
    }

    private OrdenCompra ordenSeleccionada() {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila < 0) throw new IllegalArgumentException("Seleccione una orden de la tabla.");
        return sistema.buscarOrden((Integer) modeloOrdenes.getValueAt(fila, 0));
    }

    private void completar() {
        try {
            OrdenCompra orden = ordenSeleccionada();
            List<Inventario> inventarios = sistema.getInventarios();
            if (inventarios.isEmpty()) throw new IllegalArgumentException("No hay inventarios registrados.");
            Inventario inv = (Inventario) JOptionPane.showInputDialog(this,
                    "Inventario del que se descontará el stock:", "Completar orden",
                    JOptionPane.QUESTION_MESSAGE, null, inventarios.toArray(), inventarios.get(0));
            if (inv == null) return; // el usuario canceló el diálogo
            sistema.completarOrden(orden, inv);
            refrescar();
            info("Orden #" + orden.getId() + " completada. El stock fue actualizado.");
        } catch (RuntimeException ex) {
            error(ex.getMessage());
        }
    }

    private void cancelar() {
        try {
            OrdenCompra orden = ordenSeleccionada();
            sistema.cancelarOrden(orden);
            refrescar();
            info("Orden #" + orden.getId() + " cancelada.");
        } catch (RuntimeException ex) {
            error(ex.getMessage());
        }
    }

    private void mostrarLineas() {
        modeloLineas.setRowCount(0);
        for (LineaOrden l : lineasActuales) {
            modeloLineas.addRow(new Object[]{l.getProducto(), l.getCantidad(),
                    String.format("%.2f", l.getPrecioUnitario()), String.format("%.2f", l.subtotal())});
        }
    }

    @Override
    public void refrescar() {
        cmbCliente.removeAllItems();
        for (Cliente c : sistema.getClientes()) cmbCliente.addItem(c);
        cmbProducto.removeAllItems();
        for (Producto p : sistema.getProductos()) cmbProducto.addItem(p);

        modeloOrdenes.setRowCount(0);
        for (OrdenCompra o : sistema.getOrdenes()) {
            String detalle = o.getLineas().stream()
                    .map(l -> l.getProducto().getNombre() + " x" + l.getCantidad())
                    .collect(Collectors.joining(", "));
            modeloOrdenes.addRow(new Object[]{o.getId(), o.getCliente().getNombre(), o.getFecha(),
                    String.format("%.2f", o.total()), o.getEstado(), detalle});
        }
    }
}
