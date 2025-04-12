#!/bin/bash
# filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test13/test.sh

# Load global environment variables (loads JAR_NAME from ../../.env)
source "$(dirname "$0")/../.env"

# Convert the relative JAR path to an absolute path.
BASE_DIR="$(dirname "$0")/.."
JAR_FILE="$(realpath "$BASE_DIR/$JAR_NAME")"

# Set the input file (relative to this script's directory)
INPUT_FILE="$(dirname "$0")/input.txt"

# Run the Java program with parameter "--test 13" and capture the output.
OUTPUT=$(java -jar "$JAR_FILE" --test 13 < "$INPUT_FILE")

# Define expected regex patterns.
PATTERN1="Player created with default values\. Income: 200, Score: 0"
PATTERN2="Tecton_Class Constructor called!"
PATTERN3="Tecton_Basic Created! ID: Tecton_Basic0"
PATTERN4="Tecton_Basic: Tecton_Basic0 added to TectonCollection! TectonCollection size: 1"
PATTERN5="Tecton_Basic Created! ID: Tecton_Basic1"
PATTERN6="Tecton_Basic: Tecton_Basic1 added to TectonCollection! TectonCollection size: 2"
PATTERN7="Mushroom_Class Constructor called!"
PATTERN8="Tecton's Mushroom: .*Mushroom_Shroomlet"
PATTERN9="Mushroom_Shroomlet Created! ID: Mushroom_Shroomlet0 on Tecton: Tecton_Basic0"
PATTERN10="Mushroom_Shroomlet: Mushroom_Shroomlet0 added to MushroomCollection! MushroomCollection size: 1"
PATTERN11="INIT  com\.coderunnerlovagjai\.app\.Insect_Class - Insect_Class Constructor called!"
PATTERN12="Insect_Buglet Created! ID: Insect_Buglet0 on Tecton: Tecton_Basic0"
PATTERN13="Insect_Buglet: Insect_Buglet0 added to InsectCollection! InsectCollection size: 1"
PATTERN14="Insect_Buglet Created! ID: Insect_Buglet1 on Tecton: Tecton_Basic1"
PATTERN15="Insect_Buglet: Insect_Buglet1 added to InsectCollection! InsectCollection size: 2"
PATTERN16="Mushroom: Mushroom_Shroomlet0 is attacking insect: Insect_Buglet0 with power: 25"
PATTERN17="Insect: Insect_Buglet0 HP reduced by: 25\. Current HP: 75"
PATTERN18="Mushroom: Mushroom_Shroomlet0 is attacking insect: Insect_Buglet1 with power: 25"
PATTERN19="Insect: Insect_Buglet1 HP reduced by: 25\. Current HP: 75"
PATTERN20="SUCCESS com\.coderunnerlovagjai\.app\._Tests - Test ran successfully!"

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
   [[ "$OUTPUT" =~ $PATTERN16 ]] && \
   [[ "$OUTPUT" =~ $PATTERN17 ]] && \
   [[ "$OUTPUT" =~ $PATTERN18 ]] && \
   [[ "$OUTPUT" =~ $PATTERN19 ]] && \
   [[ "$OUTPUT" =~ $PATTERN20 ]]; then
    echo "[test13] Test successful!"
else
    echo "Test failed!"
    echo "Output:"
    echo "$OUTPUT"
fi