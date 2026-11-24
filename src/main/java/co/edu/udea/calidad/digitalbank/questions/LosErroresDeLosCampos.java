package co.edu.udea.calidad.digitalbank.questions;

import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.annotations.Subject;

import java.util.List;

import static co.edu.udea.calidad.digitalbank.userinterfaces.Mensajes.ERRORES_DE_CAMPO;

/**
 * Errores de validación que el frontend muestra debajo de los campos, antes de llamar al
 * backend (por ejemplo "Escriba el monto a retirar.").
 */
public class LosErroresDeLosCampos implements Question<List<String>> {

    public static LosErroresDeLosCampos visibles() {
        return new LosErroresDeLosCampos();
    }

    @Override
    @Subject("los errores de los campos")
    public List<String> answeredBy(Actor actor) {
        return ERRORES_DE_CAMPO.resolveAllFor(actor).stream()
                .filter(WebElementFacade::isVisible)
                .map(elemento -> elemento.getText().trim())
                .toList();
    }
}
