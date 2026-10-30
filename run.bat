@echo off
setlocal enabledelayedexpansion

echo ==================================================================
echo             EVENT SPECTRA - FISAT EVENT MANAGEMENT SYSTEM         
echo                  Object-Oriented Programming in Java               
echo ==================================================================
echo.

:: Detect Java Compiler and Runtime
set "JAVA_CMD=java"
set "JAVAC_CMD=javac"

where javac >nul 2>nul
if %errorlevel% neq 0 (
    if exist "C:\Program Files\Android\Android Studio1\jbr\bin\javac.exe" (
        set "JAVA_HOME=C:\Program Files\Android\Android Studio1\jbr"
        set "PATH=C:\Program Files\Android\Android Studio1\jbr\bin;%PATH%"
        set "JAVAC_CMD=C:\Program Files\Android\Android Studio1\jbr\bin\javac.exe"
        set "JAVA_CMD=C:\Program Files\Android\Android Studio1\jbr\bin\java.exe"
    ) else if defined JAVA_HOME (
        set "JAVAC_CMD=%JAVA_HOME%\bin\javac.exe"
        set "JAVA_CMD=%JAVA_HOME%\bin\java.exe"
    ) else (
        echo [ERROR] Java Development Kit (javac) not found!
        echo Please ensure JDK 11+ or Android Studio JBR is installed.
        pause
        exit /b 1
    )
)

echo [1/3] Java Environment detected:
"%JAVA_CMD%" -version
echo.

:: Create build folder
if not exist "bin" (
    mkdir bin
)

:: Compile
echo [2/3] Compiling Java classes in src/... with MySQL Driver
"%JAVAC_CMD%" -cp "lib\mysql-connector-j.jar;." -d bin src\*.java
if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b %errorlevel%
)
echo [OK] Compilation successful!
echo.

:: Launch
echo [3/3] Starting Event Spectra Web Server with MySQL on http://localhost:8080/ ...
start "" "http://localhost:8080/"
"%JAVA_CMD%" -cp "bin;lib\mysql-connector-j.jar" Main 8080

pause
