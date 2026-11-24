package co.edu.udea.calidad.digitalbank.models;

/**
 * Correo y contraseña con los que un actor inicia sesión en Digital Bank.
 *
 * @param email    correo del usuario
 * @param password contraseña del usuario
 */
public record Credenciales(String email, String password) {

    /** Devuelve las mismas credenciales con otra contraseña (para los escenarios de error). */
    public Credenciales conPassword(String otraPassword) {
        return new Credenciales(email, otraPassword);
    }

    /** No muestra la contraseña: Serenity usa toString() en el reporte. */
    @Override
    public String toString() {
        return email;
    }
}
