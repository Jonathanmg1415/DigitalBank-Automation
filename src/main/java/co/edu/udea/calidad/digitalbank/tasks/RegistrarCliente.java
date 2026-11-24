package co.edu.udea.calidad.digitalbank.tasks;

import co.edu.udea.calidad.digitalbank.interactions.EscribirFecha;
import co.edu.udea.calidad.digitalbank.interactions.EsperarRespuesta;
import co.edu.udea.calidad.digitalbank.models.Cliente;
import co.edu.udea.calidad.digitalbank.utils.Wait;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;

import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeClientes.BTN_REGISTRAR;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeClientes.LST_TIPO_DOCUMENTO;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeClientes.TXT_APELLIDOS;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeClientes.TXT_EMAIL;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeClientes.TXT_FECHA_NACIMIENTO;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeClientes.TXT_NOMBRES;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeClientes.TXT_NUMERO_DOCUMENTO;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeClientes.TXT_PASSWORD;
import static co.edu.udea.calidad.digitalbank.userinterfaces.PaginasDeClientes.TXT_TELEFONO;

/** HU-01. El administrador registra un cliente nuevo desde la opción "Registrar cliente". */
public class RegistrarCliente implements Task {

    private final Cliente cliente;

    public RegistrarCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public static RegistrarCliente conLosDatosDe(Cliente cliente) {
        return Tasks.instrumented(RegistrarCliente.class, cliente);
    }

    @Override
    @Step("{0} registra al cliente #cliente")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                IrA.laOpcion("registrar-cliente"),
                SelectFromOptions.byValue(cliente.tipoDocumento()).from(LST_TIPO_DOCUMENTO),
                Enter.theValue(cliente.numeroDocumento()).into(TXT_NUMERO_DOCUMENTO),
                Enter.theValue(cliente.nombres()).into(TXT_NOMBRES),
                Enter.theValue(cliente.apellidos()).into(TXT_APELLIDOS),
                EscribirFecha.elValor(cliente.fechaNacimiento()).en(TXT_FECHA_NACIMIENTO),
                Enter.theValue(cliente.telefono()).into(TXT_TELEFONO),
                Enter.theValue(cliente.email()).into(TXT_EMAIL));
        // Sin la acción Enter, para que el reporte no muestre la contraseña.
        TXT_PASSWORD.resolveFor(actor).type(cliente.password());
        Wait.aLittleBitTime();
        actor.attemptsTo(
                Click.on(BTN_REGISTRAR),
                EsperarRespuesta.delBoton(BTN_REGISTRAR));
        Wait.aLittleBitTime();
    }
}
