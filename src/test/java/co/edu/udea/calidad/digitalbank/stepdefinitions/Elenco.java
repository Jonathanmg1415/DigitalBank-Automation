package co.edu.udea.calidad.digitalbank.stepdefinitions;

import co.edu.udea.calidad.digitalbank.models.Cliente;
import co.edu.udea.calidad.digitalbank.models.Credenciales;
import co.edu.udea.calidad.digitalbank.models.Cuenta;
import co.edu.udea.calidad.digitalbank.tasks.IniciarSesion;
import co.edu.udea.calidad.digitalbank.utils.MemoryKeys;
import co.edu.udea.calidad.digitalbank.utils.Usuarios;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;

/**
 * Actores de los escenarios y lo que recuerdan.
 *
 * <ul>
 *   <li>"el administrador" y "el cajero": usuarios internos con las credenciales de
 *   {@link Usuarios}.</li>
 *   <li>Ana, Luis y cualquier otro nombre propio: clientes que cada escenario crea.</li>
 * </ul>
 */
final class Elenco {

    private static final String ADMINISTRADOR = "Administrador";
    private static final String CAJERO = "Cajero";

    private Elenco() {
    }

    /** Devuelve el actor y lo pone en el centro de la escena (theActorInTheSpotlight). */
    static Actor actor(String nombre) {
        return switch (nombre) {
            case "el administrador" -> conCredenciales(ADMINISTRADOR, Usuarios.administrador());
            case "el cajero" -> conCredenciales(CAJERO, Usuarios.cajero());
            default -> OnStage.theActorCalled(nombre);
        };
    }

    /**
     * Vuelve a poner al actor en el centro de la escena. Cucumber crea todos los actores de un
     * paso antes de ejecutarlo, así que el último en crearse queda en el centro aunque no sea
     * el que actúa.
     */
    static Actor enEscena(Actor actor) {
        return OnStage.theActorCalled(actor.getName());
    }

    /** Si el actor no ha iniciado sesión en su navegador, la inicia. */
    static Actor conSesion(Actor actor) {
        enEscena(actor);
        if (!Boolean.TRUE.equals(actor.recall(MemoryKeys.SESION_INICIADA))) {
            actor.attemptsTo(IniciarSesion.con(credenciales(actor)));
            actor.remember(MemoryKeys.SESION_INICIADA, true);
        }
        return actor;
    }

    static Credenciales credenciales(Actor actor) {
        return actor.recall(MemoryKeys.CREDENCIALES);
    }

    static Cliente cliente(Actor actor) {
        return actor.recall(MemoryKeys.CLIENTE);
    }

    static Cuenta cuenta(Actor actor) {
        return actor.recall(MemoryKeys.CUENTA);
    }

    static Cuenta segundaCuenta(Actor actor) {
        return actor.recall(MemoryKeys.SEGUNDA_CUENTA);
    }

    /** Asigna al actor los datos de un cliente y sus credenciales. */
    static void esCliente(Actor actor, Cliente cliente) {
        actor.remember(MemoryKeys.CLIENTE, cliente);
        actor.remember(MemoryKeys.CREDENCIALES, cliente.credenciales());
    }

    private static Actor conCredenciales(String nombre, Credenciales credenciales) {
        Actor actor = OnStage.theActorCalled(nombre);
        if (actor.recall(MemoryKeys.CREDENCIALES) == null) {
            actor.remember(MemoryKeys.CREDENCIALES, credenciales);
        }
        return actor;
    }
}
