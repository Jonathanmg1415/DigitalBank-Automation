package co.edu.udea.calidad.digitalbank.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/** Mensajes que muestran todas las pantallas después de una operación. */
public final class Mensajes {

    /** Mensaje de operación exitosa. */
    public static final Target ALERTA_EXITO = Target.the("mensaje de éxito")
            .locatedBy("[data-testid='alerta-exito']");

    /** Mensaje de error de la operación: incluye el texto que envía el backend. */
    public static final Target ALERTA_ERROR = Target.the("mensaje de error")
            .locatedBy("[data-testid='alerta-error'], #login-error, #login-error-bloqueo");

    /**
     * Errores de validación de los campos (por ejemplo "Escriba el monto a retirar.").
     * Se usa By.cssSelector explícito: "small.campo-error" también es un nombre válido de XPath y
     * con locatedBy(...) Serenity lo interpretaría como XPath y no encontraría nada.
     */
    public static final Target ERRORES_DE_CAMPO = Target.the("errores de los campos")
            .located(By.cssSelector("small.campo-error"));

    /**
     * Cualquier señal de que la operación terminó: un mensaje de éxito o de error, un error de
     * campo o el aviso de que el backend no envió el identificador de la cuenta.
     */
    public static final Target RESPUESTA = Target.the("respuesta de la operación")
            .locatedBy("[data-testid='alerta-exito'], [data-testid='alerta-error'], small.campo-error, "
                    + "[data-testid$='-cuenta-sin-id'], #login-error, #login-error-bloqueo");

    private Mensajes() {
    }
}
