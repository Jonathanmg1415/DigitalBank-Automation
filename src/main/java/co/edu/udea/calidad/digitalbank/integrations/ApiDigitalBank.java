package co.edu.udea.calidad.digitalbank.integrations;

import co.edu.udea.calidad.digitalbank.exceptions.PreparacionDeDatosException;
import co.edu.udea.calidad.digitalbank.models.Cliente;
import co.edu.udea.calidad.digitalbank.models.Credenciales;
import co.edu.udea.calidad.digitalbank.models.Cuenta;
import co.edu.udea.calidad.digitalbank.utils.Configuracion;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Cliente de la API de Digital Bank para preparar los datos de cada escenario (Arrange):
 * registrar clientes, abrir cuentas, hacer depósitos, retiros y cambios de estado.
 *
 * <p>Lo que el escenario prueba se hace siempre desde la interfaz con tareas de Screenplay;
 * esta clase solo deja el sistema en el estado que piden los "Dado". Preparar por API hace los
 * escenarios más rápidos e independientes entre sí, y cada uno crea sus propios datos.</p>
 *
 * <p>Las peticiones van al mismo origen del frontend ({@code /svc/auth}, {@code /svc/core}),
 * que las redirige a cada API. Así no hace falta configurar las URL de las APIs.</p>
 */
public final class ApiDigitalBank {

    private static final ObjectMapper JSON = new ObjectMapper();
    /**
     * HTTP/1.1 explícito: con HTTP/2, Java intenta "subir" la conexión (Upgrade: h2c) y el
     * servidor de desarrollo de Vite la corta.
     */
    private static final HttpClient HTTP = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(30))
            .build();
    /** Render puede tardar varios minutos en despertar una API. */
    private static final Duration ESPERA_MAXIMA = Duration.ofMinutes(5);

    private ApiDigitalBank() {
    }

    /** HU-18. Inicia sesión y devuelve el token JWT. */
    public static String iniciarSesion(Credenciales credenciales) {
        Map<String, Object> cuerpo = Map.of("email", credenciales.email(), "password", credenciales.password());
        return post("/svc/auth/api/v1/auth/login", null, cuerpo, 200, "iniciar sesión como " + credenciales)
                .get("token").asText();
    }

    /**
     * HU-18. Intenta iniciar sesión y devuelve solo el código de respuesta (200, 401 o 429).
     * Sirve para dejar un usuario con intentos fallidos sin pasar por la interfaz.
     */
    public static int codigoDelInicioDeSesion(Credenciales credenciales) {
        try {
            String cuerpo = JSON.writeValueAsString(
                    Map.of("email", credenciales.email(), "password", credenciales.password()));
            HttpRequest solicitud = HttpRequest.newBuilder(URI.create(Configuracion.urlBase() + "/svc/auth/api/v1/auth/login"))
                    .timeout(ESPERA_MAXIMA)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(cuerpo))
                    .build();
            return HTTP.send(solicitud, HttpResponse.BodyHandlers.discarding()).statusCode();
        } catch (IOException e) {
            throw new PreparacionDeDatosException("No se pudo intentar el inicio de sesión: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PreparacionDeDatosException("Se interrumpió el intento de inicio de sesión", e);
        }
    }

    /** HU-01. El administrador registra un cliente y se devuelve el id del cliente. */
    public static String registrarCliente(String tokenAdmin, Cliente cliente) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("tipoDocumento", cliente.tipoDocumento());
        cuerpo.put("numeroDocumento", cliente.numeroDocumento());
        cuerpo.put("nombres", cliente.nombres());
        cuerpo.put("apellidos", cliente.apellidos());
        cuerpo.put("fechaNacimiento", cliente.fechaNacimiento());
        cuerpo.put("telefono", cliente.telefono());
        cuerpo.put("email", cliente.email());
        cuerpo.put("password", cliente.password());
        return post("/svc/auth/api/v1/clientes", tokenAdmin, cuerpo, 201, "registrar a " + cliente)
                .get("id").asText();
    }

    /** HU-05. El cliente abre una cuenta a su nombre con un saldo inicial. */
    public static Cuenta abrirCuenta(String tokenCliente, Cliente cliente, String tipoCuenta, BigDecimal saldoInicial) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("tipoDocumento", cliente.tipoDocumento());
        cuerpo.put("numeroDocumento", cliente.numeroDocumento());
        cuerpo.put("tipoCuenta", tipoCuenta);
        cuerpo.put("montoApertura", saldoInicial);
        JsonNode cuenta = post("/svc/core/api/v1/cuentas", tokenCliente, cuerpo, 201, "abrir una cuenta para " + cliente);
        return new Cuenta(cuenta.get("id").asText(), cuenta.get("numeroCuenta").asText(), cuenta.get("tipoCuenta").asText());
    }

    /** HU-10. El cajero (o el administrador) deposita en una cuenta. */
    public static void depositar(String tokenCajero, Cuenta cuenta, BigDecimal monto, String origen) {
        post("/svc/core/api/v1/cuentas/" + cuenta.id() + "/depositos", tokenCajero,
                Map.of("monto", monto, "origen", origen), 201, "depositar " + monto + " en " + cuenta);
    }

    /** HU-11. El cliente titular retira de su cuenta. */
    public static void retirar(String tokenCliente, Cuenta cuenta, BigDecimal monto) {
        post("/svc/core/api/v1/cuentas/" + cuenta.id() + "/retiros", tokenCliente,
                Map.of("monto", monto), 201, "retirar " + monto + " de " + cuenta);
    }

    /** HU-07a y HU-07b. El administrador bloquea, desbloquea o cierra una cuenta. */
    public static void cambiarEstado(String tokenAdmin, Cuenta cuenta, String accion, String motivo) {
        post("/svc/core/api/v1/cuentas/" + cuenta.id() + "/" + accion, tokenAdmin,
                Map.of("motivo", motivo), 200, accion + " la cuenta " + cuenta);
    }

    private static JsonNode post(String ruta, String token, Object cuerpo, int estadoEsperado, String operacion) {
        try {
            HttpRequest.Builder solicitud = HttpRequest.newBuilder(URI.create(Configuracion.urlBase() + ruta))
                    .timeout(ESPERA_MAXIMA)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(JSON.writeValueAsString(cuerpo)));
            if (token != null) {
                solicitud.header("Authorization", "Bearer " + token);
            }
            HttpResponse<String> respuesta = HTTP.send(solicitud.build(), HttpResponse.BodyHandlers.ofString());
            if (respuesta.statusCode() != estadoEsperado) {
                throw new PreparacionDeDatosException("No se pudo " + operacion + ": " + ruta + " respondió "
                        + respuesta.statusCode() + " " + respuesta.body());
            }
            return respuesta.body().isBlank() ? JSON.createObjectNode() : JSON.readTree(respuesta.body());
        } catch (IOException e) {
            throw new PreparacionDeDatosException("No se pudo " + operacion + ": " + e, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PreparacionDeDatosException("Se interrumpió la operación: " + operacion, e);
        }
    }
}
