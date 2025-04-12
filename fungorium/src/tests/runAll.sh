#!/bin/bash
# Navigate to the tests directory (adjust if needed)
cd "$(dirname "$0")"

# Optional: Set up some colors for nicer output
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

# Loop over folders matching "test*"
for testDir in test*; do
    if [ -d "$testDir" ] && [ -x "$testDir/test.sh" ]; then
        # Capture output (both stdout and stderr) and collapse it to one line
        OUTPUT=$( (cd "$testDir" && ./test.sh) 2>&1 )
        EXIT_CODE=$?
        # Collapse multi-line output into one line
        ONE_LINED_OUTPUT=$(echo "$OUTPUT" | tr '\n' ' ' | sed 's/[[:space:]]\+/ /g')
        TOTAL=$((TOTAL+1))
        if [ "$EXIT_CODE" -eq 0 ]; then
            SUCCESS=$((SUCCESS+1))
            echo -e "${GREEN}${ONE_LINED_OUTPUT} (code $EXIT_CODE) ✓${RESET}"
        else
            FAIL=$((FAIL+1))
            echo -e "${RED}${testDir}: ${ONE_LINED_OUTPUT} (code $EXIT_CODE)${RESET}"
        fi
    else
        echo "${CYAN}Skipping $testDir - no executable test.sh found.${RESET}"
    fi
done

echo -e "${CYAN}Summary:${RESET} Total tests run: ${TOTAL}${RESET} | Successful tests: ${GREEN}${SUCCESS}${RESET} | Failed tests: ${RED}${FAIL}${RESET}"