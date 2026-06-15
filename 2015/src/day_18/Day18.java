package day_18;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class Day18 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        // Part 1
        long startTime = System.currentTimeMillis();
        int part1Count = processGrid(inputLines, false);
        System.out.println(String.format("Part 1: %d (%d ms)", part1Count, System.currentTimeMillis() - startTime));
        
        // Part 2
        startTime = System.currentTimeMillis();
        int part2Count = processGrid(inputLines, true);
        System.out.println(String.format("Part 2: %d (%d ms)", part2Count, System.currentTimeMillis() - startTime));
        
    }
    
    private static int processGrid(List<String> inputLines, boolean permanentCorners) {
        // Build the initial grid
        boolean[][] grid = new boolean[inputLines.size()][inputLines.get(0).length()];
        
        for (int i = 0; i < inputLines.size(); i++) {
            String line = inputLines.get(i);
            
            for (int j = 0; j < line.length(); j++) {
                grid[i][j] = line.charAt(j) == '#';
            }
        }

        // Run the steps
        for (int steps = 0; steps < 100; steps++) {
            if (permanentCorners) {
                grid[0][0] = true;
                grid[0][grid[0].length - 1] = true;
                grid[grid.length - 1][0] = true;
                grid[grid.length - 1][grid[0].length - 1] = true;
            }
            
            boolean[][] newGrid = new boolean[grid.length][grid[0].length];

            // Examine each cell in the grid
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[y].length; x++) {
                    // Count number of lit neighbours
                    int litNeighbours = 0;
                    
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dx = -1; dx <= 1; dx++) {
                            if (dx == 0 && dy == 0) {
                                continue;
                            }
                            
                            int nx = x + dx;
                            int ny = y + dy;
                            
                            if (nx >= 0 && nx < grid[y].length && ny >= 0 && ny < grid.length) {
                                if (grid[ny][nx]) {
                                    litNeighbours++;
                                }
                            }
                        }
                    }
                    
                    if (grid[y][x]) {
                        // Light is currently on - does it stay that way?
                        newGrid[y][x] = (litNeighbours == 2 || litNeighbours == 3);
                    } else {
                        // Light is currently off - does it turn on?
                        newGrid[y][x] = (litNeighbours == 3);
                    }
                }
            }
            
            grid = newGrid;
        }

        if (permanentCorners) {
            grid[0][0] = true;
            grid[0][grid[0].length - 1] = true;
            grid[grid.length - 1][0] = true;
            grid[grid.length - 1][grid[0].length - 1] = true;
        }

        int finalOnCount = 0;
        
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x]) {
                    finalOnCount++;
                }
            }
        }

        return finalOnCount;
    }
}
