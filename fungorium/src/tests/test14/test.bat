@echo off
setlocal EnableDelayedExpansion

REM filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test14/test.bat

REM Get the directory of this script.
for %%I in ("%~dp0.") do set "SCRIPT_DIR=%%~fI"

REM Load global environment variables from the .env file (assumed one level above test14).
for /f "usebackq tokens=1* delims==" %%a in ("%SCRIPT_DIR%\..\.env") do (
    set "LINE=%%b"
    set "LINE=!LINE:"=!"
    set "%%a=!LINE!"
)

REM Convert the relative JAR path to an absolute path using SCRIPT_DIR as base.
for %%F in ("%SCRIPT_DIR%\..\%JAR_NAME%") do set "JAR_FILE=%%~fF"

REM Set the input file (relative to this script's directory).
set "INPUT_FILE=%SCRIPT_DIR%\input.txt"

REM Run the Java program with parameter "--test 14" and redirect output to output.log.
java -jar "%JAR_FILE%" --test 14 < "%INPUT_FILE%" > output.log 2>&1

REM Define expected regex patterns.
set "PATTERN1=Player created with default values. Income: 200, Score: 0"
set "PATTERN2=Tecton_Class Constructor called!"
set "PATTERN3=Tecton_Basic Created! ID: Tecton_Basic0"
set "PATTERN4=Tecton_Basic: Tecton_Basic0 added to TectonCollection! TectonCollection size: 1"
set "PATTERN5=GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's ID: Tecton_Basic0"
set "PATTERN6=CREATE com\.coderunnerlovagjai\.app\.Basic_Spore - Basic_Spore Created! ID: Spore_Basic0 on Tecton: Tecton_Basic0"
set "PATTERN7=ADD   com\.coderunnerlovagjai\.app\.Basic_Spore - Basic_Spore: Spore_Basic0 added to SporeCollection! SporeCollection size: 1"
set "PATTERN8=INIT  com\.coderunnerlovagjai\.app\.Insect_Class - Insect_Class Constructor called!"
set "PATTERN9=GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's Insects: \[\]"
set "PATTERN10=CREATE com\.coderunnerlovagjai\.app\.Insect_Buglet - Insect_Buglet Created! ID: Insect_Buglet0 on Tecton: Tecton_Basic0"
set "PATTERN11=ADD   com\.coderunnerlovagjai\.app\.Insect_Buglet - Insect_Buglet: Insect_Buglet0 added to InsectCollection! InsectCollection size: 1"
set "PATTERN12=GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's Spore: .*Basic_Spore@"
set "PATTERN13=DIE   com\.coderunnerlovagjai\.app\.Basic_Spore - Spore: Spore_Basic0 is dead!"
set "PATTERN14=INCOME com\.coderunnerlovagjai\.app\.Player - Income increased by 100\. New income: 300"
set "PATTERN15=SCORE com\.coderunnerlovagjai\.app\.Player - Score increased by 100\. New score: 100"
set "PATTERN16=NULL  com\.coderunnerlovagjai\.app\.Tecton_Class - Spore is null!"
set "PATTERN17=SUCCESS com\.coderunnerlovagjai\.app\._Tests - Test ran successfully!"

REM Verify that each expected pattern exists in output.log.
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

echo [test14] Test successful!
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