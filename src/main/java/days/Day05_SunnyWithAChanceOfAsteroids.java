package days;

import computer.IntcodeComputer;
import launcher.Executable;
import lombok.SneakyThrows;

import java.util.List;

public class Day05_SunnyWithAChanceOfAsteroids implements Executable {
    @Override
    @SneakyThrows
    public Object executePartOne(List<String> input) {
        IntcodeComputer intcodeComputer = new IntcodeComputer(input.get(0));
        intcodeComputer.getInputQueue().put(1);
        intcodeComputer.run();
        return intcodeComputer.getOutputQueue().take();
    }

    @Override
    @SneakyThrows
    public Object executePartTwo(List<String> input) {
        IntcodeComputer intcodeComputer = new IntcodeComputer(input.get(0));
        intcodeComputer.getInputQueue().put(5);
        intcodeComputer.run();
        return intcodeComputer.getOutputQueue().take();
    }
}
