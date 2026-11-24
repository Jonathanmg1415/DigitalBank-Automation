package co.edu.udea.calidad.digitalbank.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Componente para elegir una cuenta, compartido por las pantallas de retiro, transferencia,
 * depósito, movimientos y estado de cuentas. Cada Target recibe el prefijo de la pantalla
 * ({@code retiro}, {@code transferencia}, {@code deposito}, {@code movimientos} o
 * {@code estado-cuenta}). Uso: {@code LST_CUENTA.of("retiro")}.
 */
public final class SelectorDeCuenta {

    /** Lista de cuentas. El valor de cada opción es el número de cuenta. */
    public static final Target LST_CUENTA = Target.the("lista de cuentas")
            .locatedBy("#{0}-cuenta");

    /** Búsqueda del cliente por documento: solo la ven el administrador y el cajero. */
    public static final Target LST_TIPO_DOCUMENTO = Target.the("tipo de documento del cliente")
            .locatedBy("#{0}-buscar-tipo-documento");

    public static final Target TXT_NUMERO_DOCUMENTO = Target.the("número de documento del cliente")
            .locatedBy("#{0}-buscar-numero-documento");

    public static final Target BTN_BUSCAR_CLIENTE = Target.the("botón Buscar cliente")
            .locatedBy("#{0}-buscar-cliente");

    private SelectorDeCuenta() {
    }
}
