package launcher;

import java.util.List;

public interface Executable {
    /**
     * Execute part one of the choosen day and return the result as a String
     * @return The result as an Object for the choosen day
     */
    Object executePartOne(List<String> input);

    /**
     * Execute part two of the choosen day and return the result as a String
     * @return The result as an Object for the choosen day
     */
    Object executePartTwo(List<String> input);
}
