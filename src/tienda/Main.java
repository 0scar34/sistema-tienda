package tienda;

import tienda.modelo.Producto;
import tienda.modelo.TipoCliente;
import tienda.modelo.Cliente;
import tienda.modelo.Empleado;
import tienda.modelo.Bodega;

public class Main {
    public static void main(String[] args) {
        Producto p = new Producto("P1", "Leche", "1 litro", "Dos pinos", 12.5);
        System.out.println(p);
        System.out.println("Precio: " + p.getPrecio());

        Cliente c = new Cliente("123435465", "Ana Lopez", "Zona 1",
                java.time.LocalDate.of(1995, 3, 27), TipoCliente.VIP);
        System.out.println(c);
        System.out.println("Tipo: " + c.getTipo());

        // 1) primero se crea el empleado
        Empleado e = new Empleado("E1", "Luis Pérez", "9876543210101",
                "Zona 2", "Bodeguero", 3500.0);
        System.out.println(e);

        // 2) después se usa en la bodega
        Bodega b = new Bodega("B1", e, 500, "Zona 3");
        System.out.println(b);
        System.out.println("Encargado: " + b.getEncargado().getNombre());
        System.out.println("Capacidad: " + b.getCapacidad());
    }
}