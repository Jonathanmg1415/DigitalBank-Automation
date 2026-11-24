package co.edu.udea.calidad.digitalbank.stepdefinitions;

import co.edu.udea.calidad.digitalbank.questions.LosMovimientos;
import co.edu.udea.calidad.digitalbank.tasks.ConsultarMovimientos;
import co.edu.udea.calidad.digitalbank.utils.Montos;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.Actor;

import java.util.Arrays;
import java.util.List;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/** Pasos del historial de movimientos por cuenta (HU-13). */
public class MovimientosStepDefinitions {

    @Cuando("{actor} consulta los movimientos de la cuenta de {actor}")
    public void consultaLosMovimientos(Actor consultor, Actor titular) {
        consultar(consultor, ConsultarMovimientos.deLaCuenta(Elenco.cuenta(titular)).de(Elenco.cliente(titular)));
    }

    @Cuando("{actor} consulta los movimientos de la cuenta de {actor} con el tipo {string}")
    public void consultaLosMovimientosPorTipo(Actor consultor, Actor titular, String tipo) {
        consultar(consultor, ConsultarMovimientos.deLaCuenta(Elenco.cuenta(titular))
                .de(Elenco.cliente(titular)).conTipo(tipo));
    }

    @Cuando("{actor} consulta los movimientos de la cuenta de {actor} entre {string} y {string}")
    public void consultaLosMovimientosEntreFechas(Actor consultor, Actor titular, String desde, String hasta) {
        consultar(consultor, ConsultarMovimientos.deLaCuenta(Elenco.cuenta(titular))
                .de(Elenco.cliente(titular)).entre(desde, hasta));
    }

    /** Recibe los tipos separados por comas, por ejemplo "Depósito, Retiro, Depósito". */
    @Entonces("los movimientos son, del más reciente al más antiguo: {string}")
    public void losMovimientosSon(String tipos) {
        List<String> esperados = Arrays.stream(tipos.split(",")).map(String::trim).toList();
        theActorInTheSpotlight().should(seeThat(LosMovimientos.tipos(), equalTo(esperados)));
    }

    @Entonces("el saldo resultante del movimiento más reciente es {int}")
    public void elSaldoDelMovimientoMasRecienteEs(int saldo) {
        theActorInTheSpotlight().should(
                seeThat(LosMovimientos.saldoDelMasReciente(), equalTo(Montos.de(String.valueOf(saldo)))));
    }

    @Entonces("{actor} debería ver que no hay movimientos")
    public void deberiaVerQueNoHayMovimientos(Actor actor) {
        actor.should(seeThat(LosMovimientos.estanVacios(), is(true)));
    }

    private void consultar(Actor consultor, ConsultarMovimientos.Constructor consulta) {
        Elenco.conSesion(consultor).attemptsTo(consulta.consultar());
    }
}
