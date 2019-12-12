package days;

import launcher.ChallengeHelper;
import launcher.Executable;
import lombok.Data;
import lombok.Value;

import java.util.Arrays;
import java.util.List;

public class Day05_SunnyWithAChanceOfAsteroids implements Executable {
    @Override
    public Object executePartOne() {
        List<String> input = ChallengeHelper.readInputData(5);
        int[] initialState = parseInput(input);
        return intcodeComputer(initialState, 1);
    }

    @Override
    public Object executePartTwo() {
        List<String> input = ChallengeHelper.readInputData(5);
        int[] initialState = parseInput(input);
        return intcodeComputer(initialState, 5);
    }

    private int[] parseInput(List<String> input) {
        return Arrays.stream(input.get(0).split(","))
            .mapToInt(Integer::parseInt)
            .toArray();
    }

    private int intcodeComputer(int[] state, int input) {
        int lastResult = -1;

        for (int i = 0; i < state.length; i++) {
            int[] instructions = deconstructOpcode(state[i]);

            Parameter firstParameter = new Parameter(instructions[2]);
            Parameter secondParameter = new Parameter(instructions[3]);
            Result result;

            int opcode = instructions[0] + (instructions[1] * 10);

            switch (opcode) {
                case 1:
                    result = add(state, i, firstParameter, secondParameter);
                    break;
                case 2:
                    result = multiply(state, i, firstParameter, secondParameter);
                    break;
                case 3:
                    result = input(state, i, firstParameter, input);
                    break;
                case 4:
                    result = output(state, i, firstParameter);
                    lastResult = result.getValue();
                    break;
                case 5:
                    result = jumpIfTrue(state, i, firstParameter, secondParameter);
                    break;
                case 6:
                    result = jumpIfFalse(state, i, firstParameter, secondParameter);
                    break;
                case 7:
                    result = lessThan(state, i, firstParameter, secondParameter);
                    break;
                case 8:
                    result = equals(state, i, firstParameter, secondParameter);
                    break;
                case 99:
                    return lastResult;
                default:
                    result = new Result(0, 0, Integer.MIN_VALUE, false);
                    break;
            }

            if (result.isValuePresent()) {
                state[result.getAddress()] = result.getValue();
            }

            i = result.getInstructionPointer();
        }

        return Integer.MAX_VALUE;
    }

    private int[] deconstructOpcode(int opcode) {
        int[] digits = new int[4];
        int digitCounter = 0;

        Arrays.fill(digits, 0);

        while (opcode > 0) {
            digits[digitCounter++] = opcode % 10;
            opcode /= 10;
        }

        return digits;
    }

    private void getParameterValue(int[] state, int instructionPointer, Parameter parameter) {
        if (parameter.getMode() == 0) {
            int address = state[instructionPointer];
            parameter.setValue(state[address]);
        } else {
            parameter.setValue(state[instructionPointer]);
        }
    }

    private Result add(int[] state, int instructionPointer, Parameter firstParameter, Parameter secondParameter) {
        getParameterValue(state, ++instructionPointer, firstParameter);
        getParameterValue(state, ++instructionPointer, secondParameter);

        int result = firstParameter.getValue() + secondParameter.getValue();
        int resultAddress = state[++instructionPointer];

        return new Result(result, resultAddress, instructionPointer, true);
    }

    private Result multiply(int[] state, int instructionPointer, Parameter firstParameter, Parameter secondParameter) {
        getParameterValue(state, ++instructionPointer, firstParameter);
        getParameterValue(state, ++instructionPointer, secondParameter);

        int result = firstParameter.getValue() * secondParameter.getValue();
        int resultAddress = state[++instructionPointer];

        return new Result(result, resultAddress, instructionPointer, true);
    }

    private Result input(int[] state, int instructionPointer, Parameter firstParameter, int inputValue) {
        firstParameter.setMode(1);
        getParameterValue(state, ++instructionPointer, firstParameter);
        return new Result(inputValue, firstParameter.getValue(), instructionPointer, true);
    }

    private Result output(int[] state, int instructionPointer, Parameter firstParameter) {
        getParameterValue(state, ++instructionPointer, firstParameter);
        return new Result(firstParameter.getValue(), 0, instructionPointer, false);
    }

    private Result jumpIfTrue(int[] state, int instructionPointer, Parameter firstParameter, Parameter secondParameter) {
        getParameterValue(state, ++instructionPointer, firstParameter);
        getParameterValue(state, ++instructionPointer, secondParameter);

        if (firstParameter.getValue() != 0) {
            instructionPointer = secondParameter.getValue() - 1;
        }

        return new Result(0, 0, instructionPointer, false);
    }

    private Result jumpIfFalse(int[] state, int instructionPointer, Parameter firstParameter, Parameter secondParameter) {
        getParameterValue(state, ++instructionPointer, firstParameter);
        getParameterValue(state, ++instructionPointer, secondParameter);

        if (firstParameter.getValue() == 0) {
            instructionPointer = secondParameter.getValue() - 1;
        }

        return new Result(0, 0, instructionPointer, false);
    }

    private Result lessThan(int[] state, int instructionPointer, Parameter firstParameter, Parameter secondParameter) {
        getParameterValue(state, ++instructionPointer, firstParameter);
        getParameterValue(state, ++instructionPointer, secondParameter);
        int resultAddress = state[++instructionPointer];

        if (firstParameter.getValue() < secondParameter.getValue()) {
            return new Result(1, resultAddress, instructionPointer, true);
        } else {
            return new Result(0, resultAddress, instructionPointer, true);
        }
    }

    private Result equals(int[] state, int instructionPointer, Parameter firstParameter, Parameter secondParameter) {
        getParameterValue(state, ++instructionPointer, firstParameter);
        getParameterValue(state, ++instructionPointer, secondParameter);
        int resultAddress = state[++instructionPointer];

        if (firstParameter.getValue() == secondParameter.getValue()) {
            return new Result(1, resultAddress, instructionPointer, true);
        } else {
            return new Result(0, resultAddress, instructionPointer, true);
        }
    }

    @Data
    private static final class Parameter {
        private int mode;
        private int value;

        public Parameter(int mode) {
            this.mode = mode;
        }
    }

    @Value
    private static final class Result {
        private int value;
        private int address;
        private int instructionPointer;
        private boolean valuePresent;
    }
}
