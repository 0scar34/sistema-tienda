# Guía rápida: subir el proyecto a GitHub

1. Cree una cuenta en https://github.com e instale Git (https://git-scm.com).
2. En GitHub: **New repository** → nombre `sistema-tienda` → *Create repository* (sin README, ya existe uno).
3. En una terminal, dentro de la carpeta del proyecto:
```bash
git init
git config user.name "Su Nombre"
git config user.email "su-correo@ejemplo.com"
git add .
git commit -m "Versión inicial: modelo, persistencia, GUI y documentación"
git branch -M main
git remote add origin https://github.com/SU_USUARIO/sistema-tienda.git
git push -u origin main
```
4. Para cada cambio futuro: `git add .` → `git commit -m "descripción del cambio"` → `git push`.

Consejo: haga commits pequeños y con mensajes claros (ej. "Agrega validación de stock"); el profesor suele revisar el historial.
