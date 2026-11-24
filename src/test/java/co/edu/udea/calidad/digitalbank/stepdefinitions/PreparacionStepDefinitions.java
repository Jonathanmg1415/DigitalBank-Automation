package co.edu.udea.calidad.digitalbank.stepdefinitions;

import co.edu.udea.calidad.digitalbank.integrations.ApiDigitalBank;
import co.edu.udea.calidad.digitalbank.models.Cliente;
import co.edu.udea.calidad.digitalbank.models.Cuenta;
import co.edu.udea.calidad.digitalbank.utils.ClienteFactory;
import co.edu.udea.calidad.digitalbank.utils.MemoryKeys;
import co.edu.udea.calidad.digitalbank.utils.Usuarios;
import io.cucumber.java.es.Dado;
import net.serenitybdd.screenplay.Actor;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Pasos "Dado" que preparan los datos de los escenarios por la API (Arrange).
 *
 * <p>Cada escenario crea sus propios clientes y cuentas con datos únicos, así que no depende de
 * datos creados a mano y se puede repetir las veces que se quiera. Lo que el escenario prueba
 * (el "Cuando" y el "Entonces") se hace desde la interfaz.</p>
 */
public class PreparacionStepDefinitions {

    private static final String TOKEN = "tokenApi";
    private static final String MOTIVO = "Preparación automática del escenario";

    @Dado("que {actor} es cliente del banco")
    public void esClienteDelBanco(Actor actor) {
        Cliente cliente = ClienteFactory.unClienteNuevo(actor.getName());
        ApiDigitalBank.registrarCliente(tokenDelAdministrador(), cliente);
        Elenco.esCliente(actor, cliente);
    }

    @Dado("que {actor} es cliente del banco con una cuenta de ahorros con saldo {int}")
    public void esClienteConUnaCuenta(Actor actor, int saldo) {
        esClienteDelBanco(actor);
        actor.remember(MemoryKeys.CUENTA, abrirCuenta(actor, saldo));
    }

    @Dado("que {actor} tiene una segunda cuenta con saldo {int}")
    public void tieneUnaSegundaCuenta(Actor actor, int saldo) {
        actor.remember(MemoryKeys.SEGUNDA_CUENTA, abrirCuenta(actor, saldo));
    }

    @Dado("que el administrador bloqueó la cuenta de {actor}")
    public void elAdministradorBloqueoLaCuenta(Actor titular) {
        ApiDigitalBank.cambiarEstado(tokenDelAdministrador(), Elenco.cuenta(titular), "bloquear", MOTIVO);
    }

    @Dado("que el administrador cerró la segunda cuenta de {actor}")
    public void elAdministradorCerroLaSegundaCuenta(Actor titular) {
        ApiDigitalBank.cambiarEstado(tokenDelAdministrador(), Elenco.segundaCuenta(titular), "cerrar", MOTIVO);
    }

    @Dado("que el cajero depositó {int} en la cuenta de {actor}")
    public void elCajeroDeposito(int monto, Actor titular) {
        ApiDigitalBank.depositar(tokenDelCajero(), Elenco.cuenta(titular), BigDecimal.valueOf(monto), "EFECTIVO");
    }

    @Dado("que {actor} retiró {int} de su cuenta")
    public void retiro(Actor actor, int monto) {
        ApiDigitalBank.retirar(tokenDe(actor), Elenco.cuenta(actor), BigDecimal.valueOf(monto));
    }

    @Dado("que {actor} se equivocó de contraseña {int} veces")
    public void seEquivocoDeContrasena(Actor actor, int veces) {
        for (int intento = 1; intento <= veces; intento++) {
            int codigo = ApiDigitalBank.codigoDelInicioDeSesion(
                    Elenco.credenciales(actor).conPassword("Incorrecta#" + intento));
            assertThat("Código del intento fallido número " + intento, codigo, is(401));
        }
    }

    // ---------- Apoyo ----------

    private Cuenta abrirCuenta(Actor actor, int saldo) {
        return ApiDigitalBank.abrirCuenta(tokenDe(actor), Elenco.cliente(actor), "AHORROS", BigDecimal.valueOf(saldo));
    }

    /** Token del cliente, guardado en su memoria para no iniciar sesión en cada llamada. */
    private String tokenDe(Actor actor) {
        String token = actor.recall(TOKEN);
        if (token == null) {
            token = ApiDigitalBank.iniciarSesion(Elenco.credenciales(actor));
            actor.remember(TOKEN, token);
        }
        return token;
    }

    private String tokenDelAdministrador() {
        return ApiDigitalBank.iniciarSesion(Usuarios.administrador());
    }

    private String tokenDelCajero() {
        return ApiDigitalBank.iniciarSesion(Usuarios.cajero());
    }
}
