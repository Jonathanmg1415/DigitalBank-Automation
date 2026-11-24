# language: es
@HU-07a @F2.2 @EstadoCuentas
Característica: HU-07a Bloqueo de cuenta
  Como administrador del banco
  Quiero bloquear temporalmente una cuenta
  Para prevenir operaciones no autorizadas

  Antecedentes:
    Dado que Ana es cliente del banco con una cuenta de ahorros con saldo 150000
    Y el administrador inicia sesión en Digital Bank

  @CA1 @CA3 @positivo @CP-HU07a-01
  Escenario: Bloqueo exitoso con registro del motivo
    Cuando el administrador bloquea la cuenta de Ana con el motivo "Actividad sospechosa detectada"
    Entonces el administrador debería ver el mensaje de éxito "El estado de la cuenta se actualizó correctamente."
    Y el comprobante muestra que la cuenta pasó del estado "Activa" al estado "Bloqueada"
    Y el comprobante muestra el motivo "Actividad sospechosa detectada"

  @CA2 @positivo @CP-HU07a-01
  Escenario: Desbloqueo exitoso
    Dado que el administrador bloqueó la cuenta de Ana
    Cuando el administrador desbloquea la cuenta de Ana con el motivo "Revisión terminada sin novedad"
    Entonces el comprobante muestra que la cuenta pasó del estado "Bloqueada" al estado "Activa"

  @CA5 @negativo @CP-HU07a-01
  Escenario: Solo se bloquea una cuenta activa
    Dado que el administrador bloqueó la cuenta de Ana
    Cuando el administrador intenta bloquear la cuenta de Ana con el motivo "Segundo bloqueo de la cuenta"
    Entonces el administrador debería ver el mensaje de error "La cuenta no está activa"

  @CA5 @negativo @CP-HU07a-01
  Escenario: Solo se desbloquea una cuenta bloqueada
    Cuando el administrador intenta desbloquear la cuenta de Ana con el motivo "La cuenta no estaba bloqueada"
    Entonces el administrador debería ver el mensaje de error "La cuenta no está bloqueada"

  @CA3 @limite @negativo @CP-HU07a-01
  Escenario: El motivo tiene al menos 10 caracteres
    Cuando el administrador intenta bloquear la cuenta de Ana con el motivo "Sospechos"
    Entonces el administrador debería ver el mensaje de error "El motivo debe tener entre 10 y 255 caracteres"

  @CA3 @negativo @CP-HU07a-01
  Escenario: El motivo es obligatorio
    Cuando el administrador intenta bloquear la cuenta de Ana con el motivo ""
    Entonces el administrador debería ver el error de campo "Escriba el motivo."

  @CA4 @seguridad @CP-SEG-02
  Escenario: El cajero no puede cambiar el estado de las cuentas
    Cuando el cajero inicia sesión en Digital Bank
    Entonces el cajero no debería ver la opción "Estado de cuentas" en el menú
    Y si el cajero intenta entrar a la dirección "/cuentas/estado" debería ver la página "Acceso no permitido"
