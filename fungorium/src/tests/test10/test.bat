@echo off
setlocal EnableDelayedExpansion

REM filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test10/test.bat

REM Get the directory of the script.
for %%I in ("%~dp0.") do set "SCRIPT_DIR=%%~fI"

REM Load global environment variables from the .env file (assumed one level above test10).
for /f "usebackq tokens=1* delims==" %%a in ("%SCRIPT_DIR%\..\.env") do (
    set "LINE=%%b"
    set "LINE=!LINE:"=!"
    set "%%a=!LINE!"
)

REM Convert the relative JAR path to an absolute path using SCRIPT_DIR as base.
for %%F in ("%SCRIPT_DIR%\..\%JAR_NAME%") do set "JAR_FILE=%%~fF"

REM Set the input file (relative to the script directory)
set "INPUT_FILE=%SCRIPT_DIR%\input.txt"

REM Run the Java program with parameter "--test 10" and capture output.
for /f "delims=" %%a in ('java -jar "%JAR_FILE%" --test 10 ^< "%INPUT_FILE%"') do (
    set "OUTPUT=%%a"
)

REM Define expected substrings.
set "PATTERN1=Player created with default values. Income: 200, Score: 0"
set "PATTERN2=Tecton_Class Constructor called!"
set "PATTERN3=Tecton_Basic Created! ID: Tecton_Basic0"
set "PATTERN4=Tecton_Basic: Tecton_Basic0 added to TectonCollection! TectonCollection size: 1"
set "PATTERN5=Tecton_Basic Created! ID: Tecton_Basic1"
set "PATTERN6=Tecton_Basic: Tecton_Basic1 added to TectonCollection! TectonCollection size: 2"
set "PATTERN7=GET   com.coderunnerlovagjai.app.Tecton_Class - Tecton's ID: Tecton_Basic0"
set "PATTERN8=Thread Created! ID: Thread0 on Tecton: Tecton_Basic0"
set "PATTERN9=Thread: Thread0 added to ThreadCollection! ThreadCollection size: 1"
set "PATTERN10=GET   com.coderunnerlovagjai.app.Tecton_Class - Tecton's ID: Tecton_Basic1"
set "PATTERN11=Thread Created! ID: Thread1 on Tecton: Tecton_Basic1"
set "PATTERN12=Thread: Thread1 added to ThreadCollection! ThreadCollection size: 2"
set "PATTERN13=INIT  com.coderunnerlovagjai.app.Insect_Class - Insect_Class Constructor called!"
set "PATTERN14=GET   com.coderunnerlovagjai.app.Tecton_Class - Tecton's Insects: []"
set "PATTERN15=CREATE com.coderunnerlovagjai.app.Insect_Buglet - Insect_Buglet Created! ID: Insect_Buglet0 on Tecton: Tecton_Basic0"
set "PATTERN16=ADD   com.coderunnerlovagjai.app.Insect_Buglet - Insect_Buglet: Insect_Buglet0 added to InsectCollection! InsectCollection size: 1"
set "PATTERN17=MOVE  com.coderunnerlovagjai.app.Insect_Class - Insect: Insect_Buglet0 is trying to move to Tecton_Basic1"
set "PATTERN18=MOVE  com.coderunnerlovagjai.app.Insect_Class - Insect: Insect_Buglet0 from Tecton_Basic0 to Tecton_Basic1"
set "PATTERN19=MOVE  com.coderunnerlovagjai.app.Insect_Class - Insect: Insect_Buglet0 moved to Tecton_Basic1. Available steps: 1"
set "PATTERN20=GET   com.coderunnerlovagjai.app._Tests - Insect's Tecton: Tecton_Basic1"
set "PATTERN21=SUCCESS com.coderunnerlovagjai.app._Tests - Test ran successfully!"

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
echo %OUTPUT% | findstr /c:"%PATTERN17%" > nul
if errorlevel 1 (
    echo Test failed! - PATTERN17 not found.
    goto :end
)
echo %OUTPUT% | findstr /c:"%PATTERN18%" > nul
if errorlevel 1 (
    echo Test failed! - PATTERN18 not found.
    goto :end
)
echo %OUTPUT% | findstr /c:"%PATTERN19%" > nul
if errorlevel 1 (
    echo Test failed! - PATTERN19 not found.
    goto :end
)
echo %OUTPUT% | findstr /c:"%PATTERN20%" > nul
if errorlevel 1 (
    echo Test failed! - PATTERN20 not found.
    goto :end
)
echo %OUTPUT% | findstr /c:"%PATTERN21%" > nul
if errorlevel 1 (
    echo Test failed! - PATTERN21 not found.
    goto :end
)

echo [test10] Test successful!
goto :end

:end
endlocal