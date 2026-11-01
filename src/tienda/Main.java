package tienda;
import tienda.modelo.Producto;
public class Main {
    public static void main(String []args){
        Producto p= new Producto("P1","Leche","1 litro","Dos pinos",12.5);
        System.out.println(p);
        System.out.println("Precio: "+ p.getPrecio());
    }
}
