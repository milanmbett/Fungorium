@echo off
setlocal EnableDelayedExpansion

REM filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test22/test.bat

for %%I in ("%~dp0.") do set "SCRIPT_DIR=%%~fI"
REM Load environment variables from the .env file (assumed one level above test22)
for /f "usebackq tokens=1* delims==" %%a in ("%SCRIPT_DIR%\..\.env") do (
    set "LINE=%%b"
    set "LINE=!LINE:"=!"
    set "%%a=!LINE!"
)

REM Get the absolute path of the jar.
for %%F in ("%~dp0..\%JAR_NAME%") do set "JAR_FILE=%%~fF"

REM Set the input file relative to this script.
set "INPUT_FILE=%~dp0input.txt"

REM Run the jar with --test 22 and redirect output to output.log.
java -jar "%JAR_FILE%" --test 22 < "%INPUT_FILE%" > output.log 2>&1

REM Define essential regex patterns.
set "PATTERN1=Player created with default values. Income: 200, Score: 0"
set "PATTERN2=Tecton_Basic Created! ID: Tecton_Basic0"
set "PATTERN3=Thread Created! ID: Thread0 on Tecton: Tecton_Basic0"
set "PATTERN4=Insect_Buglet Created! ID: Insect_Buglet0 on Tecton: Tecton_Basic0"
set "PATTERN5=NULL  com\.coderunnerlovagjai\.app\.Tecton_Class - Mushroom is null!"
set "PATTERN6=Mushroom_Shroomlet Created! ID: Mushroom_Shroomlet1 on Tecton: Tecton_Basic1"
set "PATTERN7=SUCCESS com\.coderunnerlovagjai\.app\._Tests - Test ran successfully!"

REM Check that all patterns are present in output.log.
call :checkPattern "%PATTERN1%"
call :checkPattern "%PATTERN2%"
call :checkPattern "%PATTERN3%"
call :checkPattern "%PATTERN4%"
call :checkPattern "%PATTERN5%"
call :checkPattern "%PATTERN6%"
call :checkPattern "%PATTERN7%"

echo [test22] Test successful!
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