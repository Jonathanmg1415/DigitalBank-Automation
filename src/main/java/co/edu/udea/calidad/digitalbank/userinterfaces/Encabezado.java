package co.edu.udea.calidad.digitalbank.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/** Elementos comunes a todas las pantallas con sesión iniciada: encabezado, menú y título. */
public final class Encabezado {

    /** Rol del usuario con sesión iniciada ("Cliente", "Cajero" o "Administrador"). */
    public static final Target LBL_ROL = Target.the("rol del usuario")
            .located(By.id("usuario-rol"));

    public static final Target LBL_EMAIL = Target.the("correo del usuario")
            .located(By.id("usuario-email"));

    public static final Target BTN_CERRAR_SESION = Target.the("botón Cerrar sesión")
            .located(By.id("cerrar-sesion"));

    /** Opción del menú lateral. Uso: {@code OPCION_MENU.of("transferir")}. */
    public static final Target OPCION_MENU = Target.the("opción {0} del menú")
            .locatedBy("#menu-{0}");

    /** Todas las opciones visibles del menú lateral. */
    public static final Target OPCIONES_MENU = Target.the("opciones del menú")
            .locatedBy("[data-testid='menu-lateral'] a");

    /** Título (h1) de la pantalla actual. */
    public static final Target LBL_TITULO = Target.the("título de la página")
            .located(By.id("titulo-pagina"));

    private Encabezado() {
    }
}
