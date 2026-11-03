package tienda.modelo;

public class Empleado extends Persona {

    private String id;
    private String puesto;
    private double salario;

    public Empleado(String id, String nombre, String dpi, String direccion,
                    String puesto, double salario) {
        super(dpi, nombre, direccion);
        this.id = id;
        this.puesto = puesto;
        this.salario = salario;
    }

    public String getId() {
        return id;
    }

    public String getPuesto() {
        return puesto;
    }

    public double getSalario() {
        return salario;
    }

    @Override
    public String toString() {
        return id + " - " + nombre;

    }
}
