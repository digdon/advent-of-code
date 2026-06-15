package day_25;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class Day25 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        // Build intial grid
        char[][] grid = new char[inputLines.size()][];
        
        for (int row = 0; row < inputLines.size(); row++) {
            String line = inputLines.get(row);
            grid[row] = new char[line.length()];
            
            for (int col = 0; col < line.length(); col++) {
                char c = line.charAt(col);
                grid[row][col] = c == '.' ? 0 : c;
            }
        }

        int stepCount = 0;
        char[][] prevGrid = null;
        
        while (true) {
            stepCount++;
            
            if (stepCount % 10 == 0) {
                System.out.println(stepCount);
            }
            
            prevGrid = grid;
            grid = moveCucumbers(grid);

            if (stoppedMoving(prevGrid, grid)) {
                break;
            }
        }
        
        System.out.println("Part 1: " + stepCount);
    }
    
    private static char[][] moveCucumbers(char[][] grid) {
        char[][] newGrid = new char[grid.length][grid[0].length];
        boolean[][] moved = new boolean[grid.length][grid[0].length];
        
        // Move all of the east-facing herd first
        for (int row = 0; row < grid.length; row++) {
            for (int col = grid[row].length - 1; col >= 0; col--) {
                if (grid[row][col] == '>') {
                    // This is what we want to try to move
                    int nextCol = ((col + 1) < grid[row].length) ? col + 1 : 0;
                    
                    if (grid[row][nextCol] == 0 ) {
                        // The next spot is empty, so this cucumber can move
                        newGrid[row][nextCol] = '>';
                        moved[row][col] = true;
                    } else {
                        newGrid[row][col] = grid[row][col];
                    }
                }
            }
        }
        
        // Move all of the south-facing herd last
        for (int row = grid.length - 1; row >= 0; row--) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == 'v') {
                    // This is what we want to try to move
                    int nextRow = ((row + 1) < grid.length) ? row + 1 : 0;
                    
                    if ((grid[nextRow][col] == 0 || moved[nextRow][col]) && newGrid[nextRow][col] == 0) {
                        // The next spot is empty, so this cucumber can move
                        newGrid[nextRow][col] = 'v';
                    } else {
                        newGrid[row][col] = grid[row][col];
                    }
                }
            }
        }
        
        return newGrid;
    }

    private static boolean stoppedMoving(char[][] prevGrid, char[][] newGrid) {
        for (int row = 0; row < prevGrid.length; row++) {
            for (int col = 0; col < prevGrid[row].length; col++) {
                if (prevGrid[row][col] != newGrid[row][col]) {
                    return false;
                }
            }
        }
        
        return true;
    }
}
