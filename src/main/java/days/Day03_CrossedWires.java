package days;

import launcher.ChallengeHelper;
import launcher.Executable;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.*;

public class Day03_CrossedWires implements Executable {
    @Override
    public String executePartOne() {
        List<String> input = ChallengeHelper.readInputData(3);
        Set<Point> firstWire = drawWire(input.get(0));
        Set<Point> secondWire = drawWire(input.get(1));
        return String.valueOf(findClosestIntersection(firstWire, secondWire));
    }

    @Override
    public String executePartTwo() {
        List<String> input = ChallengeHelper.readInputData(3);
        Set<Point> firstWire = drawWire(input.get(0));
        Set<Point> secondWire = drawWire(input.get(1));
        return String.valueOf(findLowestStepIntersection(firstWire, secondWire));
    }

    private Set<Point> drawWire(String input) {
        Set<Point> wire = new HashSet<>();
        String[] instructions = input.split(",");

        int x = 0;
        int y = 0;
        int stepCounter = 1;

        for (String instruction : instructions) {
            char direction = instruction.charAt(0);
            int distance = Integer.parseInt(instruction.substring(1));

            for (int i = 0; i < distance; i++) {
                switch (direction) {
                    case 'R':
                        x++;
                        break;
                    case 'L':
                        x--;
                        break;
                    case 'D':
                        y++;
                        break;
                    case 'U':
                        y--;
                        break;
                }

                wire.add(new Point(x, y, stepCounter++));
            }
        }

        return wire;
    }

    private int findClosestIntersection(Set<Point> firstWire, Set<Point> secondWire) {
        Point center = new Point(0, 0, 0);
        int minDistance = Integer.MAX_VALUE;

        firstWire.retainAll(secondWire);

        for (Point point : firstWire) {
            int distance = center.distanceTo(point);

            if (distance < minDistance) {
                minDistance = distance;
            }
        }

        return minDistance;
    }

    private int findLowestStepIntersection(Set<Point> firstWire, Set<Point> secondWire) {
        int minSteps = Integer.MAX_VALUE;
        Map<Point, Point> secondWireMap = toMap(secondWire);

        for (Point firstPoint : firstWire) {
            if (secondWire.contains(firstPoint)) {
                Point secondPoint = secondWireMap.get(firstPoint);

                int totalSteps = firstPoint.getSteps() + secondPoint.getSteps();

                if (totalSteps < minSteps) {
                    minSteps = totalSteps;
                }
            }
        }

        return minSteps;
    }

    private Map<Point, Point> toMap(Set<Point> points) {
        Map<Point, Point> pointMap = new HashMap<>();

        for (Point point : points) {
            pointMap.put(point, point);
        }

        return pointMap;
    }

    @Value
    private final class Point {
        int x;
        int y;

        @EqualsAndHashCode.Exclude
        int steps;

        public int distanceTo(Point otherPoint) {
            return Math.abs(otherPoint.getX() - getX()) + Math.abs(otherPoint.getY() - getY());
        }
    }
}
