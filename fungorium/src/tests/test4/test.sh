#!/bin/bash

# Load global environment variables
source "$(dirname "$0")/../.env"  # This loads JAR_NAME as "../../target/fungorium-PROTO 0.1-jar-with-dependencies.jar"

# Convert JAR_NAME (relative path) to an absolute path.
# Here, we assume the relative path is relative to the directory where .env resides.
# Since .env is in $(dirname "$0")/.., we use that as the base.
BASE_DIR="$(dirname "$0")/.."
JAR_FILE="$(realpath "$BASE_DIR/$JAR_NAME")"

# Set the input file (relative to the script directory)
INPUT_FILE="$(dirname "$0")/input.txt"

# Run the Java program with input from input.txt
OUTPUT=$(java -jar "$JAR_FILE" --test 1 < "$INPUT_FILE")

# Define the regex patterns to search for
SUCCESS_REGEX="Test ran successfully!"
NULL_REGEX="Mushroom is null!.*Spore is null!.*Thread is null!"
EMPTY_REGEX="Tecton's Insects: \[\]"

# Check if the output matches all regex patterns
if [[ "$OUTPUT" =~ $SUCCESS_REGEX ]] && [[ "$OUTPUT" =~ $NULL_REGEX ]] && [[ "$OUTPUT" =~ $EMPTY_REGEX ]]; then
  echo "[test1] Test successful!"
else
  echo "Test failed!"
  echo "Output:"
  echo "$OUTPUT"
fi