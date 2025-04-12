#!/bin/bash
# filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/tests/test2/test.sh

# Load global environment variables; this loads JAR_NAME as "../../target/fungorium-PROTO 0.1-jar-with-dependencies.jar"
source "$(dirname "$0")/../.env"

# Convert the relative JAR path to an absolute path.
BASE_DIR="$(dirname "$0")/.."
JAR_FILE="$(realpath "$BASE_DIR/$JAR_NAME")"

# Set the input file (relative to this script's directory)
INPUT_FILE="$(dirname "$0")/input.txt"

# Run the Java program with parameter "--test 2" and capture the output.
OUTPUT=$(java -jar "$JAR_FILE" --test 2 < "$INPUT_FILE")

# Define expected regex patterns from the sample output.
# (Adjust these patterns if necessary to match your jar's actual output.)
PATTERN1="Testing: creating Tecton_Base"
PATTERN2="Player created with default values\. Income: 200, Score: 0"
PATTERN3="Tecton_Class Constructor called!"
PATTERN4="Mushroom_Class Constructor called!"
PATTERN5="Tecton's Mushroom:"
PATTERN6="Tecton ID is null!"
PATTERN7="Mushroom_Grand Created! ID: Mushroom_Grand0 on Tecton: null"
PATTERN8="Tecton_Base Created! ID: Tecton_Base0"
PATTERN9="Test ran successfully!"

if [[ "$OUTPUT" =~ $PATTERN1 ]] && \
   [[ "$OUTPUT" =~ $PATTERN2 ]] && \
   [[ "$OUTPUT" =~ $PATTERN3 ]] && \
   [[ "$OUTPUT" =~ $PATTERN4 ]] && \
   [[ "$OUTPUT" =~ $PATTERN5 ]] && \
   [[ "$OUTPUT" =~ $PATTERN6 ]] && \
   [[ "$OUTPUT" =~ $PATTERN7 ]] && \
   [[ "$OUTPUT" =~ $PATTERN8 ]] && \
   [[ "$OUTPUT" =~ $PATTERN9 ]]; then
    echo "[test2] Test successful!"
else
    echo "Test failed!"
    echo "Output:"
    echo "$OUTPUT"
fi