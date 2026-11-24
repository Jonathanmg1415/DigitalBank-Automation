package co.edu.udea.calidad.digitalbank.tasks;

import co.edu.udea.calidad.digitalbank.utils.Wait;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;

import static co.edu.udea.calidad.digitalbank.userinterfaces.Encabezado.OPCION_MENU;

/**
 * Entra a una pantalla desde el menú lateral. Recibe el identificador de la opción, por ejemplo
 * "retirar", "transferir", "depositos", "movimientos", "estado-cuentas", "mis-cuentas",
 * "abrir-cuenta" o "registrar-cliente" (ver docs/selectores.md del frontend).
 */
public class IrA implements Task {

    private final String opcion;

    public IrA(String opcion) {
        this.opcion = opcion;
    }

    public static IrA laOpcion(String opcion) {
        return Tasks.instrumented(IrA.class, opcion);
    }

    @Override
    @Step("{0} entra a la opción #opcion del menú")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Click.on(OPCION_MENU.of(opcion)));
        Wait.aLittleBitTime();
    }
}
