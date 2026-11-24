package co.edu.udea.calidad.digitalbank.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/** Pantalla de inicio de sesión ({@code /login}, HU-18). */
public final class PaginaLogin {

    public static final Target FORMULARIO = Target.the("formulario de inicio de sesión")
            .located(By.id("login-formulario"));

    public static final Target TXT_EMAIL = Target.the("campo de correo")
            .located(By.id("login-email"));

    public static final Target TXT_PASSWORD = Target.the("campo de contraseña")
            .located(By.id("login-password"));

    public static final Target BTN_INGRESAR = Target.the("botón Ingresar")
            .located(By.id("login-ingresar"));

    /** Error de credenciales (401) u otro error del inicio de sesión. */
    public static final Target LBL_ERROR = Target.the("mensaje de error del inicio de sesión")
            .located(By.id("login-error"));

    /** Error de usuario bloqueado por intentos fallidos (429). */
    public static final Target LBL_ERROR_BLOQUEO = Target.the("mensaje de usuario bloqueado")
            .located(By.id("login-error-bloqueo"));

    private PaginaLogin() {
    }
}
