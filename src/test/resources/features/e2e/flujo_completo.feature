# language: es
@E2E @R1 @CP-E2E-02
Característica: El dinero entra, sale y se mueve
  Recorre el Release 1 completo desde la interfaz, sin preparar datos por la API: el
  administrador registra a los clientes, cada uno abre su cuenta, el cajero deposita, el dinero
  sale y se mueve, la cuenta se bloquea y se desbloquea, y el historial lo cuenta todo.

  @positivo
  Escenario: Ciclo completo de una cuenta
    Dado que el administrador registró desde la aplicación a Ana y a Luis
    Y que Ana y Luis abrieron cada uno una cuenta de ahorros desde la aplicación
    Y que el cajero depositó desde la aplicación "200000" en la cuenta de Ana y "50000" en la de Luis
    Cuando Ana retira "50000" de su cuenta
    Y Ana transfiere "30000" hacia "la cuenta de Luis" con la descripción "Pago de arriendo"
    Y el administrador bloquea la cuenta de Ana con el motivo "Actividad sospechosa detectada"
    Y el cajero deposita "1000" en la cuenta de Ana
    Y el administrador desbloquea la cuenta de Ana con el motivo "Revisión terminada sin novedad"
    Y Ana consulta los movimientos de la cuenta de Ana
    Entonces los movimientos son, del más reciente al más antiguo: "Depósito, Transferencia saliente, Retiro, Depósito"
    Y el saldo resultante del movimiento más reciente es 121000
    Y el saldo disponible de la cuenta de Ana es 121000
    Y el saldo disponible de la cuenta de Luis es 80000
