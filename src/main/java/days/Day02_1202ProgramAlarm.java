package days;

import launcher.ChallengeHelper;
import launcher.Executable;

import java.util.Arrays;
import java.util.List;

public class Day02_1202ProgramAlarm implements Executable {
    @Override
    public String executePartOne() {
        // Get input from input file
        List<String> input = ChallengeHelper.readInputData(2);
        // Parse input into an integer array
        int[] parsedInput = parseInput(input);
        // Prepare the initial state with a noun = 12 and verb = 2
        int[] initialState = prepareState(12, 2, parsedInput);
        // Compute the final state of the machine
        int[] finalState = computeState(initialState);
        // Print the first value of the state
        return String.valueOf(finalState[0]);
    }

    @Override
    public String executePartTwo() {
        List<String> input = ChallengeHelper.readInputData(2);
        int[] parsedInput = parseInput(input);
        return String.valueOf(findState(parsedInput));
    }

    private int findState(int[] initialState) {
        for (int i = 0; i < 99; i++) {
            for (int j = 0; j < 99; j++) {
                // Make a deep copy of the initial state to prevent altering it
                int[] copiedState = Arrays.copyOf(initialState, initialState.length);
                // Prepare the initial state with a noun and verb
                int[] preparedState = prepareState(i, j, copiedState);
                // Compute the state for this noun and verb
                int[] finalState = computeState(preparedState);

                // If the first value of the final state is 19 690 720
                // we found the state we are looking for
                if (finalState[0] == 19_690_720) {
                    return (100 * i) + j;
                }
            }
        }

        // Shouldn't happen
        return -1;
    }

    private int[] parseInput(List<String> input) {
        return Arrays.stream(input.get(0).split(","))
            .mapToInt(Integer::parseInt)
            .toArray();
    }

    private int[] prepareState(int noun, int verb, int[] state) {
        state[1] = noun;
        state[2] = verb;
        return state;
    }

    private int[] computeState(int[] state) {
        for (int i = 0; i < state.length; i += 4) {
            int opcode = state[i];

            // If opcode is 99, halt machine here and return the state
            if (opcode == 99) {
                return state;
            }

            int firstAddress = state[i + 1];
            int secondAddress = state[i + 2];
            int resultAddress = state[i + 3];

            int result = 0;

            if (opcode == 1) {
                result = state[firstAddress] + state[secondAddress];
            } else if (opcode == 2) {
                result = state[firstAddress] * state[secondAddress];
            }

            state[resultAddress] = result;
        }

        return state;
    }
}
