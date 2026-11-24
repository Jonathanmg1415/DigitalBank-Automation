package co.edu.udea.calidad.digitalbank.tasks;

import co.edu.udea.calidad.digitalbank.interactions.EsperarRespuesta;
import co.edu.udea.calidad.digitalbank.models.Cliente;
import co.edu.udea.calidad.digitalbank.models.Cuenta;
import co.edu.udea.calidad.digitalbank.utils.Configuracion;
import co.edu.udea.calidad.digitalbank.utils.MemoryKeys;
import co.edu.udea.calidad.digitalbank.utils.Wait;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeClientes.BTN_ABRIR_CUENTA;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeClientes.LBL_NUMERO_CUENTA_ABIERTA;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeClientes.LST_APERTURA_TIPO_DOCUMENTO;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeClientes.LST_TIPO_CUENTA;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeClientes.TXT_APERTURA_NUMERO_DOCUMENTO;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeClientes.TXT_MONTO_APERTURA;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/**
 * HU-05. El cliente abre una cuenta a su nombre desde la opción "Abrir cuenta". Guarda en su
 * memoria ({@link MemoryKeys#CUENTA}) el número de la cuenta que muestra la pantalla.
 */
public class AbrirUnaCuenta implements Task {

    private final String tipoCuenta;
    private final String montoApertura;

    public AbrirUnaCuenta(String tipoCuenta, String montoApertura) {
        this.tipoCuenta = tipoCuenta;
        this.montoApertura = montoApertura;
    }

    public static AbrirUnaCuenta deAhorros() {
        return Tasks.instrumented(AbrirUnaCuenta.class, "AHORROS", "0");
    }

    @Override
    @Step("{0} abre una cuenta #tipoCuenta con #montoApertura")
    public <T extends Actor> void performAs(T actor) {
        Cliente cliente = actor.recall(MemoryKeys.CLIENTE);
        actor.attemptsTo(
                IrA.laOpcion("abrir-cuenta"),
                SelectFromOptions.byValue(cliente.tipoDocumento()).from(LST_APERTURA_TIPO_DOCUMENTO),
                Enter.theValue(cliente.numeroDocumento()).into(TXT_APERTURA_NUMERO_DOCUMENTO),
                SelectFromOptions.byValue(tipoCuenta).from(LST_TIPO_CUENTA),
                Enter.theValue(montoApertura).into(TXT_MONTO_APERTURA));
        Wait.aLittleBitTime();
        actor.attemptsTo(
                Click.on(BTN_ABRIR_CUENTA),
                EsperarRespuesta.delBoton(BTN_ABRIR_CUENTA),
                WaitUntil.the(LBL_NUMERO_CUENTA_ABIERTA, isVisible())
                        .forNoMoreThan(Configuracion.segundosDeEspera()).seconds());
        String numero = Text.of(LBL_NUMERO_CUENTA_ABIERTA).answeredBy(actor).trim();
        // La interfaz no muestra el id interno de la cuenta: las tareas de la interfaz no lo necesitan.
        actor.remember(MemoryKeys.CUENTA, new Cuenta(null, numero, tipoCuenta));
        Wait.aLittleBitTime();
    }
}
