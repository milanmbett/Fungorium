#!/bin/bash
# filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test6/test.sh

# Load global environment variables; this loads JAR_NAME from ../../.env
source "$(dirname "$0")/../.env"

# Convert the relative JAR path to an absolute path.
BASE_DIR="$(dirname "$0")/.."
JAR_FILE="$(realpath "$BASE_DIR/$JAR_NAME")"

# Set the input file (relative to this script's directory)
INPUT_FILE="$(dirname "$0")/input.txt"

# Run the Java program with parameter "--test 6" and capture the output.
OUTPUT=$(java -jar "$JAR_FILE" --test 6 < "$INPUT_FILE")

# Define expected regex patterns.
PATTERN1="Player created with default values\. Income: 200, Score: 0"
PATTERN2="Tecton_Class Constructor called!"
PATTERN3="Mushroom_Class Constructor called!"
PATTERN4="Tecton's Mushroom:"
PATTERN5="Tecton ID is null!"
PATTERN6="Mushroom_Grand Created! ID: Mushroom_Grand0 on Tecton: null"
PATTERN7="Tecton_Base Created! ID: Tecton_Base0"
PATTERN8="Insect_Class Constructor called!"
PATTERN9="Tecton's Insects: \[\]"
PATTERN10="Insect_Tektonizator Created! ID: Insect_Tektonizator0 on Tecton: Tecton_Base0"
PATTERN11="WARN.*cannot be cracked"
PATTERN12="Test ran successfully!"

# Check that all the expected patterns are present.
if [[ "$OUTPUT" =~ $PATTERN1 ]] && \
   [[ "$OUTPUT" =~ $PATTERN2 ]] && \
   [[ "$OUTPUT" =~ $PATTERN3 ]] && \
   [[ "$OUTPUT" =~ $PATTERN4 ]] && \
   [[ "$OUTPUT" =~ $PATTERN5 ]] && \
   [[ "$OUTPUT" =~ $PATTERN6 ]] && \
   [[ "$OUTPUT" =~ $PATTERN7 ]] && \
   [[ "$OUTPUT" =~ $PATTERN8 ]] && \
   [[ "$OUTPUT" =~ $PATTERN9 ]] && \
   [[ "$OUTPUT" =~ $PATTERN10 ]] && \
   [[ "$OUTPUT" =~ $PATTERN11 ]] && \
   [[ "$OUTPUT" =~ $PATTERN12 ]]; then
    echo "[test6] Test successful!"
else
    echo "Test failed!"
    echo "Output:"
    echo "$OUTPUT"
fi