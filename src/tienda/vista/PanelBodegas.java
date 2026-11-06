package tienda.vista;

import tienda.modelo.Bodega;
import tienda.modelo.Empleado;
import tienda.servicio.SistemaTienda;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;

public class PanelBodegas extends PanelBase {
    private final JTextField txtId = new JTextField();
    private final JComboBox<Empleado> cmbEmpleado = new JComboBox<>();
    private final JTextField txtCapacidad = new JTextField();
    private final JTextField txtDireccion = new JTextField();
    private final DefaultTableModel modelo = modeloTabla("ID", "Encargado", "Capacidad (unidades)", "Dirección");

    public PanelBodegas(SistemaTienda sistema) {
        super(sistema);
        add(formulario(new String[]{"ID bodega:", "Empleado encargado:", "Capacidad (unidades):", "Dirección:"},
                new JComponent[]{txtId, cmbEmpleado, txtCapacidad, txtDireccion}), BorderLayout.NORTH);
        add(new JScrollPane(new JTable(modelo)), BorderLayout.CENTER);

        JButton btnGuardar = new JButton("Registrar bodega");
        btnGuardar.addActionListener(e -> registrar());
        add(botonera(btnGuardar), BorderLayout.SOUTH);
        refrescar();
    }

    private void registrar() {
        try {
            Bodega b = new Bodega(texto(txtId, "ID bodega"), (Empleado) cmbEmpleado.getSelectedItem(),
                    entero(txtCapacidad, "Capacidad"), texto(txtDireccion, "Dirección"));
            sistema.registrarBodega(b);
            limpiar(txtId, txtCapacidad, txtDireccion);
            refrescar();
            info("Bodega registrada correctamente.");
        } catch (RuntimeException ex) {
            error(ex.getMessage());
        }
    }

    @Override
    public void refrescar() {
        cmbEmpleado.removeAllItems();
        for (Empleado e : sistema.getEmpleados()) cmbEmpleado.addItem(e);
        modelo.setRowCount(0);
        for (Bodega b : sistema.getBodegas()) {
            modelo.addRow(new Object[]{b.getId(), b.getEncargado(), b.getCapacidad(), b.getDireccion()});
        }
    }
}
