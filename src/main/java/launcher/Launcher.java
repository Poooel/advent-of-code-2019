package launcher;

import days.*;
import lombok.SneakyThrows;

public class Launcher {
    @SneakyThrows
    public static void main(String[] args) {
        System.out.println("Welcome to the Advent of Code 2019!");

        System.out.println("Choose a day to begin: (1-25)");
        int choosenDay = LauncherHelper.getValidIntegerInput(
            "Please input a day between 1 and 25.",
            1,
            25
        );

        System.out.println("Choose a part for the day: (1-2)");
        int choosenPart = LauncherHelper.getValidIntegerInput(
            "Please input a part between 1 and 2.",
            1,
            2
        );

        if (choosenPart == 1) {
            long startTime = System.currentTimeMillis();
            String result = getCorrespondingExecutableDay(choosenDay).executePartOne();
            long endTime = System.currentTimeMillis();

            System.out.println(
                String.format(
                    "The answer for Day %02d Part %d is: %s (executed in approx. %dms)",
                    choosenDay,
                    choosenPart,
                    result,
                    endTime - startTime
                )
            );
        } else {
            long startTime = System.currentTimeMillis();
            String result = getCorrespondingExecutableDay(choosenDay).executePartTwo();
            long endTime = System.currentTimeMillis();

            System.out.println(
                String.format(
                    "The answer for Day %02d Part %d is: %s (executed in approx. %dms)",
                    choosenDay,
                    choosenPart,
                    result,
                    endTime - startTime
                )
            );
        }
    }

    private static Executable getCorrespondingExecutableDay(int day) {
        switch (day) {
            case 1:
                return new Day01_TheTyrannyOfTheRocketEquation();
            case 2:
                return new Day02_1202ProgramAlarm();
            case 3:
                return new Day03_CrossedWires();
            case 4:
                return new Day04_SecureContainer();
            default:
                return new Day00_NotDoneYet();
        }
    }
}
