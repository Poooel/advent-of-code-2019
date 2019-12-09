package launcher;

public interface Executable {
    /**
     * Execute part one of the choosen day and return the result as a String
     * @return The result as a String for the choosen day
     */
    Object executePartOne();

    /**
     * Execute part two of the choosen day and return the result as a String
     * @return The result as a String for the choosen day
     */
    Object executePartTwo();
}
