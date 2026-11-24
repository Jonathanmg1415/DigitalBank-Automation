package co.edu.udea.calidad.digitalbank.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Convierte los montos que muestra la interfaz (formato colombiano, por ejemplo
 * "$ 150.000,00" o "−$ 1.000,00") a números, para compararlos sin depender del formato.
 */
public final class Montos {

    private Montos() {
    }

    public static BigDecimal aNumero(String textoEnPantalla) {
        String limpio = textoEnPantalla
                .replace('−', '-')        // signo menos tipográfico
                .replaceAll("[^0-9,\\-]", "")  // quita $, puntos de miles y espacios
                .replace(',', '.');
        return new BigDecimal(limpio).setScale(2, RoundingMode.UNNECESSARY);
    }

    public static BigDecimal de(String valor) {
        return new BigDecimal(valor).setScale(2, RoundingMode.UNNECESSARY);
    }
}
