@echo off
setlocal EnableDelayedExpansion

REM filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test6/test.bat

REM Get the directory of this script.
for %%I in ("%~dp0.") do set "SCRIPT_DIR=%%~fI"

REM Load global environment variables from the .env file (assumed one level above test6)
for /f "usebackq tokens=1* delims==" %%a in ("%SCRIPT_DIR%\..\.env") do (
    set "LINE=%%b"
    set "LINE=!LINE:"=!"
    set "%%a=!LINE!"
)

REM Convert the relative JAR path to an absolute path using SCRIPT_DIR as base.
for %%F in ("%SCRIPT_DIR%\..\%JAR_NAME%") do set "JAR_FILE=%%~fF"

REM Set the input file (relative to this script's directory)
set "INPUT_FILE=%SCRIPT_DIR%\input.txt"

REM Run the Java program with parameter "--test 6" and redirect output to output.log
java -jar "%JAR_FILE%" --test 6 < "%INPUT_FILE%" > output.log 2>&1

REM Define expected substrings (patterns)
set "PATTERN1=Player created with default values. Income: 200, Score: 0"
set "PATTERN2=Tecton_Class Constructor called!"
set "PATTERN3=Mushroom_Class Constructor called!"
set "PATTERN4=Tecton's Mushroom:"
set "PATTERN5=Tecton ID is null!"
set "PATTERN6=Mushroom_Grand Created! ID: Mushroom_Grand0 on Tecton: null"
set "PATTERN7=Tecton_Base Created! ID: Tecton_Base0"
set "PATTERN8=Insect_Class Constructor called!"
set "PATTERN9=Tecton's Insects: []"
set "PATTERN10=Insect_Tektonizator Created! ID: Insect_Tektonizator0 on Tecton: Tecton_Base0"
set "PATTERN11=WARN.*cannot be cracked"
set "PATTERN12=Test ran successfully!"

REM Check that all expected patterns are present in output.log
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

echo [test6] Test successful!
exit /b 0

:checkPattern
REM Disable delayed expansion in this block so exclamation marks are preserved.
setlocal DisableDelayedExpansion
findstr /c:"%~1" output.log >nul
if errorlevel 1 (
    endlocal    
    exit /b 1
) else (
    endlocal
)
goto :eof