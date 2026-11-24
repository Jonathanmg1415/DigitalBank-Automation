package co.edu.udea.calidad.digitalbank.tasks;

import co.edu.udea.calidad.digitalbank.utils.Configuracion;
import co.edu.udea.calidad.digitalbank.utils.Wait;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Open;

/**
 * Abre el navegador en una ruta de Digital Bank. La URL base no está escrita en el código:
 * se toma de {@code webdriver.base.url} en {@code serenity.conf}.
 */
public class AbrirDigitalBank implements Task {

    private final String ruta;

    public AbrirDigitalBank(String ruta) {
        this.ruta = ruta;
    }

    /** Abre la pantalla de inicio de sesión. */
    public static AbrirDigitalBank enElLogin() {
        return enLaRuta("/login");
    }

    /** Abre una ruta escribiéndola en la barra de direcciones, por ejemplo "/retiros". */
    public static AbrirDigitalBank enLaRuta(String ruta) {
        return Tasks.instrumented(AbrirDigitalBank.class, ruta);
    }

    @Override
    @Step("{0} abre Digital Bank en #ruta")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Open.url(Configuracion.urlBase() + ruta));
        Wait.aLittleBitTime();
    }
}
