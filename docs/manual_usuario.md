# Manual de usuario

## Iniciar el programa
Ejecute `compilar_y_ejecutar.bat` (Windows) o `./compilar_y_ejecutar.sh` (Linux/Mac). Se abre la ventana principal con seis pestañas.
Los datos se guardan automáticamente en la carpeta `datos/`; al volver a abrir el programa siguen ahí.

## Orden recomendado de uso
Algunos datos dependen de otros, por eso conviene registrar en este orden:

1. **Productos** → 2. **Clientes** → 3. **Empleados** → 4. **Bodegas** → 5. **Inventarios** (y su stock) → 6. **Órdenes de compra**

## Pestañas

### Productos
Llene código, nombre, descripción, marca y precio y presione **Registrar producto**. El código no puede repetirse.

### Clientes
Llene DPI, nombre, dirección, fecha de nacimiento (formato `AAAA-MM-DD`, ej. `1995-03-27`), elija el tipo (REGULAR o VIP) y presione **Registrar cliente**.

### Empleados
Llene ID, nombre, DPI, dirección, puesto y salario y presione **Registrar empleado**.

### Bodegas
Escriba el ID, elija el empleado encargado en la lista (debe existir antes), indique la capacidad en unidades y la dirección. Presione **Registrar bodega**.

### Inventarios
1. En la sección **1)** escriba un ID y elija la bodega; presione **Crear inventario**.
2. En la sección **2)** elija el inventario y el producto, escriba la cantidad y presione **Agregar stock**.
La tabla muestra el stock de cada producto. No se puede superar la capacidad de la bodega.

### Órdenes de compra
1. Elija el **cliente**.
2. Elija un **producto**, escriba la **cantidad** y presione **Agregar producto a la orden**. Repita para más productos (mínimo uno).
3. Presione **Registrar orden**: queda en estado **PENDIENTE**.
4. Para venderla, seleccione la orden en la tabla inferior y presione **Completar orden seleccionada**. Elija el inventario del que sale la mercadería. Si hay stock suficiente, la orden pasa a **COMPLETADA** y el stock se resta automáticamente (puede verificarlo en la pestaña Inventarios).
5. Para anular una orden pendiente, selecciónela y presione **Cancelar orden seleccionada** (no modifica el stock).

## Mensajes de error frecuentes
| Mensaje | Qué hacer |
|---------|-----------|
| El campo "X" es obligatorio | Complete el campo |
| Debe ser un número | Use solo dígitos (decimales con punto: `25.50`) |
| Formato AAAA-MM-DD | Escriba la fecha como `1995-03-27` |
| Ya existe ... | El código/ID/DPI ya está registrado |
| Stock insuficiente | Agregue stock al inventario o reduzca la cantidad |
| Capacidad insuficiente | Use otra bodega o una con más capacidad |
