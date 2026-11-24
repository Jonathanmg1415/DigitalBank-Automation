package co.edu.udea.calidad.digitalbank.utils;

/**
 * Pausa la ejecución entre pasos para que, durante la exposición, se pueda
 * observar con detenimiento lo que hace el navegador.
 *
 * <p>Tiene fines académicos: las esperas reales del proyecto son por condiciones
 * (ver {@code EsperarRespuesta}). La pausa por defecto es de 1000 ms y se cambia al
 * ejecutar: {@code gradlew clean test aggregate -Dstep.pause=0} (sin pausas).</p>
 */
public final class Wait {

    private static final int DEFAULT_PAUSE_MS = 1000;

    private Wait() {
    }

    /** Pausa el tiempo configurado en {@code step.pause} (1000 ms si no se indica). */
    public static void aLittleBitTime() {
        aLittleBitTime(Integer.getInteger("step.pause", DEFAULT_PAUSE_MS));
    }

    public static void aLittleBitTime(int milliseconds) {
        if (milliseconds <= 0) {
            return;
        }
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
