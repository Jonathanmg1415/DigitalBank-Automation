package co.edu.udea.calidad.digitalbank.questions;

import co.edu.udea.calidad.digitalbank.utils.Montos;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Question;

import java.math.BigDecimal;
import java.util.List;

import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginaMovimientos.CELDAS_SALDO_NUEVO;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginaMovimientos.CELDAS_TIPO;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginaMovimientos.LBL_SIN_MOVIMIENTOS;

/** Resultado de la consulta del historial de movimientos (HU-13), en el orden en que se muestra. */
public final class LosMovimientos {

    private LosMovimientos() {
    }

    /** Tipo de cada movimiento, por ejemplo ["Depósito", "Transferencia saliente", "Retiro"]. */
    public static Question<List<String>> tipos() {
        return Question.about("los tipos de los movimientos").answeredBy(actor ->
                CELDAS_TIPO.resolveAllFor(actor).stream().map(WebElementFacade::getText).map(String::trim).toList());
    }

    /** Saldo resultante del movimiento más reciente (la primera fila). */
    public static Question<BigDecimal> saldoDelMasReciente() {
        return Question.about("el saldo resultante del movimiento más reciente").answeredBy(actor ->
                Montos.aNumero(CELDAS_SALDO_NUEVO.resolveAllFor(actor).get(0).getText()));
    }

    /** Indica si la pantalla muestra el mensaje de que no hay movimientos. */
    public static Question<Boolean> estanVacios() {
        return Question.about("si la lista de movimientos está vacía").answeredBy(actor ->
                LBL_SIN_MOVIMIENTOS.resolveAllFor(actor).stream().anyMatch(WebElementFacade::isVisible));
    }
}
