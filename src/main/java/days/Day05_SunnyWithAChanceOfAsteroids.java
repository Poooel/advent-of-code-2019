package days;

import computer.IntcodeComputer;
import launcher.Executable;
import lombok.SneakyThrows;

import java.util.List;

public class Day05_SunnyWithAChanceOfAsteroids implements Executable {
    @Override
    @SneakyThrows
    public Object executePartOne(List<String> input) {
        IntcodeComputer intcodeComputer = new IntcodeComputer();
        intcodeComputer.getInputBus().put(1);
        intcodeComputer.run(input.get(0));

        long diagnosticCode = intcodeComputer.getOutputBus().poll();
        Long testCode = intcodeComputer.getOutputBus().poll();

        while (testCode != null) {
            diagnosticCode = testCode;
            testCode = intcodeComputer.getOutputBus().poll();
        }

        return diagnosticCode;
    }

    @Override
    @SneakyThrows
    public Object executePartTwo(List<String> input) {
        IntcodeComputer intcodeComputer = new IntcodeComputer();
        intcodeComputer.getInputBus().put(5);
        intcodeComputer.run(input.get(0));
        return intcodeComputer.getOutputBus().take();
    }
}
