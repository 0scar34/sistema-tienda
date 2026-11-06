package tienda.modelo;

public class Empleado extends Persona implements Persistible {
    private final String id;
    private final String puesto;
    private final double salario;

    public Empleado(String id, String nombre, String dpi, String direccion, String puesto, double salario) {
        super(dpi, nombre, direccion);
        this.id = id;
        this.puesto = puesto;
        this.salario = salario;
    }

    public String getId() { return id; }
    public String getPuesto() { return puesto; }
    public double getSalario() { return salario; }

    @Override
    public String aLinea() {
        return String.join("|", id, nombre, dpi, direccion, puesto, String.valueOf(salario));
    }

    public static Empleado deLinea(String linea) {
        String[] p = linea.split("\\|", -1);
        return new Empleado(p[0], p[1], p[2], p[3], p[4], Double.parseDouble(p[5]));
    }

    @Override public String toString() { return id + " - " + nombre; }
}
