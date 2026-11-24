package co.edu.udea.calidad.digitalbank.questions;

import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.annotations.Subject;
import net.serenitybdd.screenplay.targets.Target;

import static co.edu.udea.calidad.digitalbank.userinterfaces.Mensajes.ALERTA_ERROR;
import static co.edu.udea.calidad.digitalbank.userinterfaces.Mensajes.ALERTA_EXITO;

/**
 * Texto del mensaje que muestra la pantalla después de una operación: el de éxito o el de error.
 * El mensaje de error trae el texto que envía el backend (por ejemplo "Saldo insuficiente").
 * Si no hay mensaje visible, responde una cadena vacía.
 */
public class ElMensaje implements Question<String> {

    private final Target mensaje;
    private final String descripcion;

    public ElMensaje(Target mensaje, String descripcion) {
        this.mensaje = mensaje;
        this.descripcion = descripcion;
    }

    public static ElMensaje deError() {
        return new ElMensaje(ALERTA_ERROR, "el mensaje de error");
    }

    public static ElMensaje deExito() {
        return new ElMensaje(ALERTA_EXITO, "el mensaje de éxito");
    }

    @Override
    @Subject("#descripcion")
    public String answeredBy(Actor actor) {
        return mensaje.resolveAllFor(actor).stream()
                .filter(WebElementFacade::isVisible)
                .map(elemento -> elemento.getText().trim())
                .findFirst()
                .orElse("");
    }
}
