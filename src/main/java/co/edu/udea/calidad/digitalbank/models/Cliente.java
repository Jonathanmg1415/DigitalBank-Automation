package co.edu.udea.calidad.digitalbank.models;

/**
 * Datos de un cliente del banco, tal como los pide el registro (HU-01).
 *
 * @param tipoDocumento   CC, CE, PASAPORTE o NIT
 * @param numeroDocumento número de documento, único en el banco
 * @param nombres         nombres del cliente
 * @param apellidos       apellidos del cliente
 * @param fechaNacimiento fecha en formato yyyy-MM-dd
 * @param telefono        10 dígitos
 * @param email           correo, que también es el usuario de inicio de sesión
 * @param password        contraseña inicial (mínimo 8 caracteres)
 */
public record Cliente(String tipoDocumento, String numeroDocumento, String nombres, String apellidos,
                      String fechaNacimiento, String telefono, String email, String password) {

    public Credenciales credenciales() {
        return new Credenciales(email, password);
    }

    public String nombreCompleto() {
        return nombres + " " + apellidos;
    }

    /** El mismo cliente con otra fecha de nacimiento (para la validación de edad). */
    public Cliente conFechaNacimiento(String otraFecha) {
        return new Cliente(tipoDocumento, numeroDocumento, nombres, apellidos, otraFecha, telefono, email, password);
    }

    /** El mismo cliente con otro correo (para registrar un documento repetido). */
    public Cliente conEmail(String otroEmail) {
        return new Cliente(tipoDocumento, numeroDocumento, nombres, apellidos, fechaNacimiento, telefono, otroEmail, password);
    }

    /** No muestra la contraseña: Serenity usa toString() en el reporte. */
    @Override
    public String toString() {
        return nombreCompleto() + " (" + tipoDocumento + " " + numeroDocumento + ")";
    }
}
