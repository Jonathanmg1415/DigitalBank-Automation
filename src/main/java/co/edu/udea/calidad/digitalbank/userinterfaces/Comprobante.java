package co.edu.udea.calidad.digitalbank.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Datos del resultado de una operación (el "comprobante"). En el frontend cada valor tiene
 * {@code data-testid="<prefijo>-<dato>"}, por ejemplo {@code retiro-resultado-saldo-nuevo} o
 * {@code estado-cuenta-resultado-estado-nuevo}. La lista completa está en
 * {@code docs/selectores.md} del frontend.
 */
public final class Comprobante {

    /** Uso: {@code DATO.of("retiro-resultado-saldo-nuevo")}. */
    public static final Target DATO = Target.the("dato {0} del comprobante")
            .locatedBy("[data-testid='{0}']");

    private Comprobante() {
    }
}
