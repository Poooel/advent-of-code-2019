package computer;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

public class IntcodeComputer {
    public static final long ENDING_SIGNAL = 9_223_372_036_854_775_806L;

    private final Memory memory;

    @Getter
    @Setter
    private IOBus inputBus;

    @Getter
    @Setter
    private IOBus outputBus;

    private int instructionPointer;

    public IntcodeComputer() {
        this.inputBus = new IOBus();
        this.outputBus = new IOBus();
        this.memory = new Memory();
    }

    public void runAsync(String program) {
        new Thread(() -> run(program)).start();
    }

    public void run(String program) {
        long[] compiledProgram = compileProgram(program);
        writeProgramToMemory(compiledProgram);
        resetInstructionPointer();

        while (true) {
            Instruction instruction = translateInstruction(memory.read(getInstructionPointer()));

            switch (instruction.getOpcode()) {
                case 1:
                    add(instruction);
                    break;
                case 2:
                    multiply(instruction);
                    break;
                case 3:
                    input(instruction);
                    break;
                case 4:
                    output(instruction);
                    break;
                case 5:
                    jumpIfTrue(instruction);
                    break;
                case 6:
                    jumpIfFalse(instruction);
                    break;
                case 7:
                    lessThan(instruction);
                    break;
                case 8:
                    equals(instruction);
                    break;
                case 9:
                    adjustRelativeBaseOffset(instruction);
                    break;
                case 99:
                    outputBus.put(ENDING_SIGNAL);
                    return;
                default:
                    throw new UnknownOpcodeException("Unknown opcode: " + instruction.getOpcode());
            }
        }
    }

    private Instruction translateInstruction(long instruction) {
        long[] rawInstruction = new long[5];

        int digitCounter = 0;

        while (instruction > 0) {
            rawInstruction[digitCounter++] = instruction % 10;
            instruction /= 10;
        }

        int opcode = (int) (rawInstruction[0] + (rawInstruction[1] * 10));
        int[] rawParameterModes = convertToIntArray(Arrays.copyOfRange(rawInstruction, 2, rawInstruction.length));

        Mode[] parameterModes = new Mode[rawParameterModes.length];

        for (int i = 0; i < rawParameterModes.length; i++) {
            parameterModes[i] = getMode(rawParameterModes[i]);
        }

        return new Instruction(opcode, parameterModes);
    }

    private int[] convertToIntArray(long[] longArray) {
        int[] intArray = new int[longArray.length];

        for (int i = 0; i < longArray.length; i++) {
            intArray[i] = (int) longArray[i];
        }

        return intArray;
    }

    private long[] compileProgram(String program) {
        return Arrays.stream(program.split(","))
            .mapToLong(Long::parseLong)
            .toArray();
    }

    private void writeProgramToMemory(long[] program) {
        memory.writeRange(0, program);
    }

    private Mode getMode(int mode) {
        if (mode < Mode.values().length) {
            return Mode.values()[mode];
        } else {
            throw new UnknownModeException("Unknown mode: " + mode);
        }
    }

    private int getInstructionPointer() {
        return instructionPointer++;
    }

    private void setInstructionPointer(int instructionPointer) {
        this.instructionPointer = instructionPointer;
    }

    private void resetInstructionPointer() {
        instructionPointer = 0;
    }

    /**
     * https://adventofcode.com/2019/day/2
     *
     * Opcode 1 adds together numbers read from two positions and stores the result in a third position.
     * The three integers immediately after the opcode tell you these three positions -
     * the first two indicate the positions from which you should read the input values,
     * and the third indicates the position at which the output should be stored.
     */
    private void add(Instruction instruction) {
        long a = memory.read(getInstructionPointer(), instruction.getParameterModes()[0]);
        long b = memory.read(getInstructionPointer(), instruction.getParameterModes()[1]);

        long r = a + b;

        memory.write(getInstructionPointer(), r, instruction.getParameterModes()[2]);
    }

