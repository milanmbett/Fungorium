#!/bin/bash
# filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test4/test.sh

# Load global environment variables; this loads JAR_NAME as defined in ../../.env
source "$(dirname "$0")/../.env"

# Convert the relative JAR path to an absolute path.
BASE_DIR="$(dirname "$0")/.."
JAR_FILE="$(realpath "$BASE_DIR/$JAR_NAME")"

# Set the input file (relative to this script's directory)
INPUT_FILE="$(dirname "$0")/input.txt"

# Run the Java program with parameter "--test 4" and capture the output.
OUTPUT=$(java -jar "$JAR_FILE" --test 4 < "$INPUT_FILE")

# Define expected regex patterns.
PATTERN1="Tecton_Basic Created! ID: Tecton_Basic0"
PATTERN2="Tecton_Basic Created! ID: Tecton_Basic1"
PATTERN3="Tecton_Basic Created! ID: Tecton_Basic2"
PATTERN4="Tecton's Neighbours:"
PATTERN5="Tecton0's Neighbours:"
PATTERN6="Tecton1's Neighbours:"
PATTERN7="Tecton2's Neighbours:"
PATTERN8="Test ran successfully!"

# Verify that all expected patterns exist in the output.
if [[ "$OUTPUT" =~ $PATTERN1 ]] && \
   [[ "$OUTPUT" =~ $PATTERN2 ]] && \
   [[ "$OUTPUT" =~ $PATTERN3 ]] && \
   [[ "$OUTPUT" =~ $PATTERN4 ]] && \
   [[ "$OUTPUT" =~ $PATTERN5 ]] && \
   [[ "$OUTPUT" =~ $PATTERN6 ]] && \
   [[ "$OUTPUT" =~ $PATTERN7 ]] && \
   [[ "$OUTPUT" =~ $PATTERN8 ]]; then
    echo "[test4] Test successful!"
else
    echo "Test failed!"
    echo "Output:"
    echo "$OUTPUT"
fi