package day_11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Day11 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        // Build energy grid
        int[][] energyGrid = new int[inputLines.size()][inputLines.get(0).length()];
        
        for (int row = 0; row < inputLines.size(); row++) {
            String line = inputLines.get(row);
            
            for (int col = 0; col < line.length(); col++) {
                energyGrid[row][col] = line.charAt(col) - '0';
            }
        }

        // Part 1 - iterate grid 100 times
        int flashCount = 0;
        int part1Count = 0;
        int part2Step = 0;
        int step = 1;

        while (part1Count == 0 || part2Step == 0) {
            int iterationCount = increaseEnergy(energyGrid);
            
            if (iterationCount == (energyGrid.length * energyGrid[0].length)) {
                part2Step = step;
            }
            
            flashCount += iterationCount;
            
            if (step == 100) {
                part1Count = flashCount;
            }
            
            step++;
        }

        displayGrid(energyGrid);
        System.out.println();
        System.out.println("Part 1: " + part1Count);
        System.out.println("Part 2: " + part2Step);
    }
    
    private static int increaseEnergy(int[][] grid) {
        Deque<Integer> rowQueue = new LinkedList<>();
        Deque<Integer> colQueue = new LinkedList<>();
        
        // Increase the energy of each octopus by 1
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                grid[row][col]++;
                
                if (grid[row][col] > 9) {
                    rowQueue.add(row);
                    colQueue.add(col);
                }
            }
        }
        
        while (rowQueue.isEmpty() == false) {
            int row = rowQueue.remove();
            int col = colQueue.remove();
            
            for (int i = 0; i < DIRECTIONS.length; i++) {
                int newRow = row + DIRECTIONS[i][0];
                int newCol = col + DIRECTIONS[i][1];
                
                if (newRow >= 0 && newRow < grid.length && newCol >= 0 && newCol < grid[newRow].length) {
                    if (grid[newRow][newCol] <= 9) {
                        grid[newRow][newCol]++;
                        
                        if (grid[newRow][newCol] > 9) {
                            // This octopus has now flashed too
                            rowQueue.add(newRow);
                            colQueue.add(newCol);
                        }
                    }
                }
            }
        }

        // Reset flashed octopuses back to 0
        int flashCount = 0;

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] > 9) {
                    grid[row][col] = 0;
                    flashCount++;
                }
            }
        }
        
        return flashCount;
    }
    
    private static final int[][] DIRECTIONS = {
        { -1, -1 },
        { -1, 0 },
        { -1, 1 },
        { 0, -1 },
        { 0, 1 },
        { 1, -1 },
        { 1, 0 },
        { 1, 1 }
    };
    
    private static void displayGrid(int[][] grid) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == 0) {
                    System.out.print("\033[31m0\033[0m");
                } else {
                    System.out.print(grid[row][col]);
                }
            }
            
            System.out.println();
        }
    }
}
