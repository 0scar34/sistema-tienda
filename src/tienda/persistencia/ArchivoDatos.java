package tienda.persistencia;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class ArchivoDatos {

    public static List<String> leer(String ruta) {
        Path path = Paths.get(ruta);
        List<String> resultado = new ArrayList<>();
        if (!Files.exists(path)) {
            return resultado; 
        }
        try {
            for (String linea : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                if (!linea.isBlank()) {
                    resultado.add(linea);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer el archivo " + ruta, e);
        }
        return resultado;
    }

    public static void escribir(String ruta, List<String> lineas) {
        try {
            Path path = Paths.get(ruta);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.write(path, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo escribir el archivo " + ruta, e);
        }
    }
}
