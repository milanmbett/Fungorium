#!/bin/bash
# filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test21/test.sh

# Load global environment variables (loads JAR_NAME from ../../.env)
source "$(dirname "$0")/../.env"

# Get the absolute path for the jar file.
BASE_DIR="$(dirname "$0")/.."
JAR_FILE="$(realpath "$BASE_DIR/$JAR_NAME")"

# Set the input file relative to this script.
INPUT_FILE="$(dirname "$0")/input.txt"

# Run the Java program with parameter "--test 21" and capture the output.
OUTPUT=$(java -jar "$JAR_FILE" --test 21 < "$INPUT_FILE")

# Define expected regex patterns.
PATTERN1="Player created with default values\. Income: 200, Score: 0"
PATTERN2="Tecton_Class Constructor called!"
PATTERN3="Tecton_Basic Created! ID: Tecton_Basic0"
PATTERN4="Tecton_Basic: Tecton_Basic0 added to TectonCollection! TectonCollection size: 1"
PATTERN5="Mushroom_Class Constructor called!"
PATTERN6="SET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's Mushroom: .*Mushroom_Shroomlet@"
PATTERN7="GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's ID: Tecton_Basic0"
PATTERN8="CREATE com\.coderunnerlovagjai\.app\.Mushroom_Shroomlet - Mushroom_Shroomlet Created! ID: Mushroom_Shroomlet0 on Tecton: Tecton_Basic0"
PATTERN9="ADD   com\.coderunnerlovagjai\.app\.Mushroom_Shroomlet - Mushroom_Shroomlet: Mushroom_Shroomlet0 added to MushroomCollection! MushroomCollection size: 1"
PATTERN10="INIT  com\.coderunnerlovagjai\.app\.Insect_Class - Insect_Class Constructor called!"
PATTERN11="GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's Insects: \[\]"
PATTERN12="CREATE com\.coderunnerlovagjai\.app\.Insect_Buglet - Insect_Buglet Created! ID: Insect_Buglet0 on Tecton: Tecton_Basic0"
PATTERN13="ADD   com\.coderunnerlovagjai\.app\.Insect_Buglet - Insect_Buglet: Insect_Buglet0 added to InsectCollection! InsectCollection size: 1"
PATTERN14="GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's Insects: \[.*\]" 
PATTERN15="GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's Neighbours: \[\]"
PATTERN16="GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's Insects: \[\]"
PATTERN17="SUCCESS com\.coderunnerlovagjai\.app\._Tests - Test ran successfully!"

# Check that all expected patterns appear in the output.
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
   [[ "$OUTPUT" =~ $PATTERN17 ]]; then
    echo "[test21] Test successful!"
else
    echo "Test failed!"
    echo "Output:"
    echo "$OUTPUT"
fi