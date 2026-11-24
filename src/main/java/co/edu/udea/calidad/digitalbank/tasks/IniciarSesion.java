package co.edu.udea.calidad.digitalbank.tasks;

import co.edu.udea.calidad.digitalbank.interactions.EsperarRespuesta;
import co.edu.udea.calidad.digitalbank.interactions.LlenarFormularioLogin;
import co.edu.udea.calidad.digitalbank.models.Credenciales;
import co.edu.udea.calidad.digitalbank.utils.Configuracion;
import co.edu.udea.calidad.digitalbank.utils.Wait;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static co.edu.udea.calidad.digitalbank.userinterfaces.Encabezado.LBL_ROL;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginaLogin.BTN_INGRESAR;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/**
 * Inicia sesión desde la pantalla de login.
 *
 * <ul>
 *   <li>{@link #con(Credenciales)}: credenciales válidas; espera a que el encabezado muestre el
 *   rol del usuario, que confirma que la sesión quedó iniciada.</li>
 *   <li>{@link #intentandoCon(Credenciales)}: cualquier credencial; solo espera la respuesta, sin
 *   suponer que funcionó (para los escenarios de error).</li>
 * </ul>
 */
public class IniciarSesion implements Task {

    private final Credenciales credenciales;
    private final boolean esperaExito;

    public IniciarSesion(Credenciales credenciales, boolean esperaExito) {
        this.credenciales = credenciales;
        this.esperaExito = esperaExito;
    }

    public static IniciarSesion con(Credenciales credenciales) {
        return Tasks.instrumented(IniciarSesion.class, credenciales, true);
    }

    public static IniciarSesion intentandoCon(Credenciales credenciales) {
        return Tasks.instrumented(IniciarSesion.class, credenciales, false);
    }

    @Override
    @Step("{0} inicia sesión como #credenciales")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                AbrirDigitalBank.enElLogin(),
                LlenarFormularioLogin.con(credenciales),
                EsperarRespuesta.delBoton(BTN_INGRESAR));
        if (esperaExito) {
            actor.attemptsTo(WaitUntil.the(LBL_ROL, isVisible())
                    .forNoMoreThan(Configuracion.segundosDeEspera()).seconds());
        }
        Wait.aLittleBitTime();
    }
}
