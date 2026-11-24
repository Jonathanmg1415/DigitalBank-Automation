package co.edu.udea.calidad.digitalbank.stepdefinitions;

import co.edu.udea.calidad.digitalbank.interactions.LlenarFormularioLogin;
import co.edu.udea.calidad.digitalbank.models.Credenciales;
import co.edu.udea.calidad.digitalbank.questions.LaSesion;
import co.edu.udea.calidad.digitalbank.tasks.AbrirDigitalBank;
import co.edu.udea.calidad.digitalbank.tasks.IniciarSesion;
import co.edu.udea.calidad.digitalbank.utils.ClienteFactory;
import co.edu.udea.calidad.digitalbank.utils.MemoryKeys;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.Actor;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Pasos del feature autenticacion/inicio_sesion.feature (HU-18).
 *
 * <p>El inicio de sesión exitoso usa {@link IniciarSesion#con}, que espera a ver el rol en el
 * encabezado. Los escenarios de error usan {@link IniciarSesion#intentandoCon}: como el rol
 * nunca aparece, esperarlo terminaría en un timeout.</p>
 */
public class AutenticacionStepDefinitions {

    private static final String SUFIJO_INCORRECTO = "_incorrecta";

    @Cuando("{actor} inicia sesión con sus credenciales")
    public void iniciaSesionConSusCredenciales(Actor actor) {
        actor.attemptsTo(IniciarSesion.con(Elenco.credenciales(actor)));
        actor.remember(MemoryKeys.SESION_INICIADA, true);
    }

    @Cuando("{actor} inicia sesión con sus credenciales correctas")
    public void iniciaSesionConSusCredencialesCorrectas(Actor actor) {
        // Se espera un error (usuario bloqueado), así que no se usa IniciarSesion.con.
        actor.attemptsTo(IniciarSesion.intentandoCon(Elenco.credenciales(actor)));
    }

    @Cuando("{actor} inicia sesión con una contraseña errónea")
    public void iniciaSesionConUnaContrasenaErronea(Actor actor) {
        Credenciales credenciales = Elenco.credenciales(actor);
        actor.attemptsTo(IniciarSesion.intentandoCon(
                credenciales.conPassword(credenciales.password() + SUFIJO_INCORRECTO)));
    }

    @Cuando("{actor} inicia sesión con un correo que no está registrado")
    public void iniciaSesionConUnCorreoNoRegistrado(Actor actor) {
        actor.attemptsTo(IniciarSesion.intentandoCon(
                new Credenciales(ClienteFactory.correoInexistente(), ClienteFactory.PASSWORD)));
    }

    @Cuando("{actor} intenta ingresar sin escribir el correo ni la contraseña")
    public void intentaIngresarSinDatos(Actor actor) {
        actor.attemptsTo(
                AbrirDigitalBank.enElLogin(),
                LlenarFormularioLogin.con(new Credenciales("", "")));
    }

    @Entonces("{actor} debería ver su rol {string} en el encabezado")
    public void deberiaVerSuRol(Actor actor, String rol) {
        actor.should(seeThat(LaSesion.rolDelUsuario(), equalTo(rol)));
    }
}
