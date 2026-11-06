# Manual técnico

## 1. Descripción general
Aplicación de escritorio en Java con interfaz gráfica Swing. Sigue una arquitectura simple en capas:

| Capa | Paquete | Responsabilidad |
|------|---------|-----------------|
| Vista | `tienda.vista` | Ventanas y formularios; valida entradas del usuario |
| Servicio | `tienda.servicio` | Reglas de negocio (`SistemaTienda`) |
| Persistencia | `tienda.persistencia` | Leer/escribir archivos (`ArchivoDatos`) |
| Modelo | `tienda.modelo` | Clases del dominio |

## 2. Conceptos de POO aplicados
- **Encapsulamiento:** atributos privados con getters.
- **Herencia:** `Cliente` y `Empleado` extienden de la clase abstracta `Persona` (dpi, nombre, dirección).
- **Interfaz:** `Persistible` obliga a cada clase a saber convertirse en una línea de archivo (`aLinea()`).
- **Polimorfismo:** `SistemaTienda.guardar(...)` guarda cualquier lista de `Persistible`.
- **Composición/Asociación:** `OrdenCompra` tiene un `Cliente` y varias `LineaOrden`; `Inventario` tiene una `Bodega` y un mapa Producto→cantidad; `Bodega` tiene un `Empleado`.
- **Enumeraciones:** `TipoCliente` (REGULAR, VIP) y `EstadoOrden` (PENDIENTE, COMPLETADA, CANCELADA).

## 3. Diagrama UML de clases
(GitHub muestra este diagrama automáticamente. También puede pegarlo en https://mermaid.live para exportarlo como imagen.)

```mermaid
classDiagram
    direction LR
    class Persistible {
        <<interface>>
        +aLinea() String
    }
    class Persona {
        <<abstract>>
        #dpi: String
        #nombre: String
        #direccion: String
    }
    class Cliente {
        -fechaNacimiento: LocalDate
        -tipo: TipoCliente
    }
    class Empleado {
        -id: String
        -puesto: String
        -salario: double
    }
    class Producto {
        -codigo: String
        -nombre: String
        -descripcion: String
        -marca: String
        -precio: double
    }
    class Bodega {
        -id: String
        -capacidad: int
        -direccion: String
    }
    class Inventario {
        -id: String
        -stock: Map~Producto,Integer~
        +agregar(Producto, int)
        +descontar(Producto, int)
        +stockDe(Producto) int
        +totalUnidades() int
    }
    class OrdenCompra {
        -id: int
        -fecha: LocalDate
        -estado: EstadoOrden
        +total() double
    }
    class LineaOrden {
        -cantidad: int
        -precioUnitario: double
        +subtotal() double
    }
    class TipoCliente {
        <<enumeration>>
        REGULAR
        VIP
    }
    class EstadoOrden {
        <<enumeration>>
        PENDIENTE
        COMPLETADA
        CANCELADA
    }
    class SistemaTienda {
        +registrarProducto(Producto)
        +registrarCliente(Cliente)
        +registrarEmpleado(Empleado)
        +registrarBodega(Bodega)
        +registrarInventario(Inventario)
        +agregarStock(Inventario, Producto, int)
        +registrarOrden(Cliente, List~LineaOrden~) OrdenCompra
        +completarOrden(OrdenCompra, Inventario)
        +cancelarOrden(OrdenCompra)
    }
    class ArchivoDatos {
        +leer(String) List~String~
        +escribir(String, List~String~)
    }

    Persona <|-- Cliente
    Persona <|-- Empleado
    Persistible <|.. Producto
    Persistible <|.. Cliente
    Persistible <|.. Empleado
    Persistible <|.. Bodega
    Persistible <|.. Inventario
    Persistible <|.. OrdenCompra
    Cliente --> TipoCliente
    OrdenCompra --> EstadoOrden
    OrdenCompra --> Cliente
    OrdenCompra "1" *-- "1..*" LineaOrden
    LineaOrden --> Producto
    Bodega --> Empleado : encargado
    Inventario --> Bodega
    Inventario --> Producto : stock
    SistemaTienda ..> ArchivoDatos
    SistemaTienda o-- Producto
    SistemaTienda o-- Cliente
    SistemaTienda o-- Empleado
    SistemaTienda o-- Bodega
    SistemaTienda o-- Inventario
    SistemaTienda o-- OrdenCompra
```

## 4. Persistencia en archivos
Se usan archivos de texto en la carpeta `datos/` (se crea sola). Cada línea es un registro y los campos se separan con `|`.

| Archivo | Formato de cada línea |
|---------|----------------------|
| `productos.txt` | `codigo\|nombre\|descripcion\|marca\|precio` |
| `clientes.txt` | `dpi\|nombre\|direccion\|AAAA-MM-DD\|tipo` |
| `empleados.txt` | `id\|nombre\|dpi\|direccion\|puesto\|salario` |
| `bodegas.txt` | `id\|idEmpleado\|capacidad\|direccion` |
| `inventarios.txt` | `id\|idBodega\|codProd:cantidad;codProd:cantidad` |
| `ordenes.txt` | `id\|dpiCliente\|fecha\|total\|estado\|codProd:cantidad:precio;...` |

- **Guardado:** cada vez que se registra o modifica algo, se reescribe el archivo correspondiente.
- **Carga:** al iniciar, `SistemaTienda.cargar()` lee los archivos en orden de dependencia (productos, clientes, empleados → bodegas → inventarios → órdenes).
- Por esto los campos de texto no pueden contener el símbolo `|` (la vista lo valida).

## 5. Reglas de negocio
1. No se permiten códigos/IDs/DPI duplicados.
2. Precio, salario y capacidad deben ser mayores que cero.
3. Una orden debe tener **al menos un producto**; nace en estado `PENDIENTE`.
4. **Completar una orden:** primero se valida que el inventario elegido tenga stock suficiente de *todos* los productos; si es así se resta el stock, se cambia el estado a `COMPLETADA` y se guardan inventarios y órdenes. Si falta stock no se modifica nada.
5. Solo se pueden completar o cancelar órdenes `PENDIENTE`.
6. Al agregar stock, el total de unidades de todos los inventarios de una bodega no puede superar su capacidad.

## 6. Flujo de completar una venta
```mermaid
sequenceDiagram
    actor U as Usuario
    participant V as PanelOrdenes
    participant S as SistemaTienda
    participant I as Inventario
    participant A as ArchivoDatos
    U->>V: Completar orden seleccionada
    V->>S: completarOrden(orden, inventario)
    S->>I: stockDe(producto) por cada línea
    alt stock suficiente
        S->>I: descontar(producto, cantidad)
        S->>S: estado = COMPLETADA
        S->>A: escribir inventarios.txt y ordenes.txt
        S-->>V: OK
    else stock insuficiente
        S-->>V: IllegalArgumentException
        V-->>U: mensaje de error
    end
```

## 7. Limitaciones y mejoras posibles
- No hay edición ni eliminación de registros (solo registro, según el alcance).
- Se podría guardar en JSON/CSV o base de datos, y agregar pruebas unitarias con JUnit.
