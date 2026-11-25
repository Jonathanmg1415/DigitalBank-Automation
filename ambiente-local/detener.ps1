<#
.SYNOPSIS
    Detiene el ambiente local de Digital Bank que levantó iniciar.ps1: el frontend, las tres APIs
    y PostgreSQL. Los datos de la base se conservan para la próxima vez.

.PARAMETER BorrarDatos
    Además de detener todo, borra la base de datos local para empezar de cero la próxima vez.
#>
param([switch]$BorrarDatos)

# 'Continue': en PowerShell 5.1, la salida de errores de pg_ctl o taskkill no debe detener el script.
$ErrorActionPreference = 'Continue'
$aqui   = $PSScriptRoot
$datos  = Join-Path $aqui 'datos'
$pgData = Join-Path $datos 'pgdata'

foreach ($nombre in 'frontend', 'reportes-api', 'auth-api', 'core-api') {
    $archivo = Join-Path $datos "$nombre.pid"
    if (-not (Test-Path $archivo)) { continue }
    $id = (Get-Content $archivo -Raw).Trim()
    if (Get-Process -Id $id -ErrorAction SilentlyContinue) {
        # /T detiene también los procesos hijos (npm inicia node, por ejemplo).
        & taskkill.exe /PID $id /T /F 2>&1 | Out-Null
        Write-Host "$nombre detenido."
    }
    Remove-Item $archivo
}

$pgCtl = Get-ChildItem 'C:\Program Files\PostgreSQL' -Directory -ErrorAction SilentlyContinue |
    Sort-Object Name -Descending | Select-Object -First 1 |
    ForEach-Object { Join-Path $_.FullName 'bin\pg_ctl.exe' }
if ($pgCtl -and (Test-Path (Join-Path $pgData 'PG_VERSION'))) {
    & $pgCtl -D $pgData status 2>&1 | Out-Null
    if ($LASTEXITCODE -eq 0) {
        & $pgCtl -D $pgData stop -m fast 2>&1 | Out-Null
        Write-Host 'PostgreSQL detenido.'
    }
}

if ($BorrarDatos -and (Test-Path $pgData)) {
    Remove-Item -Recurse -Force $pgData
    Write-Host 'Base de datos local borrada.'
}
Write-Host 'Ambiente local detenido.' -ForegroundColor Green
