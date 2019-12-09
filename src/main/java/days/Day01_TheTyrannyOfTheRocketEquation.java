package days;

import launcher.ChallengeHelper;
import launcher.Executable;

import java.util.List;
import java.util.stream.Collectors;

public class Day01_TheTyrannyOfTheRocketEquation implements Executable {
    @Override
    public Object executePartOne() {
        List<String> input = ChallengeHelper.readInputData(1);
        // Cast strings to doubles to be able to divide it and keep precision
        List<Double> inputAsDoubles = input.stream()
            .mapToDouble(Double::parseDouble)
            .boxed()
            .collect(Collectors.toList());

        return inputAsDoubles
            .stream()
            .mapToDouble(fuel -> Math.floor(fuel / 3) - 2)
            .sum();
    }

    @Override
    public Object executePartTwo() {
        List<String> input = ChallengeHelper.readInputData(1);
        List<Double> inputAsDoubles = input.stream()
            .mapToDouble(Double::parseDouble)
            .boxed()
            .collect(Collectors.toList());

        return inputAsDoubles
            .stream()
            .mapToDouble(fuel -> computeFuelRequirements(fuel, 0))
            .sum();
    }

    private double computeFuelRequirements(double fuel, double sum) {
        double fuelRequirement = Math.floor(fuel / 3) - 2;

        // If the fuel requirement is negative or zero, we should halt there
        if (fuelRequirement <= 0) {
            return sum;
        } else {
            return computeFuelRequirements(fuelRequirement, sum + fuelRequirement);
        }
    }
}
