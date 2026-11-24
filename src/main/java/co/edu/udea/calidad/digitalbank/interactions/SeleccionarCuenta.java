package co.edu.udea.calidad.digitalbank.interactions;

import co.edu.udea.calidad.digitalbank.models.Cliente;
import co.edu.udea.calidad.digitalbank.models.Cuenta;
import co.edu.udea.calidad.digitalbank.utils.Configuracion;
import co.edu.udea.calidad.digitalbank.utils.Wait;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static co.edu.udea.calidad.digitalbank.userinterfaces.SelectorDeCuenta.BTN_BUSCAR_CLIENTE;
import static co.edu.udea.calidad.digitalbank.userinterfaces.SelectorDeCuenta.LST_CUENTA;
import static co.edu.udea.calidad.digitalbank.userinterfaces.SelectorDeCuenta.LST_TIPO_DOCUMENTO;
import static co.edu.udea.calidad.digitalbank.userinterfaces.SelectorDeCuenta.TXT_NUMERO_DOCUMENTO;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/**
 * Elige una cuenta en el selector compartido de las pantallas de operaciones.
 *
 * <p>Un cliente ve directamente la lista de sus cuentas. El administrador y el cajero primero
 * buscan al titular por su documento y luego eligen la cuenta. La interacción detecta cuál de
 * los dos casos aplica según los campos que muestre la pantalla.</p>
 */
public class SeleccionarCuenta implements Interaction {

    private final String pantalla;
    private final Cliente titular;
    private final Cuenta cuenta;

    public SeleccionarCuenta(String pantalla, Cliente titular, Cuenta cuenta) {
        this.pantalla = pantalla;
        this.titular = titular;
        this.cuenta = cuenta;
    }

    /**
     * @param pantalla prefijo de la pantalla: retiro, transferencia, deposito, movimientos o estado-cuenta
     * @param titular  cliente dueño de la cuenta (para la búsqueda por documento)
     * @param cuenta   cuenta que se elige
     */
    public static SeleccionarCuenta en(String pantalla, Cliente titular, Cuenta cuenta) {
        return Tasks.instrumented(SeleccionarCuenta.class, pantalla, titular, cuenta);
    }

    @Override
    @Step("{0} elige la cuenta #cuenta de #titular")
    public <T extends Actor> void performAs(T actor) {
        boolean buscaPorDocumento = !TXT_NUMERO_DOCUMENTO.of(pantalla).resolveAllFor(actor).isEmpty();
        if (buscaPorDocumento) {
            actor.attemptsTo(
                    SelectFromOptions.byValue(titular.tipoDocumento()).from(LST_TIPO_DOCUMENTO.of(pantalla)),
                    Enter.theValue(titular.numeroDocumento()).into(TXT_NUMERO_DOCUMENTO.of(pantalla)),
                    Click.on(BTN_BUSCAR_CLIENTE.of(pantalla)));
        }
        actor.attemptsTo(
                WaitUntil.the(LST_CUENTA.of(pantalla), isVisible())
                        .forNoMoreThan(Configuracion.segundosDeEspera()).seconds(),
                SelectFromOptions.byValue(cuenta.numeroCuenta()).from(LST_CUENTA.of(pantalla)));
        Wait.aLittleBitTime();
    }
}
