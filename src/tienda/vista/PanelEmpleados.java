package tienda.vista;
import tienda.modelo.Empleado;
import tienda.servicio.SistemaTienda;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;

public class PanelEmpleados extends PanelBase {
    private final JTextField txtId = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtDpi = new JTextField();
    private final JTextField txtDireccion = new JTextField();
    private final JTextField txtPuesto = new JTextField();
    private final JTextField txtSalario = new JTextField();
    private final DefaultTableModel modelo = modeloTabla("ID", "Nombre", "DPI", "Dirección", "Puesto", "Salario (Q)");

    public PanelEmpleados(SistemaTienda sistema) {
        super(sistema);
        add(formulario(new String[]{"ID empleado:", "Nombre:", "DPI:", "Dirección:", "Puesto:", "Salario (Q):"},
                new JComponent[]{txtId, txtNombre, txtDpi, txtDireccion, txtPuesto, txtSalario}), BorderLayout.NORTH);
        add(new JScrollPane(new JTable(modelo)), BorderLayout.CENTER);

        JButton btnGuardar = new JButton("Registrar empleado");
        btnGuardar.addActionListener(e -> registrar());
        add(botonera(btnGuardar), BorderLayout.SOUTH);
        refrescar();
    }

    private void registrar() {
        try {
            Empleado emp = new Empleado(texto(txtId, "ID empleado"), texto(txtNombre, "Nombre"), texto(txtDpi, "DPI"),
                    texto(txtDireccion, "Dirección"), texto(txtPuesto, "Puesto"), decimal(txtSalario, "Salario"));
            sistema.registrarEmpleado(emp);
            limpiar(txtId, txtNombre, txtDpi, txtDireccion, txtPuesto, txtSalario);
            refrescar();
            info("Empleado registrado correctamente.");
        } catch (RuntimeException ex) {
            error(ex.getMessage());
        }
    }

    @Override
    public void refrescar() {
        modelo.setRowCount(0);
        for (Empleado e : sistema.getEmpleados()) {
            modelo.addRow(new Object[]{e.getId(), e.getNombre(), e.getDpi(), e.getDireccion(), e.getPuesto(),
                    String.format("%.2f", e.getSalario())});
        }
    }
}