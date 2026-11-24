package co.edu.udea.calidad.digitalbank.tasks;

import co.edu.udea.calidad.digitalbank.interactions.EsperarRespuesta;
import co.edu.udea.calidad.digitalbank.interactions.SeleccionarCuenta;
import co.edu.udea.calidad.digitalbank.models.Cliente;
import co.edu.udea.calidad.digitalbank.models.Cuenta;
import co.edu.udea.calidad.digitalbank.utils.MemoryKeys;
import co.edu.udea.calidad.digitalbank.utils.Wait;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;

import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeOperaciones.BTN_RETIRAR;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeOperaciones.TXT_MONTO_RETIRO;

/**
 * HU-11. El cliente titular retira dinero de una de sus cuentas desde la opción "Retirar".
 * El monto es texto para poder probar valores como "0", "-1" o un campo vacío.
 */
public class Retirar implements Task {

    private final String monto;
    private final Cuenta cuenta;

    public Retirar(String monto, Cuenta cuenta) {
        this.monto = monto;
        this.cuenta = cuenta;
    }

    /** Uso: {@code Retirar.elMonto("50000").deLaCuenta(cuenta)}. */
    public static Constructor elMonto(String monto) {
        return new Constructor(monto);
    }

    @Override
    @Step("{0} retira #monto de la cuenta #cuenta")
    public <T extends Actor> void performAs(T actor) {
        Cliente titular = actor.recall(MemoryKeys.CLIENTE);
        actor.attemptsTo(
                IrA.laOpcion("retirar"),
                SeleccionarCuenta.en("retiro", titular, cuenta));
        // Un monto vacío se deja así para probar que el campo es obligatorio.
        if (!monto.isEmpty()) {
            actor.attemptsTo(Enter.theValue(monto).into(TXT_MONTO_RETIRO));
        }
        Wait.aLittleBitTime();
        actor.attemptsTo(
                Click.on(BTN_RETIRAR),
                EsperarRespuesta.delBoton(BTN_RETIRAR));
        Wait.aLittleBitTime();
    }

    public static final class Constructor {
        private final String monto;

        private Constructor(String monto) {
            this.monto = monto;
        }

        public Retirar deLaCuenta(Cuenta cuenta) {
            return Tasks.instrumented(Retirar.class, monto, cuenta);
        }
    }
}
