@echo off
setlocal EnableDelayedExpansion

REM filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test6/test.bat

REM Get the directory of the script.
for %%I in ("%~dp0.") do set "SCRIPT_DIR=%%~fI"

REM Load global environment variables from the .env file (assumed one level above test6).
for /f "usebackq tokens=1* delims==" %%a in ("%SCRIPT_DIR%\..\.env") do (
    set "LINE=%%b"
    set "LINE=!LINE:"=!"
    set "%%a=!LINE!"
)

REM Convert the relative JAR path to an absolute path using SCRIPT_DIR as base.
for %%F in ("%SCRIPT_DIR%\..\%JAR_NAME%") do set "JAR_FILE=%%~fF"

REM Set the input file (relative to the script directory)
set "INPUT_FILE=%SCRIPT_DIR%\input.txt"

REM Run the Java program with parameter "--test 6" and capture output.
for /f "delims=" %%a in ('java -jar "%JAR_FILE%" --test 6 ^< "%INPUT_FILE%"') do (
    set "OUTPUT=%%a"
)


REM Define expected substrings.
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
set "PATTERN11=cannot be cracked"
set "PATTERN12=Test ran successfully!"

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

echo [test6] Test successful!
goto :end

:end
endlocal