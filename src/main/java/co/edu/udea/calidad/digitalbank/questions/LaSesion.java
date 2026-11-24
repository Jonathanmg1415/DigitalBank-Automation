package co.edu.udea.calidad.digitalbank.questions;

import co.edu.udea.calidad.digitalbank.utils.Configuracion;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.waits.WaitUntil;

import java.util.List;

import static co.edu.udea.calidad.digitalbank.userinterfaces.Encabezado.LBL_ROL;
import static co.edu.udea.calidad.digitalbank.userinterfaces.Encabezado.LBL_TITULO;
import static co.edu.udea.calidad.digitalbank.userinterfaces.Encabezado.OPCIONES_MENU;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginaLogin.FORMULARIO;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/** Lo que el usuario ve de su sesión: rol, menú, título de la página y si está en el login. */
public final class LaSesion {

    private LaSesion() {
    }

    /** Rol que muestra el encabezado: "Cliente", "Cajero" o "Administrador". */
    public static Question<String> rolDelUsuario() {
        return Question.about("el rol del usuario").answeredBy(actor -> Text.of(LBL_ROL).answeredBy(actor).trim());
    }

    /** Textos de las opciones visibles en el menú lateral. */
    public static Question<List<String>> opcionesDelMenu() {
        return Question.about("las opciones del menú").answeredBy(actor ->
                OPCIONES_MENU.resolveAllFor(actor).stream().map(WebElementFacade::getText).map(String::trim).toList());
    }

    /** Título de la pantalla actual, por ejemplo "Acceso no permitido". */
    public static Question<String> tituloDeLaPagina() {
        return Question.about("el título de la página").answeredBy(actor -> {
            actor.attemptsTo(WaitUntil.the(LBL_TITULO, isVisible())
                    .forNoMoreThan(Configuracion.segundosDeEspera()).seconds());
            return Text.of(LBL_TITULO).answeredBy(actor).trim();
        });
    }

    /** Indica si se muestra el formulario de inicio de sesión. */
    public static Question<Boolean> estaEnElLogin() {
        return Question.about("si se muestra el formulario de inicio de sesión").answeredBy(actor ->
                FORMULARIO.resolveAllFor(actor).stream().anyMatch(WebElementFacade::isVisible));
    }
}
