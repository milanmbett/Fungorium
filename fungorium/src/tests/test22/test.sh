#!/bin/bash
# filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test22/test.sh

# Load environment variables (assumes .env is one level above test22)
source "$(dirname "$0")/../.env"

# Get the absolute path of the jar.
BASE_DIR="$(dirname "$0")/.."
JAR_FILE="$(realpath "$BASE_DIR/$JAR_NAME")"

# Input file relative to this script.
INPUT_FILE="$(dirname "$0")/input.txt"

# Run the jar with --test 22 and capture output.
OUTPUT=$(java -jar "$JAR_FILE" --test 22 < "$INPUT_FILE")

# Define essential regex patterns.
PATTERN1="Player created with default values\. Income: 200, Score: 0"
PATTERN2="Tecton_Basic Created! ID: Tecton_Basic0"
PATTERN3="Thread Created! ID: Thread0 on Tecton: Tecton_Basic0"
PATTERN4="Insect_Buglet Created! ID: Insect_Buglet0 on Tecton: Tecton_Basic0"
PATTERN5="NULL  com\.coderunnerlovagjai\.app\.Tecton_Class - Mushroom is null!"
PATTERN6="Mushroom_Shroomlet Created! ID: Mushroom_Shroomlet1 on Tecton: Tecton_Basic1"
PATTERN7="SUCCESS com\.coderunnerlovagjai\.app\._Tests - Test ran successfully!"

# Check that all patterns are present.
if [[ "$OUTPUT" =~ $PATTERN1 ]] && \
   [[ "$OUTPUT" =~ $PATTERN2 ]] && \
   [[ "$OUTPUT" =~ $PATTERN3 ]] && \
   [[ "$OUTPUT" =~ $PATTERN4 ]] && \
   [[ "$OUTPUT" =~ $PATTERN5 ]] && \
   [[ "$OUTPUT" =~ $PATTERN6 ]] && \
   [[ "$OUTPUT" =~ $PATTERN7 ]]; then
    echo "[test22] Test successful!"
else
    echo "[test22] Test failed!"
    echo "Output:"
    echo "$OUTPUT"
fi