package tienda.vista;

import tienda.modelo.Cliente;
import tienda.modelo.TipoCliente;
import tienda.servicio.SistemaTienda;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;

public class PanelClientes extends PanelBase {
    private final JTextField txtDpi = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtDireccion = new JTextField();
    private final JTextField txtFecha = new JTextField();
    private final JComboBox<TipoCliente> cmbTipo = new JComboBox<>(TipoCliente.values());
    private final DefaultTableModel modelo = modeloTabla("DPI", "Nombre", "Dirección", "Fecha nacimiento", "Tipo");

    public PanelClientes(SistemaTienda sistema) {
        super(sistema);
        add(formulario(new String[]{"DPI:", "Nombre:", "Dirección:", "Fecha de nacimiento (AAAA-MM-DD):", "Tipo:"},
                new JComponent[]{txtDpi, txtNombre, txtDireccion, txtFecha, cmbTipo}), BorderLayout.NORTH);
        add(new JScrollPane(new JTable(modelo)), BorderLayout.CENTER);

        JButton btnGuardar = new JButton("Registrar cliente");
        btnGuardar.addActionListener(e -> registrar());
        add(botonera(btnGuardar), BorderLayout.SOUTH);
        refrescar();
    }

    private void registrar() {
        try {
            Cliente c = new Cliente(texto(txtDpi, "DPI"), texto(txtNombre, "Nombre"), texto(txtDireccion, "Dirección"),
                    fecha(txtFecha, "Fecha de nacimiento"), (TipoCliente) cmbTipo.getSelectedItem());
            sistema.registrarCliente(c);
            limpiar(txtDpi, txtNombre, txtDireccion, txtFecha);
            refrescar();
            info("Cliente registrado correctamente.");
        } catch (RuntimeException ex) {
            error(ex.getMessage());
        }
    }

    @Override
    public void refrescar() {
        modelo.setRowCount(0);
        for (Cliente c : sistema.getClientes()) {
            modelo.addRow(new Object[]{c.getDpi(), c.getNombre(), c.getDireccion(), c.getFechaNacimiento(), c.getTipo()});
        }
    }
}
