package co.edu.udea.calidad.digitalbank.stepdefinitions;

import co.edu.udea.calidad.digitalbank.models.Cuenta;
import co.edu.udea.calidad.digitalbank.questions.ElComprobante;
import co.edu.udea.calidad.digitalbank.tasks.Depositar;
import co.edu.udea.calidad.digitalbank.tasks.Retirar;
import co.edu.udea.calidad.digitalbank.tasks.Transferir;
import co.edu.udea.calidad.digitalbank.utils.Montos;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.Actor;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.equalTo;

/**
 * Pasos de las operaciones con dinero: retiro (HU-11), transferencia (HU-09) y depósito (HU-10).
 * Los montos llegan como texto para poder probar "0", "-1", "200000.01" o un campo vacío.
 */
public class OperacionesStepDefinitions {

    private static final String PREFIJO_CUENTA_DE = "la cuenta de ";
    private static final String PREFIJO_SEGUNDA_CUENTA_DE = "la segunda cuenta de ";
    private static final String PROPIA_CUENTA = "su propia cuenta";

    // ---------- Retiro ----------

    @Cuando("{actor} retira {string} de su cuenta")
    public void retiraDeSuCuenta(Actor actor, String monto) {
        Elenco.conSesion(actor).attemptsTo(Retirar.elMonto(monto).deLaCuenta(Elenco.cuenta(actor)));
    }

    @Entonces("el comprobante del retiro muestra el saldo anterior {int} y el saldo nuevo {int}")
    public void elComprobanteDelRetiroMuestraLosSaldos(int saldoAnterior, int saldoNuevo) {
        theActorInTheSpotlight().should(
                seeThat(ElComprobante.monto("retiro-resultado-saldo-anterior"), equalTo(monto(saldoAnterior))),
                seeThat(ElComprobante.monto("retiro-resultado-saldo-nuevo"), equalTo(monto(saldoNuevo))));
    }

    // ---------- Transferencia ----------

    @Cuando("{actor} transfiere {string} hacia {string} con la descripción {string}")
    public void transfiereConDescripcion(Actor actor, String monto, String destino, String descripcion) {
        // El destino se resuelve antes: resolverlo llama al otro actor y lo pone en escena.
        String numeroDestino = numeroDeCuenta(actor, destino);
        Elenco.conSesion(actor).attemptsTo(Transferir.elMonto(monto)
                .desde(Elenco.cuenta(actor))
                .hacia(numeroDestino)
                .conDescripcion(descripcion));
    }

    @Cuando("{actor} transfiere {string} hacia {string}")
    public void transfiere(Actor actor, String monto, String destino) {
        String numeroDestino = numeroDeCuenta(actor, destino);
        Elenco.conSesion(actor).attemptsTo(Transferir.elMonto(monto)
                .desde(Elenco.cuenta(actor))
                .hacia(numeroDestino)
                .sinDescripcion());
    }

    @Cuando("{actor} transfiere {string} hacia {string} con una descripción de {int} letras")
    public void transfiereConDescripcionDeLongitud(Actor actor, String monto, String destino, int letras) {
        transfiereConDescripcion(actor, monto, destino, "a".repeat(letras));
    }

    @Entonces("el comprobante de la transferencia muestra el monto {int} y el estado {string}")
    public void elComprobanteDeLaTransferencia(int monto, String estado) {
        theActorInTheSpotlight().should(
                seeThat(ElComprobante.monto("transferencia-resultado-monto"), equalTo(monto(monto))),
                seeThat(ElComprobante.texto("transferencia-resultado-estado"), equalTo(estado)));
    }

    // ---------- Depósito ----------

    @Cuando("{actor} deposita {string} en la cuenta de {actor}")
    public void depositaEnLaCuenta(Actor cajero, String monto, Actor titular) {
        depositar(cajero, monto, titular, Elenco.cuenta(titular));
    }

    @Cuando("{actor} deposita {string} en la segunda cuenta de {actor}")
    public void depositaEnLaSegundaCuenta(Actor cajero, String monto, Actor titular) {
        depositar(cajero, monto, titular, Elenco.segundaCuenta(titular));
    }

    @Entonces("el comprobante del depósito muestra el saldo nuevo {int}")
    public void elComprobanteDelDepositoMuestraElSaldoNuevo(int saldoNuevo) {
        theActorInTheSpotlight().should(
                seeThat(ElComprobante.monto("deposito-resultado-saldo-nuevo"), equalTo(monto(saldoNuevo))));
    }

    // ---------- Apoyo ----------

    private void depositar(Actor cajero, String monto, Actor titular, Cuenta cuenta) {
        Elenco.conSesion(cajero).attemptsTo(
                Depositar.elMonto(monto).enLaCuenta(cuenta).de(Elenco.cliente(titular)).enEfectivo());
    }

    /**
     * Traduce el destino del feature a un número de cuenta: "la cuenta de Luis",
     * "la segunda cuenta de Luis", "su propia cuenta" o un número escrito directamente.
     */
    private String numeroDeCuenta(Actor origen, String destino) {
        if (destino.startsWith(PREFIJO_SEGUNDA_CUENTA_DE)) {
            return Elenco.segundaCuenta(Elenco.actor(destino.substring(PREFIJO_SEGUNDA_CUENTA_DE.length()))).numeroCuenta();
        }
        if (destino.startsWith(PREFIJO_CUENTA_DE)) {
            return Elenco.cuenta(Elenco.actor(destino.substring(PREFIJO_CUENTA_DE.length()))).numeroCuenta();
        }
        if (PROPIA_CUENTA.equals(destino)) {
            return Elenco.cuenta(origen).numeroCuenta();
        }
        return destino;
    }

    private static java.math.BigDecimal monto(int valor) {
        return Montos.de(String.valueOf(valor));
    }
}
