package tienda.modelo;

import java.time.LocalDate; // para la fecha: año, mes, día

public class Cliente extends Persona {
    private LocalDate fechaNacimiento;
    private TipoCliente tipo;

    // constructor: un solo constructor con todos los datos
    public Cliente(String dpi, String nombre, String direccion,
                   LocalDate fechaNacimiento, TipoCliente tipo) {
        super(dpi, nombre, direccion); // guarda los datos que vienen de Persona
        this.fechaNacimiento = fechaNacimiento;
        this.tipo = tipo;
    }

    // getters
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public TipoCliente getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return nombre + " (" + dpi + ")";
    }
}
