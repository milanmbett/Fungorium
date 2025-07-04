#!/bin/bash
# filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test19/test.sh

# Load global environment variables (loads JAR_NAME from ../../.env)
source "$(dirname "$0")/../.env"

# Get the absolute path for the jar file.
BASE_DIR="$(dirname "$0")/.."
JAR_FILE="$(realpath "$BASE_DIR/$JAR_NAME")"

# Set the input file (relative to this script's directory)
INPUT_FILE="$(dirname "$0")/input.txt"

# Run the Java program with parameter "--test 19" and capture its output.
OUTPUT=$(java -jar "$JAR_FILE" --test 19 < "$INPUT_FILE")

# Define expected regex patterns.
PATTERN1="Player created with default values\. Income: 200, Score: 0"
PATTERN2="Tecton_Class Constructor called!"
PATTERN3="Tecton_Basic Created! ID: Tecton_Basic0"
PATTERN4="Tecton_Basic: Tecton_Basic0 added to TectonCollection! TectonCollection size: 1"
PATTERN5="GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's ID: Tecton_Basic0"
PATTERN6="CREATE com\.coderunnerlovagjai\.app\.Basic_Spore - Basic_Spore Created! ID: Spore_Basic0 on Tecton: Tecton_Basic0"
PATTERN7="ADD   com\.coderunnerlovagjai\.app\.Basic_Spore - Basic_Spore: Spore_Basic0 added to SporeCollection! SporeCollection size: 1"
PATTERN8="INIT  com\.coderunnerlovagjai\.app\.Insect_Class - Insect_Class Constructor called!"
PATTERN9="GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's Insects: \[\]"
PATTERN10="CREATE com\.coderunnerlovagjai\.app\.Insect_Buglet - Insect_Buglet Created! ID: Insect_Buglet0 on Tecton: Tecton_Basic0"
PATTERN11="ADD   com\.coderunnerlovagjai\.app\.Insect_Buglet - Insect_Buglet: Insect_Buglet0 added to InsectCollection! InsectCollection size: 1"
PATTERN12="INIT  com\.coderunnerlovagjai\.app\.Insect_Class - Insect_Class Constructor called!"
PATTERN13="GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's Insects: \[.*\]"
PATTERN14="GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's ID: Tecton_Basic0"
PATTERN15="CREATE com\.coderunnerlovagjai\.app\.Insect_Buglet - Insect_Buglet Created! ID: Insect_Buglet1 on Tecton: Tecton_Basic0"
PATTERN16="ADD   com\.coderunnerlovagjai\.app\.Insect_Buglet - Insect_Buglet: Insect_Buglet1 added to InsectCollection! InsectCollection size: 2"
PATTERN17="GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's Spore: .*Basic_Spore@"
PATTERN18="DIE   com\.coderunnerlovagjai\.app\.Basic_Spore - Spore: Spore_Basic0 is dead!"
PATTERN19="INCOME com\.coderunnerlovagjai\.app\.Player - Income increased by 100\. New income: 300"
PATTERN20="SCORE com\.coderunnerlovagjai\.app\.Player - Score increased by 100\. New score: 100"
PATTERN21="NULL  com\.coderunnerlovagjai\.app\.Tecton_Class - Spore is null!"
PATTERN22="NULL  com\.coderunnerlovagjai\.app\.Insect_Class - Spore is null!"
PATTERN23="SUCCESS com\.coderunnerlovagjai\.app\._Tests - Test ran successfully!"

# Verify that all expected patterns are present in the output.
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
   [[ "$OUTPUT" =~ $PATTERN21 ]] && \
   [[ "$OUTPUT" =~ $PATTERN22 ]] && \
   [[ "$OUTPUT" =~ $PATTERN23 ]]; then
    echo "[test19] Test successful!"
else
    echo "Test failed!"
    echo "Output:"
    echo "$OUTPUT"
fi