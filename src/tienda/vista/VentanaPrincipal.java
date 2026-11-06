package tienda.vista;

import tienda.servicio.SistemaTienda;

import javax.swing.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal(SistemaTienda sistema) {
        setTitle("Sistema de Tienda - Gestión de ventas e inventario");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Productos", new PanelProductos(sistema));
        pestanas.addTab("Clientes", new PanelClientes(sistema));
        pestanas.addTab("Empleados", new PanelEmpleados(sistema));
        pestanas.addTab("Bodegas", new PanelBodegas(sistema));
        pestanas.addTab("Inventarios", new PanelInventarios(sistema));
        pestanas.addTab("Órdenes de compra", new PanelOrdenes(sistema));

        // Al cambiar de pestaña se actualizan las listas desplegables y tablas
        pestanas.addChangeListener(e -> ((PanelBase) pestanas.getSelectedComponent()).refrescar());
        add(pestanas);
    }
}
