# Digital Bank: pruebas automatizadas con Screenplay

Pruebas de aceptación automatizadas del frontend de Digital Bank ([DigitalBank-Frontend](https://github.com/Jonathanmg1415/DigitalBank-Frontend)), que consume las APIs de [DigitalBank-Backend](https://github.com/DubanLopezS/DigitalBank-Backend). Los escenarios salen del documento *03 Casos de prueba y Gherkin* del Sprint 2, adaptados a la interfaz, y se amplían con el inicio de sesión (HU-18), el registro de clientes (HU-01) y los depósitos (HU-10).

Curso Calidad de Software, Universidad de Antioquia, 2026-2.

## Tecnologías

| Herramienta | Versión |
|---|---|
| Java | 17 |
| Gradle (wrapper) | 7.6.4 |
| Serenity BDD con Screenplay y Cucumber | 4.1 |
| Navegador | Chrome (el driver se descarga solo) |
| Scaffold | Plugin `io.github.jumosqu12.screenplayarchitecture` 2.0.1 |

La estructura base se generó con el plugin de scaffold, igual que en el taller de BuggyCars:

```bash
gradlew screenPlayArchitecture --type=UX --groupId=co.edu.udea.calidad --principalPackage=digitalbank --javaVersion=17
```

## Estructura

```text
src/main/java/co/edu/udea/calidad/digitalbank/
  exceptions/      PreparacionDeDatosException: falla del ambiente al preparar datos, no de la funcionalidad
  integrations/    ApiDigitalBank: prepara los datos de cada escenario por la API (Arrange)
  interactions/    LlenarFormularioLogin, SeleccionarCuenta, EscribirFecha, EsperarRespuesta
  models/          Credenciales, Cliente, Cuenta
  questions/       ElMensaje, ElComprobante, LaCuenta, LosMovimientos, LaSesion, LosErroresDeLosCampos
  tasks/           IniciarSesion, Retirar, Transferir, Depositar, CambiarEstadoDeCuenta, ConsultarMovimientos,
                   RegistrarCliente, AbrirUnaCuenta, IrA, AbrirDigitalBank, CerrarSesion
  userinterfaces/  Targets de cada pantalla, ubicados por id
  utils/           ClienteFactory, Usuarios, Configuracion, Montos, MemoryKeys, Wait
src/test/java/co/edu/udea/calidad/digitalbank/
  runners/         Runner
  stepdefinitions/ Pasos por feature, Elenco (actores) y PreparacionStepDefinitions (datos por API)
src/test/resources/
  features/        Un feature por historia de usuario, más el flujo de extremo a extremo
  serenity.conf    Navegador, tiempos de espera y URL del frontend por ambiente
```

## Escenarios

| Feature | Historia | Escenarios | Casos de prueba del documento |
|---|---|---|---|
| `autenticacion/inicio_sesion.feature` | HU-18 | 8 | — |
| `clientes/registro_cliente.feature` | HU-01 | 3 | — |
| `deposito/deposito.feature` | HU-10 | 5 | CP-HU07a-01 (depósito en cuenta bloqueada) |
| `retiro/retiro.feature` | HU-11 | 7 | CP-HU11-01, CP-HU07a-01, CP-SEG-02 |
| `transferencias/transferencia.feature` | HU-09a, 09b, 09c | 10 | CP-HU09-01, CP-HU09-02, CP-SEG-02 |
| `estado_cuentas/bloqueo_cuenta.feature` | HU-07a | 7 | CP-HU07a-01, CP-SEG-02 |
| `estado_cuentas/cierre_cuenta.feature` | HU-07b | 7 | CP-HU07b-01 |
| `movimientos/historial.feature` | HU-13 | 5 | CP-HU13-01, CP-SEG-02 |
| `e2e/flujo_completo.feature` | Release 1 | 1 | CP-E2E-02 |
| **Total** | | **53** | |

Cada escenario conserva las etiquetas del documento: historia (`@HU-11`), criterio (`@CA3`), tipo (`@positivo`, `@negativo`, `@limite`, `@seguridad`) y caso de prueba (`@CP-HU11-01`).

### Adaptación de los casos del documento a la interfaz

Los casos del Sprint 2 se escribieron contra la API: verifican códigos HTTP y cuerpos JSON. En la interfaz se verifica lo que ve el usuario:

| En el documento | En la automatización |
|---|---|
| "La respuesta tiene el código 409 y el mensaje *Saldo insuficiente*" | La pantalla muestra el mensaje de error *Saldo insuficiente*, que es el texto que envía el backend |
| "La respuesta tiene el código 201" y el cuerpo de la transacción | La pantalla muestra el mensaje de éxito y el comprobante con los saldos |
| "El saldo de la cuenta es 150000.00" | El cliente ve ese saldo disponible en "Mis cuentas" |
| "Alguien sin token: 401" | Sin sesión, la aplicación lleva al formulario de inicio de sesión |
| "El cajero: 403" | El cajero no ve la opción en el menú y, si escribe la dirección, ve "Acceso no permitido" |
| "Ningún motivo: 400 *El motivo es obligatorio*" | El frontend no envía la solicitud y muestra el error del campo *Escriba el motivo.* |

Quedan por fuera las filas que solo se pueden comprobar en la base de datos (filas de auditoría, número de registros) o con valores que la interfaz no permite enviar (un tipo de movimiento "PAGO", un tamaño de página de 101).

## Preparación de los datos

Cada escenario crea sus propios clientes y cuentas por la API, con documento, correo y teléfono únicos (`ClienteFactory`), antes de actuar en la interfaz. Así:

- los escenarios no dependen de datos creados a mano ni entre sí;
- se pueden repetir las veces que se quiera sin limpiar la base;
- lo que se prueba (el "Cuando" y el "Entonces") se hace siempre desde la interfaz, con tareas de Screenplay.

La única excepción es el flujo de extremo a extremo, que crea todo desde la interfaz: el administrador registra a los clientes, cada uno abre su cuenta y el cajero deposita.

Las llamadas a la API pasan por el mismo origen del frontend (`/svc/auth`, `/svc/core`, `/svc/reportes`), así que solo hay que configurar la URL del frontend.

## Requisitos para ejecutar

1. **Java 17.** Gradle 7.6.4 no funciona con Java 21 ni con Java 25. Si tiene varias versiones, en PowerShell:
   ```powershell
   $env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.x.x-hotspot"
   ```
2. **El frontend corriendo**, por defecto en http://localhost:5173 (`npm run dev` en DigitalBank-Frontend).
3. **Un backend con la rama `main`** (rutas `/api/v1/...`) que devuelva el campo `id` en el listado de cuentas (ver "Dependencias con el backend").
4. **Usuarios ADMIN y CAJERO** en ese backend. Copie `usuarios.example.properties` como `usuarios.local.properties` y escriba sus credenciales. Ese archivo está en el `.gitignore`. Los clientes los crea cada escenario.

## Ejecución

```bash
# Todos los escenarios y el reporte
gradlew clean test aggregate

# Solo una historia o un tipo de escenario
gradlew clean test aggregate -Dcucumber.filter.tags="@HU-11"
gradlew clean test aggregate -Dcucumber.filter.tags="@seguridad"

# Sin pausas entre pasos y sin abrir ventanas del navegador
gradlew clean test aggregate -Dstep.pause=0 -Dheadless.mode=true

# Contra el frontend desplegado (ajuste la URL del ambiente "vercel" en serenity.conf)
gradlew clean test aggregate -Denvironment=vercel
```

El reporte queda en `target/site/serenity/index.html`.

`step.pause` agrega una pausa entre pasos (1000 ms por defecto) para que en una exposición se pueda seguir lo que hace el navegador. Las esperas reales son por condición: la interacción `EsperarRespuesta` espera a que el botón que envió la operación deje de estar ocupado (`aria-busy`), con un límite de `digitalbank.respuesta.timeout` segundos (90 por defecto, porque Render tarda en despertar las APIs).

## Dependencias con el backend

Al 24/11/2026 las pruebas no se pueden ejecutar contra el despliegue de Render, por dos razones:

1. **Render sirve una versión anterior del backend** (Sprint 2): rutas `/api/...` sin `/v1` y sin los endpoints del Sprint 3. Hay que volver a desplegar `main`.
2. **El listado de cuentas no devuelve el identificador de la cuenta.** Retiro, transferencia, depósito, bloqueo, cierre e historial reciben el UUID de la cuenta en la ruta, pero `GET /api/v1/clientes/{clienteId}/cuentas` solo devuelve el número. Se corrige agregando `id` a `CuentaResumenResponse` en core-api (y ajustando `ClienteCuentasControllerTest`). Sin ese cambio, el frontend muestra un aviso y no envía esas operaciones, y los escenarios correspondientes fallan con ese aviso.

Para validar la automatización se usó un backend local con ese cambio aplicado: las tres APIs de `main` sobre PostgreSQL, con los usuarios ADMIN-QA y CAJERO-QA creados con `docs/scripts/crear-usuarios-internos.sql`.