    /**
     * https://adventofcode.com/2019/day/2
     *
     * Opcode 2 works exactly like opcode 1, except it multiplies the two inputs instead
     * of adding them. Again, the three integers after the opcode indicate where the inputs
     * and outputs are, not their values.
     */
    private void multiply(Instruction instruction) {
        long a = memory.read(getInstructionPointer(), instruction.getParameterModes()[0]);
        long b = memory.read(getInstructionPointer(), instruction.getParameterModes()[1]);

        long r = a * b;

        memory.write(getInstructionPointer(), r, instruction.getParameterModes()[2]);
    }

    /**
     * https://adventofcode.com/2019/day/5
     *
     * Opcode 3 takes a single integer as input and saves it to the
     * position given by its only parameter. For example, the instruction
     * 3,50 would take an input value and store it at address 50.
     */
    private void input(Instruction instruction) {
        memory.write(getInstructionPointer(), inputBus.take(), instruction.getParameterModes()[0]);
    }

    /**
     * https://adventofcode.com/2019/day/5
     *
     * Opcode 4 outputs the value of its only parameter. For example,
     * the instruction 4,50 would output the value at address 50.
     */
    private void output(Instruction instruction) {
        outputBus.put(memory.read(getInstructionPointer(), instruction.getParameterModes()[0]));
    }

    /**
     * https://adventofcode.com/2019/day/5
     *
     * Opcode 5 is jump-if-true: if the first parameter is non-zero,
     * it sets the instruction pointer to the value from the second parameter.
     * Otherwise, it does nothing.
     */
    private void jumpIfTrue(Instruction instruction) {
        long a = memory.read(getInstructionPointer(), instruction.getParameterModes()[0]);
        long b = memory.read(getInstructionPointer(), instruction.getParameterModes()[1]);

        if (a != 0) {
            setInstructionPointer((int) b);
        }
    }

    /**
     * https://adventofcode.com/2019/day/5
     *
     * Opcode 6 is jump-if-false: if the first parameter is zero,
     * it sets the instruction pointer to the value from the second parameter.
     * Otherwise, it does nothing.
     */
    private void jumpIfFalse(Instruction instruction) {
        long a = memory.read(getInstructionPointer(), instruction.getParameterModes()[0]);
        long b = memory.read(getInstructionPointer(), instruction.getParameterModes()[1]);

        if (a == 0) {
            setInstructionPointer((int) b);
        }
    }

    /**
     * https://adventofcode.com/2019/day/5
     *
     * Opcode 7 is less than: if the first parameter is less than the second parameter,
     * it stores 1 in the position given by the third parameter.
     * Otherwise, it stores 0.
     */
    private void lessThan(Instruction instruction) {
        long a = memory.read(getInstructionPointer(), instruction.getParameterModes()[0]);
        long b = memory.read(getInstructionPointer(), instruction.getParameterModes()[1]);

        long r = a < b ? 1 : 0;

        memory.write(getInstructionPointer(), r, instruction.getParameterModes()[2]);
    }

    /**
     * https://adventofcode.com/2019/day/5
     *
     * Opcode 8 is equals: if the first parameter is equal to the second parameter,
     * it stores 1 in the position given by the third parameter.
     * Otherwise, it stores 0.
     */
    private void equals(Instruction instruction) {
        long a = memory.read(getInstructionPointer(), instruction.getParameterModes()[0]);
        long b = memory.read(getInstructionPointer(), instruction.getParameterModes()[1]);

        long r = a == b ? 1 : 0;

        memory.write(getInstructionPointer(), r, instruction.getParameterModes()[2]);
    }

    /**
     * https://adventofcode.com/2019/day/9
     *
     * Opcode 9 adjusts the relative base by the value of its only parameter.
     * The relative base increases (or decreases, if the value is negative)
     * by the value of the parameter.
     */
    private void adjustRelativeBaseOffset(Instruction instruction) {
        long a = memory.read(getInstructionPointer(), instruction.getParameterModes()[0]);

        memory.addToRelativeBaseOffset((int) a);
    }
}
