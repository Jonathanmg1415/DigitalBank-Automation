# language: es
@HU-13 @F4.1 @Movimientos
Característica: HU-13 Historial de transacciones por cuenta
  Como cliente del banco
  Quiero consultar los movimientos de mi cuenta
  Para saber en qué se ha usado mi dinero

  Antecedentes:
    Dado que Ana es cliente del banco con una cuenta de ahorros con saldo 0
    Y que el cajero depositó 100000 en la cuenta de Ana
    Y que Ana retiró 20000 de su cuenta
    Y que el cajero depositó 5000 en la cuenta de Ana
    Y Ana inicia sesión en Digital Bank

  @CA1 @CA4 @positivo @CP-HU13-01
  Escenario: Consulta sin filtros, del más reciente al más antiguo
    Cuando Ana consulta los movimientos de la cuenta de Ana
    Entonces los movimientos son, del más reciente al más antiguo: "Depósito, Retiro, Depósito"
    Y el saldo resultante del movimiento más reciente es 85000

  @CA2 @positivo @CP-HU13-01
  Escenario: Filtro por tipo de movimiento
    Cuando Ana consulta los movimientos de la cuenta de Ana con el tipo "RETIRO"
    Entonces los movimientos son, del más reciente al más antiguo: "Retiro"

  @CA2 @positivo @CP-HU13-01
  Escenario: Un rango de fechas sin movimientos
    Cuando Ana consulta los movimientos de la cuenta de Ana entre "2026-01-01" y "2026-01-31"
    Entonces Ana debería ver que no hay movimientos

  @CA3 @negativo @CP-HU13-01
  Escenario: Rango de fechas inválido
    Cuando Ana consulta los movimientos de la cuenta de Ana entre "2026-10-20" y "2026-10-19"
    Entonces Ana debería ver el mensaje de error "Rango de fechas inválido"

  @seguridad @CP-SEG-02
  Escenario: El cajero puede consultar el historial de cualquier cuenta
    Cuando el cajero consulta los movimientos de la cuenta de Ana
    Entonces los movimientos son, del más reciente al más antiguo: "Depósito, Retiro, Depósito"
