package co.edu.udea.calidad.digitalbank.exceptions;

/**
 * Error al preparar los datos de un escenario por la API (registrar clientes, abrir cuentas,
 * hacer depósitos). Indica un problema del ambiente, no un defecto de la funcionalidad que
 * prueba el escenario.
 */
public class PreparacionDeDatosException extends RuntimeException {

    public PreparacionDeDatosException(String mensaje) {
        super(mensaje);
    }

    public PreparacionDeDatosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
