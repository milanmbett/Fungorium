#!/bin/bash
# filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test12/test.sh

# Load global environment variables (this loads JAR_NAME from ../../.env)
source "$(dirname "$0")/../.env"

# Convert the relative JAR path to an absolute path.
BASE_DIR="$(dirname "$0")/.."
JAR_FILE="$(realpath "$BASE_DIR/$JAR_NAME")"

# Set the input file (relative to this script's directory)
INPUT_FILE="$(dirname "$0")/input.txt"

# Run the Java program with parameter "--test 12" and capture the output.
OUTPUT=$(java -jar "$JAR_FILE" --test 12 < "$INPUT_FILE")

# Define expected regex patterns.
PATTERN1="Player created with default values\. Income: 200, Score: 0"
PATTERN2="Tecton_Class Constructor called!"
PATTERN3="Tecton_Basic Created! ID: Tecton_Basic0"
PATTERN4="Tecton_Basic: Tecton_Basic0 added to TectonCollection! TectonCollection size: 1"
PATTERN5="Mushroom_Class Constructor called!"
PATTERN6="Tecton's Mushroom: .*Mushroom_Shroomlet"
PATTERN7="GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's ID: Tecton_Basic0"
PATTERN8="CREATE com\.coderunnerlovagjai\.app\.Mushroom_Shroomlet - Mushroom_Shroomlet Created! ID: Mushroom_Shroomlet0 on Tecton: Tecton_Basic0"
PATTERN9="ADD   com\.coderunnerlovagjai\.app\.Mushroom_Shroomlet - Mushroom_Shroomlet: Mushroom_Shroomlet0 added to MushroomCollection! MushroomCollection size: 1"
PATTERN10="INIT  com\.coderunnerlovagjai\.app\.Insect_Class - Insect_Class Constructor called!"
PATTERN11="CREATE com\.coderunnerlovagjai\.app\.Insect_Buglet - Insect_Buglet Created! ID: Insect_Buglet0 on Tecton: Tecton_Basic0"
PATTERN12="ADD   com\.coderunnerlovagjai\.app\.Insect_Buglet - Insect_Buglet: Insect_Buglet0 added to InsectCollection! InsectCollection size: 1"
PATTERN13="ATTACK com\.coderunnerlovagjai\.app\.Insect_Class - Insect: Insect_Buglet0 is trying to attack mushroom: Mushroom_Shroomlet0 on tecton: Tecton_Basic0"
PATTERN14="ATTACK com\.coderunnerlovagjai\.app\.Insect_Class - Insect: Insect_Buglet0 is attacking mushroom: Mushroom_Shroomlet0HP: 250 on tecton: Tecton_Basic0"
PATTERN15="ATTACK com\.coderunnerlovagjai\.app\.Mushroom_Class - Mushroom: Mushroom_Shroomlet0 is attacked by an insect\. Damage done: 100 HP left: 150"
PATTERN16="SUCCESS com\.coderunnerlovagjai\.app\._Tests - Test ran successfully!"

# Verify that each expected pattern exists in the output.
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
    echo "[test12] Test successful!"
else
    echo "Test failed!"
    echo "Output:"
    echo "$OUTPUT"
fi