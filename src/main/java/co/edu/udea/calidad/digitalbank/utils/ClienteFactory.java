package co.edu.udea.calidad.digitalbank.utils;

import co.edu.udea.calidad.digitalbank.models.Cliente;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Crea clientes con datos únicos en cada ejecución (documento, correo y teléfono), para que los
 * escenarios no dependan de datos creados a mano ni choquen con ejecuciones anteriores.
 */
public final class ClienteFactory {

    /** Contraseña de todos los clientes de prueba (la del documento de casos de prueba). */
    public static final String PASSWORD = "Clave#2026";
    private static final String FECHA_NACIMIENTO = "1995-05-10";

    private ClienteFactory() {
    }

    /**
     * Cliente nuevo con el nombre indicado. Ana y Luis reciben los apellidos del documento de
     * casos de prueba; cualquier otro nombre recibe "Prueba Automatizada".
     */
    public static Cliente unClienteNuevo(String nombre) {
        String apellidos = switch (nombre) {
            case "Ana" -> "Restrepo Gómez";
            case "Luis" -> "Pérez";
            default -> "Prueba Automatizada";
        };
        String sufijo = Long.toString(System.currentTimeMillis()).substring(5) + numeros(3);
        String correo = (sinTildes(nombre) + "." + sufijo + "@correo.test").toLowerCase(Locale.ROOT);
        return new Cliente("CC", "9" + numeros(9), nombre, apellidos, FECHA_NACIMIENTO,
                "3" + numeros(9), correo, PASSWORD);
    }

    /** Fecha de nacimiento de alguien que cumple 18 años mañana (todavía es menor de edad). */
    public static String fechaDeUnMenorDeEdad() {
        return LocalDate.now().minusYears(18).plusDays(1).toString();
    }

    /** Correo único que no pertenece a ningún usuario. */
    public static String correoInexistente() {
        return "noexiste." + System.currentTimeMillis() + "@correo.test";
    }

    private static String numeros(int cantidad) {
        StringBuilder texto = new StringBuilder();
        for (int i = 0; i < cantidad; i++) {
            texto.append(ThreadLocalRandom.current().nextInt(10));
        }
        return texto.toString();
    }

    private static String sinTildes(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }
}
