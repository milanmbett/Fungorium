@echo off
setlocal EnableDelayedExpansion

REM Get the directory of the script
for %%I in ("%~dp0.") do set "SCRIPT_DIR=%%~fI"

REM Load global environment variables from the .env file
REM (Assuming .env is one level above test2, i.e. in %SCRIPT_DIR%\..)
for /f "usebackq tokens=1* delims==" %%a in ("%SCRIPT_DIR%\..\.env") do (
    REM Remove quotes from the variable value if they exist
    set "LINE=%%b"
    set "LINE=!LINE:"=!"
    set "%%a=!LINE!"
)

REM Convert the relative JAR path to an absolute path using SCRIPT_DIR as base
for %%F in ("%SCRIPT_DIR%\..\%JAR_NAME%") do set "JAR_FILE=%%~fF"

REM Set the input file (relative to the script directory)
set "INPUT_FILE=%SCRIPT_DIR%\input.txt"

REM Run the Java program for the 2nd test (using --test 2)
for /f "delims=" %%a in ('java -jar "%JAR_FILE%" --test 2 ^< "%INPUT_FILE%"') do (
    set "OUTPUT=%%a"
)

REM Define the expected regex patterns for the 2nd test
REM Expected to see both the success message and a creation message for Tecton_Base.
set "SUCCESS_REGEX=Test ran successfully!"
set "CREATED_REGEX=Tecton_Base Created!"

REM Check if the output contains the expected patterns
echo %OUTPUT% | findstr "%SUCCESS_REGEX%" > nul
if %errorlevel% equ 0 (
    echo %OUTPUT% | findstr "%CREATED_REGEX%" > nul
    if %errorlevel% equ 0 (
        echo [test2] Test successful!
    ) else (
        echo Test failed! - CREATED_REGEX not found
        echo Output:
        echo %OUTPUT%
    )
) else (
    echo Test failed! - SUCCESS_REGEX not found
    echo Output:
    echo %OUTPUT%
)

endlocal