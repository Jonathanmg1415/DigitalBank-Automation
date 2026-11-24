package co.edu.udea.calidad.digitalbank.stepdefinitions;

import co.edu.udea.calidad.digitalbank.questions.ElMensaje;
import co.edu.udea.calidad.digitalbank.questions.LaCuenta;
import co.edu.udea.calidad.digitalbank.questions.LaSesion;
import co.edu.udea.calidad.digitalbank.questions.LosErroresDeLosCampos;
import co.edu.udea.calidad.digitalbank.tasks.AbrirDigitalBank;
import co.edu.udea.calidad.digitalbank.tasks.IrA;
import co.edu.udea.calidad.digitalbank.utils.Montos;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.Actor;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Pasos que se repiten en varios features: iniciar sesión, mensajes, saldo de una cuenta,
 * opciones del menú y acceso por dirección. Cucumber no permite definir dos veces el mismo
 * texto de paso, por eso viven aquí una sola vez.
 */
public class ComunesStepDefinitions {

    @Dado("{actor} inicia sesión en Digital Bank")
    public void iniciaSesion(Actor actor) {
        Elenco.conSesion(actor);
    }

    @Cuando("{actor} intenta entrar a la dirección {string}")
    public void intentaEntrarALaDireccion(Actor actor, String ruta) {
        Elenco.enEscena(actor).attemptsTo(AbrirDigitalBank.enLaRuta(ruta));
    }

    // ---------- Mensajes ----------

    @Entonces("{actor} debería ver el mensaje de éxito {string}")
    public void deberiaVerElMensajeDeExito(Actor actor, String mensaje) {
        actor.should(seeThat(ElMensaje.deExito(), equalTo(mensaje)));
    }

    @Entonces("{actor} debería ver el mensaje de error {string}")
    public void deberiaVerElMensajeDeError(Actor actor, String mensaje) {
        actor.should(seeThat(ElMensaje.deError(), equalTo(mensaje)));
    }

    @Entonces("{actor} debería ver el error de campo {string}")
    public void deberiaVerElErrorDeCampo(Actor actor, String mensaje) {
        actor.should(seeThat(LosErroresDeLosCampos.visibles(), hasItem(mensaje)));
    }

    // ---------- Saldo ----------

    /** El cliente revisa el saldo en "Mis cuentas" desde su propio navegador. */
    @Entonces("el saldo disponible de la cuenta de {actor} es {int}")
    public void elSaldoDisponibleEs(Actor titular, int saldo) {
        Elenco.conSesion(titular).attemptsTo(IrA.laOpcion("mis-cuentas"));
        titular.should(seeThat(LaCuenta.saldoDisponibleDe(Elenco.cuenta(titular)),
                equalTo(Montos.de(String.valueOf(saldo)))));
    }

    // ---------- Permisos por rol ----------

    @Entonces("{actor} no debería ver la opción {string} en el menú")
    public void noDeberiaVerLaOpcion(Actor actor, String opcion) {
        actor.should(seeThat(LaSesion.opcionesDelMenu(), not(hasItem(opcion))));
    }

    @Entonces("si {actor} intenta entrar a la dirección {string} debería ver la página {string}")
    public void siIntentaEntrarDeberiaVerLaPagina(Actor actor, String ruta, String titulo) {
        actor.attemptsTo(AbrirDigitalBank.enLaRuta(ruta));
        actor.should(seeThat(LaSesion.tituloDeLaPagina(), equalTo(titulo)));
    }

    @Entonces("{actor} debería ver el formulario de inicio de sesión")
    public void deberiaVerElFormularioDeInicioDeSesion(Actor actor) {
        actor.should(seeThat(LaSesion.estaEnElLogin(), is(true)));
    }
}
