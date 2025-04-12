@echo off
setlocal EnableDelayedExpansion

REM filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test8/test.bat

REM Get the directory of this script.
for %%I in ("%~dp0.") do set "SCRIPT_DIR=%%~fI"

REM Load global environment variables from the .env file (assumed one level above test8).
for /f "usebackq tokens=1* delims==" %%a in ("%SCRIPT_DIR%\..\.env") do (
    set "LINE=%%b"
    set "LINE=!LINE:"=!"
    set "%%a=!LINE!"
)

REM Convert the relative JAR path to an absolute path using SCRIPT_DIR as base.
for %%F in ("%SCRIPT_DIR%\..\%JAR_NAME%") do set "JAR_FILE=%%~fF"

REM Set the input file (relative to this script's directory).
set "INPUT_FILE=%SCRIPT_DIR%\input.txt"

REM Run the Java program with parameter "--test 8" and redirect output to output.log.
java -jar "%JAR_FILE%" --test 8 < "%INPUT_FILE%" > output.log 2>&1

REM Define expected patterns.
set "PATTERN1=Tecton_Class Constructor called!"
set "PATTERN2=Tecton_Basic Created! ID: Tecton_Basic0"
set "PATTERN3=Tecton_Basic: Tecton_Basic0 added to TectonCollection! TectonCollection size: 1"
set "PATTERN4=Tecton_Basic Created! ID: Tecton_Basic1"
set "PATTERN5=Tecton's ID: Tecton_Basic0"
set "PATTERN6=Thread Created! ID: Thread0 on Tecton: Tecton_Basic0"
set "PATTERN7=Thread: Thread0 added to ThreadCollection! ThreadCollection size: 1"
set "PATTERN8=EXPAND com.coderunnerlovagjai.app.Thread_Class - Thread: Thread0 is trying to expand!"
set "PATTERN9=Tecton's Neighbours:"
set "PATTERN10=NULL  com.coderunnerlovagjai.app.Tecton_Class - Thread is null!"
set "PATTERN11=EXPAND com.coderunnerlovagjai.app.Thread_Class - Thread: Thread0 is expanding to tecton: Tecton_Basic1"
set "PATTERN12=Thread Created! ID: Thread1 on Tecton: Tecton_Basic1"
set "PATTERN13=Thread: Thread1 added to ThreadCollection! ThreadCollection size: 2"
set "PATTERN14=EXPAND com.coderunnerlovagjai.app.Thread_Class - Thread: Thread0 expanded to tecton: Tecton_Basic1"
set "PATTERN15=Tecton2's Thread: Thread1"
set "PATTERN16=Test ran successfully!"

REM Check each expected pattern in output.log.
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

echo [test8] Test successful!
goto :end

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

:end
endlocal