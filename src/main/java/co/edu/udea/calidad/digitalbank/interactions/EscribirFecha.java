package co.edu.udea.calidad.digitalbank.interactions;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

/**
 * Escribe una fecha (yyyy-MM-dd) en un campo {@code input type="date"}.
 *
 * <p>Chrome muestra esos campos con el formato del idioma del sistema (dd/mm/aaaa) y teclear la
 * fecha depende de ese idioma. Por eso se asigna el valor con JavaScript y se dispara el evento
 * "input" que React escucha para actualizar el formulario.</p>
 */
public class EscribirFecha implements Interaction {

    private static final String SCRIPT =
            "const campo = arguments[0];"
            + "const asignar = Object.getOwnPropertyDescriptor(HTMLInputElement.prototype, 'value').set;"
            + "asignar.call(campo, arguments[1]);"
            + "campo.dispatchEvent(new Event('input', { bubbles: true }));";

    private final String fecha;
    private final Target campo;

    public EscribirFecha(String fecha, Target campo) {
        this.fecha = fecha;
        this.campo = campo;
    }

    /** Uso: {@code EscribirFecha.elValor("2026-10-20").en(TXT_FECHA_INICIO)}. */
    public static Constructor elValor(String fecha) {
        return new Constructor(fecha);
    }

    @Override
    @Step("{0} escribe la fecha #fecha en #campo")
    public <T extends Actor> void performAs(T actor) {
        WebElement elemento = campo.resolveFor(actor);
        ((JavascriptExecutor) BrowseTheWeb.as(actor).getDriver()).executeScript(SCRIPT, elemento, fecha);
        Serenity.recordReportData().withTitle("Fecha escrita en " + campo).andContents(fecha);
    }

    /** Permite escribir {@code EscribirFecha.elValor(fecha).en(campo)}. */
    public static final class Constructor {
        private final String fecha;

        private Constructor(String fecha) {
            this.fecha = fecha;
        }

        public EscribirFecha en(Target campo) {
            return Tasks.instrumented(EscribirFecha.class, fecha, campo);
        }
    }
}
