@echo off
setlocal EnableDelayedExpansion

REM filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test8/test.bat

REM Get the directory of the script.
for %%I in ("%~dp0.") do set "SCRIPT_DIR=%%~fI"

REM Load global environment variables from the .env file (assumed one level above test8).
for /f "usebackq tokens=1* delims==" %%a in ("%SCRIPT_DIR%\..\.env") do (
    set "LINE=%%b"
    set "LINE=!LINE:"=!"
    set "%%a=!LINE!"
)

REM Convert the relative JAR path to an absolute path using SCRIPT_DIR as base.
for %%F in ("%SCRIPT_DIR%\..\%JAR_NAME%") do set "JAR_FILE=%%~fF"

REM Set the input file (relative to the script directory)
set "INPUT_FILE=%SCRIPT_DIR%\input.txt"

REM Run the Java program with parameter "--test 8" and capture output.
for /f "delims=" %%a in ('java -jar "%JAR_FILE%" --test 8 ^< "%INPUT_FILE%"') do (
    set "OUTPUT=%%a"
)

REM Define expected substrings.
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

REM Check each expected pattern in the output.
echo %OUTPUT% | findstr /c:"%PATTERN1%" > nul
if errorlevel 1 (
    echo Test failed! - PATTERN1 not found.
    goto :end
)
echo %OUTPUT% | findstr /c:"%PATTERN2%" > nul
if errorlevel 1 (
    echo Test failed! - PATTERN2 not found.
    goto :end
)
echo %OUTPUT% | findstr /c:"%PATTERN3%" > nul
if errorlevel 1 (
    echo Test failed! - PATTERN3 not found.
    goto :end
)
echo %OUTPUT% | findstr /c:"%PATTERN4%" > nul
if errorlevel 1 (
    echo Test failed! - PATTERN4 not found.
    goto :end
)
echo %OUTPUT% | findstr /c:"%PATTERN5%" > nul
if errorlevel 1 (
    echo Test failed! - PATTERN5 not found.
    goto :end
)
echo %OUTPUT% | findstr /c:"%PATTERN6%" > nul
if errorlevel 1 (
    echo Test failed! - PATTERN6 not found.
    goto :end
)
echo %OUTPUT% | findstr /c:"%PATTERN7%" > nul
if errorlevel 1 (
    echo Test failed! - PATTERN7 not found.
    goto :end
)
echo %OUTPUT% | findstr /c:"%PATTERN8%" > nul
if errorlevel 1 (
    echo Test failed! - PATTERN8 not found.
    goto :end
)
echo %OUTPUT% | findstr /c:"%PATTERN9%" > nul
if errorlevel 1 (
    echo Test failed! - PATTERN9 not found.
    goto :end
)
echo %OUTPUT% | findstr /c:"%PATTERN10%" > nul
if errorlevel 1 (
    echo Test failed! - PATTERN10 not found.
    goto :end
)
echo %OUTPUT% | findstr /c:"%PATTERN11%" > nul
if errorlevel 1 (
    echo Test failed! - PATTERN11 not found.
    goto :end
)
echo %OUTPUT% | findstr /c:"%PATTERN12%" > nul
if errorlevel 1 (
    echo Test failed! - PATTERN12 not found.
    goto :end
)
echo %OUTPUT% | findstr /c:"%PATTERN13%" > nul
if errorlevel 1 (
    echo Test failed! - PATTERN13 not found.
    goto :end
)
echo %OUTPUT% | findstr /c:"%PATTERN14%" > nul
if errorlevel 1 (
    echo Test failed! - PATTERN14 not found.
    goto :end
)
echo %OUTPUT% | findstr /c:"%PATTERN15%" > nul
if errorlevel 1 (
    echo Test failed! - PATTERN15 not found.
    goto :end
)
echo %OUTPUT% | findstr /c:"%PATTERN16%" > nul
if errorlevel 1 (
    echo Test failed! - PATTERN16 not found.
    goto :end
)

echo [test8] Test successful!
goto :end

:end
endlocal