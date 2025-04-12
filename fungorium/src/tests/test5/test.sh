#!/bin/bash
# filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test5/test.sh

# Load global environment variables; this loads JAR_NAME from ../../.env
source "$(dirname "$0")/../.env"

# Convert the relative JAR path to an absolute path.
BASE_DIR="$(dirname "$0")/.."
JAR_FILE="$(realpath "$BASE_DIR/$JAR_NAME")"

# Set the input file (relative to this script's directory)
INPUT_FILE="$(dirname "$0")/input.txt"

# Run the Java program with parameter "--test 5" and capture the output.
OUTPUT=$(java -jar "$JAR_FILE" --test 5 < "$INPUT_FILE")

# Define expected regex patterns (adjust as needed).
PATTERN1="Testing: Tecton cracking"
PATTERN2="Player created with default values\. Income: 200, Score: 0"
PATTERN3="Tecton_Basic Created! ID: Tecton_Basic0"
PATTERN4="Tecton_Basic Created! ID: Tecton_Basic1"
PATTERN5="Tecton_Basic Created! ID: Tecton_Basic2"
PATTERN6="Insect_Tektonizator Created! ID: Insect_Tektonizator0 on Tecton: Tecton_Basic0"
PATTERN7="CRACK .* is cracking tecton: Tecton_Basic0"
PATTERN8="Tecton cracked successfully\.  New tectons: Tecton_Basic3 and Tecton_Basic4"
PATTERN9="Test ran successfully!"

# Check that all expected patterns are present in the output.
if [[ "$OUTPUT" =~ $PATTERN1 ]] && \
   [[ "$OUTPUT" =~ $PATTERN2 ]] && \
   [[ "$OUTPUT" =~ $PATTERN3 ]] && \
   [[ "$OUTPUT" =~ $PATTERN4 ]] && \
   [[ "$OUTPUT" =~ $PATTERN5 ]] && \
   [[ "$OUTPUT" =~ $PATTERN6 ]] && \
   [[ "$OUTPUT" =~ $PATTERN7 ]] && \
   [[ "$OUTPUT" =~ $PATTERN8 ]] && \
   [[ "$OUTPUT" =~ $PATTERN9 ]]; then
    echo "[test5] Test successful!"
else
    echo "Test failed!"
    echo "Output:"
    echo "$OUTPUT"
fi