package co.edu.udea.calidad.digitalbank.questions;

import co.edu.udea.calidad.digitalbank.models.Cuenta;
import co.edu.udea.calidad.digitalbank.utils.Configuracion;
import co.edu.udea.calidad.digitalbank.utils.Montos;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.waits.WaitUntil;

import java.math.BigDecimal;

import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginaMisCuentas.ESTADO;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginaMisCuentas.SALDO_DISPONIBLE;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/**
 * Saldo y estado de una cuenta en la pantalla "Mis cuentas" (HU-08). El actor debe estar en esa
 * pantalla; la pregunta espera a que la fila de la cuenta aparezca.
 */
public final class LaCuenta {

    private LaCuenta() {
    }

    public static Question<BigDecimal> saldoDisponibleDe(Cuenta cuenta) {
        return Question.about("el saldo disponible de la cuenta " + cuenta).answeredBy(actor -> {
            actor.attemptsTo(WaitUntil.the(SALDO_DISPONIBLE.of(cuenta.numeroCuenta()), isVisible())
                    .forNoMoreThan(Configuracion.segundosDeEspera()).seconds());
            return Montos.aNumero(Text.of(SALDO_DISPONIBLE.of(cuenta.numeroCuenta())).answeredBy(actor));
        });
    }

    public static Question<String> estadoDe(Cuenta cuenta) {
        return Question.about("el estado de la cuenta " + cuenta).answeredBy(actor -> {
            actor.attemptsTo(WaitUntil.the(ESTADO.of(cuenta.numeroCuenta()), isVisible())
                    .forNoMoreThan(Configuracion.segundosDeEspera()).seconds());
            return Text.of(ESTADO.of(cuenta.numeroCuenta())).answeredBy(actor).trim();
        });
    }
}
