@echo off
setlocal EnableDelayedExpansion

REM filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test4/test.bat

REM Get the directory of the script.
for %%I in ("%~dp0.") do set "SCRIPT_DIR=%%~fI"

REM Load global environment variables from the .env file
REM (Assuming .env is one level above test4)
for /f "usebackq tokens=1* delims==" %%a in ("%SCRIPT_DIR%\..\.env") do (
    REM Remove any quotes in the variable value.
    set "LINE=%%b"
    set "LINE=!LINE:"=!"
    set "%%a=!LINE!"
)

REM Convert the relative JAR path to an absolute path using SCRIPT_DIR as base.
for %%F in ("%SCRIPT_DIR%\..\%JAR_NAME%") do set "JAR_FILE=%%~fF"

REM Set the input file (relative to this script's directory)
set "INPUT_FILE=%SCRIPT_DIR%\input.txt"

REM Run the Java program with parameter "--test 4" and capture output.
for /f "delims=" %%a in ('java -jar "%JAR_FILE%" --test 4 ^< "%INPUT_FILE%"') do (
    set "OUTPUT=%%a"
)

REM Define expected substrings.
set "PATTERN1=Tecton_Basic Created! ID: Tecton_Basic0"
set "PATTERN2=Tecton_Basic Created! ID: Tecton_Basic1"
set "PATTERN3=Tecton_Basic Created! ID: Tecton_Basic2"
set "PATTERN4=Tecton's Neighbours:"
set "PATTERN5=Tecton0's Neighbours:"
set "PATTERN6=Tecton1's Neighbours:"
set "PATTERN7=Tecton2's Neighbours:"
set "PATTERN8=Test ran successfully!"

REM Check each pattern in the output.
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

echo [test4] Test successful!
goto :end

:end
endlocal