<#
.SYNOPSIS
    Levanta el ambiente local completo de Digital Bank: PostgreSQL, las tres APIs y el frontend.

.DESCRIPTION
    Espera encontrar los tres repositorios en la misma carpeta:
        DigitalBank-Backend\     (la rama que tenga activa es la que se compila)
        DigitalBank-Frontend\
        DigitalBank-Automation\  (este repositorio)

    Todo queda dentro de ambiente-local\datos y ambiente-local\logs, que no se suben a GitHub.
    No usa Docker ni el servicio de PostgreSQL instalado: crea un clúster propio en el puerto 55433.

.PARAMETER Compilar
    Vuelve a compilar el backend aunque ya existan los .jar (por ejemplo, después de cambiar de rama).

.PARAMETER SinFrontend
    Levanta solo la base de datos y las APIs.

.EXAMPLE
    .\ambiente-local\iniciar.ps1
    .\ambiente-local\iniciar.ps1 -Compilar
#>
param(
    [switch]$Compilar,
    [switch]$SinFrontend
)

# 'Continue' y no 'Stop': en PowerShell 5.1, un programa externo que escribe en la salida de errores
# (openssl, Maven, PostgreSQL lo hacen aunque todo salga bien) se tomaría como fallo. El éxito de
# cada programa se revisa con su código de salida en la función Verificar.
$ErrorActionPreference = 'Continue'

function Verificar($descripcion) {
    if ($LASTEXITCODE -ne 0) { throw "Falló: $descripcion (código $LASTEXITCODE)" }
}

# ---------- Configuración ----------
$aqui      = $PSScriptRoot
$raiz      = (Resolve-Path (Join-Path $aqui '..\..')).Path
$backend   = Join-Path $raiz 'DigitalBank-Backend'
$frontend  = Join-Path $raiz 'DigitalBank-Frontend'
$datos     = Join-Path $aqui 'datos'
$logs      = Join-Path $aqui 'logs'
$pgData    = Join-Path $datos 'pgdata'
$puertoBd  = 55433
$claveBd   = 'pruebas_local'
$secretoJwt = 'secreto-local-solo-para-pruebas-automatizadas-2026'

# Usuarios internos del documento 03_Casos_de_Prueba_y_Gherkin (Sprint 2, sección 2).
$usuariosInternos = @{
    '<CORREO_ADMIN>'      = 'admin.qa@digitalbank.test'
    '<CONTRASENA_ADMIN>'  = 'Admin#2026'
    '<CORREO_CAJERO>'     = 'cajero.qa@digitalbank.test'
    '<CONTRASENA_CAJERO>' = 'Cajero#2026'
}

$apis = @(
    @{ Modulo = 'core-api';     Puerto = 8082 },   # primero: aplica las migraciones de Flyway
    @{ Modulo = 'auth-api';     Puerto = 8081 },
    @{ Modulo = 'reportes-api'; Puerto = 8083 }
)

# ---------- Funciones de apoyo ----------
function Escribir-Paso($texto) { Write-Host "`n==> $texto" -ForegroundColor Cyan }

function Buscar-Carpeta($rutas, $filtro, $descripcion) {
    $carpeta = Get-ChildItem $rutas -Directory -Filter $filtro -ErrorAction SilentlyContinue |
        Sort-Object Name -Descending | Select-Object -First 1
    if (-not $carpeta) { throw "No se encontró $descripcion en: $($rutas -join ', ')" }
    return $carpeta.FullName
}

function Puerto-Ocupado($puerto) {
    return [bool](Get-NetTCPConnection -State Listen -LocalPort $puerto -ErrorAction SilentlyContinue)
}

function Esperar-Texto($archivo, $patron, $segundos) {
    $limite = (Get-Date).AddSeconds($segundos)
    while ((Get-Date) -lt $limite) {
        if ((Test-Path $archivo) -and (Select-String -Path $archivo -Pattern $patron -Quiet)) { return $true }
        Start-Sleep -Seconds 2
    }
    return $false
}

