package days;

import computer.IntcodeComputer;
import launcher.Executable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Day11_SpacePolice implements Executable {
    @Override
    public Object executePartOne(List<String> input) {
        Map<Coordinates, Integer> paintedPanels = computePanelsToPaint(input, 0);
        return paintedPanels.size();
    }

    @Override
    public Object executePartTwo(List<String> input) {
        Map<Coordinates, Integer> paintedPanels = computePanelsToPaint(input, 1);

        int maxX = paintedPanels.keySet().stream().map(Coordinates::getX).mapToInt(Integer::intValue).max().getAsInt();
        int maxY = paintedPanels.keySet().stream().map(Coordinates::getY).mapToInt(Integer::intValue).max().getAsInt();

        char[][] registrationIdentifier = new char[maxY + 1][];

        for (int i = 0; i < registrationIdentifier.length; i++) {
            registrationIdentifier[i] = new char[maxX + 1];
        }

        for (Map.Entry<Coordinates, Integer> entry : paintedPanels.entrySet()) {
            registrationIdentifier[entry.getKey().getY()][entry.getKey().getX()] = entry.getValue() == 0 ? ' ' : '#';
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");

        for (char[] line : registrationIdentifier) {
            stringBuilder.append(new String(line)).append("\n");
        }

        return stringBuilder.toString();
    }

    private Map<Coordinates, Integer> computePanelsToPaint(List<String> input, int startingColor) {
        IntcodeComputer intcodeComputer = new IntcodeComputer();

        new Thread(() -> intcodeComputer.run(input.get(0))).start();

        Map<Coordinates, Integer> paintedPanels = new HashMap<>();
        Coordinates robotCoordinates = new Coordinates(0, 0);
        List<Coordinates> directions = Arrays.asList(
            new Coordinates(0, -1), // ^
            new Coordinates(1, 0),  // >
            new Coordinates(0, 1),  // v
            new Coordinates(-1, 0)  // <
        );
        int directionCursor = 0;

        paintedPanels.put(robotCoordinates.copy(), startingColor);
        intcodeComputer.getInputBus().put(startingColor);

        while (true) {
            Long colorToPaint = intcodeComputer.getOutputBus().poll(10, TimeUnit.MILLISECONDS);
            Long direction = intcodeComputer.getOutputBus().poll(10, TimeUnit.MILLISECONDS);

            if (colorToPaint == null && direction == null) {
                break;
            }

            paintedPanels.replace(robotCoordinates.copy(), colorToPaint.intValue());

            if (direction == 0) {
                directionCursor--;
            } else if (direction == 1) {
                directionCursor++;
            }

            if (directionCursor == -1) {
                directionCursor = directions.size() - 1;
            } else if (directionCursor == 4) {
                directionCursor = 0;
            }

            robotCoordinates.setX(robotCoordinates.getX() + directions.get(directionCursor).getX());
            robotCoordinates.setY(robotCoordinates.getY() + directions.get(directionCursor).getY());

            int color = paintedPanels.computeIfAbsent(robotCoordinates.copy(), (coordinates) -> 0);

            intcodeComputer.getInputBus().put(color);
        }

        return paintedPanels;
    }

    @Data
    @AllArgsConstructor
    private class Coordinates {
        private int x;
        private int y;

        public Coordinates copy() {
            return new Coordinates(x, y);
        }
    }
}
