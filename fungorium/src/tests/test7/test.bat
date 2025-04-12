@echo off
setlocal EnableDelayedExpansion

REM filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test7/test.bat

REM Get the directory of this script.
for %%I in ("%~dp0.") do set "SCRIPT_DIR=%%~fI"

REM Load global environment variables from the .env file (assumed one level above test7)
for /f "usebackq tokens=1* delims==" %%a in ("%SCRIPT_DIR%\..\.env") do (
    set "LINE=%%b"
    set "LINE=!LINE:"=!"
    set "%%a=!LINE!"
)

REM Convert the relative JAR path to an absolute path using SCRIPT_DIR as base.
for %%F in ("%SCRIPT_DIR%\..\%JAR_NAME%") do set "JAR_FILE=%%~fF"

REM Set the input file (relative to this script's directory)
set "INPUT_FILE=%SCRIPT_DIR%\input.txt"

REM Run the Java program with parameter "--test 7" and redirect output to output.log
java -jar "%JAR_FILE%" --test 7 < "%INPUT_FILE%" > output.log 2>&1

REM Define expected patterns.
set "PATTERN1=Tecton_Class Constructor called!"
set "PATTERN2=Tecton_Basic Created! ID: Tecton_Basic0"
set "PATTERN3=Tecton_Basic: Tecton_Basic0 added to TectonCollection! TectonCollection size: 1"
set "PATTERN4=Tecton's ID: Tecton_Basic0"
set "PATTERN5=Thread Created! ID: Thread0 on Tecton: Tecton_Basic0"
set "PATTERN6=Thread: Thread0 added to ThreadCollection! ThreadCollection size: 1"
set "PATTERN7=Tecton's Thread:"
set "PATTERN8=Tecton's Thread: .*Thread0"
set "PATTERN9=Test ran successfully!"

REM Check that all expected patterns (except PATTERN8) are present.
call :checkPattern "%PATTERN1%"
call :checkPattern "%PATTERN2%"
call :checkPattern "%PATTERN3%"
call :checkPattern "%PATTERN4%"
call :checkPattern "%PATTERN5%"
call :checkPattern "%PATTERN6%"
call :checkPattern "%PATTERN7%"
call :checkPattern "%PATTERN9%"

echo [test7] Test successful!
exit /b 0

:checkPattern
REM Use regex search (/r) and disable delayed expansion so that special characters are preserved.
setlocal DisableDelayedExpansion
findstr /r /c:"%~1" output.log >nul
if errorlevel 1 (
    endlocal
    exit /b 1
) else (
    endlocal
)
goto :eof