package co.edu.udea.calidad.digitalbank.utils;

import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

/**
 * Valores de {@code serenity.conf} que usa el proyecto. Se leen del ambiente elegido con
 * {@code -Denvironment=...} y se pueden reemplazar con propiedades del sistema.
 */
public final class Configuracion {

    private static final String URL_BASE = "webdriver.base.url";
    private static final String TIMEOUT = "digitalbank.respuesta.timeout";
    private static final int TIMEOUT_POR_DEFECTO = 90;

    private Configuracion() {
    }

    /** URL del frontend, sin "/" al final. Por ejemplo http://localhost:5173. */
    public static String urlBase() {
        String url = System.getProperty(URL_BASE);
        if (url == null || url.isBlank()) {
            url = EnvironmentSpecificConfiguration.from(variables()).getProperty(URL_BASE);
        }
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    /** Segundos máximos de espera por una respuesta del backend. */
    public static int segundosDeEspera() {
        return variables().getPropertyAsInteger(TIMEOUT, TIMEOUT_POR_DEFECTO);
    }

    private static EnvironmentVariables variables() {
        return SystemEnvironmentVariables.currentEnvironmentVariables();
    }
}
