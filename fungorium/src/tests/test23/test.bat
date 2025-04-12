@echo off
setlocal EnableDelayedExpansion

REM filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test23/test.bat

REM Get the directory of this script.
for %%I in ("%~dp0.") do set "SCRIPT_DIR=%%~fI"

REM Load environment variables from ../.env.
for /f "usebackq tokens=1* delims==" %%a in ("%SCRIPT_DIR%\..\.env") do (
    set "LINE=%%b"
    set "LINE=!LINE:"=!"
    set "%%a=!LINE!"
)

REM Compute absolute path of the jar.
for %%F in ("%SCRIPT_DIR%\..\%JAR_NAME%") do set "JAR_FILE=%%~fF"

REM Set the input file.
set "INPUT_FILE=%SCRIPT_DIR%\input.txt"

REM Run the jar with "--test 23" and capture its output to output.log.
(
    for /f "delims=" %%a in ('java -jar "%JAR_FILE%" --test 23 ^< "%INPUT_FILE%"') do (
        echo %%a
    )
) > output.log

REM Check essential patterns.
call :check "Tecton_Class Constructor called!"
call :check "Tecton_Basic Created! ID: Tecton_Basic0"
call :check "Tecton_Dead Created! ID: Tecton_Dead1"
call :check "NULL  com.coderunnerlovagjai.app.Tecton_Class - Thread is null!"
call :check "Cannot create thread on dead tecton!"
call :check "SUCCESS com.coderunnerlovagjai.app._Tests - Test ran successfully!"

echo [test23] Test successful!
goto :eof

:check
findstr /c:"%~1" output.log >nul
if errorlevel 1 (
    echo Test failed! Pattern not found:
    echo %~1
    type output.log
    exit /b 1
)
goto :eof