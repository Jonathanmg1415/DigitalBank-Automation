package co.edu.udea.calidad.digitalbank.tasks;

import co.edu.udea.calidad.digitalbank.utils.Wait;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static co.edu.udea.calidad.digitalbank.userinterfaces.Encabezado.BTN_CERRAR_SESION;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginaLogin.FORMULARIO;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/** Cierra la sesión desde el encabezado y espera a ver de nuevo el formulario de login. */
public class CerrarSesion implements Task {

    public static CerrarSesion delUsuarioActual() {
        return Tasks.instrumented(CerrarSesion.class);
    }

    @Override
    @Step("{0} cierra la sesión")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(BTN_CERRAR_SESION),
                WaitUntil.the(FORMULARIO, isVisible()).forNoMoreThan(10).seconds());
        Wait.aLittleBitTime();
    }
}
