package day_11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Day11 {

    public static void main(String[] args) {
        List<String> gridList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        
        try {
            while ((inputLine = reader.readLine()) != null) {
                gridList.add(inputLine);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        
        // Convert to 2D array
        char[][] grid = new char[gridList.size()][gridList.get(0).length()];
        
        for (int row = 0; row < grid.length; row++) {
            String rowData = gridList.get(row);
            
            for (int col = 0; col < grid[row].length; col++) {
                grid[row][col] = rowData.charAt(col);
            }
        }
        
        // Part one
        System.out.println("Part one: " + workTheGrid(grid, true, 4));
        
        // Part two
        System.out.println("Part two: " + workTheGrid(grid, false, 5));
    }
    
    private static int workTheGrid(char[][] grid, boolean adjacentOnly, int tolerance) {
        int stableIterCount = 0;
        int seatCount = 0;
        
        while (stableIterCount < 3) {
            grid = applySeatingRules(grid, adjacentOnly, tolerance);
            int tempSeatCount = countSeats(grid);
            
            if (tempSeatCount == seatCount) {
                stableIterCount++;
            } else {
                seatCount = tempSeatCount;
                stableIterCount = 0;
            }
        }

        return seatCount;
    }

    private static char[][] applySeatingRules(char[][] inputGrid, boolean adjacentOnly, int tolerance) {
        char[][] outputGrid = new char[inputGrid.length][inputGrid[0].length];
        
        for (int row = 0; row < inputGrid.length; row++) {
            for (int col = 0; col < inputGrid[row].length; col++) {
                char current = inputGrid[row][col];

                if (current == 'L' && willBeOccupied(inputGrid, row, col, adjacentOnly)) {
                    current = '#';
                } else if (current == '#' && willBeEmpty(inputGrid, row, col, adjacentOnly, tolerance)) {
                    current = 'L';
                }
                
                outputGrid[row][col] = current;
            }
        }
        
        return outputGrid;
    }

    private static final int[][] DIRECTIONS = new int[][] {
        { -1, -1 },
        { -1, 0 },
        { -1, 1 },
        { 0, -1 },
        { 0, 1 },
        { 1, -1 },
        { 1, 0 },
        { 1, 1 }
    };
        
    private static boolean willBeOccupied(char[][] grid, int row, int col, boolean adjacentOnly) {
        for (int i = 0; i < DIRECTIONS.length; i++) {
            int y = row + DIRECTIONS[i][0];
            int x = col + DIRECTIONS[i][1];

            while (y >= 0 && y < grid.length && x >= 0 && x < grid[y].length) {
                if (grid[y][x] == '#') {
                    // First visible seat is occupied
                    return false;
                } else if (grid[y][x] == 'L') {
                    // First visible seat is empty
                    break;
                }
                
                if (adjacentOnly) {
                    break;
                } else {
                    y += DIRECTIONS[i][0];
                    x += DIRECTIONS[i][1];
                }
            }
        }
        
        return true;
    }

    private static boolean willBeEmpty(char[][] grid, int row, int col, boolean adjacentOnly, int tolerance) {
        int occupiedCount = 0;
        
        for (int i = 0; i < DIRECTIONS.length; i++) {
            int y = row + DIRECTIONS[i][0];
            int x = col + DIRECTIONS[i][1];
            
            while (y >= 0 && y < grid.length && x >= 0 && x < grid[y].length) {
                if (grid[y][x] == '#') {
                    // First visible seat is occupied
                    occupiedCount++;
                    break;
                } else if (grid[y][x] == 'L') {
                    // First visible seat is empty
                    break;
                }
                
                if (adjacentOnly) {
                    break;
                } else {
                    y += DIRECTIONS[i][0];
                    x += DIRECTIONS[i][1];
                }
            }
        }
        
        return occupiedCount >= tolerance;
    }
    
    private static int countSeats(char[][] grid) {
        int seatCount = 0;
        
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == '#') {
                    seatCount++;
                }
            }
        }
        
        return seatCount;
    }
    
    private static void displayGrid(char[][] grid) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                System.out.print(grid[row][col]);
            }
            
            System.out.println();
        }
        
        System.out.println();
    }
}
