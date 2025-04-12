@echo off
setlocal EnableDelayedExpansion

REM filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test24/test.bat
for %%I in ("%~dp0.") do set "SCRIPT_DIR=%%~fI"

REM Load environment variables from the .env file (assumed one level above test24).
for /f "usebackq tokens=1* delims==" %%a in ("%SCRIPT_DIR%\..\.env") do (
    set "LINE=%%b"
    set "LINE=!LINE:"=!"
    set "%%a=!LINE!"
)

REM Get the absolute path for the jar.
for %%F in ("%~dp0..\%JAR_NAME%") do set "JAR_FILE=%%~fF"

REM Set the input file (relative to this script).
set "INPUT_FILE=%~dp0input.txt"

REM Run the Java program with parameter "--test 24" and redirect output to output.log.
java -jar "%JAR_FILE%" --test 24 < "%INPUT_FILE%" > output.log 2>&1

REM Define essential regex patterns.
set "PATTERN1=Tecton_Class Constructor called!"
set "PATTERN2=Tecton_Basic Created! ID: Tecton_Basic0"
set "PATTERN3=Mushroom_Class Constructor called!"
set "PATTERN4=There is already a mushroom on this tecton!"
set "PATTERN5=SUCCESS com\.coderunnerlovagjai\.app\._Tests - Test ran successfully!"

REM Verify that all essential patterns are present in output.log.
call :checkPattern "%PATTERN1%"
call :checkPattern "%PATTERN2%"
call :checkPattern "%PATTERN3%"
call :checkPattern "%PATTERN4%"
call :checkPattern "%PATTERN5%"

echo [test24] Test successful!
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