package co.edu.udea.calidad.digitalbank.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/** Registro de clientes (HU-01) y apertura de cuenta (HU-05). */
public final class PaginasDeClientes {

    // ---------- Registrar cliente (/clientes/registrar) ----------

    public static final Target LST_TIPO_DOCUMENTO = Target.the("tipo de documento")
            .located(By.id("registro-tipo-documento"));
    public static final Target TXT_NUMERO_DOCUMENTO = Target.the("número de documento")
            .located(By.id("registro-numero-documento"));
    public static final Target TXT_NOMBRES = Target.the("nombres")
            .located(By.id("registro-nombres"));
    public static final Target TXT_APELLIDOS = Target.the("apellidos")
            .located(By.id("registro-apellidos"));
    public static final Target TXT_FECHA_NACIMIENTO = Target.the("fecha de nacimiento")
            .located(By.id("registro-fecha-nacimiento"));
    public static final Target TXT_TELEFONO = Target.the("teléfono")
            .located(By.id("registro-telefono"));
    public static final Target TXT_EMAIL = Target.the("correo")
            .located(By.id("registro-email"));
    public static final Target TXT_PASSWORD = Target.the("contraseña inicial")
            .located(By.id("registro-password"));
    public static final Target BTN_REGISTRAR = Target.the("botón Registrar cliente")
            .located(By.id("registro-enviar"));

    // ---------- Abrir cuenta (/cuentas/abrir) ----------

    public static final Target LST_APERTURA_TIPO_DOCUMENTO = Target.the("tipo de documento")
            .located(By.id("abrir-cuenta-tipo-documento"));
    public static final Target TXT_APERTURA_NUMERO_DOCUMENTO = Target.the("número de documento")
            .located(By.id("abrir-cuenta-numero-documento"));
    public static final Target LST_TIPO_CUENTA = Target.the("tipo de cuenta")
            .located(By.id("abrir-cuenta-tipo-cuenta"));
    public static final Target TXT_MONTO_APERTURA = Target.the("monto de apertura")
            .located(By.id("abrir-cuenta-monto"));
    public static final Target BTN_ABRIR_CUENTA = Target.the("botón Abrir cuenta")
            .located(By.id("abrir-cuenta-enviar"));
    /** Número de la cuenta recién abierta. */
    public static final Target LBL_NUMERO_CUENTA_ABIERTA = Target.the("número de la cuenta abierta")
            .located(By.id("apertura-numero-cuenta"));

    private PaginasDeClientes() {
    }
}
