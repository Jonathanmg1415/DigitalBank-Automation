# language: es
@HU-07b @F2.2 @EstadoCuentas
Característica: HU-07b Cierre de cuenta
  Como administrador del banco
  Quiero cerrar definitivamente una cuenta
  Para finalizar la relación comercial con el cliente

  Antecedentes:
    Dado que Ana es cliente del banco con una cuenta de ahorros con saldo 50000
    Y el administrador inicia sesión en Digital Bank

  @CA1 @negativo @CP-HU07b-01
  Escenario: No se cierra una cuenta con saldo
    Dado que Ana tiene una segunda cuenta con saldo 1000
    Cuando el administrador intenta cerrar la segunda cuenta de Ana con el motivo "Cierre solicitado por la cliente"
    Entonces el administrador debería ver el mensaje de error "La cuenta debe tener saldo $0 para cerrarse"

  @CA1 @CA3 @positivo @CP-HU07b-01
  Escenario: Cierre exitoso de una cuenta sin saldo
    Dado que Ana tiene una segunda cuenta con saldo 0
    Cuando el administrador cierra la segunda cuenta de Ana con el motivo "Cierre solicitado por la cliente"
    Entonces el administrador debería ver el mensaje de éxito "El estado de la cuenta se actualizó correctamente."
    Y el comprobante muestra que la cuenta pasó del estado "Activa" al estado "Cerrada"

  @CA2 @negativo @CP-HU07b-01
  Esquema del escenario: Una cuenta cerrada no se reactiva
    Dado que Ana tiene una segunda cuenta con saldo 0
    Y que el administrador cerró la segunda cuenta de Ana
    Cuando el administrador intenta <acción> la segunda cuenta de Ana con el motivo "Solicitud posterior al cierre"
    Entonces el administrador debería ver el mensaje de error "<mensaje>"

    Ejemplos:
      | acción      | mensaje                     |
      | desbloquear | La cuenta no está bloqueada |
      | bloquear    | La cuenta no está activa    |
      | cerrar      | La cuenta ya está cerrada   |

  @CA2 @negativo @CP-HU07b-01
  Escenario: Una cuenta cerrada no recibe depósitos
    Dado que Ana tiene una segunda cuenta con saldo 0
    Y que el administrador cerró la segunda cuenta de Ana
    Cuando el cajero deposita "1000" en la segunda cuenta de Ana
    Entonces el cajero debería ver el mensaje de error "Cuenta no disponible"

  @CA4 @positivo @CP-HU07b-01
  Escenario: La cuenta cerrada sigue visible para la cliente
    Dado que Ana tiene una segunda cuenta con saldo 0
    Y que el administrador cerró la segunda cuenta de Ana
    Cuando Ana inicia sesión en Digital Bank
    Entonces Ana ve su segunda cuenta en estado "Cerrada" con saldo 0
