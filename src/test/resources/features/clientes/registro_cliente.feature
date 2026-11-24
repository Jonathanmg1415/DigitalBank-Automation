# language: es
@HU-01 @F1.1 @Clientes
Característica: HU-01 Registro de cliente
  Como administrador del banco
  Quiero registrar clientes nuevos
  Para que puedan abrir cuentas y usar Digital Bank

  Antecedentes:
    Dado el administrador inicia sesión en Digital Bank

  @positivo
  Escenario: Registro exitoso de un cliente
    Cuando el administrador registra a una cliente nueva llamada "Ana"
    Entonces el administrador debería ver el mensaje de éxito "El cliente Ana Restrepo Gómez se registró correctamente."
    Y la cliente registrada puede iniciar sesión con su correo y su contraseña

  @negativo
  Escenario: No se registra dos veces el mismo documento
    Dado que Luis es cliente del banco
    Cuando el administrador registra otro cliente con el documento de Luis
    Entonces el administrador debería ver el mensaje de error "El cliente ya existe"

  @negativo @limite
  Escenario: El cliente debe ser mayor de edad
    Cuando el administrador registra a un cliente que cumple 18 años mañana
    Entonces el administrador debería ver el mensaje de error "El cliente debe ser mayor de edad"
