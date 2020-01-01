package computer;

import lombok.Data;
import lombok.Value;

import java.util.Arrays;

public class IntcodeComputer {
    private final int[] program;
    private int[] inputs;

    public IntcodeComputer(int[] program) {
        this.program = program;
    }

    public IntcodeComputer(String program) {
        this.program = parseInput(program);
    }

    public void setInputs(int... inputs) {
        this.inputs = inputs;
    }

    private int[] parseInput(String input) {
        return Arrays.stream(input.split(","))
            .mapToInt(Integer::parseInt)
            .toArray();
    }

    public int run() {
        int lastResult = -1;
        int inputPointer = 0;
        int[] program = Arrays.copyOf(this.program, this.program.length);

        for (int i = 0; i < program.length; i++) {
            int[] instructions = deconstructOpcode(program[i]);

            Parameter firstParameter = new Parameter(instructions[2]);
            Parameter secondParameter = new Parameter(instructions[3]);
            Result result;

            int opcode = instructions[0] + (instructions[1] * 10);

            switch (opcode) {
                case 1:
                    result = add(program, i, firstParameter, secondParameter);
                    break;
                case 2:
                    result = multiply(program, i, firstParameter, secondParameter);
                    break;
                case 3:
                    result = input(program, i, firstParameter, inputs[inputPointer++]);
                    break;
                case 4:
                    result = output(program, i, firstParameter);
                    lastResult = result.getValue();
                    break;
                case 5:
                    result = jumpIfTrue(program, i, firstParameter, secondParameter);
                    break;
                case 6:
                    result = jumpIfFalse(program, i, firstParameter, secondParameter);
                    break;
                case 7:
                    result = lessThan(program, i, firstParameter, secondParameter);
                    break;
                case 8:
                    result = equals(program, i, firstParameter, secondParameter);
                    break;
                case 99:
                    return lastResult;
                default:
                    result = new Result(0, 0, Integer.MIN_VALUE, false);
                    break;
            }

            if (result.isValuePresent()) {
                program[result.getAddress()] = result.getValue();
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

    private void getParameterValue(int[] program, int instructionPointer, Parameter parameter) {
        if (parameter.getMode() == 0) {
            int address = program[instructionPointer];
            parameter.setValue(program[address]);
        } else {
            parameter.setValue(program[instructionPointer]);
        }
    }

    private Result add(int[] program, int instructionPointer, Parameter firstParameter, Parameter secondParameter) {
        getParameterValue(program, ++instructionPointer, firstParameter);
        getParameterValue(program, ++instructionPointer, secondParameter);

        int result = firstParameter.getValue() + secondParameter.getValue();
        int resultAddress = program[++instructionPointer];

        return new Result(result, resultAddress, instructionPointer, true);
    }

    private Result multiply(int[] program, int instructionPointer, Parameter firstParameter, Parameter secondParameter) {
        getParameterValue(program, ++instructionPointer, firstParameter);
        getParameterValue(program, ++instructionPointer, secondParameter);

        int result = firstParameter.getValue() * secondParameter.getValue();
        int resultAddress = program[++instructionPointer];

        return new Result(result, resultAddress, instructionPointer, true);
    }

    private Result input(int[] program, int instructionPointer, Parameter firstParameter, int inputValue) {
        firstParameter.setMode(1);
        getParameterValue(program, ++instructionPointer, firstParameter);
        return new Result(inputValue, firstParameter.getValue(), instructionPointer, true);
    }

    private Result output(int[] program, int instructionPointer, Parameter firstParameter) {
        getParameterValue(program, ++instructionPointer, firstParameter);
        return new Result(firstParameter.getValue(), 0, instructionPointer, false);
    }

    private Result jumpIfTrue(int[] program, int instructionPointer, Parameter firstParameter, Parameter secondParameter) {
        getParameterValue(program, ++instructionPointer, firstParameter);
        getParameterValue(program, ++instructionPointer, secondParameter);

        if (firstParameter.getValue() != 0) {
            instructionPointer = secondParameter.getValue() - 1;
        }

        return new Result(0, 0, instructionPointer, false);
    }

    private Result jumpIfFalse(int[] program, int instructionPointer, Parameter firstParameter, Parameter secondParameter) {
        getParameterValue(program, ++instructionPointer, firstParameter);
        getParameterValue(program, ++instructionPointer, secondParameter);

        if (firstParameter.getValue() == 0) {
            instructionPointer = secondParameter.getValue() - 1;
        }

        return new Result(0, 0, instructionPointer, false);
    }

    private Result lessThan(int[] program, int instructionPointer, Parameter firstParameter, Parameter secondParameter) {
        getParameterValue(program, ++instructionPointer, firstParameter);
        getParameterValue(program, ++instructionPointer, secondParameter);
        int resultAddress = program[++instructionPointer];

        if (firstParameter.getValue() < secondParameter.getValue()) {
            return new Result(1, resultAddress, instructionPointer, true);
        } else {
            return new Result(0, resultAddress, instructionPointer, true);
        }
    }

    private Result equals(int[] program, int instructionPointer, Parameter firstParameter, Parameter secondParameter) {
        getParameterValue(program, ++instructionPointer, firstParameter);
        getParameterValue(program, ++instructionPointer, secondParameter);
        int resultAddress = program[++instructionPointer];

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
