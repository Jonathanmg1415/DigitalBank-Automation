package co.edu.udea.calidad.digitalbank.stepdefinitions;

import co.edu.udea.calidad.digitalbank.utils.Usuarios;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

/**
 * Configuración común a todos los escenarios: hooks de Cucumber y tipos de parámetro.
 *
 * <p>Antes de cada escenario prepara el "escenario" de Screenplay con un {@link OnlineCast}:
 * cada actor que se llame con {@code OnStage.theActorCalled("Ana")} recibe la habilidad de
 * navegar en la web (BrowseTheWeb) con su propio navegador.</p>
 */
public class StepDefinition {

    @Before
    public void setTheStage() {
        OnStage.setTheStage(new OnlineCast());
    }

    @After
    public void drawTheCurtain() {
        OnStage.drawTheCurtain();
    }

    /**
     * Convierte el nombre del feature en un actor: "el administrador", "el cajero" o un nombre
     * propio como "Ana" o "Luis". Al administrador y al cajero se les asignan sus credenciales
     * (ver {@link Usuarios}).
     */
    @ParameterType("el administrador|el cajero|[A-ZÁÉÍÓÚÑ][a-záéíóúñ]+")
    public Actor actor(String nombre) {
        return Elenco.actor(nombre);
    }

    /** Acción sobre el estado de una cuenta: devuelve "bloquear", "desbloquear" o "cerrar". */
    @ParameterType("bloquea|desbloquea|cierra|intenta bloquear|intenta desbloquear|intenta cerrar")
    public String accion(String texto) {
        String verbo = texto.replace("intenta ", "");
        return switch (verbo) {
            case "bloquea", "bloquear" -> "bloquear";
            case "desbloquea", "desbloquear" -> "desbloquear";
            default -> "cerrar";
        };
    }
}
