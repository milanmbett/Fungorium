@echo off
setlocal EnableDelayedExpansion

REM filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test23/test.bat

REM Get the directory of this script.
for %%I in ("%~dp0.") do set "SCRIPT_DIR=%%~fI"

REM Load global environment variables from the .env file (assumed one level above test23).
for /f "usebackq tokens=1* delims==" %%a in ("%SCRIPT_DIR%\..\.env") do (
    set "LINE=%%b"
    set "LINE=!LINE:"=!"
    set "%%a=!LINE!"
)

REM Get the absolute path for the jar file.
for %%F in ("%SCRIPT_DIR%\..\%JAR_NAME%") do set "JAR_FILE=%%~fF"

REM Set the input file relative to this script.
set "INPUT_FILE=%SCRIPT_DIR%\input.txt"

REM Run the jar with "--test 23" and redirect output to output.log.
java -jar "%JAR_FILE%" --test 23 < "%INPUT_FILE%" > output.log 2>&1

REM Define essential regex patterns.
set "PATTERN1=Tecton_Class Constructor called!"
set "PATTERN2=Tecton_Basic Created! ID: Tecton_Basic0"
set "PATTERN3=Tecton_Dead Created! ID: Tecton_Dead1"
set "PATTERN4=NULL  com\.coderunnerlovagjai\.app\.Tecton_Class - Thread is null!"
set "PATTERN5=Cannot create thread on dead tecton!"
set "PATTERN6=SUCCESS com\.coderunnerlovagjai\.app\._Tests - Test ran successfully!"

REM Check that all patterns are present in output.log.
call :checkPattern "%PATTERN1%"
call :checkPattern "%PATTERN2%"
call :checkPattern "%PATTERN3%"
call :checkPattern "%PATTERN4%"
call :checkPattern "%PATTERN5%"
call :checkPattern "%PATTERN6%"

echo [test23] Test successful!
exit /b 0

:checkPattern
REM Disable delayed expansion so that special characters are preserved.
setlocal DisableDelayedExpansion
findstr /c:"%~1" output.log >nul
if errorlevel 1 (
    endlocal
    exit /b 1
) else (
    endlocal
)
goto :eof