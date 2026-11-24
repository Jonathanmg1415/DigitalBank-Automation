package co.edu.udea.calidad.digitalbank.interactions;

import co.edu.udea.calidad.digitalbank.utils.Configuracion;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.targets.Target;

import java.time.Duration;
import java.time.Instant;
import java.util.function.BooleanSupplier;

import static co.edu.udea.calidad.digitalbank.userinterfaces.Mensajes.RESPUESTA;

/**
 * Espera a que termine la operación que se envió con un botón.
 *
 * <p>Mientras el frontend espera al backend, el botón queda con {@code aria-busy="true"}.
 * La interacción espera primero a que el botón entre en ese estado (o a que aparezca de una vez
 * una respuesta, como un error de validación que no llega al backend) y luego a que salga de él.
 * No usa tiempos fijos: el límite lo da {@code digitalbank.respuesta.timeout} en
 * {@code serenity.conf}, amplio porque Render tarda en despertar las APIs.</p>
 */
public class EsperarRespuesta implements Interaction {

    private static final Duration INICIO_MAXIMO = Duration.ofSeconds(3);
    private static final long INTERVALO_MS = 200;

    private final Target boton;

    public EsperarRespuesta(Target boton) {
        this.boton = boton;
    }

    /** Uso: {@code EsperarRespuesta.delBoton(BTN_RETIRAR)} después de hacer clic en él. */
    public static EsperarRespuesta delBoton(Target boton) {
        return Tasks.instrumented(EsperarRespuesta.class, boton);
    }

    @Override
    @Step("{0} espera la respuesta de #boton")
    public <T extends Actor> void performAs(T actor) {
        esperarHasta(() -> estaOcupado(actor) || hayRespuesta(actor), INICIO_MAXIMO);
        esperarHasta(() -> !estaOcupado(actor), Duration.ofSeconds(Configuracion.segundosDeEspera()));
    }

    private boolean estaOcupado(Actor actor) {
        return boton.resolveAllFor(actor).stream()
                .anyMatch(elemento -> "true".equals(elemento.getAttribute("aria-busy")));
    }

    private boolean hayRespuesta(Actor actor) {
        return RESPUESTA.resolveAllFor(actor).stream().anyMatch(WebElementFacade::isVisible);
    }

    private static void esperarHasta(BooleanSupplier condicion, Duration limite) {
        Instant fin = Instant.now().plus(limite);
        while (Instant.now().isBefore(fin)) {
            if (condicion.getAsBoolean()) {
                return;
            }
            try {
                Thread.sleep(INTERVALO_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
