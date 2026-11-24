package co.edu.udea.calidad.digitalbank.questions;

import co.edu.udea.calidad.digitalbank.utils.Montos;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

import java.math.BigDecimal;

import static co.edu.udea.calidad.digitalbank.userinterfaces.Comprobante.DATO;

/**
 * Datos del comprobante de una operación. Recibe el {@code data-testid} del dato, por ejemplo
 * {@code retiro-resultado-saldo-nuevo} o {@code estado-cuenta-resultado-estado-nuevo}.
 */
public final class ElComprobante {

    private ElComprobante() {
    }

    /** Texto del dato tal como se ve, por ejemplo "Bloqueada". */
    public static Question<String> texto(String dato) {
        return Question.about("el dato " + dato + " del comprobante")
                .answeredBy(actor -> Text.of(DATO.of(dato)).answeredBy(actor).trim());
    }

    /** Monto del dato convertido a número, por ejemplo 150000.00. */
    public static Question<BigDecimal> monto(String dato) {
        return Question.about("el monto " + dato + " del comprobante")
                .answeredBy(actor -> Montos.aNumero(Text.of(DATO.of(dato)).answeredBy(actor)));
    }
}
