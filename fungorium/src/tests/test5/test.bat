@echo off
setlocal EnableDelayedExpansion

REM filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test5/test.bat

REM Get the directory of the script
for %%I in ("%~dp0.") do set "SCRIPT_DIR=%%~fI"

REM Load global environment variables from the .env file
for /f "usebackq tokens=1* delims==" %%a in ("%SCRIPT_DIR%\..\.env") do (
    set "LINE=%%b"
    set "LINE=!LINE:"=!"
    set "%%a=!LINE!"
)

REM Convert the relative JAR path to an absolute path using SCRIPT_DIR as base.
for %%F in ("%SCRIPT_DIR%\..\%JAR_NAME%") do set "JAR_FILE=%%~fF"

REM Set the input file (relative to the script directory)
set "INPUT_FILE=%SCRIPT_DIR%\input.txt"

REM Run the Java program with parameter "--test 5" and capture output.
for /f "delims=" %%a in ('java -jar "%JAR_FILE%" --test 5 ^< "%INPUT_FILE%"') do (
    set "OUTPUT=%%a"
)

REM Define expected substrings.
set "PATTERN1=Testing: Tecton cracking"
set "PATTERN2=Player created with default values. Income: 200, Score: 0"
set "PATTERN3=Tecton_Basic Created! ID: Tecton_Basic0"
set "PATTERN4=Tecton_Basic Created! ID: Tecton_Basic1"
set "PATTERN5=Tecton_Basic Created! ID: Tecton_Basic2"
set "PATTERN6=Insect_Tektonizator Created! ID: Insect_Tektonizator0 on Tecton: Tecton_Basic0"
set "PATTERN7=CRACK"
set "PATTERN8=Tecton cracked successfully.  New tectons: Tecton_Basic3 and Tecton_Basic4"
set "PATTERN9=Test ran successfully!"

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
REM For PATTERN7, we only check if the word "CRACK" appears.
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

echo [test5] Test successful!
goto :end

:end
endlocal