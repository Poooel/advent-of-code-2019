package days;

import launcher.ChallengeHelper;
import launcher.Executable;

import java.util.*;

public class Day04_SecureContainer implements Executable {
    @Override
    public Object executePartOne() {
        List<String> input = ChallengeHelper.readInputData(4);
        String[] inputs = input.get(0).split("-");
        int lowerBound = Integer.parseInt(inputs[0]);
        int upperBound = Integer.parseInt(inputs[1]);
        return possibleSolutions(lowerBound, upperBound, false);
    }

    @Override
    public Object executePartTwo() {
        List<String> input = ChallengeHelper.readInputData(4);
        String[] inputs = input.get(0).split("-");
        int lowerBound = Integer.parseInt(inputs[0]);
        int upperBound = Integer.parseInt(inputs[1]);
        return possibleSolutions(lowerBound, upperBound, true);
    }

    private int possibleSolutions(int lowerBound, int upperBound, boolean strict) {
        int possibleSolutions = 0;

        for (int i = lowerBound; i <= upperBound; i++) {
            if (strict) {
                if (isIncreasingOrEqual(i) && exactlyTwoAdjacentDigitsEqual(i)) {
                    possibleSolutions++;
                }
            } else {
                if (isIncreasingOrEqual(i) && twoAdjacentDigitsEqual(i)) {
                    possibleSolutions++;
                }
            }
        }

        return possibleSolutions;
    }

    private boolean isIncreasingOrEqual(int number) {
        List<Integer> digits = getDigits(number);

        for (int i = 0; i < digits.size() - 1; i++) {
            if (digits.get(i) > digits.get(i + 1)) {
                return false;
            }
        }

        return true;
    }

    private boolean twoAdjacentDigitsEqual(int number) {
        List<Integer> digits = getDigits(number);

        for (int i = 0; i < digits.size() - 1; i++) {
            if (digits.get(i).equals(digits.get(i + 1))) {
                return true;
            }
        }

        return false;
    }

    private boolean exactlyTwoAdjacentDigitsEqual(int number) {
        List<Integer> digits = getDigits(number);
        Map<Integer, Integer> digitsAppearance = new HashMap<>();

        for (int i = 0; i < digits.size() - 1; i++) {
            if (digits.get(i).equals(digits.get(i + 1))) {
                digitsAppearance.compute(digits.get(i), (key, value) -> {
                    if (value == null) {
                        return 2;
                    } else {
                        return value + 1;
                    }
                });
            }
        }

        return digitsAppearance.containsValue(2);

    }

    // https://stackoverflow.com/questions/3389264/how-to-get-the-separate-digits-of-an-int-number
    private List<Integer> getDigits(int number) {
        List<Integer> digits = new ArrayList<>();

        while (number > 0) {
            digits.add(number % 10);
            number /= 10;
        }

        Collections.reverse(digits);

        return digits;
    }
}
