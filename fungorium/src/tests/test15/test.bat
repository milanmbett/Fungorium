@echo off
setlocal EnableDelayedExpansion

REM filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test15/test.bat

REM Get the directory of this script.
for %%I in ("%~dp0.") do set "SCRIPT_DIR=%%~fI"

REM Load global environment variables from the .env file (assumed one level above test15).
for /f "usebackq tokens=1* delims==" %%a in ("%SCRIPT_DIR%\..\.env") do (
    set "LINE=%%b"
    set "LINE=!LINE:"=!"
    set "%%a=!LINE!"
)

REM Get the absolute path of the jar file.
for %%F in ("%SCRIPT_DIR%\..\%JAR_NAME%") do set "JAR_FILE=%%~fF"

REM Set the input file (relative to this script's directory).
set "INPUT_FILE=%SCRIPT_DIR%\input.txt"

REM Run the Java program with parameter "--test 15" and redirect output to output.log.
java -jar "%JAR_FILE%" --test 15 < "%INPUT_FILE%" > output.log 2>&1

REM Define expected regex patterns.
set "PATTERN1=Player created with default values. Income: 200, Score: 0"
set "PATTERN2=Tecton_Class Constructor called!"
set "PATTERN3=Tecton_Basic Created! ID: Tecton_Basic0"
set "PATTERN4=Tecton_Basic: Tecton_Basic0 added to TectonCollection! TectonCollection size: 1"
set "PATTERN5=INIT  com\.coderunnerlovagjai\.app\.Basic_Spore - Basic_Spore Constructor called!"
set "PATTERN6=GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's ID: Tecton_Basic0"
set "PATTERN7=CREATE com\.coderunnerlovagjai\.app\.Spore_Duplicator - Spore_Duplicator Created! ID: Spore_Duplicator0 on Tecton: Tecton_Basic0"
set "PATTERN8=ADD   com\.coderunnerlovagjai\.app\.Spore_Duplicator - Spore_Duplicator: Spore_Duplicator0 added to SporeCollection! SporeCollection size: 1"
set "PATTERN9=INIT  com\.coderunnerlovagjai\.app\.Insect_Class - Insect_Class Constructor called!"
set "PATTERN10=GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's Insects: \[\]"
set "PATTERN11=CREATE com\.coderunnerlovagjai\.app\.Insect_Buglet - Insect_Buglet Created! ID: Insect_Buglet0 on Tecton: Tecton_Basic0"
set "PATTERN12=ADD   com\.coderunnerlovagjai\.app\.Insect_Buglet - Insect_Buglet: Insect_Buglet0 added to InsectCollection! InsectCollection size: 1"
set "PATTERN13=GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's Spore: .*Spore_Duplicator@"
set "PATTERN14=DIE   com\.coderunnerlovagjai\.app\.Basic_Spore - Spore: Spore_Duplicator0 is dead!"
set "PATTERN15=CREATE com\.coderunnerlovagjai\.app\.Insect_Buglet - Insect_Buglet Created! ID: Insect_Buglet1 on Tecton: Tecton_Basic0"
set "PATTERN16=ADD   com\.coderunnerlovagjai\.app\.Insect_Buglet - Insect_Buglet: Insect_Buglet1 added to InsectCollection! InsectCollection size: 2"
set "PATTERN17=DUPLICATE com\.coderunnerlovagjai\.app\.Spore_Duplicator - Insect: Insect_Buglet0 duplicated by Spore_Duplicator: Spore_Duplicator0"
set "PATTERN18=GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's Insects: \[.*\]"
set "PATTERN19=DUPLICATE com\.coderunnerlovagjai\.app\.Spore_Duplicator - Insect: Insect_Buglet0 on Tecton: Tecton_Basic0"
set "PATTERN20=DUPLICATE com\.coderunnerlovagjai\.app\.Spore_Duplicator - Insect: Insect_Buglet1 on Tecton: Tecton_Basic0"
set "PATTERN21=INCOME com\.coderunnerlovagjai\.app\.Player - Income increased by 100. New income: 300"
set "PATTERN22=SCORE com\.coderunnerlovagjai\.app\.Player - Score increased by 100. New score: 100"
set "PATTERN23=GET   com\.coderunnerlovagjai\.app\._Tests - Listing Insects on tecton"
set "PATTERN24=GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's Insects: \[.*\]"
set "PATTERN25=GET   com\.coderunnerlovagjai\.app\._Tests - Insect ID: Insect_Buglet0"
set "PATTERN26=GET   com\.coderunnerlovagjai\.app\._Tests - Insect ID: Insect_Buglet1"
set "PATTERN27=SUCCESS com\.coderunnerlovagjai\.app\._Tests - Test ran successfully!"

REM Check that all expected patterns exist in the output.
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
call :checkPattern "%PATTERN21%"
call :checkPattern "%PATTERN22%"
call :checkPattern "%PATTERN23%"
call :checkPattern "%PATTERN24%"
call :checkPattern "%PATTERN25%"
call :checkPattern "%PATTERN26%"
call :checkPattern "%PATTERN27%"

echo [test15] Test successful!
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