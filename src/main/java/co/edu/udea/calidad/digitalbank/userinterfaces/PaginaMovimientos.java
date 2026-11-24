package co.edu.udea.calidad.digitalbank.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/** Historial de movimientos de una cuenta ({@code /movimientos}, HU-13). */
public final class PaginaMovimientos {

    public static final Target TXT_FECHA_INICIO = Target.the("fecha inicial")
            .located(By.id("movimientos-fecha-inicio"));

    public static final Target TXT_FECHA_FIN = Target.the("fecha final")
            .located(By.id("movimientos-fecha-fin"));

    public static final Target LST_TIPO = Target.the("tipo de movimiento")
            .located(By.id("movimientos-tipo"));

    public static final Target BTN_CONSULTAR = Target.the("botón Consultar")
            .located(By.id("movimientos-consultar"));

    /** Tarjeta con el resultado (tabla o mensaje de lista vacía). */
    public static final Target RESULTADO = Target.the("resultado de la consulta")
            .located(By.id("resultado-movimientos"));

    /** Columna "Tipo" de cada fila, en el orden en que se muestran. */
    public static final Target CELDAS_TIPO = Target.the("tipos de los movimientos")
            .locatedBy("[data-testid^='fila-movimiento-'] [data-testid='movimiento-tipo']");

    /** Columna "Saldo nuevo" de cada fila. */
    public static final Target CELDAS_SALDO_NUEVO = Target.the("saldos nuevos de los movimientos")
            .locatedBy("[data-testid^='fila-movimiento-'] [data-testid='movimiento-saldo-nuevo']");

    public static final Target LBL_SIN_MOVIMIENTOS = Target.the("mensaje de lista vacía")
            .located(By.id("movimientos-vacio"));

    private PaginaMovimientos() {
    }
}
