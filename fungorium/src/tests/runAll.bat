@echo off
setlocal EnableDelayedExpansion

echo Running all tests...
echo ----------------------------------

set TOTAL=0
set SUCCESS=0
set FAIL=0

:: Loop over directories starting with "test"
for /d %%D in (test*) do (
    if exist "%%D\test.bat" (
        echo Running %%D\test.bat...
        pushd "%%D"
        call test.bat
        set "EXIT_CODE=%ERRORLEVEL%"
        popd
        set /a TOTAL+=1
        if !EXIT_CODE! EQU 0 (
            set /a SUCCESS+=1
            echo Finished %%D with exit code !EXIT_CODE! [OK]
        ) else (
            set /a FAIL+=1
            echo Finished %%D with exit code !EXIT_CODE!
        )
        echo ----------------------------------
    ) else (
        echo Skipping %%D - no executable test.bat found.
    )
)

echo.
echo Summary: Total tests run: !SUCCESS!+!FAIL! = !TOTAL! ^| Successful tests: !SUCCESS! ^| Failed tests: !FAIL!

endlocal