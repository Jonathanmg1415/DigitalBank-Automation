package co.edu.udea.calidad.digitalbank.models;

/**
 * Cuenta bancaria creada para un escenario.
 *
 * @param id           identificador interno (UUID) que devuelve el backend al abrirla
 * @param numeroCuenta número de cuenta que se ve en la interfaz
 * @param tipoCuenta   AHORROS o CORRIENTE
 */
public record Cuenta(String id, String numeroCuenta, String tipoCuenta) {

    @Override
    public String toString() {
        return numeroCuenta;
    }
}
