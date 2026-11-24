# language: es
@HU-18 @Autenticacion
Característica: HU-18 Inicio de sesión
  Como usuario de Digital Bank
  Quiero iniciar sesión con mi correo y mi contraseña
  Para usar las opciones que corresponden a mi rol

  Antecedentes:
    Dado que Ana es cliente del banco

  @positivo
  Esquema del escenario: Cada usuario entra con el rol que le corresponde
    Cuando <usuario> inicia sesión con sus credenciales
    Entonces <usuario> debería ver su rol "<rol>" en el encabezado

    Ejemplos:
      | usuario          | rol           |
      | Ana              | Cliente       |
      | el cajero        | Cajero        |
      | el administrador | Administrador |

  @negativo
  Escenario: Contraseña incorrecta
    Cuando Ana inicia sesión con una contraseña errónea
    Entonces Ana debería ver el mensaje de error "Credenciales inválidas"

  @negativo
  Escenario: Correo que no está registrado
    Cuando Visitante inicia sesión con un correo que no está registrado
    Entonces Visitante debería ver el mensaje de error "Credenciales inválidas"

  @negativo @limite
  Escenario: El usuario se bloquea al tercer intento fallido
    Dado que Ana se equivocó de contraseña 3 veces
    Cuando Ana inicia sesión con sus credenciales correctas
    Entonces Ana debería ver el mensaje de error "Cuenta bloqueada temporalmente por múltiples intentos fallidos"

  @negativo
  Escenario: El correo y la contraseña son obligatorios
    Cuando Ana intenta ingresar sin escribir el correo ni la contraseña
    Entonces Ana debería ver el error de campo "Escriba su correo electrónico."
    Y Ana debería ver el error de campo "Escriba su contraseña."

  @seguridad
  Escenario: Sin sesión iniciada no se puede entrar a las opciones del banco
    Cuando Visitante intenta entrar a la dirección "/transferencias"
    Entonces Visitante debería ver el formulario de inicio de sesión
