@echo off
setlocal EnableDelayedExpansion

REM filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test9/test.bat

REM Get the directory of the script.
for %%I in ("%~dp0.") do set "SCRIPT_DIR=%%~fI"

REM Load global environment variables from the .env file (assumed one level above test9).
for /f "usebackq tokens=1* delims==" %%a in ("%SCRIPT_DIR%\..\.env") do (
    set "LINE=%%b"
    set "LINE=!LINE:"=!"
    set "%%a=!LINE!"
)

REM Convert the relative JAR path to an absolute path using SCRIPT_DIR as base.
for %%F in ("%SCRIPT_DIR%\..\%JAR_NAME%") do set "JAR_FILE=%%~fF"

REM Set the input file (relative to the script directory)
set "INPUT_FILE=%SCRIPT_DIR%\input.txt"

REM Run the Java program with parameter "--test 9" and capture output.
for /f "delims=" %%a in ('java -jar "%JAR_FILE%" --test 9 ^< "%INPUT_FILE%"') do (
    set "OUTPUT=%%a"
)

REM Define expected substrings.
set "PATTERN1=Player created with default values. Income: 200, Score: 0"
set "PATTERN2=Tecton_Class Constructor called!"
set "PATTERN3=Tecton_Basic Created! ID: Tecton_Basic0"
set "PATTERN4=Tecton_Basic: Tecton_Basic0 added to TectonCollection! TectonCollection size: 1"
set "PATTERN5=Insect_Class Constructor called!"
set "PATTERN6=Tecton's Insects: []"
set "PATTERN7=Tecton's ID: Tecton_Basic0"
set "PATTERN8=Insect_Buglet Created! ID: Insect_Buglet0 on Tecton: Tecton_Basic0"
set "PATTERN9=Insect_Buglet: Insect_Buglet0 added to InsectCollection! InsectCollection size: 1"
set "PATTERN10=Test ran successfully!"

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

echo [test9] Test successful!
goto :end

:end
endlocal