# language: es
@HU-10 @F3.1 @Deposito
Característica: HU-10 Depósito a cuenta
  Como cajero del banco
  Quiero registrar depósitos en las cuentas de los clientes
  Para que el dinero quede disponible en su saldo

  Antecedentes:
    Dado que Ana es cliente del banco con una cuenta de ahorros con saldo 0
    Y el cajero inicia sesión en Digital Bank

  @CA1 @positivo
  Escenario: Depósito exitoso en efectivo
    Cuando el cajero deposita "50000" en la cuenta de Ana
    Entonces el cajero debería ver el mensaje de éxito "La operación se realizó correctamente."
    Y el comprobante del depósito muestra el saldo nuevo 50000
    Y el saldo disponible de la cuenta de Ana es 50000

  @CA2 @limite @negativo
  Esquema del escenario: El monto del depósito debe ser mayor que cero
    Cuando el cajero deposita "<monto>" en la cuenta de Ana
    Entonces el cajero debería ver el mensaje de error "Monto inválido"

    Ejemplos:
      | monto |
      | 0     |
      | -100  |

  @HU-07a @CA1 @positivo @CP-HU07a-01
  Escenario: Una cuenta bloqueada sigue recibiendo depósitos
    Dado que el administrador bloqueó la cuenta de Ana
    Cuando el cajero deposita "1000" en la cuenta de Ana
    Entonces el cajero debería ver el mensaje de éxito "La operación se realizó correctamente."

  @seguridad
  Escenario: El cliente no tiene la opción de depositar
    Cuando Ana inicia sesión en Digital Bank
    Entonces Ana no debería ver la opción "Depósitos" en el menú
    Y si Ana intenta entrar a la dirección "/depositos" debería ver la página "Acceso no permitido"
