package tienda.vista;

import tienda.modelo.Producto;
import tienda.servicio.SistemaTienda;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PanelProductos extends PanelBase {
    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtDescripcion = new JTextField();
    private final JTextField txtMarca = new JTextField();
    private final JTextField txtPrecio = new JTextField();
    private final DefaultTableModel modelo = modeloTabla("Código", "Nombre", "Descripción", "Marca", "Precio (Q)");

    public PanelProductos(SistemaTienda sistema) {
        super(sistema);
        add(formulario(new String[]{"Código:", "Nombre:", "Descripción:", "Marca:", "Precio (Q):"},
                new JComponent[]{txtCodigo, txtNombre, txtDescripcion, txtMarca, txtPrecio}), java.awt.BorderLayout.NORTH);
        add(new JScrollPane(new JTable(modelo)), java.awt.BorderLayout.CENTER);

        JButton btnGuardar = new JButton("Registrar producto");
        btnGuardar.addActionListener(e -> registrar());
        add(botonera(btnGuardar), java.awt.BorderLayout.SOUTH);
        refrescar();
    }

    private void registrar() {
        try {
            Producto p = new Producto(texto(txtCodigo, "Código"), texto(txtNombre, "Nombre"),
                    texto(txtDescripcion, "Descripción"), texto(txtMarca, "Marca"), decimal(txtPrecio, "Precio"));
            sistema.registrarProducto(p);
            limpiar(txtCodigo, txtNombre, txtDescripcion, txtMarca, txtPrecio);
            refrescar();
            info("Producto registrado correctamente.");
        } catch (RuntimeException ex) {
            error(ex.getMessage());
        }
    }

    @Override
    public void refrescar() {
        modelo.setRowCount(0);
        for (Producto p : sistema.getProductos()) {
            modelo.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getDescripcion(), p.getMarca(),
                    String.format("%.2f", p.getPrecio())});
        }
    }
}
