package co.edu.udea.calidad.digitalbank.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/** Pantalla para bloquear, desbloquear y cerrar cuentas ({@code /cuentas/estado}, HU-07a y HU-07b). */
public final class PaginaEstadoCuentas {

    /** Acción a realizar. Uso: {@code RDB_ACCION.of("bloquear")}, "desbloquear" o "cerrar". */
    public static final Target RDB_ACCION = Target.the("acción {0}")
            .locatedBy("#estado-cuenta-accion-{0}");

    public static final Target TXT_MOTIVO = Target.the("motivo del cambio de estado")
            .located(By.id("estado-cuenta-motivo"));

    public static final Target BTN_CONFIRMAR = Target.the("botón para cambiar el estado")
            .located(By.id("estado-cuenta-enviar"));

    private PaginaEstadoCuentas() {
    }
}
