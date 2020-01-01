package days;

import computer.IntcodeComputer;
import launcher.ChallengeHelper;
import launcher.Executable;

import java.util.List;

public class Day05_SunnyWithAChanceOfAsteroids implements Executable {
    @Override
    public Object executePartOne() {
        List<String> input = ChallengeHelper.readInputData(5);
        IntcodeComputer intcodeComputer = new IntcodeComputer(input.get(0), () -> 1);
        return intcodeComputer.run();
    }

    @Override
    public Object executePartTwo() {
        List<String> input = ChallengeHelper.readInputData(5);
        IntcodeComputer intcodeComputer = new IntcodeComputer(input.get(0), () -> 5);
        return intcodeComputer.run();
    }
}
