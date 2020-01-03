package days;

import computer.IntcodeComputer;
import launcher.Executable;
import lombok.Value;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Day13_CarePackage extends JPanel implements Executable {
    private static final int TILE_SIZE = 30;
    private Map<Coordinates, Integer> gameMap;

    @Override
    public Object executePartOne(List<String> input) {
        return getGameMap(input).values().stream().filter(value -> value == 2).count();
    }

    @Override
    public Object executePartTwo(List<String> input) {
        return playGame(input);
    }

    private Map<Coordinates, Integer> getGameMap(List<String> input) {
        IntcodeComputer intcodeComputer = new IntcodeComputer();

        // 0 is an empty tile. No game object appears in this tile.
        // 1 is a wall tile. Walls are indestructible barriers.
        // 2 is a block tile. Blocks can be broken by the ball.
        // 3 is a horizontal paddle tile. The paddle is indestructible.
        // 4 is a ball tile. The ball moves diagonally and bounces off objects.
        Map<Coordinates, Integer> gameMap = new HashMap<>();

        intcodeComputer.runAsync(input.get(0));

        while (true) {
            Long x = intcodeComputer.getOutputBus().poll(10, TimeUnit.MILLISECONDS);
            Long y = intcodeComputer.getOutputBus().poll(10, TimeUnit.MILLISECONDS);
            Long tileId = intcodeComputer.getOutputBus().poll(10, TimeUnit.MILLISECONDS);

            if (x != null && x == IntcodeComputer.ENDING_SIGNAL) {
                return gameMap;
            }

            gameMap.put(new Coordinates(x.intValue(), y.intValue()), tileId.intValue());
        }
    }

    private int playGame(List<String> input) {
        gameMap = getGameMap(input);
        IntcodeComputer intcodeComputer = new IntcodeComputer();

        JFrame f = new JFrame();
        f.add(this);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        f.setSize(((getMaxX(gameMap) + 2) * TILE_SIZE) - (TILE_SIZE / 2), (getMaxY(gameMap) + 2) * TILE_SIZE);

        String originalInput = input.get(0);
        StringBuilder alteredOutput = new StringBuilder(originalInput);
        alteredOutput.setCharAt(0, '2');

        intcodeComputer.runAsync(alteredOutput.toString());

        int score = 0;

        f.repaint();

        while (true) {
            Long x = intcodeComputer.getOutputBus().poll(5, TimeUnit.MILLISECONDS);
            Long y = intcodeComputer.getOutputBus().poll(5, TimeUnit.MILLISECONDS);
            Long tileId = intcodeComputer.getOutputBus().poll(5, TimeUnit.MILLISECONDS);

            if (x != null && x == IntcodeComputer.ENDING_SIGNAL) {
                return score;
            }

            if (x == null || y == null || tileId == null) {
                intcodeComputer.getInputBus().put(Integer.compare(getBall(gameMap).getX(), getPaddle(gameMap).getX()));
                continue;
            }

            if (x == -1 && y == 0) {
                score = tileId.intValue();
                continue;
            }

            gameMap.replace(new Coordinates(x.intValue(), y.intValue()), tileId.intValue());

            f.repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Map.Entry<Coordinates, Integer> entry : gameMap.entrySet()) {
            int x = entry.getKey().getX();
            int y = entry.getKey().getY();
            int tileId = entry.getValue();

            int scaledX = x * TILE_SIZE;
            int scaledY = y * TILE_SIZE;

            switch (tileId) {
                case 0:
                    continue;
                case 1:
                    g.setColor(Color.BLACK);
                    g.fillRect(scaledX, scaledY, TILE_SIZE, TILE_SIZE);
                    break;
                case 2:
                    g.setColor(Color.PINK);
                    g.fillRect(scaledX, scaledY, TILE_SIZE, TILE_SIZE);
                    break;
                case 3:
                    g.setColor(Color.GREEN);
                    g.fillRect(scaledX, scaledY, TILE_SIZE, TILE_SIZE);
                    break;
                case 4:
                    g.setColor(Color.ORANGE);
                    g.fillOval(scaledX, scaledY, TILE_SIZE, TILE_SIZE);
                    break;
            }
        }
    }

    private Coordinates getPaddle(Map<Coordinates, Integer> gameMap) {
        return gameMap.entrySet().stream().filter(entry -> entry.getValue() == 3).map(Map.Entry::getKey).findFirst().get();
    }

    private Coordinates getBall(Map<Coordinates, Integer> gameMap) {
        return gameMap.entrySet().stream().filter(entry -> entry.getValue() == 4).map(Map.Entry::getKey).findFirst().get();
    }

    private int getMaxX(Map<Coordinates, Integer> gameMap) {
        return gameMap.keySet().stream().mapToInt(Coordinates::getX).max().getAsInt();
    }

    private int getMaxY(Map<Coordinates, Integer> gameMap) {
        return gameMap.keySet().stream().mapToInt(Coordinates::getY).max().getAsInt();
    }

    @Value
    private class Coordinates {
        private int x;
        private int y;
    }
}
