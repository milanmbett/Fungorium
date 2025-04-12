@echo off
setlocal EnableDelayedExpansion

REM filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test3/test.bat

REM Get the directory of this script
for %%I in ("%~dp0.") do set "SCRIPT_DIR=%%~fI"

REM Load global environment variables from the .env file (assumed one level above test3)
for /f "usebackq tokens=1* delims==" %%a in ("%SCRIPT_DIR%\..\.env") do (
    REM Remove any quotes from the value
    set "LINE=%%b"
    set "LINE=!LINE:"=!"
    set "%%a=!LINE!"
)

REM Convert the relative JAR path to an absolute path using SCRIPT_DIR as base
for %%F in ("%SCRIPT_DIR%\..\%JAR_NAME%") do set "JAR_FILE=%%~fF"

REM Set the input file path (relative to this script)
set "INPUT_FILE=%SCRIPT_DIR%\input.txt"

REM Run the Java program with parameter "--test 3" and redirect output to output.log
java -jar "%JAR_FILE%" --test 3 < "%INPUT_FILE%" > output.log 2>&1

REM Define expected substrings (patterns)
set "PATTERN1=Testing: Tecton death"
set "PATTERN2=Tecton_Class Constructor called!"
set "PATTERN3=Tecton_Basic Created! ID: Tecton_Basic0"
set "PATTERN4=Thread Created! ID: Thread0 on Tecton: Tecton_Basic0"
set "PATTERN5=Tecton transformed to Dead! New ID: Tecton_Dead0"
set "PATTERN6=Dead tecton correctly rejected mushroom!"
set "PATTERN7=Dead tecton correctly rejected spore!"
set "PATTERN8=Test ran successfully!"

REM Check each pattern in output.log using findstr
call :checkPattern "%PATTERN1%"
call :checkPattern "%PATTERN2%"
call :checkPattern "%PATTERN3%"
call :checkPattern "%PATTERN4%"
call :checkPattern "%PATTERN5%"
call :checkPattern "%PATTERN6%"
call :checkPattern "%PATTERN7%"
call :checkPattern "%PATTERN8%"

echo [test3] Test successful!
exit /b 0

:checkPattern
rem Disable delayed expansion in this block so exclamation marks are preserved.
setlocal DisableDelayedExpansion
findstr /c:"%~1" output.log >nul
if errorlevel 1 (
    endlocal
    exit /b 1
) else (
    endlocal
    exit /b 0
)
goto :eof