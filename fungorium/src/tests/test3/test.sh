#!/bin/bash
# filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test3/test.sh

# Load global environment variables; this loads JAR_NAME as "../../target/fungorium-PROTO 0.1-jar-with-dependencies.jar"
source "$(dirname "$0")/../.env"

# Convert the relative JAR path to an absolute path using SCRIPT_DIR as base.
BASE_DIR="$(dirname "$0")/.."
JAR_FILE="$(realpath "$BASE_DIR/$JAR_NAME")"

# Set the input file (relative to this script's directory)
INPUT_FILE="$(dirname "$0")/input.txt"

# Run the Java program with parameter "--test 3" and capture the output.
OUTPUT=$(java -jar "$JAR_FILE" --test 3 < "$INPUT_FILE")

# Define expected regex patterns from the sample output.
PATTERN1="Testing: Tecton death"
PATTERN2="Tecton_Class Constructor called!"
PATTERN3="Tecton_Basic Created! ID: Tecton_Basic0"
PATTERN4="Thread Created! ID: Thread0 on Tecton: Tecton_Basic0"
PATTERN5="Tecton transformed to Dead! New ID: Tecton_Dead0"
PATTERN6="Dead tecton correctly rejected mushroom!"
PATTERN7="Dead tecton correctly rejected spore!"
PATTERN8="Test ran successfully!"

# Evaluate the output.
if [[ "$OUTPUT" =~ $PATTERN1 ]] && \
   [[ "$OUTPUT" =~ $PATTERN2 ]] && \
   [[ "$OUTPUT" =~ $PATTERN3 ]] && \
   [[ "$OUTPUT" =~ $PATTERN4 ]] && \
   [[ "$OUTPUT" =~ $PATTERN5 ]] && \
   [[ "$OUTPUT" =~ $PATTERN6 ]] && \
   [[ "$OUTPUT" =~ $PATTERN7 ]] && \
   [[ "$OUTPUT" =~ $PATTERN8 ]]; then
    echo "[test3] Test successful!"
else
    echo "Test failed!"
    echo "Output:"
    echo "$OUTPUT"
fi