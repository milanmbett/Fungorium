#!/bin/bash
# filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test10/test.sh

# Load global environment variables; this loads JAR_NAME from ../../.env
source "$(dirname "$0")/../.env"

# Convert the relative JAR path to an absolute path.
BASE_DIR="$(dirname "$0")/.."
JAR_FILE="$(realpath "$BASE_DIR/$JAR_NAME")"

# Set the input file (relative to this script's directory)
INPUT_FILE="$(dirname "$0")/input.txt"

# Run the Java program with parameter "--test 10" and capture the output.
OUTPUT=$(java -jar "$JAR_FILE" --test 10 < "$INPUT_FILE")

# Define expected patterns.
PATTERN1="Player created with default values. Income: 200, Score: 0"
PATTERN2="Tecton_Class Constructor called!"
PATTERN3="Tecton_Basic Created! ID: Tecton_Basic0"
PATTERN4="Tecton_Basic: Tecton_Basic0 added to TectonCollection! TectonCollection size: 1"
PATTERN5="Tecton_Basic Created! ID: Tecton_Basic1"
PATTERN6="Tecton_Basic: Tecton_Basic1 added to TectonCollection! TectonCollection size: 2"
PATTERN7="GET   com.coderunnerlovagjai.app.Tecton_Class - Tecton's ID: Tecton_Basic0"
PATTERN8="Thread Created! ID: Thread0 on Tecton: Tecton_Basic0"
PATTERN9="Thread: Thread0 added to ThreadCollection! ThreadCollection size: 1"
PATTERN10="GET   com.coderunnerlovagjai.app.Tecton_Class - Tecton's ID: Tecton_Basic1"
PATTERN11="Thread Created! ID: Thread1 on Tecton: Tecton_Basic1"
PATTERN12="Thread: Thread1 added to ThreadCollection! ThreadCollection size: 2"
PATTERN13="INIT  com.coderunnerlovagjai.app.Insect_Class - Insect_Class Constructor called!"
PATTERN14="GET   com.coderunnerlovagjai.app.Tecton_Class - Tecton's Insects: \[\]"
PATTERN15="CREATE com.coderunnerlovagjai.app.Insect_Buglet - Insect_Buglet Created! ID: Insect_Buglet0 on Tecton: Tecton_Basic0"
PATTERN16="ADD   com.coderunnerlovagjai.app.Insect_Buglet - Insect_Buglet: Insect_Buglet0 added to InsectCollection! InsectCollection size: 1"
PATTERN17="MOVE  com.coderunnerlovagjai.app.Insect_Class - Insect: Insect_Buglet0 is trying to move to Tecton_Basic1"
PATTERN18="MOVE  com.coderunnerlovagjai.app.Insect_Class - Insect: Insect_Buglet0 from Tecton_Basic0 to Tecton_Basic1"
PATTERN19="MOVE  com.coderunnerlovagjai.app.Insect_Class - Insect: Insect_Buglet0 moved to Tecton_Basic1. Available steps: 1"
PATTERN20="GET   com.coderunnerlovagjai.app._Tests - Insect's Tecton: Tecton_Basic1"
PATTERN21="SUCCESS com.coderunnerlovagjai.app._Tests - Test ran successfully!"

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
   [[ "$OUTPUT" =~ $PATTERN16 ]] && \
   [[ "$OUTPUT" =~ $PATTERN17 ]] && \
   [[ "$OUTPUT" =~ $PATTERN18 ]] && \
   [[ "$OUTPUT" =~ $PATTERN19 ]] && \
   [[ "$OUTPUT" =~ $PATTERN20 ]] && \
   [[ "$OUTPUT" =~ $PATTERN21 ]]; then
    echo "[test10] Test successful!"
else
    echo "Test failed!"
    echo "Output:"
    echo "$OUTPUT"
fi