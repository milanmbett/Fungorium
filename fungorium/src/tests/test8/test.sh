#!/bin/bash
# filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test8/test.sh

# Load global environment variables; this loads JAR_NAME from ../../.env
source "$(dirname "$0")/../.env"

# Convert the relative JAR path to an absolute path.
BASE_DIR="$(dirname "$0")/.."
JAR_FILE="$(realpath "$BASE_DIR/$JAR_NAME")"

# Set the input file (relative to this script's directory)
INPUT_FILE="$(dirname "$0")/input.txt"

# Run the Java program with parameter "--test 8" and capture the output.
OUTPUT=$(java -jar "$JAR_FILE" --test 8 < "$INPUT_FILE")

# Define expected patterns (adjust if necessary).
PATTERN1="Tecton_Class Constructor called!"
PATTERN2="Tecton_Basic Created! ID: Tecton_Basic0"
PATTERN3="Tecton_Basic: Tecton_Basic0 added to TectonCollection! TectonCollection size: 1"
PATTERN4="Tecton_Basic Created! ID: Tecton_Basic1"
PATTERN5="Tecton's ID: Tecton_Basic0"
PATTERN6="Thread Created! ID: Thread0 on Tecton: Tecton_Basic0"
PATTERN7="Thread: Thread0 added to ThreadCollection! ThreadCollection size: 1"
PATTERN8="EXPAND com.coderunnerlovagjai.app.Thread_Class - Thread: Thread0 is trying to expand!"
PATTERN9="Tecton's Neighbours:"
PATTERN10="NULL  com.coderunnerlovagjai.app.Tecton_Class - Thread is null!"
PATTERN11="EXPAND com.coderunnerlovagjai.app.Thread_Class - Thread: Thread0 is expanding to tecton: Tecton_Basic1"
PATTERN12="Thread Created! ID: Thread1 on Tecton: Tecton_Basic1"
PATTERN13="Thread: Thread1 added to ThreadCollection! ThreadCollection size: 2"
PATTERN14="EXPAND com.coderunnerlovagjai.app.Thread_Class - Thread: Thread0 expanded to tecton: Tecton_Basic1"
PATTERN15="Tecton2's Thread: Thread1"
PATTERN16="Test ran successfully!"

# Verify all expected patterns exist in the output.
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
   [[ "$OUTPUT" =~ $PATTERN12 ]] && \
   [[ "$OUTPUT" =~ $PATTERN13 ]] && \
   [[ "$OUTPUT" =~ $PATTERN14 ]] && \
   [[ "$OUTPUT" =~ $PATTERN15 ]] && \
   [[ "$OUTPUT" =~ $PATTERN16 ]]; then
    echo "[test8] Test successful!"
else
    echo "Test failed!"
    echo "Output:"
    echo "$OUTPUT"
fi