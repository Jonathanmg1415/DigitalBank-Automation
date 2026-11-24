package co.edu.udea.calidad.digitalbank.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/**
 * Campos y botones de las pantallas de operaciones con dinero: retiro (HU-11),
 * transferencia (HU-09) y depósito (HU-10).
 */
public final class PaginasDeOperaciones {

    // ---------- Retiro (/retiros) ----------

    public static final Target TXT_MONTO_RETIRO = Target.the("monto del retiro")
            .located(By.id("retiro-monto"));

    public static final Target BTN_RETIRAR = Target.the("botón Retirar")
            .located(By.id("retiro-enviar"));

    // ---------- Transferencia (/transferencias) ----------

    public static final Target TXT_CUENTA_DESTINO = Target.the("número de la cuenta destino")
            .located(By.id("transferencia-destino"));

    public static final Target TXT_MONTO_TRANSFERENCIA = Target.the("monto de la transferencia")
            .located(By.id("transferencia-monto"));

    public static final Target TXT_DESCRIPCION = Target.the("descripción de la transferencia")
            .located(By.id("transferencia-descripcion"));

    public static final Target BTN_TRANSFERIR = Target.the("botón Transferir")
            .located(By.id("transferencia-enviar"));

    // ---------- Depósito (/depositos) ----------

    public static final Target TXT_MONTO_DEPOSITO = Target.the("monto del depósito")
            .located(By.id("deposito-monto"));

    public static final Target LST_ORIGEN_DEPOSITO = Target.the("origen del depósito")
            .located(By.id("deposito-origen"));

    public static final Target BTN_DEPOSITAR = Target.the("botón Depositar")
            .located(By.id("deposito-enviar"));

    private PaginasDeOperaciones() {
    }
}
