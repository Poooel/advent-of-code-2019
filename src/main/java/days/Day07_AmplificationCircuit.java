package days;

import com.google.common.math.IntMath;
import computer.IntcodeComputer;
import launcher.ChallengeHelper;
import launcher.Executable;

import java.util.*;

public class Day07_AmplificationCircuit implements Executable {
    @Override
    public Object executePartOne() {
        List<String> input = ChallengeHelper.readInputData(7);
        return findLargestOutput(input);
    }

    @Override
    public Object executePartTwo() {
        return null;
    }

    private int findLargestOutput(List<String> input) {
        IntcodeComputer intcodeComputer = new IntcodeComputer(input.get(0));
        Set<List<Integer>> phaseSettingCombinations = getAllCombinations(0, 5);
        int maximumOutput = Integer.MIN_VALUE;

        for (List<Integer> phaseSettings : phaseSettingCombinations) {
            int outputSignal = 0;

            for (Integer phaseSetting : phaseSettings) {
                intcodeComputer.setInputs(phaseSetting, outputSignal);
                outputSignal = intcodeComputer.run();
            }

            if (outputSignal > maximumOutput) {
                maximumOutput = outputSignal;
            }
        }

        return maximumOutput;
    }

    private int findLargestOutputWithFeedbackLoop(List<String> input) {
        IntcodeComputer intcodeComputer = new IntcodeComputer(input.get(0));
        Set<List<Integer>> phaseSettingCombinations = getAllCombinations(5, 10);
        int maximumOutput = Integer.MIN_VALUE;

        

        return maximumOutput;
    }

    private Set<List<Integer>> getAllCombinations(int lowerBound, int upperBound) {
        Set<List<Integer>> combinations = new HashSet<>();
        List<Integer> initialCombination = new ArrayList<>();

        for (int i = lowerBound; i < upperBound; i++) {
            initialCombination.add(i);
        }

        combinations.add(initialCombination);

        while (combinations.size() < IntMath.factorial(initialCombination.size())) {
            Collections.shuffle(initialCombination);
            combinations.add(new ArrayList<>(initialCombination));
        }

        return combinations;
    }
}
