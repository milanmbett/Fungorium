@echo off
setlocal EnableDelayedExpansion

REM filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test4/test.bat

REM Get the directory of this script.
for %%I in ("%~dp0.") do set "SCRIPT_DIR=%%~fI"

REM Load global environment variables from the .env file (assumed one level above test4).
for /f "usebackq tokens=1* delims==" %%a in ("%SCRIPT_DIR%\..\.env") do (
    REM Remove any quotes from the value.
    set "LINE=%%b"
    set "LINE=!LINE:"=!"
    set "%%a=!LINE!"
)

REM Convert the relative JAR path to an absolute path using SCRIPT_DIR as base.
for %%F in ("%SCRIPT_DIR%\..\%JAR_NAME%") do set "JAR_FILE=%%~fF"

REM Set the input file path (relative to this script).
set "INPUT_FILE=%SCRIPT_DIR%\input.txt"

REM Run the Java program with parameter "--test 4" and redirect output to output.log.
java -jar "%JAR_FILE%" --test 4 < "%INPUT_FILE%" > output.log 2>&1

REM Define expected substrings (patterns).
set "PATTERN1=Tecton_Basic Created! ID: Tecton_Basic0"
set "PATTERN2=Tecton_Basic Created! ID: Tecton_Basic1"
set "PATTERN3=Tecton_Basic Created! ID: Tecton_Basic2"
set "PATTERN4=Tecton's Neighbours:"
set "PATTERN5=Tecton0's Neighbours:"
set "PATTERN6=Tecton1's Neighbours:"
set "PATTERN7=Tecton2's Neighbours:"
set "PATTERN8=Test ran successfully!"

REM Check each pattern in output.log using findstr.
call :checkPattern "%PATTERN1%"
call :checkPattern "%PATTERN2%"
call :checkPattern "%PATTERN3%"
call :checkPattern "%PATTERN4%"
call :checkPattern "%PATTERN5%"
call :checkPattern "%PATTERN6%"
call :checkPattern "%PATTERN7%"
call :checkPattern "%PATTERN8%"

echo [test4] Test successful!
exit /b 0

:checkPattern
findstr /c:"%~1" output.log >nul
if errorlevel 1 (
    exit /b 1
)
goto :eof