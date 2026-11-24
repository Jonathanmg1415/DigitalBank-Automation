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

import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeOperaciones.BTN_TRANSFERIR;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeOperaciones.TXT_CUENTA_DESTINO;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeOperaciones.TXT_DESCRIPCION;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeOperaciones.TXT_MONTO_TRANSFERENCIA;

/**
 * HU-09a, HU-09b y HU-09c. El cliente titular transfiere dinero desde una de sus cuentas
 * hacia el número de otra cuenta, desde la opción "Transferir".
 */
public class Transferir implements Task {

    private final String monto;
    private final Cuenta origen;
    private final String numeroDestino;
    private final String descripcion;

    public Transferir(String monto, Cuenta origen, String numeroDestino, String descripcion) {
        this.monto = monto;
        this.origen = origen;
        this.numeroDestino = numeroDestino;
        this.descripcion = descripcion;
    }

    /** Uso: {@code Transferir.elMonto("30000").desde(cuentaAna).hacia("1000000002").conDescripcion("Pago")}. */
    public static Constructor elMonto(String monto) {
        return new Constructor(monto);
    }

    @Override
    @Step("{0} transfiere #monto desde la cuenta #origen hacia la cuenta #numeroDestino")
    public <T extends Actor> void performAs(T actor) {
        Cliente titular = actor.recall(MemoryKeys.CLIENTE);
        actor.attemptsTo(
                IrA.laOpcion("transferir"),
                SeleccionarCuenta.en("transferencia", titular, origen),
                Enter.theValue(numeroDestino).into(TXT_CUENTA_DESTINO),
                Enter.theValue(monto).into(TXT_MONTO_TRANSFERENCIA));
        if (!descripcion.isEmpty()) {
            actor.attemptsTo(Enter.theValue(descripcion).into(TXT_DESCRIPCION));
        }
        Wait.aLittleBitTime();
        actor.attemptsTo(
                Click.on(BTN_TRANSFERIR),
                EsperarRespuesta.delBoton(BTN_TRANSFERIR));
        Wait.aLittleBitTime();
    }

    public static final class Constructor {
        private final String monto;
        private Cuenta origen;
        private String numeroDestino;

        private Constructor(String monto) {
            this.monto = monto;
        }

        public Constructor desde(Cuenta cuenta) {
            this.origen = cuenta;
            return this;
        }

        public Constructor hacia(String numeroCuenta) {
            this.numeroDestino = numeroCuenta;
            return this;
        }

        public Transferir conDescripcion(String descripcion) {
            return Tasks.instrumented(Transferir.class, monto, origen, numeroDestino, descripcion);
        }

        public Transferir sinDescripcion() {
            return conDescripcion("");
        }
    }
}
