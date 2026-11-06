# Sistema de Tienda (Java + POO + Swing)

Sistema de escritorio para registrar **productos, clientes, empleados, bodegas, inventarios y órdenes de compra**,
con persistencia en archivos de texto. Al **completar** una orden se descuenta automáticamente el stock del inventario.

## Requisitos
- JDK 17 o superior (probado con JDK 21)

## Cómo ejecutarlo
**Windows:** doble clic en `compilar_y_ejecutar.bat`
**Linux / Mac:** `./compilar_y_ejecutar.sh`

O manualmente, desde la carpeta del proyecto:
```bash
mkdir out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out tienda.Main
```
(En IntelliJ / NetBeans / Eclipse: abra la carpeta como proyecto y ejecute `tienda.Main`.)

## Estructura
```
src/tienda/
  Main.java              -> punto de entrada
  modelo/                -> clases del dominio (Producto, Cliente, Empleado, Bodega, Inventario, OrdenCompra...)
  servicio/              -> SistemaTienda: reglas de negocio
  persistencia/          -> ArchivoDatos: lectura/escritura de archivos
  vista/                 -> interfaz gráfica Swing (una pestaña por entidad)
datos/                   -> archivos .txt generados automáticamente
docs/                    -> manual técnico (con diagrama UML) y manual de usuario
```


## Autores
Oscar chavez,
Daniel Pirir,
Alan Fuentes,
Algoritmos-2026

