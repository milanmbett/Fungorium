#!/bin/bash
# filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test7/test.sh

# Load global environment variables; this loads JAR_NAME from ../../.env
source "$(dirname "$0")/../.env"

# Convert the relative JAR path to an absolute path.
BASE_DIR="$(dirname "$0")/.."
JAR_FILE="$(realpath "$BASE_DIR/$JAR_NAME")"

# Set the input file (relative to this script's directory)
INPUT_FILE="$(dirname "$0")/input.txt"

# Run the Java program with parameter "--test 7" and capture the output.
OUTPUT=$(java -jar "$JAR_FILE" --test 7 < "$INPUT_FILE")

# Define expected patterns.
PATTERN1="Tecton_Class Constructor called!"
PATTERN2="Tecton_Basic Created! ID: Tecton_Basic0"
PATTERN3="Tecton_Basic: Tecton_Basic0 added to TectonCollection! TectonCollection size: 1"
PATTERN4="Tecton's ID: Tecton_Basic0"
PATTERN5="Thread Created! ID: Thread0 on Tecton: Tecton_Basic0"
PATTERN6="Thread: Thread0 added to ThreadCollection! ThreadCollection size: 1"
PATTERN7="Tecton's Thread:"
PATTERN8="Tecton's Thread: .*Thread0"
PATTERN9="Test ran successfully!"

# Check that all expected patterns are present.
if [[ "$OUTPUT" =~ $PATTERN1 ]] && \
   [[ "$OUTPUT" =~ $PATTERN2 ]] && \
   [[ "$OUTPUT" =~ $PATTERN3 ]] && \
   [[ "$OUTPUT" =~ $PATTERN4 ]] && \
   [[ "$OUTPUT" =~ $PATTERN5 ]] && \
   [[ "$OUTPUT" =~ $PATTERN6 ]] && \
   [[ "$OUTPUT" =~ $PATTERN7 ]] && \
   [[ "$OUTPUT" =~ $PATTERN9 ]]; then
    echo "[test7] Test successful!"
else
    echo "Test failed!"
    echo "Output:"
    echo "$OUTPUT"
fi