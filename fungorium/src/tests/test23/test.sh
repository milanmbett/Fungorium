#!/bin/bash
# filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test23/test.sh

# Load global environment variables (assumes .env is one level above test23)
source "$(dirname "$0")/../.env"

# Get the absolute path of the jar.
BASE_DIR="$(dirname "$0")/.."
JAR_FILE="$(realpath "$BASE_DIR/$JAR_NAME")"

# Input file for this test (you can reuse input.txt if it contains test number 23)
INPUT_FILE="$(dirname "$0")/input.txt"

# Run the jar with "--test 23" and capture output.
OUTPUT=$(java -jar "$JAR_FILE" --test 23 < "$INPUT_FILE")

# Define essential regex patterns.
PATTERN1="Tecton_Class Constructor called!"
PATTERN2="Tecton_Basic Created! ID: Tecton_Basic0"
PATTERN3="Tecton_Dead Created! ID: Tecton_Dead1"
PATTERN4="NULL  com\.coderunnerlovagjai\.app\.Tecton_Class - Thread is null!"
PATTERN5="Cannot create thread on dead tecton!"
PATTERN6="SUCCESS com\.coderunnerlovagjai\.app\._Tests - Test ran successfully!"

# Check that all patterns are present.
if [[ "$OUTPUT" =~ $PATTERN1 ]] && \
   [[ "$OUTPUT" =~ $PATTERN2 ]] && \
   [[ "$OUTPUT" =~ $PATTERN3 ]] && \
   [[ "$OUTPUT" =~ $PATTERN4 ]] && \
   [[ "$OUTPUT" =~ $PATTERN5 ]] && \
   [[ "$OUTPUT" =~ $PATTERN6 ]]; then
    echo "[test23] Test successful!"
else
    echo "[test23] Test failed!"
    echo "Output:"
    echo "$OUTPUT"
fi