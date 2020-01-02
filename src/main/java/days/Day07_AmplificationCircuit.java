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
        IntcodeComputer intcodeComputer = new IntcodeComputer();
        Set<List<Integer>> phaseSettingCombinations = getAllCombinations(0, 5);
        long maximumOutput = Integer.MIN_VALUE;

        for (List<Integer> phaseSettings : phaseSettingCombinations) {
            long outputSignal = 0;

            for (Integer phaseSetting : phaseSettings) {
                intcodeComputer.getInputBus().put((long) phaseSetting);
                intcodeComputer.getInputBus().put(outputSignal);

                intcodeComputer.run(input.get(0));

                outputSignal = intcodeComputer.getOutputBus().take();
            }

            maximumOutput = Math.max(maximumOutput, outputSignal);
        }

        return (int) maximumOutput;
    }

    @SneakyThrows
    private int findLargestOutputWithFeedbackLoop(List<String> input) {
        IntcodeComputer amplifierA = new IntcodeComputer();
        IntcodeComputer amplifierB = new IntcodeComputer();
        IntcodeComputer amplifierC = new IntcodeComputer();
        IntcodeComputer amplifierD = new IntcodeComputer();
        IntcodeComputer amplifierE = new IntcodeComputer();

        Set<List<Integer>> phaseSettingCombinations = getAllCombinations(5, 10);
        long maximumOutput = Integer.MIN_VALUE;

        amplifierB.setInputBus(amplifierA.getOutputBus());
        amplifierC.setInputBus(amplifierB.getOutputBus());
        amplifierD.setInputBus(amplifierC.getOutputBus());
        amplifierE.setInputBus(amplifierD.getOutputBus());
        amplifierA.setInputBus(amplifierE.getOutputBus());

        List<IntcodeComputer> computers = Arrays.asList(amplifierA, amplifierB, amplifierC, amplifierD, amplifierE);

        for (List<Integer> phaseSettings : phaseSettingCombinations) {
            for (int i = 0; i < computers.size(); i++) {
                computers.get(i).getInputBus().put((long) phaseSettings.get(i));
            }

            amplifierA.getInputBus().put(0);

            ExecutorService executorService = Executors.newFixedThreadPool(computers.size());
            CountDownLatch countDownLatch = new CountDownLatch(computers.size());

            for (IntcodeComputer computer : computers) {
                executorService.submit(() -> {
                    computer.run(input.get(0));
                    countDownLatch.countDown();
                });
            }

            countDownLatch.await();

            maximumOutput = Math.max(maximumOutput, amplifierE.getOutputBus().take());

            for (IntcodeComputer computer : computers) {
                computer.getInputBus().clear();
                computer.getOutputBus().clear();
            }
        }

        return (int) maximumOutput;
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
