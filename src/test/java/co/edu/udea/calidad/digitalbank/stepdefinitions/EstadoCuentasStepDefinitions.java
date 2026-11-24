package co.edu.udea.calidad.digitalbank.stepdefinitions;

import co.edu.udea.calidad.digitalbank.models.Cuenta;
import co.edu.udea.calidad.digitalbank.questions.ElComprobante;
import co.edu.udea.calidad.digitalbank.questions.LaCuenta;
import co.edu.udea.calidad.digitalbank.tasks.CambiarEstadoDeCuenta;
import co.edu.udea.calidad.digitalbank.tasks.IrA;
import co.edu.udea.calidad.digitalbank.utils.Montos;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.Actor;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.equalTo;

/** Pasos de bloqueo, desbloqueo (HU-07a) y cierre de cuentas (HU-07b). */
public class EstadoCuentasStepDefinitions {

    /** Ejemplo: "el administrador bloquea la cuenta de Ana con el motivo ..." o "intenta cerrar ...". */
    @Cuando("{actor} {accion} la cuenta de {actor} con el motivo {string}")
    public void cambiaElEstadoDeLaCuenta(Actor administrador, String accion, Actor titular, String motivo) {
        cambiarEstado(administrador, accion, titular, Elenco.cuenta(titular), motivo);
    }

    @Cuando("{actor} {accion} la segunda cuenta de {actor} con el motivo {string}")
    public void cambiaElEstadoDeLaSegundaCuenta(Actor administrador, String accion, Actor titular, String motivo) {
        cambiarEstado(administrador, accion, titular, Elenco.segundaCuenta(titular), motivo);
    }

    @Entonces("el comprobante muestra que la cuenta pasó del estado {string} al estado {string}")
    public void elComprobanteMuestraElCambioDeEstado(String estadoAnterior, String estadoNuevo) {
        theActorInTheSpotlight().should(
                seeThat(ElComprobante.texto("estado-cuenta-resultado-estado-anterior"), equalTo(estadoAnterior)),
                seeThat(ElComprobante.texto("estado-cuenta-resultado-estado-nuevo"), equalTo(estadoNuevo)));
    }

    @Entonces("el comprobante muestra el motivo {string}")
    public void elComprobanteMuestraElMotivo(String motivo) {
        theActorInTheSpotlight().should(
                seeThat(ElComprobante.texto("estado-cuenta-resultado-motivo"), equalTo(motivo)));
    }

    @Entonces("{actor} ve su segunda cuenta en estado {string} con saldo {int}")
    public void veSuSegundaCuenta(Actor titular, String estado, int saldo) {
        Cuenta cuenta = Elenco.segundaCuenta(titular);
        Elenco.conSesion(titular).attemptsTo(IrA.laOpcion("mis-cuentas"));
        titular.should(
                seeThat(LaCuenta.estadoDe(cuenta), equalTo(estado)),
                seeThat(LaCuenta.saldoDisponibleDe(cuenta), equalTo(Montos.de(String.valueOf(saldo)))));
    }

    private void cambiarEstado(Actor administrador, String accion, Actor titular, Cuenta cuenta, String motivo) {
        Elenco.conSesion(administrador).attemptsTo(CambiarEstadoDeCuenta.para(accion)
                .laCuenta(cuenta)
                .de(Elenco.cliente(titular))
                .conMotivo(motivo));
    }
}
