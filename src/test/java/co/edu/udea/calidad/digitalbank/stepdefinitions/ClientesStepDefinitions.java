package co.edu.udea.calidad.digitalbank.stepdefinitions;

import co.edu.udea.calidad.digitalbank.models.Cliente;
import co.edu.udea.calidad.digitalbank.questions.ElMensaje;
import co.edu.udea.calidad.digitalbank.questions.LaSesion;
import co.edu.udea.calidad.digitalbank.tasks.AbrirUnaCuenta;
import co.edu.udea.calidad.digitalbank.tasks.Depositar;
import co.edu.udea.calidad.digitalbank.tasks.IniciarSesion;
import co.edu.udea.calidad.digitalbank.tasks.RegistrarCliente;
import co.edu.udea.calidad.digitalbank.utils.ClienteFactory;
import co.edu.udea.calidad.digitalbank.utils.MemoryKeys;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Pasos del registro de clientes (HU-01) y de la preparación del flujo de extremo a extremo,
 * que crea todo desde la interfaz en lugar de usar la API.
 */
public class ClientesStepDefinitions {

    private static final String EXITO_OPERACION = "La operación se realizó correctamente.";

    // ---------- HU-01 Registro de cliente ----------

    @Cuando("el administrador registra a una cliente nueva llamada {string}")
    public void registraAUnaClienteNueva(String nombre) {
        registrar(ClienteFactory.unClienteNuevo(nombre));
    }

    @Cuando("el administrador registra otro cliente con el documento de {actor}")
    public void registraOtroClienteConElMismoDocumento(Actor clienteExistente) {
        Cliente repetido = Elenco.cliente(clienteExistente).conEmail(ClienteFactory.correoInexistente());
        registrar(repetido);
    }

    @Cuando("el administrador registra a un cliente que cumple 18 años mañana")
    public void registraAUnMenorDeEdad() {
        registrar(ClienteFactory.unClienteNuevo("Menor").conFechaNacimiento(ClienteFactory.fechaDeUnMenorDeEdad()));
    }

    @Entonces("la cliente registrada puede iniciar sesión con su correo y su contraseña")
    public void laClienteRegistradaPuedeIniciarSesion() {
        Cliente registrada = administrador().recall(MemoryKeys.CLIENTE_REGISTRADO);
        Actor cliente = OnStage.theActorCalled(registrada.nombres());
        Elenco.esCliente(cliente, registrada);
        cliente.attemptsTo(IniciarSesion.con(registrada.credenciales()));
        cliente.should(seeThat(LaSesion.rolDelUsuario(), equalTo("Cliente")));
    }

    // ---------- Preparación del flujo de extremo a extremo (desde la interfaz) ----------

    @Dado("que el administrador registró desde la aplicación a {actor} y a {actor}")
    public void elAdministradorRegistroDesdeLaAplicacion(Actor primero, Actor segundo) {
        for (Actor cliente : new Actor[]{primero, segundo}) {
            Cliente datos = ClienteFactory.unClienteNuevo(cliente.getName());
            registrar(datos);
            administrador().should(seeThat(ElMensaje.deExito(),
                    equalTo("El cliente " + datos.nombreCompleto() + " se registró correctamente.")));
            Elenco.esCliente(cliente, datos);
        }
    }

    @Dado("que {actor} y {actor} abrieron cada uno una cuenta de ahorros desde la aplicación")
    public void abrieronUnaCuentaDesdeLaAplicacion(Actor primero, Actor segundo) {
        for (Actor cliente : new Actor[]{primero, segundo}) {
            Elenco.conSesion(cliente).attemptsTo(AbrirUnaCuenta.deAhorros());
        }
    }

    @Dado("que el cajero depositó desde la aplicación {string} en la cuenta de {actor} y {string} en la de {actor}")
    public void elCajeroDepositoDesdeLaAplicacion(String montoPrimero, Actor primero, String montoSegundo, Actor segundo) {
        Actor cajero = Elenco.conSesion(Elenco.actor("el cajero"));
        cajero.attemptsTo(Depositar.elMonto(montoPrimero).enLaCuenta(Elenco.cuenta(primero)).de(Elenco.cliente(primero)).enEfectivo());
        cajero.should(seeThat(ElMensaje.deExito(), equalTo(EXITO_OPERACION)));
        cajero.attemptsTo(Depositar.elMonto(montoSegundo).enLaCuenta(Elenco.cuenta(segundo)).de(Elenco.cliente(segundo)).enEfectivo());
        cajero.should(seeThat(ElMensaje.deExito(), equalTo(EXITO_OPERACION)));
    }

    // ---------- Apoyo ----------

    private void registrar(Cliente cliente) {
        Actor administrador = Elenco.conSesion(administrador());
        administrador.attemptsTo(RegistrarCliente.conLosDatosDe(cliente));
        administrador.remember(MemoryKeys.CLIENTE_REGISTRADO, cliente);
    }

    private Actor administrador() {
        return Elenco.actor("el administrador");
    }
}
