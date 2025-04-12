#!/bin/bash
# filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test15/test.sh

# Load global environment variables (loads JAR_NAME from ../../.env)
source "$(dirname "$0")/../.env"

# Get the absolute path for the jar file.
BASE_DIR="$(dirname "$0")/.."
JAR_FILE="$(realpath "$BASE_DIR/$JAR_NAME")"

# Set the input file (relative to this script's directory)
INPUT_FILE="$(dirname "$0")/input.txt"

# Run the Java program with parameter "--test 15" and capture its output.
OUTPUT=$(java -jar "$JAR_FILE" --test 15 < "$INPUT_FILE")

# Define expected regex patterns.
PATTERN1="Player created with default values\. Income: 200, Score: 0"
PATTERN2="Tecton_Class Constructor called!"
PATTERN3="Tecton_Basic Created! ID: Tecton_Basic0"
PATTERN4="Tecton_Basic: Tecton_Basic0 added to TectonCollection! TectonCollection size: 1"
PATTERN5="INIT  com\.coderunnerlovagjai\.app\.Basic_Spore - Basic_Spore Constructor called!"
PATTERN6="GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's ID: Tecton_Basic0"
PATTERN7="CREATE com\.coderunnerlovagjai\.app\.Spore_Duplicator - Spore_Duplicator Created! ID: Spore_Duplicator0 on Tecton: Tecton_Basic0"
PATTERN8="ADD   com\.coderunnerlovagjai\.app\.Spore_Duplicator - Spore_Duplicator: Spore_Duplicator0 added to SporeCollection! SporeCollection size: 1"
PATTERN9="INIT  com\.coderunnerlovagjai\.app\.Insect_Class - Insect_Class Constructor called!"
PATTERN10="GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's Insects: \[\]"
PATTERN11="CREATE com\.coderunnerlovagjai\.app\.Insect_Buglet - Insect_Buglet Created! ID: Insect_Buglet0 on Tecton: Tecton_Basic0"
PATTERN12="ADD   com\.coderunnerlovagjai\.app\.Insect_Buglet - Insect_Buglet: Insect_Buglet0 added to InsectCollection! InsectCollection size: 1"
PATTERN13="GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's Spore: .*Spore_Duplicator@"
PATTERN14="DIE   com\.coderunnerlovagjai\.app\.Basic_Spore - Spore: Spore_Duplicator0 is dead!"
PATTERN15="CREATE com\.coderunnerlovagjai\.app\.Insect_Buglet - Insect_Buglet Created! ID: Insect_Buglet1 on Tecton: Tecton_Basic0"
PATTERN16="ADD   com\.coderunnerlovagjai\.app\.Insect_Buglet - Insect_Buglet: Insect_Buglet1 added to InsectCollection! InsectCollection size: 2"
PATTERN17="DUPLICATE com\.coderunnerlovagjai\.app\.Spore_Duplicator - Insect: Insect_Buglet0 duplicated by Spore_Duplicator: Spore_Duplicator0"
PATTERN18="GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's Insects: \[.*\]"
PATTERN19="DUPLICATE com\.coderunnerlovagjai\.app\.Spore_Duplicator - Insect: Insect_Buglet0 on Tecton: Tecton_Basic0"
PATTERN20="DUPLICATE com\.coderunnerlovagjai\.app\.Spore_Duplicator - Insect: Insect_Buglet1 on Tecton: Tecton_Basic0"
PATTERN21="INCOME com\.coderunnerlovagjai\.app\.Player - Income increased by 100\. New income: 300"
PATTERN22="SCORE com\.coderunnerlovagjai\.app\.Player - Score increased by 100\. New score: 100"
PATTERN23="GET   com\.coderunnerlovagjai\.app\._Tests - Listing Insects on tecton"
PATTERN24="GET   com\.coderunnerlovagjai\.app\.Tecton_Class - Tecton's Insects: \[.*\]"
PATTERN25="GET   com\.coderunnerlovagjai\.app\._Tests - Insect ID: Insect_Buglet0"
PATTERN26="GET   com\.coderunnerlovagjai\.app\._Tests - Insect ID: Insect_Buglet1"
PATTERN27="SUCCESS com\.coderunnerlovagjai\.app\._Tests - Test ran successfully!"

# Check that all expected patterns exist in the output.
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
   [[ "$OUTPUT" =~ $PATTERN23 ]] && \
   [[ "$OUTPUT" =~ $PATTERN24 ]] && \
   [[ "$OUTPUT" =~ $PATTERN25 ]] && \
   [[ "$OUTPUT" =~ $PATTERN26 ]] && \
   [[ "$OUTPUT" =~ $PATTERN27 ]]; then
    echo "[test15] Test successful!"
else
    echo "Test failed!"
    echo "Output:"
    echo "$OUTPUT"
fi