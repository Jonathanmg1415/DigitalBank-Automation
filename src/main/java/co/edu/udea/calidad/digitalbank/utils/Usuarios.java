package co.edu.udea.calidad.digitalbank.utils;

import co.edu.udea.calidad.digitalbank.exceptions.PreparacionDeDatosException;
import co.edu.udea.calidad.digitalbank.models.Credenciales;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Credenciales de los usuarios internos (ADMIN y CAJERO), que no se pueden crear por la API.
 *
 * <p>Se toman, en este orden, de las propiedades del sistema ({@code -Dadmin.email=...}) o del
 * archivo {@code usuarios.local.properties} en la raíz del proyecto, que no se sube a GitHub.
 * Ver {@code usuarios.example.properties}.</p>
 */
public final class Usuarios {

    private static final Path ARCHIVO = Path.of("usuarios.local.properties");
    private static final Properties PROPIEDADES = cargar();

    private Usuarios() {
    }

    public static Credenciales administrador() {
        return new Credenciales(leer("admin.email"), leer("admin.password"));
    }

    public static Credenciales cajero() {
        return new Credenciales(leer("cajero.email"), leer("cajero.password"));
    }

    private static String leer(String clave) {
        String valor = System.getProperty(clave, PROPIEDADES.getProperty(clave, "")).trim();
        if (valor.isEmpty()) {
            throw new PreparacionDeDatosException("Falta la propiedad '" + clave + "'. Escríbala en "
                    + ARCHIVO + " (ver usuarios.example.properties) o pásela con -D" + clave + "=...");
        }
        return valor;
    }

    private static Properties cargar() {
        Properties propiedades = new Properties();
        if (Files.exists(ARCHIVO)) {
            try (InputStream entrada = Files.newInputStream(ARCHIVO)) {
                propiedades.load(entrada);
            } catch (IOException e) {
                throw new PreparacionDeDatosException("No se pudo leer " + ARCHIVO, e);
            }
        }
        return propiedades;
    }
}
