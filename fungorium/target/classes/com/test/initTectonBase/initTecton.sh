#!/bin/bash
# test_fungorium.sh
# This script runs 20 tests on the FungoriumPrototype Java application.
# Make sure the Java program is compiled (e.g., FungoriumPrototype.class is present)
# and that the java command is available.

JAVA_CMD="java FungoriumPrototype"

# Function to run a single test:
# Arguments:
#   1: Test name
#   2: Input string (with newline escapes) to feed into the Java program
#   3: Expected substring in the output
run_test() {
    local test_name="$1"
    local input="$2"
    local expected="$3"

    echo "------------------------------"
    echo "Running test: $test_name"
    # Run the Java program with input from echo (simulate interactive input)
    output=$(echo -e "$input" | $JAVA_CMD)
    echo "Output:"
    echo "$output"
    
    # Check for the expected output substring
    if echo "$output" | grep -q "$expected"; then
        echo "Test $test_name: PASS"
    else
        echo "Test $test_name: FAIL - Expected to find: \"$expected\""
    fi
    echo "------------------------------"
    echo
}

# Test 1: Game Initialization Test
# Input: "5" (initialize game) then "0" to exit.
run_test "Game Initialization" "5\n0" "Game initialized"

# Test 2: Place Mushroom – Successful Placement
# Input: Initialize then option 1 should place a mushroom.
run_test "Place Mushroom Success" "5\n1\n0" "Mushroom placed successfully"

# Test 3: Place Mushroom – No Available Tecton
# After initialization, there are only 4 free tectons.
# By calling option 1 five times, the fifth should report no available tecton.
run_test "Place Mushroom No Tecton" "5\n1\n1\n1\n1\n1\n0" "No available tecton"

# Test 4: Move Insect – Valid Move
# Input: Initialize, place insect (option 3), then move insect (option 2).
run_test "Move Insect Valid" "5\n3\n2\n0" "Insect moved successfully"

# Test 5: Move Insect – No Valid Neighbor
# Insect is placed on Tecton3.
# By placing a mushroom (option 1) right after, we fill its neighbor (Tecton2).
# Then, moving the insect (option 2) should fail.
run_test "Move Insect No Valid Neighbor" "5\n3\n1\n2\n0" "No valid neighboring tecton"

# Test 6: Place Insect – Successful Placement
run_test "Place Insect Success" "5\n3\n0" "Insect placed successfully"

# Test 7: Place Insect – Starting Tile Already Occupied
# Try to place an insect twice (option 3 twice).
run_test "Place Insect Already Occupied" "5\n3\n3\n0" "Starting position is already occupied"

# Test 8: Upgrade Mushroom
# Upgrading a mushroom (option 4) should output a success message.
run_test "Upgrade Mushroom" "5\n4\n0" "Mushroom upgraded successfully"

# Test 9: End Game & Evaluation – Base Mushroom Intact
# Option 6 should display a final evaluation message.
run_test "End Game Evaluation Base Intact" "5\n6\n0" "Game over"

# Test 10: End Game & Evaluation – Base Mushroom Destroyed
# Simulate an insect attacking the base mushroom twice so that its HP reaches 0.
run_test "End Game Evaluation Base Destroyed" "5\n3\n7\n7\n6\n0" "The mushroom has been destroyed"

# Test 11: Insect Attacks Mushroom
run_test "Insect Attacks Mushroom" "5\n3\n7\n0" "attacks the mushroom"

# Test 12: Mushroom Attacks Insect
run_test "Mushroom Attacks Insect" "5\n8\n0" "attacks insect"

# Test 13: Generate Spore
run_test "Generate Spore" "5\n9\n0" "generates a"

# Test 14: Consume Spore
run_test "Consume Spore" "5\n9\n10\n0" "consumes the"

# Test 15: Crack Tecton
run_test "Crack Tecton" "5\n11\n0" "cracks the tecton"

# Test 16: Eat Fungal Thread
run_test "Eat Fungal Thread" "5\n12\n0" "eats the fungal thread"

# Test 17: Destroy Tecton
run_test "Destroy Tecton" "5\n13\n0" "destroy"

# Test 18: Thread Spreads
run_test "Thread Spreads" "5\n14\n0" "spreads"

# Test 19: Currency Boundary Conditions for Fungus – Upgrade Mushroom fails due to low funds
# Place mushrooms to reduce fungus currency then try upgrading.
run_test "Currency Boundary Upgrade" "5\n1\n1\n1\n1\n4\n0" "Not enough resources"

# Test 20: Additional Test – Insect Attack Leading to Insect Death
# For example, if an insect attacks a mushroom and its HP goes to 0, it should be removed.
# (This test checks for the insect removal message.)
run_test "Insect Attack and Removal" "5\n3\n7\n0" "The insect is killed"

# End of tests
echo "All tests completed."
