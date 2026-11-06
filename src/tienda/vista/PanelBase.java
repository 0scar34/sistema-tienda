package tienda.vista;

import tienda.servicio.SistemaTienda;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/** Clase base de todas las pestañas: trae utilidades comunes para formularios y validación. */
public abstract class PanelBase extends JPanel {
    protected final SistemaTienda sistema;

    protected PanelBase(SistemaTienda sistema) {
        this.sistema = sistema;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /** Se llama al abrir la pestaña para actualizar tablas y listas desplegables. */
    public abstract void refrescar();

    protected JPanel formulario(String[] etiquetas, JComponent[] campos) {
        JPanel panel = new JPanel(new GridLayout(etiquetas.length, 2, 6, 6));
        for (int i = 0; i < etiquetas.length; i++) {
            panel.add(new JLabel(etiquetas[i]));
            panel.add(campos[i]);
        }
        return panel;
    }

    protected JPanel botonera(JButton... botones) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        for (JButton b : botones) panel.add(b);
        return panel;
    }

    protected DefaultTableModel modeloTabla(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
        };
    }

    protected void error(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected void info(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    // ---- lectura y validación de campos ----
    protected String texto(JTextField campo, String nombre) {
        String t = campo.getText().trim();
        if (t.isEmpty()) throw new IllegalArgumentException("El campo \"" + nombre + "\" es obligatorio.");
        if (t.contains("|")) throw new IllegalArgumentException("El campo \"" + nombre + "\" no puede contener el símbolo |");
        return t;
    }

    protected double decimal(JTextField campo, String nombre) {
        try {
            return Double.parseDouble(texto(campo, nombre));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo \"" + nombre + "\" debe ser un número (ej. 25.50).");
        }
    }

    protected int entero(JTextField campo, String nombre) {
        try {
            return Integer.parseInt(texto(campo, nombre));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo \"" + nombre + "\" debe ser un número entero.");
        }
    }

    protected LocalDate fecha(JTextField campo, String nombre) {
        try {
            return LocalDate.parse(texto(campo, nombre));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("El campo \"" + nombre + "\" debe tener formato AAAA-MM-DD (ej. 1995-03-27).");
        }
    }

    protected void limpiar(JTextField... campos) {
        for (JTextField c : campos) c.setText("");
    }
}
