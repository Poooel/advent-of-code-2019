package days;

import com.google.common.math.IntMath;
import computer.IntcodeComputer;
import launcher.Executable;
import lombok.SneakyThrows;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Day07_AmplificationCircuit implements Executable {
    @Override
    public Object executePartOne(List<String> input) {
        return findLargestOutput(input);
    }

    @Override
    public Object executePartTwo(List<String> input) {
        return findLargestOutputWithFeedbackLoop(input);
    }

    @SneakyThrows
    private int findLargestOutput(List<String> input) {
        IntcodeComputer intcodeComputer = new IntcodeComputer(input.get(0));
        Set<List<Integer>> phaseSettingCombinations = getAllCombinations(0, 5);
        int maximumOutput = Integer.MIN_VALUE;

        for (List<Integer> phaseSettings : phaseSettingCombinations) {
            int outputSignal = 0;

            for (Integer phaseSetting : phaseSettings) {
                intcodeComputer.getInputQueue().put(phaseSetting);
                intcodeComputer.getInputQueue().put(outputSignal);

                intcodeComputer.run();

                outputSignal = intcodeComputer.getOutputQueue().take();
            }

            maximumOutput = Math.max(maximumOutput, outputSignal);
        }

        return maximumOutput;
    }

    @SneakyThrows
    private int findLargestOutputWithFeedbackLoop(List<String> input) {
        IntcodeComputer amplifierA = new IntcodeComputer(input.get(0));
        IntcodeComputer amplifierB = new IntcodeComputer(input.get(0));
        IntcodeComputer amplifierC = new IntcodeComputer(input.get(0));
        IntcodeComputer amplifierD = new IntcodeComputer(input.get(0));
        IntcodeComputer amplifierE = new IntcodeComputer(input.get(0));

        Set<List<Integer>> phaseSettingCombinations = getAllCombinations(5, 10);
        int maximumOutput = Integer.MIN_VALUE;

        amplifierB.setInputQueue(amplifierA.getOutputQueue());
        amplifierC.setInputQueue(amplifierB.getOutputQueue());
        amplifierD.setInputQueue(amplifierC.getOutputQueue());
        amplifierE.setInputQueue(amplifierD.getOutputQueue());
        amplifierA.setInputQueue(amplifierE.getOutputQueue());

        List<IntcodeComputer> computers = Arrays.asList(amplifierA, amplifierB, amplifierC, amplifierD, amplifierE);

        for (List<Integer> phaseSettings : phaseSettingCombinations) {
            for (int i = 0; i < computers.size(); i++) {
                computers.get(i).getInputQueue().put(phaseSettings.get(i));
            }

            amplifierA.getInputQueue().put(0);

            ExecutorService executorService = Executors.newFixedThreadPool(computers.size());
            CountDownLatch countDownLatch = new CountDownLatch(computers.size());

            for (IntcodeComputer computer : computers) {
                executorService.submit(() -> {
                    computer.run();
                    countDownLatch.countDown();
                });
            }

            countDownLatch.await();

            maximumOutput = Math.max(maximumOutput, amplifierE.getOutputQueue().take());

            for (IntcodeComputer computer : computers) {
                computer.getInputQueue().clear();
                computer.getOutputQueue().clear();
            }
        }

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
