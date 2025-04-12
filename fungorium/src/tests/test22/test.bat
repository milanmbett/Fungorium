@echo off
setlocal EnableDelayedExpansion

REM filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test22/test.bat

REM Get the script directory.
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

REM Run the jar with --test 22 and capture its output to output.log.
(
    for /f "delims=" %%a in ('java -jar "%JAR_FILE%" --test 22 ^< "%INPUT_FILE%"') do (
        echo %%a
    )
) > output.log

REM Check essential patterns.
call :check "Player created with default values. Income: 200, Score: 0"
call :check "Tecton_Basic Created! ID: Tecton_Basic0"
call :check "Thread Created! ID: Thread0 on Tecton: Tecton_Basic0"
call :check "Insect_Buglet Created! ID: Insect_Buglet0 on Tecton: Tecton_Basic0"
call :check "NULL  com.coderunnerlovagjai.app.Tecton_Class - Mushroom is null!"
call :check "Mushroom_Shroomlet Created! ID: Mushroom_Shroomlet1 on Tecton: Tecton_Basic1"
call :check "SUCCESS com.coderunnerlovagjai.app._Tests - Test ran successfully!"

echo [test22] Test successful!
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