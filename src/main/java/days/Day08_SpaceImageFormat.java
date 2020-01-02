package days;

import launcher.Executable;

import java.util.ArrayList;
import java.util.List;

public class Day08_SpaceImageFormat implements Executable {
    private static final int IMAGE_HEIGHT = 6;
    private static final int IMAGE_WIDTH = 25;

    @Override
    public Object executePartOne(List<String> input) {
        int[] imageData = parseInput(input);
        List<int[][]> image = parseImage(imageData);
        int[][] layer = findLayerWithFewestZeroes(image);
        return countDigit(1, layer) * countDigit(2, layer);
    }

    @Override
    public Object executePartTwo(List<String> input) {
        int[] imageData = parseInput(input);
        List<int[][]> image = parseImage(imageData);
        int[][] finalImage = flattenImage(image);
        displayImage(finalImage);
        return "Answer is in output.";
    }

    private List<int[][]> parseImage(int[] imageData) {
        List<int[][]> image = new ArrayList<>();

        int layers = imageData.length / (IMAGE_HEIGHT * IMAGE_WIDTH);

        for (int i = 0; i < layers; i++) {
            int[][] imageLayer = new int[IMAGE_HEIGHT][];
            for (int j = 0; j < IMAGE_HEIGHT; j++) {
                imageLayer[j] = new int[IMAGE_WIDTH];
                for (int k = 0; k < IMAGE_WIDTH; k++) {
                    imageLayer[j][k] = imageData[(i * IMAGE_WIDTH * IMAGE_HEIGHT) + ((j * IMAGE_WIDTH) + (k % IMAGE_WIDTH))];
                }
            }

            image.add(imageLayer);
        }

        return image;
    }

    private int[] parseInput(List<String> input) {
        String data = input.get(0);

        return data.chars()
            .map(Character::getNumericValue)
            .toArray();
    }

    private int[][] findLayerWithFewestZeroes(List<int[][]> image) {
        int fewestZeroes = Integer.MAX_VALUE;
        int[][] layerWithFewestZeroes = null;

        for (int[][] layer : image) {
            int zeroes = 0;

            for (int[] row : layer) {
                for (int pixel : row) {
                    if (pixel == 0) {
                        zeroes++;
                    }
                }
            }

            if (zeroes < fewestZeroes) {
                fewestZeroes = zeroes;
                layerWithFewestZeroes = layer;
            }
        }

        return layerWithFewestZeroes;
    }

    private int countDigit(int digit, int[][] layer) {
        int digitCounter = 0;

        for (int[] row : layer) {
            for (int pixel : row) {
                if (pixel == digit) {
                    digitCounter++;
                }
            }
        }

        return digitCounter;
    }

    private int[][] flattenImage(List<int[][]> image) {
        int[][] finalImage = initializeImage();

        for (int i = image.size() - 1; i >= 0; i--) {
            int[][] layer = image.get(i);

            for (int j = 0; j < layer.length; j++) {
                for (int k = 0; k < layer[j].length; k++) {
                    int layerPixel = layer[j][k];

                    if (layerPixel != 2) {
                        finalImage[j][k] = layerPixel;
                    }
                }
            }
        }

        return finalImage;
    }

    private int[][] initializeImage() {
        int[][] image = new int[IMAGE_HEIGHT][];

        for (int i = 0; i < image.length; i++) {
            image[i] = new int[IMAGE_WIDTH];
        }

        return image;
    }

    private void displayImage(int[][] image) {
        char black = '⬛';
        char white = '⬜';

        StringBuilder imageRepresentation = new StringBuilder();

        for (int[] row : image) {
            for (int pixel : row) {
                imageRepresentation.append(pixel == 0 ? black : white);
            }

            imageRepresentation.append("\n");
        }

        System.out.println(imageRepresentation.toString());
    }
}