# ---------- Verificaciones ----------
Escribir-Paso 'Verificando requisitos'
foreach ($repo in $backend, $frontend) {
    if (-not (Test-Path $repo)) { throw "No se encontró el repositorio $repo" }
}
$java21 = if ($env:JAVA21_HOME) { $env:JAVA21_HOME } else {
    Buscar-Carpeta @('C:\Program Files\Eclipse Adoptium', 'C:\Program Files\Java') 'jdk-21*' 'un JDK 21'
}
$pgBin = Join-Path (Buscar-Carpeta @('C:\Program Files\PostgreSQL') '*' 'PostgreSQL') 'bin'
$openssl = 'C:\Program Files\Git\usr\bin\openssl.exe'
New-Item -ItemType Directory -Force $datos, $logs | Out-Null
$rama = (& git -C $backend rev-parse --abbrev-ref HEAD).Trim()
Write-Host "JDK 21: $java21"
Write-Host "PostgreSQL: $pgBin"
Write-Host "Backend en la rama: $rama"

# ---------- 1. Compilar el backend ----------
$jars = $apis | ForEach-Object { Join-Path $backend "$($_.Modulo)\target\$($_.Modulo)-0.0.1-SNAPSHOT.jar" }
if ($Compilar -or ($jars | Where-Object { -not (Test-Path $_) })) {
    Escribir-Paso "Compilando el backend (rama $rama). La primera vez descarga las dependencias y tarda varios minutos"
    $env:JAVA_HOME = $java21
    # Configuración de Maven propia: no usa el mirror del settings.xml del usuario.
    & (Join-Path $backend 'mvnw.cmd') -q -B -f (Join-Path $backend 'pom.xml') `
        -s (Join-Path $aqui 'maven-settings.xml') "-Dmaven.repo.local=$(Join-Path $datos 'm2')" `
        clean package '-Dmaven.test.skip=true'
    Verificar 'la compilación del backend'
} else {
    Write-Host "`nLos .jar ya existen; se usan tal cual (use -Compilar después de cambiar de rama)."
}

# ---------- 2. PostgreSQL ----------
Escribir-Paso "Base de datos PostgreSQL en el puerto $puertoBd"
if (-not (Test-Path (Join-Path $pgData 'PG_VERSION'))) {
    Write-Host 'Creando el clúster (solo la primera vez)'
    $archivoClave = Join-Path $datos 'clave-bd.txt'
    Set-Content -Path $archivoClave -Value $claveBd -Encoding ASCII
    & (Join-Path $pgBin 'initdb.exe') -D $pgData -U postgres "--pwfile=$archivoClave" -A scram-sha-256 -E UTF8 --locale=C 2>&1 | Out-Null
    $codigo = $LASTEXITCODE
    Remove-Item $archivoClave
    $LASTEXITCODE = $codigo; Verificar 'la creación del clúster de PostgreSQL'
}
# El backend exige sslmode=require: certificado autofirmado solo para este ambiente.
if (-not (Test-Path (Join-Path $pgData 'server.crt'))) {
    & $openssl req -new -x509 -days 3650 -nodes -subj '/CN=localhost' `
        -keyout (Join-Path $pgData 'server.key') -out (Join-Path $pgData 'server.crt') 2>&1 | Out-Null
    Verificar 'la creación del certificado SSL'
}
$configuracion = Join-Path $pgData 'postgresql.conf'
if (-not (Select-String -Path $configuracion -Pattern "^port = $puertoBd" -Quiet)) {
    Add-Content -Path $configuracion -Value "`nport = $puertoBd`nssl = on`nlisten_addresses = 'localhost'"
}
& (Join-Path $pgBin 'pg_isready.exe') -h localhost -p $puertoBd -q
if ($LASTEXITCODE -ne 0) {
    # pg_ctl se inicia como proceso aparte: si se invoca directamente, el servidor hereda la salida
    # del script y PowerShell se queda esperando a que esa salida se cierre.
    Start-Process -FilePath (Join-Path $pgBin 'pg_ctl.exe') -WindowStyle Hidden `
        -ArgumentList '-D', "`"$pgData`"", '-l', "`"$(Join-Path $logs 'postgresql.log')`"", 'start' | Out-Null
    $limite = (Get-Date).AddSeconds(60)
    do {
        Start-Sleep -Seconds 1
        & (Join-Path $pgBin 'pg_isready.exe') -h localhost -p $puertoBd -q
    } while ($LASTEXITCODE -ne 0 -and (Get-Date) -lt $limite)
    Verificar "el arranque de PostgreSQL (revise $logs\postgresql.log)"
}
Write-Host 'PostgreSQL listo.'

