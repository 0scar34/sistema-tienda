package tienda.modelo;

import java.time.LocalDate;

public class Cliente extends Persona implements Persistible {
    private final LocalDate fechaNacimiento;
    private final TipoCliente tipo;

    public Cliente(String dpi, String nombre, String direccion, LocalDate fechaNacimiento, TipoCliente tipo) {
        super(dpi, nombre, direccion);
        this.fechaNacimiento = fechaNacimiento;
        this.tipo = tipo;
    }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public TipoCliente getTipo() { return tipo; }

    @Override
    public String aLinea() {
        return String.join("|", dpi, nombre, direccion, fechaNacimiento.toString(), tipo.name());
    }

    public static Cliente deLinea(String linea) {
        String[] p = linea.split("\\|", -1);
        return new Cliente(p[0], p[1], p[2], LocalDate.parse(p[3]), TipoCliente.valueOf(p[4]));
    }

    @Override public String toString() { return nombre + " (" + dpi + ")"; }
}
