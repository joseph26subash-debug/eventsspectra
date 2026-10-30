# Event Spectra - PowerShell Runner Script
# Compiles and starts the Java backend & opens the browser

Write-Host "==================================================================" -ForegroundColor Cyan
Write-Host "             EVENT SPECTRA - FISAT EVENT MANAGEMENT SYSTEM        " -ForegroundColor Cyan
Write-Host "                  Object-Oriented Programming in Java             " -ForegroundColor Cyan
Write-Host "==================================================================" -ForegroundColor Cyan

# Locate Java
$javaBin = $null
if (Get-Command javac -ErrorAction SilentlyContinue) {
    $javaBin = ""
} elseif (Test-Path "C:\Program Files\Android\Android Studio1\jbr\bin\javac.exe") {
    $env:PATH = "C:\Program Files\Android\Android Studio1\jbr\bin;" + $env:PATH
    $javaBin = "C:\Program Files\Android\Android Studio1\jbr\bin\"
} elseif ($env:JAVA_HOME -and (Test-Path "$env:JAVA_HOME\bin\javac.exe")) {
    $env:PATH = "$env:JAVA_HOME\bin;" + $env:PATH
    $javaBin = "$env:JAVA_HOME\bin\"
} else {
    Write-Host "[ERROR] javac not found in PATH or standard paths." -ForegroundColor Red
    exit 1
}

Write-Host "[1/3] Java detected:" -ForegroundColor Green
& "${javaBin}javac" -version

if (!(Test-Path "bin")) {
    New-Item -ItemType Directory -Path "bin" | Out-Null
}

Write-Host "[2/3] Compiling Java classes in src/... with MySQL Driver" -ForegroundColor Yellow
& "${javaBin}javac" -cp "lib/mysql-connector-j.jar;." -d bin src/*.java
if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Compilation failed!" -ForegroundColor Red
    exit 1
}
Write-Host "[OK] Compilation complete!" -ForegroundColor Green

Write-Host "[3/3] Starting Event Spectra Web Server with MySQL on http://localhost:8080/ ..." -ForegroundColor Cyan
Start-Process "http://localhost:8080/"
& "${javaBin}java" -cp "bin;lib/mysql-connector-j.jar" Main 8080
