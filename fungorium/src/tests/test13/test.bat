@echo off
setlocal EnableDelayedExpansion

REM filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test13/test.bat

REM Get the directory of this script.
for %%I in ("%~dp0.") do set "SCRIPT_DIR=%%~fI"

REM Load global environment variables from the .env file (assumed one level above test13).
for /f "usebackq tokens=1* delims==" %%a in ("%SCRIPT_DIR%\..\.env") do (
    set "LINE=%%b"
    set "LINE=!LINE:"=!"
    set "%%a=!LINE!"
)

REM Convert the relative JAR path to an absolute path using SCRIPT_DIR as base.
for %%F in ("%SCRIPT_DIR%\..\%JAR_NAME%") do set "JAR_FILE=%%~fF"

REM Set the input file (relative to this script's directory).
set "INPUT_FILE=%SCRIPT_DIR%\input.txt"

REM Run the Java program with parameter "--test 13" and redirect output to output.log.
java -jar "%JAR_FILE%" --test 13 < "%INPUT_FILE%" > output.log 2>&1

REM Define expected regex patterns.
set "PATTERN1=Player created with default values. Income: 200, Score: 0"
set "PATTERN2=Tecton_Class Constructor called!"
set "PATTERN3=Tecton_Basic Created! ID: Tecton_Basic0"
set "PATTERN4=Tecton_Basic: Tecton_Basic0 added to TectonCollection! TectonCollection size: 1"
set "PATTERN5=Tecton_Basic Created! ID: Tecton_Basic1"
set "PATTERN6=Tecton_Basic: Tecton_Basic1 added to TectonCollection! TectonCollection size: 2"
set "PATTERN7=Mushroom_Class Constructor called!"
set "PATTERN8=Tecton's Mushroom: .*Mushroom_Shroomlet"
set "PATTERN9=Mushroom_Shroomlet Created! ID: Mushroom_Shroomlet0 on Tecton: Tecton_Basic0"
set "PATTERN10=Mushroom_Shroomlet: Mushroom_Shroomlet0 added to MushroomCollection! MushroomCollection size: 1"
set "PATTERN11=INIT  com\.coderunnerlovagjai\.app\.Insect_Class - Insect_Class Constructor called!"
set "PATTERN12=Insect_Buglet Created! ID: Insect_Buglet0 on Tecton: Tecton_Basic0"
set "PATTERN13=Insect_Buglet: Insect_Buglet0 added to InsectCollection! InsectCollection size: 1"
set "PATTERN14=Insect_Buglet Created! ID: Insect_Buglet1 on Tecton: Tecton_Basic1"
set "PATTERN15=Insect_Buglet: Insect_Buglet1 added to InsectCollection! InsectCollection size: 2"
set "PATTERN16=Mushroom: Mushroom_Shroomlet0 is attacking insect: Insect_Buglet0 with power: 25"
set "PATTERN17=Insect: Insect_Buglet0 HP reduced by: 25. Current HP: 75"
set "PATTERN18=Mushroom: Mushroom_Shroomlet0 is attacking insect: Insect_Buglet1 with power: 25"
set "PATTERN19=Insect: Insect_Buglet1 HP reduced by: 25. Current HP: 75"
set "PATTERN20=SUCCESS com\.coderunnerlovagjai\.app\._Tests - Test ran successfully!"

REM Verify that each expected pattern exists in the output.
call :checkPattern "%PATTERN1%"
call :checkPattern "%PATTERN2%"
call :checkPattern "%PATTERN3%"
call :checkPattern "%PATTERN4%"
call :checkPattern "%PATTERN5%"
call :checkPattern "%PATTERN6%"
call :checkPattern "%PATTERN7%"
call :checkPattern "%PATTERN8%"
call :checkPattern "%PATTERN9%"
call :checkPattern "%PATTERN10%"
call :checkPattern "%PATTERN11%"
call :checkPattern "%PATTERN12%"
call :checkPattern "%PATTERN13%"
call :checkPattern "%PATTERN14%"
call :checkPattern "%PATTERN15%"
call :checkPattern "%PATTERN16%"
call :checkPattern "%PATTERN17%"
call :checkPattern "%PATTERN18%"
call :checkPattern "%PATTERN19%"
call :checkPattern "%PATTERN20%"

echo [test13] Test successful!
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
exit /b