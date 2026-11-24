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

import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginaEstadoCuentas.BTN_CONFIRMAR;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginaEstadoCuentas.RDB_ACCION;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginaEstadoCuentas.TXT_MOTIVO;

/**
 * HU-07a y HU-07b. El administrador bloquea, desbloquea o cierra la cuenta de un cliente
 * desde la opción "Estado de cuentas".
 */
public class CambiarEstadoDeCuenta implements Task {

    private final String accion;
    private final Cliente titular;
    private final Cuenta cuenta;
    private final String motivo;

    public CambiarEstadoDeCuenta(String accion, Cliente titular, Cuenta cuenta, String motivo) {
        this.accion = accion;
        this.titular = titular;
        this.cuenta = cuenta;
        this.motivo = motivo;
    }

    /**
     * Uso: {@code CambiarEstadoDeCuenta.para("bloquear").laCuenta(cuenta).de(ana).conMotivo("...")}.
     *
     * @param accion "bloquear", "desbloquear" o "cerrar"
     */
    public static Constructor para(String accion) {
        return new Constructor(accion);
    }

    @Override
    @Step("{0} quiere #accion la cuenta #cuenta de #titular con el motivo '#motivo'")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                IrA.laOpcion("estado-cuentas"),
                SeleccionarCuenta.en("estado-cuenta", titular, cuenta),
                Click.on(RDB_ACCION.of(accion)));
        if (!motivo.isEmpty()) {
            actor.attemptsTo(Enter.theValue(motivo).into(TXT_MOTIVO));
        }
        Wait.aLittleBitTime();
        actor.attemptsTo(
                Click.on(BTN_CONFIRMAR),
                EsperarRespuesta.delBoton(BTN_CONFIRMAR));
        Wait.aLittleBitTime();
    }

    public static final class Constructor {
        private final String accion;
        private Cuenta cuenta;
        private Cliente titular;

        private Constructor(String accion) {
            this.accion = accion;
        }

        public Constructor laCuenta(Cuenta cuenta) {
            this.cuenta = cuenta;
            return this;
        }

        public Constructor de(Cliente titular) {
            this.titular = titular;
            return this;
        }

        public CambiarEstadoDeCuenta conMotivo(String motivo) {
            return Tasks.instrumented(CambiarEstadoDeCuenta.class, accion, titular, cuenta, motivo);
        }
    }
}
