package day_09;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Day09 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        // Build grid
        int[][] grid = new int[inputLines.size()][inputLines.get(0).length()];
        boolean[][] basin = new boolean[inputLines.size()][inputLines.get(0).length()];
        
        for (int row = 0; row < inputLines.size(); row++) {
            String line = inputLines.get(row);
            
            for (int col = 0; col < line.length(); col++) {
                grid[row][col] = line.charAt(col) - '0';
            }
        }

        // Find the low points and calculate total risk
        int totalRisk = 0;
        List<Integer> basinSizes = new ArrayList<>();
        
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                // For each grid point, examine all other around it to see if they're higher
                if (isLowPoint(grid, row, col)) {
                    totalRisk += (grid[row][col] + 1);
                    
                    int basinSize = calculateBasin(grid, basin, row, col);
                    basinSizes.add(basinSize);
                    System.out.println(basinSize);
                }
            }
        }

        displayGrid(grid, basin);

        // Part 1
        System.out.println("Part 1: " + totalRisk);

        // Part 2
        basinSizes.sort((v1, v2) -> -(v1.compareTo(v2)));
        int basinMult = 1;
        
        for (int i = 0; i < Math.min(3, basinSizes.size()); i++) {
            basinMult *= basinSizes.get(i);
        }
        
        System.out.println("Part 2: " + basinMult);
    }
    
    private static boolean isLowPoint(int[][] grid, int row, int col) {
        int value = grid[row][col];

        if (value == 9) {
            return false;
        }
        
        // Check above
        if (row - 1 >= 0 && grid[row - 1][col] < value) {
            // value above is lower - this can't be low point
            return false;
        }

        // Check below
        if (row + 1 < grid.length && grid[row + 1][col] < value) {
            // value below is lower - this can't be low point
            return false;
        }

        // Check left
        if (col - 1 >= 0 && grid[row][col - 1] < value) {
            // value to left is lower - this can't be low point
            return false;
        }
        
        // Check right
        if (col + 1 < grid[row].length && grid[row][col + 1] < value) {
            // value to right is lower - this can't be low point
            return false;
        }
        
        return true;
    }
    
    private static int calculateBasin(int[][] grid, boolean[][] basin, int startRow, int startCol) {
        int size = 0;
        Deque<Integer> rowQueue = new LinkedList<>();
        Deque<Integer> colQueue = new LinkedList<>();
        rowQueue.add(startRow);
        colQueue.add(startCol);

        while (rowQueue.isEmpty() == false) {
            int row = rowQueue.remove();
            int col = colQueue.remove();
            int value = grid[row][col];
            
            if (basin[row][col] == true) {
                // already seen - skip
                continue;
            }

            if (grid[row][col] == 9) {
                // edge of basin - we don't count these
                continue;
            }
            
            basin[row][col] = true;
            size++;
            
            if (row - 1 >= 0 && grid[row - 1][col] >= value) {
                rowQueue.add(row - 1);
                colQueue.add(col);
            }
            
            if (row + 1 < grid.length && grid[row + 1][col] >= value) {
                rowQueue.add(row + 1);
                colQueue.add(col);
            }
            
            if (col - 1 >= 0 && grid[row][col - 1] >= value) {
                rowQueue.add(row);
                colQueue.add(col - 1);
            }
            
            if (col + 1 < grid[row].length && grid[row][col + 1] >= value) {
                rowQueue.add(row);
                colQueue.add(col + 1);
            }
        }
        
        return size;
    }
    
    private static void displayGrid(int[][]grid, boolean[][] basin) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (basin[row][col]) {
                    System.out.print("\033[31m" + grid[row][col] + "\033[0m");
                } else {
                    System.out.print(grid[row][col]);
                }
            }
            
            System.out.println();
        }
    }
}
