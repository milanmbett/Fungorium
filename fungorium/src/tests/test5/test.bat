@echo off
setlocal EnableDelayedExpansion

REM filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test5/test.bat

REM Get the directory of this script
for %%I in ("%~dp0.") do set "SCRIPT_DIR=%%~fI"

REM Load global environment variables from the .env file (assumed one level above test5)
for /f "usebackq tokens=1* delims==" %%a in ("%SCRIPT_DIR%\..\.env") do (
    set "LINE=%%b"
    set "LINE=!LINE:"=!"
    set "%%a=!LINE!"
)

REM Convert the relative JAR path to an absolute path using SCRIPT_DIR as base
for %%F in ("%SCRIPT_DIR%\..\%JAR_NAME%") do set "JAR_FILE=%%~fF"

REM Set the input file (relative to the script's directory)
set "INPUT_FILE=%SCRIPT_DIR%\input.txt"

REM Run the Java program with parameter "--test 5" and redirect output to output.log
java -jar "%JAR_FILE%" --test 5 < "%INPUT_FILE%" > output.log 2>&1

REM Define expected substrings (patterns)
set "PATTERN1=Testing: Tecton cracking"
set "PATTERN2=Player created with default values. Income: 200, Score: 0"
set "PATTERN3=Tecton_Basic Created! ID: Tecton_Basic0"
set "PATTERN4=Tecton_Basic Created! ID: Tecton_Basic1"
set "PATTERN5=Tecton_Basic Created! ID: Tecton_Basic2"
set "PATTERN6=Insect_Tektonizator Created! ID: Insect_Tektonizator0 on Tecton: Tecton_Basic0"
set "PATTERN7=CRACK .* is cracking tecton: Tecton_Basic0"
set "PATTERN8=Tecton cracked successfully.  New tectons: Tecton_Basic3 and Tecton_Basic4"
set "PATTERN9=Test ran successfully!"

REM Check that all expected patterns are present in the output (output.log).
call :checkPattern "%PATTERN1%"
call :checkPattern "%PATTERN2%"
call :checkPattern "%PATTERN3%"
call :checkPattern "%PATTERN4%"
call :checkPattern "%PATTERN5%"
call :checkPattern "%PATTERN6%"
call :checkPattern "%PATTERN7%"
call :checkPattern "%PATTERN8%"
call :checkPattern "%PATTERN9%"

echo [test5] Test successful!
exit /b 0

:checkPattern
rem Disable delayed expansion in this block so that exclamation marks are preserved.
setlocal DisableDelayedExpansion
findstr /c:"%~1" output.log >nul
if errorlevel 1 (
    endlocal    
    exit /b 1
) else (
    endlocal
)
goto :eof