package tienda.vista;
import tienda.modelo.Bodega;
import tienda.modelo.Inventario;
import tienda.modelo.Producto;
import tienda.servicio.SistemaTienda;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.Map;

public class PanelInventarios extends PanelBase {
    
    private final JTextField txtId = new JTextField();
    private final JComboBox<Bodega> cmbBodega = new JComboBox<>();

    private final JComboBox<Inventario> cmbInventario = new JComboBox<>();
    private final JComboBox<Producto> cmbProducto = new JComboBox<>();
    private final JTextField txtCantidad = new JTextField();

    private final DefaultTableModel modelo = modeloTabla("Inventario", "Bodega", "Producto", "Stock");

    public PanelInventarios(SistemaTienda sistema) {
        super(sistema);

        JButton btnCrear = new JButton("Crear inventario");
        btnCrear.addActionListener(e -> crearInventario());
        JButton btnStock = new JButton("Agregar stock");
        btnStock.addActionListener(e -> agregarStock());

        JPanel norte = new JPanel();
        norte.setLayout(new BoxLayout(norte, BoxLayout.Y_AXIS));
        norte.add(new JLabel("1) Crear un inventario en una bodega"));
        norte.add(formulario(new String[]{"ID inventario:", "Bodega:"}, new JComponent[]{txtId, cmbBodega}));
        norte.add(botonera(btnCrear));
        norte.add(new JLabel("2) Agregar productos (stock) a un inventario"));
        norte.add(formulario(new String[]{"Inventario:", "Producto:", "Cantidad:"},
                new JComponent[]{cmbInventario, cmbProducto, txtCantidad}));
        norte.add(botonera(btnStock));

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(new JTable(modelo)), BorderLayout.CENTER);
        refrescar();
    }

    private void crearInventario() {
        try {
            sistema.registrarInventario(new Inventario(texto(txtId, "ID inventario"), (Bodega) cmbBodega.getSelectedItem()));
            limpiar(txtId);
            refrescar();
            info("Inventario creado correctamente.");
        } catch (RuntimeException ex) {
            error(ex.getMessage());
        }
    }

    private void agregarStock() {
        try {
            sistema.agregarStock((Inventario) cmbInventario.getSelectedItem(),
                    (Producto) cmbProducto.getSelectedItem(), entero(txtCantidad, "Cantidad"));
            limpiar(txtCantidad);
            refrescar();
            info("Stock agregado correctamente.");
        } catch (RuntimeException ex) {
            error(ex.getMessage());
        }
    }

    @Override
    public void refrescar() {
        cmbBodega.removeAllItems();
        for (Bodega b : sistema.getBodegas()) cmbBodega.addItem(b);
        cmbInventario.removeAllItems();
        for (Inventario i : sistema.getInventarios()) cmbInventario.addItem(i);
        cmbProducto.removeAllItems();
        for (Producto p : sistema.getProductos()) cmbProducto.addItem(p);

        modelo.setRowCount(0);
        for (Inventario inv : sistema.getInventarios()) {
            for (Map.Entry<Producto, Integer> e : inv.getStock().entrySet()) {
                modelo.addRow(new Object[]{inv.getId(), inv.getBodega().getId(), e.getKey(), e.getValue()});
            }
        }
    }
}