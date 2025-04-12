#!/bin/bash
# filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test16/test.sh

# Load global environment variables; this loads JAR_NAME from ../../.env
source "$(dirname "$0")/../.env"

# Convert the relative JAR path to an absolute path.
BASE_DIR="$(dirname "$0")/.."
JAR_FILE="$(realpath "$BASE_DIR/$JAR_NAME")"

# Set the input file (relative to this script's directory)
INPUT_FILE="$(dirname "$0")/input.txt"

# Run the Java program with parameter "--test 16" and capture the output.
OUTPUT=$(java -jar "$JAR_FILE" --test 16 < "$INPUT_FILE")

# Define expected regex patterns.
PATTERN1="Player created with default values\. Income: 200, Score: 0"
PATTERN2="Tecton_Class Constructor called!"
PATTERN3="Tecton_Basic Created! ID: Tecton_Basic0"
PATTERN4="Tecton_Basic: Tecton_Basic0 added to TectonCollection! TectonCollection size: 1"
PATTERN5="INIT  com\.coderunnerlovagjai\.app\.Basic_Spore - Basic_Spore Constructor called!"
PATTERN6="GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's ID: Tecton_Basic0"
PATTERN7="CREATE com\.coderunnerlovagjai\.app\.Spore_Paralysing - Spore_Paralysing Created! ID: Spore_Paralysing0 on Tecton: Tecton_Basic0"
PATTERN8="ADD   com\.coderunnerlovagjai\.app\.Spore_Paralysing - Spore_Paralysing: Spore_Paralysing0 added to SporeCollection! SporeCollection size: 1"
PATTERN9="INIT  com\.coderunnerlovagjai\.app\.Insect_Class - Insect_Class Constructor called!"
PATTERN10="GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's Insects: \[\]"
PATTERN11="GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's ID: Tecton_Basic0"
PATTERN12="CREATE com\.coderunnerlovagjai\.app\.Insect_Buglet - Insect_Buglet Created! ID: Insect_Buglet0 on Tecton: Tecton_Basic0"
PATTERN13="ADD   com\.coderunnerlovagjai\.app\.Insect_Buglet - Insect_Buglet: Insect_Buglet0 added to InsectCollection! InsectCollection size: 1"
PATTERN14="CONSUME com\.coderunnerlovagjai\.app\.Spore_Paralysing - Spore_Paralysing: Spore_Paralysing0 is consumed by Insect: Insect_Buglet0"
PATTERN15="PARALYSE com\.coderunnerlovagjai\.app\.Insect_Class - Insect: Insect_Buglet0 is paralysed!"
PATTERN16="GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's Spore: .*Spore_Paralysing@"
PATTERN17="DIE   com\.coderunnerlovagjai\.app\.Basic_Spore - Spore: Spore_Paralysing0 is dead!"
PATTERN18="INCOME com\.coderunnerlovagjai\.app\.Player - Income increased by 100\. New income: 300"
PATTERN19="SCORE com\.coderunnerlovagjai\.app\.Player - Score increased by 100\. New score: 100"
PATTERN20="NULL  com\.coderunnerlovagjai\.app\.Tecton_Class - Spore is null!"
PATTERN21="SUCCESS com\.coderunnerlovagjai\.app\._Tests - Test ran successfully!"

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
   [[ "$OUTPUT" =~ $PATTERN16 ]] && \
   [[ "$OUTPUT" =~ $PATTERN17 ]] && \
   [[ "$OUTPUT" =~ $PATTERN18 ]] && \
   [[ "$OUTPUT" =~ $PATTERN19 ]] && \
   [[ "$OUTPUT" =~ $PATTERN20 ]] && \
   [[ "$OUTPUT" =~ $PATTERN21 ]]; then
    echo "[test16] Test successful!"
else
    echo "Test failed!"
    echo "Output:"
    echo "$OUTPUT"
fi