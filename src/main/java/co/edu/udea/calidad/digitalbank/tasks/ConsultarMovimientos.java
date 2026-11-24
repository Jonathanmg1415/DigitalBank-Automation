package co.edu.udea.calidad.digitalbank.tasks;

import co.edu.udea.calidad.digitalbank.interactions.EscribirFecha;
import co.edu.udea.calidad.digitalbank.interactions.EsperarRespuesta;
import co.edu.udea.calidad.digitalbank.interactions.SeleccionarCuenta;
import co.edu.udea.calidad.digitalbank.models.Cliente;
import co.edu.udea.calidad.digitalbank.models.Cuenta;
import co.edu.udea.calidad.digitalbank.utils.Wait;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.SelectFromOptions;

import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginaMovimientos.BTN_CONSULTAR;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginaMovimientos.LST_TIPO;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginaMovimientos.TXT_FECHA_FIN;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginaMovimientos.TXT_FECHA_INICIO;

/**
 * HU-13. Consulta el historial de movimientos de una cuenta, con filtros opcionales por tipo y
 * por rango de fechas, desde la opción "Movimientos".
 */
public class ConsultarMovimientos implements Task {

    private final Cliente titular;
    private final Cuenta cuenta;
    private final String tipo;
    private final String fechaInicio;
    private final String fechaFin;

    public ConsultarMovimientos(Cliente titular, Cuenta cuenta, String tipo, String fechaInicio, String fechaFin) {
        this.titular = titular;
        this.cuenta = cuenta;
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    /** Uso: {@code ConsultarMovimientos.deLaCuenta(cuenta).de(ana).conTipo("RETIRO").consultar()}. */
    public static Constructor deLaCuenta(Cuenta cuenta) {
        return new Constructor(cuenta);
    }

    @Override
    @Step("{0} consulta los movimientos de la cuenta #cuenta (tipo: '#tipo', desde '#fechaInicio' hasta '#fechaFin')")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                IrA.laOpcion("movimientos"),
                SeleccionarCuenta.en("movimientos", titular, cuenta));
        if (!tipo.isEmpty()) {
            actor.attemptsTo(SelectFromOptions.byValue(tipo).from(LST_TIPO));
        }
        if (!fechaInicio.isEmpty()) {
            actor.attemptsTo(EscribirFecha.elValor(fechaInicio).en(TXT_FECHA_INICIO));
        }
        if (!fechaFin.isEmpty()) {
            actor.attemptsTo(EscribirFecha.elValor(fechaFin).en(TXT_FECHA_FIN));
        }
        Wait.aLittleBitTime();
        actor.attemptsTo(
                Click.on(BTN_CONSULTAR),
                EsperarRespuesta.delBoton(BTN_CONSULTAR));
        Wait.aLittleBitTime();
    }

    public static final class Constructor {
        private final Cuenta cuenta;
        private Cliente titular;
        private String tipo = "";
        private String fechaInicio = "";
        private String fechaFin = "";

        private Constructor(Cuenta cuenta) {
            this.cuenta = cuenta;
        }

        public Constructor de(Cliente titular) {
            this.titular = titular;
            return this;
        }

        /** DEPOSITO, RETIRO, TRANSFERENCIA o REVERSO. */
        public Constructor conTipo(String tipo) {
            this.tipo = tipo;
            return this;
        }

        /** Fechas en formato yyyy-MM-dd. */
        public Constructor entre(String fechaInicio, String fechaFin) {
            this.fechaInicio = fechaInicio;
            this.fechaFin = fechaFin;
            return this;
        }

        public ConsultarMovimientos consultar() {
            return Tasks.instrumented(ConsultarMovimientos.class, titular, cuenta, tipo, fechaInicio, fechaFin);
        }
    }
}