# ---------- 3. APIs ----------
$env:DB_HOST = 'localhost'; $env:DB_PORT = "$puertoBd"; $env:DB_NAME = 'postgres'
$env:DB_USERNAME = 'postgres'; $env:DB_PASSWORD = $claveBd; $env:JWT_SECRET = $secretoJwt
foreach ($api in $apis) {
    Escribir-Paso "$($api.Modulo) en el puerto $($api.Puerto)"
    if (Puerto-Ocupado $api.Puerto) { Write-Host 'Ya está corriendo.'; continue }
    $env:PORT = "$($api.Puerto)"
    $log = Join-Path $logs "$($api.Modulo).log"
    $jar = Join-Path $backend "$($api.Modulo)\target\$($api.Modulo)-0.0.1-SNAPSHOT.jar"
    $proceso = Start-Process -FilePath (Join-Path $java21 'bin\java.exe') `
        -ArgumentList '-Xmx400m', '-jar', "`"$jar`"" `
        -RedirectStandardOutput $log -RedirectStandardError (Join-Path $logs "$($api.Modulo).err.log") `
        -WindowStyle Hidden -PassThru
    Set-Content -Path (Join-Path $datos "$($api.Modulo).pid") -Value $proceso.Id
    if (-not (Esperar-Texto $log 'Started \w+Application' 150)) {
        throw "$($api.Modulo) no arrancó. Revise $log y $($api.Modulo).err.log"
    }
    Write-Host 'Lista.'
}

# ---------- 4. Usuarios internos ----------
Escribir-Paso 'Usuarios ADMIN y CAJERO'
$sql = Get-Content (Join-Path $backend 'docs\scripts\crear-usuarios-internos.sql') -Raw
foreach ($marcador in $usuariosInternos.Keys) { $sql = $sql.Replace($marcador, $usuariosInternos[$marcador]) }
$env:PGPASSWORD = $claveBd
$sql | & (Join-Path $pgBin 'psql.exe') -h localhost -p $puertoBd -U postgres -q -v ON_ERROR_STOP=1 2>&1 | Out-Null
Verificar 'la creación de los usuarios internos'
Write-Host 'Creados (si ya existían, se dejan como estaban).'

# ---------- 5. Frontend ----------
if (-not $SinFrontend) {
    Escribir-Paso 'Frontend en http://localhost:5173'
    Set-Content -Path (Join-Path $frontend '.env.local') -Encoding ASCII -Value @(
        'AUTH_API_URL=http://localhost:8081',
        'CORE_API_URL=http://localhost:8082',
        'REPORTES_API_URL=http://localhost:8083')
    if (Puerto-Ocupado 5173) {
        Write-Host 'Ya está corriendo (si se inició antes de este script, reinícielo para que tome .env.local).'
    } else {
        if (-not (Test-Path (Join-Path $frontend 'node_modules'))) {
            Write-Host 'Instalando dependencias (npm install)'
            Push-Location $frontend; try { & npm.cmd install --no-audit --no-fund 2>&1 | Out-Null } finally { Pop-Location }
            Verificar 'npm install del frontend'
        }
        $log = Join-Path $logs 'frontend.log'
        $proceso = Start-Process -FilePath 'cmd.exe' -ArgumentList '/c', 'npm.cmd run dev' `
            -WorkingDirectory $frontend -RedirectStandardOutput $log `
            -RedirectStandardError (Join-Path $logs 'frontend.err.log') -WindowStyle Hidden -PassThru
        Set-Content -Path (Join-Path $datos 'frontend.pid') -Value $proceso.Id
        if (-not (Esperar-Texto $log 'Local:' 60)) { throw "El frontend no arrancó. Revise $log" }
        Write-Host 'Listo.'
    }
}

# ---------- Resumen ----------
Write-Host "`nAmbiente local listo." -ForegroundColor Green
Write-Host '  Frontend:      http://localhost:5173'
Write-Host '  auth-api:      http://localhost:8081/swagger-ui.html'
Write-Host '  core-api:      http://localhost:8082/swagger-ui.html'
Write-Host '  reportes-api:  http://localhost:8083/swagger-ui.html'
Write-Host "  PostgreSQL:    localhost:$puertoBd (usuario postgres)"
Write-Host '  Usuarios:      ver usuarios.local.properties. Los clientes los crea el ADMIN o cada escenario.'
Write-Host "  Registros:     $logs"
Write-Host '  Para detener:  .\ambiente-local\detener.ps1'
