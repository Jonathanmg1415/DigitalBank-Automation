package co.edu.udea.calidad.digitalbank.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

/** Cuentas del cliente con su saldo ({@code /cuentas}, HU-08). */
public final class PaginaMisCuentas {

    /** Saldo disponible de una cuenta. Uso: {@code SALDO_DISPONIBLE.of(numeroCuenta)}. */
    public static final Target SALDO_DISPONIBLE = Target.the("saldo disponible de la cuenta {0}")
            .locatedBy("[data-testid='fila-cuenta-{0}'] [data-testid='cuenta-saldo-disponible']");

    /** Estado de una cuenta. Uso: {@code ESTADO.of(numeroCuenta)}. */
    public static final Target ESTADO = Target.the("estado de la cuenta {0}")
            .locatedBy("[data-testid='fila-cuenta-{0}'] [data-testid='cuenta-estado']");

    private PaginaMisCuentas() {
    }
}
