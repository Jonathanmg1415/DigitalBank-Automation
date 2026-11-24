package co.edu.udea.calidad.digitalbank.utils;

/**
 * Nombres con los que el actor guarda y recupera datos entre pasos
 * ({@code actor.remember(...)} y {@code actor.recall(...)}).
 */
public final class MemoryKeys {

    /** Credenciales de inicio de sesión del actor. */
    public static final String CREDENCIALES = "credenciales";
    /** Datos del cliente (solo para los actores que son clientes). */
    public static final String CLIENTE = "cliente";
    /** Cuenta principal del cliente. */
    public static final String CUENTA = "cuenta";
    /** Segunda cuenta del cliente (por ejemplo, la que se cierra). */
    public static final String SEGUNDA_CUENTA = "segundaCuenta";
    /** Marca que indica que el actor ya inició sesión en su navegador. */
    public static final String SESION_INICIADA = "sesionIniciada";
    /** Cliente que el administrador acaba de intentar registrar desde la interfaz. */
    public static final String CLIENTE_REGISTRADO = "clienteRegistrado";

    private MemoryKeys() {
    }
}
