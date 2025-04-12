#!/bin/bash
# filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test24/test.sh

# Load environment variables (assumes .env is one level above test24)
source "$(dirname "$0")/../.env"

# Get the absolute path for the jar.
BASE_DIR="$(dirname "$0")/.."
JAR_FILE="$(realpath "$BASE_DIR/$JAR_NAME")"

# Set the input file (relative to this script)
INPUT_FILE="$(dirname "$0")/input.txt"

# Run the Java program with parameter "--test 24" and capture its output.
OUTPUT=$(java -jar "$JAR_FILE" --test 24 < "$INPUT_FILE")

# Define essential regex patterns.
PATTERN1="Tecton_Class Constructor called!"
PATTERN2="Tecton_Basic Created! ID: Tecton_Basic0"
PATTERN3="Mushroom_Class Constructor called!"
PATTERN4="There is already a mushroom on this tecton!"
PATTERN5="SUCCESS com\.coderunnerlovagjai\.app\._Tests - Test ran successfully!"

# Verify that all essential patterns are present.
if [[ "$OUTPUT" =~ $PATTERN1 ]] && \
   [[ "$OUTPUT" =~ $PATTERN2 ]] && \
   [[ "$OUTPUT" =~ $PATTERN3 ]] && \
   [[ "$OUTPUT" =~ $PATTERN4 ]] && \
   [[ "$OUTPUT" =~ $PATTERN5 ]]; then
    echo "[test24] Test successful!"
else
    echo "[test24] Test failed!"
    echo "Output:"
    echo "$OUTPUT"
fi