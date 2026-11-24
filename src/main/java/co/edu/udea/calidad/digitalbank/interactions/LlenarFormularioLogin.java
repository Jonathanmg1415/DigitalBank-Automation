package co.edu.udea.calidad.digitalbank.interactions;

import co.edu.udea.calidad.digitalbank.models.Credenciales;
import co.edu.udea.calidad.digitalbank.utils.Wait;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;

import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginaLogin.BTN_INGRESAR;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginaLogin.TXT_EMAIL;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginaLogin.TXT_PASSWORD;

/**
 * Diligencia el formulario de inicio de sesión y hace clic en "Ingresar". No verifica el
 * resultado: así sirve tanto para credenciales válidas como inválidas.
 */
public class LlenarFormularioLogin implements Interaction {

    private final Credenciales credenciales;

    public LlenarFormularioLogin(Credenciales credenciales) {
        this.credenciales = credenciales;
    }

    public static LlenarFormularioLogin con(Credenciales credenciales) {
        return Tasks.instrumented(LlenarFormularioLogin.class, credenciales);
    }

    @Override
    @Step("{0} diligencia el formulario de inicio de sesión como #credenciales")
    public <T extends Actor> void performAs(T actor) {
        TXT_EMAIL.resolveFor(actor).clear();
        if (!credenciales.email().isEmpty()) {
            actor.attemptsTo(Enter.theValue(credenciales.email()).into(TXT_EMAIL));
        }
        // La contraseña se escribe directamente sobre el elemento, sin la acción Enter,
        // para que el reporte de Serenity no la muestre en texto plano.
        TXT_PASSWORD.resolveFor(actor).clear();
        if (!credenciales.password().isEmpty()) {
            TXT_PASSWORD.resolveFor(actor).type(credenciales.password());
        }
        Wait.aLittleBitTime();
        actor.attemptsTo(Click.on(BTN_INGRESAR));
    }
}
