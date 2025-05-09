#!/bin/bash
# filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/runAll.sh
cd "$(dirname "$0")"

RED=$(tput setaf 1)
GREEN=$(tput setaf 2)
YELLOW=$(tput setaf 3)
CYAN=$(tput setaf 6)
RESET=$(tput sgr0)

echo "${CYAN}Running all tests...${RESET}"
echo "----------------------------------"

TOTAL=0
SUCCESS=0
FAIL=0

# Loop over sorted folders matching "test*"
for testDir in $(ls -d test* | sort -V); do
    if [ -d "$testDir" ] && [ -x "$testDir/test.sh" ]; then
        OUTPUT=$( (cd "$testDir" && ./test.sh) 2>&1 )
        EXIT_CODE=$?
        ONE_LINED_OUTPUT=$(echo "$OUTPUT" | tr '\n' ' ' | sed 's/[[:space:]]\+/ /g')
        TOTAL=$((TOTAL+1))
        # Check for zero exit and explicit success message
        if [ "$EXIT_CODE" -eq 0 ] && [[ "$OUTPUT" == *"Test ran successfully!"* ]]; then
            SUCCESS=$((SUCCESS+1))
            echo -e "${GREEN}${ONE_LINED_OUTPUT} (code $EXIT_CODE) ✓${RESET}"
        else  # failure or missing success marker
            FAIL=$((FAIL+1))
            echo -e "${RED}${testDir}: ${ONE_LINED_OUTPUT} (code $EXIT_CODE)${RESET}"
        fi
    else
        echo "${CYAN}Skipping $testDir - no executable test.sh found.${RESET}"
    fi
done

echo -e "${CYAN}Summary:${RESET} Total tests run: ${TOTAL}${RESET} | Successful tests: ${GREEN}${SUCCESS}${RESET} | Failed tests: ${RED}${FAIL}${RESET}"