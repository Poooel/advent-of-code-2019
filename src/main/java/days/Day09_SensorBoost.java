package days;

import computer.IntcodeComputer;
import launcher.Executable;
import lombok.SneakyThrows;

import java.util.List;

public class Day09_SensorBoost implements Executable {
    @Override
    @SneakyThrows
    public Object executePartOne(List<String> input) {
        IntcodeComputer intcodeComputer = new IntcodeComputer();
        intcodeComputer.getInputBus().put(1);
        intcodeComputer.run(input.get(0));
        return intcodeComputer.getOutputBus().take();
    }

    @Override
    public Object executePartTwo(List<String> input) {
        IntcodeComputer intcodeComputer = new IntcodeComputer();
        intcodeComputer.getInputBus().put(2);
        intcodeComputer.run(input.get(0));
        return intcodeComputer.getOutputBus().take();
    }
}
