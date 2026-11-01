package tienda;
import tienda.modelo.Producto;
import tienda.modelo.TipoCliente;
import tienda.modelo.Cliente;
public class Main {
    public static void main(String[] args) {
        Producto p = new Producto("P1", "Leche", "1 litro", "Dos pinos", 12.5);
        System.out.println(p);
        System.out.println("Precio: " + p.getPrecio());

        Cliente c = new Cliente("123435465", "Ana Lopez", "Zona 1",
                java.time.LocalDate.of(1995, 3, 27), TipoCliente.VIP);
        System.out.println(c);
        System.out.println("Tipo: " + c.getTipo());
    }
}