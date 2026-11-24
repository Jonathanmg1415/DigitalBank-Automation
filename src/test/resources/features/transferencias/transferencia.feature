# language: es
@HU-09a @HU-09b @HU-09c @F3.3 @Transferencias
Característica: HU-09 Transferencias entre cuentas
  Como cliente del banco
  Quiero transferir dinero a otra cuenta de Digital Bank
  Para pagar o enviar dinero a otras personas

  Antecedentes:
    Dado que Ana es cliente del banco con una cuenta de ahorros con saldo 150000
    Y que Luis es cliente del banco con una cuenta de ahorros con saldo 50000
    Y Ana inicia sesión en Digital Bank

  @HU-09a @CA1 @CA2 @CA3 @CA4 @positivo @CP-HU09-01
  Escenario: Transferencia exitosa
    Cuando Ana transfiere "30000" hacia "la cuenta de Luis" con la descripción "Pago de arriendo"
    Entonces Ana debería ver el mensaje de éxito "La transferencia se realizó correctamente."
    Y el comprobante de la transferencia muestra el monto 30000 y el estado "Completada"
    Y el saldo disponible de la cuenta de Ana es 120000
    Y el saldo disponible de la cuenta de Luis es 80000

  @HU-09b @CA1 @CA3 @limite @negativo @CP-HU09-02
  Esquema del escenario: Validaciones de los datos de la transferencia
    Cuando Ana transfiere "<monto>" hacia "<destino>" con una descripción de <letras> letras
    Entonces Ana debería ver el mensaje de error "<mensaje>"
    Y el saldo disponible de la cuenta de Ana es 150000

    Ejemplos:
      | monto | destino           | letras | mensaje                                         |
      | 0     | la cuenta de Luis | 10     | Monto inválido                                  |
      | -5    | la cuenta de Luis | 10     | Monto inválido                                  |
      | 100   | su propia cuenta  | 10     | Cuenta origen y destino no pueden ser iguales   |
      | 100   | la cuenta de Luis | 256    | La descripción no puede superar los 255 caracteres |

  @HU-09b @CA2 @limite @negativo @CP-HU09-02
  Escenario: Saldo insuficiente
    Cuando Ana transfiere "150000.01" hacia "la cuenta de Luis"
    Entonces Ana debería ver el mensaje de error "Saldo insuficiente"
    Y el saldo disponible de la cuenta de Ana es 150000

  @HU-09c @CA1 @negativo @CP-HU09-02
  Escenario: La cuenta destino no existe
    Cuando Ana transfiere "100" hacia "9999999999"
    Entonces Ana debería ver el mensaje de error "Cuenta destino no encontrada"

  @HU-09b @CA5 @negativo @CP-HU09-02
  Escenario: No se transfiere desde una cuenta bloqueada
    Dado que el administrador bloqueó la cuenta de Ana
    Cuando Ana transfiere "100" hacia "la cuenta de Luis"
    Entonces Ana debería ver el mensaje de error "Cuenta origen bloqueada"

  @HU-09c @CA6 @negativo @CP-HU09-02
  Escenario: No se transfiere hacia una cuenta cerrada
    Dado que Luis tiene una segunda cuenta con saldo 0
    Y que el administrador cerró la segunda cuenta de Luis
    Cuando Ana transfiere "100" hacia "la segunda cuenta de Luis"
    Entonces Ana debería ver el mensaje de error "Cuenta destino no disponible"

  @HU-09b @CA4 @seguridad @CP-SEG-02
  Escenario: El administrador no tiene la opción de transferir
    Cuando el administrador inicia sesión en Digital Bank
    Entonces el administrador no debería ver la opción "Transferir" en el menú
    Y si el administrador intenta entrar a la dirección "/transferencias" debería ver la página "Acceso no permitido"
