@echo off
setlocal EnableDelayedExpansion

REM Get the directory of the script
for %%I in ("%~dp0.") do set "SCRIPT_DIR=%%~fI"

REM Load global environment variables from the .env file
REM (Assuming .env is one level above test1, i.e. in %SCRIPT_DIR%\..)
for /f "usebackq tokens=1* delims==" %%a in ("%SCRIPT_DIR%\..\.env") do (
    REM Remove quotes from the variable value if they exist
    set "LINE=%%b"
    set "LINE=!LINE:"=!"
    set "%%a=!LINE!"
)


REM Convert the relative JAR path to an absolute path.
REM This uses the SCRIPT_DIR as the base directory.
for %%F in ("%SCRIPT_DIR%\..\%JAR_NAME%") do set "JAR_FILE=%%~fF"

REM Set the input file (relative to the script directory)
set "INPUT_FILE=%SCRIPT_DIR%\input.txt"

REM Run the Java program with input from input.txt
for /f "delims=" %%a in ('java -jar "%JAR_FILE%" --test 1 ^< "%INPUT_FILE%"') do (
    set "OUTPUT=%%a"
)

REM Define the regex patterns to search for
set "SUCCESS_REGEX=Test ran successfully!"
set "NULL_REGEX=Mushroom is null!.*Spore is null!.*Thread is null!"
set "EMPTY_REGEX=Tecton's Insects: \[\]"

REM Check if the output matches all regex patterns
echo %OUTPUT% | findstr "%SUCCESS_REGEX%" > nul
if %errorlevel% equ 0 (
    echo %OUTPUT% | findstr "%NULL_REGEX%" > nul
    if %errorlevel% equ 0 (
        echo %OUTPUT% | findstr "%EMPTY_REGEX%" > nul
        if %errorlevel% equ 0 (
            echo [test1] Test successful!
        ) else (
            echo Test failed! - EMPTY_REGEX not found
            echo Output:
            echo %OUTPUT%
        )
    ) else (
        echo Test failed! - NULL_REGEX not found
        echo Output:
        echo %OUTPUT%
    )
) else (
    echo Test failed! - SUCCESS_REGEX not found
    echo Output:
    echo %OUTPUT%
)

endlocal