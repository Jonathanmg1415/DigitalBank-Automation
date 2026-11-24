package co.edu.udea.calidad.digitalbank.tasks;

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
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;

import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeOperaciones.BTN_DEPOSITAR;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeOperaciones.LST_ORIGEN_DEPOSITO;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeOperaciones.TXT_MONTO_DEPOSITO;

/**
 * HU-10. El cajero (o el administrador) busca al cliente por su documento, elige la cuenta y
 * registra un depósito desde la opción "Depósitos".
 */
public class Depositar implements Task {

    private final String monto;
    private final Cliente titular;
    private final Cuenta cuenta;
    private final String origen;

    public Depositar(String monto, Cliente titular, Cuenta cuenta, String origen) {
        this.monto = monto;
        this.titular = titular;
        this.cuenta = cuenta;
        this.origen = origen;
    }

    /** Uso: {@code Depositar.elMonto("1000").enLaCuenta(cuenta).de(ana).enEfectivo()}. */
    public static Constructor elMonto(String monto) {
        return new Constructor(monto);
    }

    @Override
    @Step("{0} deposita #monto en la cuenta #cuenta de #titular")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                IrA.laOpcion("depositos"),
                SeleccionarCuenta.en("deposito", titular, cuenta),
                Enter.theValue(monto).into(TXT_MONTO_DEPOSITO),
                SelectFromOptions.byValue(origen).from(LST_ORIGEN_DEPOSITO));
        Wait.aLittleBitTime();
        actor.attemptsTo(
                Click.on(BTN_DEPOSITAR),
                EsperarRespuesta.delBoton(BTN_DEPOSITAR));
        Wait.aLittleBitTime();
    }

    public static final class Constructor {
        private final String monto;
        private Cuenta cuenta;
        private Cliente titular;

        private Constructor(String monto) {
            this.monto = monto;
        }

        public Constructor enLaCuenta(Cuenta cuenta) {
            this.cuenta = cuenta;
            return this;
        }

        public Constructor de(Cliente titular) {
            this.titular = titular;
            return this;
        }

        /** Origen EFECTIVO, CHEQUE u OTRO. */
        public Depositar conOrigen(String origen) {
            return Tasks.instrumented(Depositar.class, monto, titular, cuenta, origen);
        }

        public Depositar enEfectivo() {
            return conOrigen("EFECTIVO");
        }
    }
}
