# language: es
@HU-11 @F3.2 @Retiro
Característica: HU-11 Retiro de cuenta
  Como cliente del banco
  Quiero retirar dinero de mi cuenta
  Para disponer de mi saldo

  Antecedentes:
    Dado que Ana es cliente del banco con una cuenta de ahorros con saldo 200000
    Y Ana inicia sesión en Digital Bank

  @CA6 @positivo @CP-HU11-01
  Escenario: Retiro exitoso
    Cuando Ana retira "50000" de su cuenta
    Entonces Ana debería ver el mensaje de éxito "La operación se realizó correctamente."
    Y el comprobante del retiro muestra el saldo anterior 200000 y el saldo nuevo 150000
    Y el saldo disponible de la cuenta de Ana es 150000

  @CA1 @CA2 @limite @negativo @CP-HU11-01
  Esquema del escenario: El monto debe ser mayor que cero
    Cuando Ana retira "<monto>" de su cuenta
    Entonces Ana debería ver el mensaje de error "Monto inválido"
    Y el saldo disponible de la cuenta de Ana es 200000

    Ejemplos:
      | monto |
      | 0     |
      | -1    |

  @CA1 @negativo @CP-HU11-01
  Escenario: El monto es obligatorio
    Cuando Ana retira "" de su cuenta
    Entonces Ana debería ver el error de campo "Escriba el monto a retirar."

  @CA3 @CA5 @limite @negativo @CP-HU11-01
  Escenario: El saldo disponible debe cubrir el retiro
    Cuando Ana retira "200000.01" de su cuenta
    Entonces Ana debería ver el mensaje de error "Saldo insuficiente"
    Y el saldo disponible de la cuenta de Ana es 200000

  @CA4 @negativo @CP-HU07a-01
  Escenario: No se retira de una cuenta bloqueada
    Dado que el administrador bloqueó la cuenta de Ana
    Cuando Ana retira "1000" de su cuenta
    Entonces Ana debería ver el mensaje de error "Cuenta no disponible"

  @CA7 @seguridad @CP-SEG-02
  Escenario: El cajero no tiene la opción de retirar
    Cuando el cajero inicia sesión en Digital Bank
    Entonces el cajero no debería ver la opción "Retirar" en el menú
    Y si el cajero intenta entrar a la dirección "/retiros" debería ver la página "Acceso no permitido"
