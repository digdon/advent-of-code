package day_08;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day08 {

    public static void main(String[] args) throws IOException {
        // Load in the puzzle input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        // Build the grid
        int[][] grid = new int[inputLines.size()][];

        for (int i = 0; i < inputLines.size(); i++) {
            String line = inputLines.get(i);
            grid[i] = line.chars().map(n -> n - '0').toArray();
        }

        int treeCount = (grid.length * 2) + ((grid[0].length - 2) * 2); // Count all of the trees along the edge of the grid
        
        Set<Point> visibleTrees = new HashSet<>();

        // Count visible trees for inner rows (ie, not top and bottom)
        for (int row = 1; row < grid.length - 1; row++) {
            List<Point> items = new ArrayList<>();

            for (int col = 0; col < grid[row].length; col++) {
                items.add(new Point(row, col));
            }
            
            // First do it from one end
            findVisibleTrees(grid, items, visibleTrees);

            // now do it from the other end
            Collections.reverse(items);
            findVisibleTrees(grid, items, visibleTrees);
        }

        // Count visible trees for inner columns (ie, not far left and far right)
        for (int col = 1; col < grid[0].length - 1; col++) {
            List<Point> items = new ArrayList<>();

            for (int row = 0; row < grid.length; row++) {
                items.add(new Point(row, col));
            }
            
            // First do it from one end
            findVisibleTrees(grid, items, visibleTrees);

            // now do it from the other end
            Collections.reverse(items);
            findVisibleTrees(grid, items, visibleTrees);
        }

        treeCount += visibleTrees.size();
        
        System.out.println("Part 1: " + treeCount);
        
        // Part 2 stuff
        int maxScore = 0;
        
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                int score = scoreTrees(grid, row, col);
                
                if (score > maxScore) {
                    maxScore = score;
                }
            }
        }
        
        System.out.println("Part 2: " + maxScore);
    }

    private static void findVisibleTrees(int[][] grid, List<Point> trees, Set<Point> visibleTrees) {
        // Grab the height of the first tree (edge)
        Point tree = trees.get(0);
        int tallest = grid[tree.row()][tree.col()];

        for (int i = 1; i < trees.size() - 1; i++) {
            if (tallest == 9) {
                // Cannot see anything past this tree, we're done
                break;
            }

            tree = trees.get(i);
            int height = grid[tree.row()][tree.col()];
            
            if (height > tallest) {
                visibleTrees.add(tree);
                tallest = height;
            }
        }
    }
    
    record Point(int row, int col) {}
    
    private static final int[][] DIRECTIONS = {
            { 0, 1 },   // left to right
            { 1, 0 },   // top to bottom
            { 0, -1 },  // right to left
            { -1, 0 }   // bottom to top
    };
    
    public static int scoreTrees(int[][] grid, int startRow, int startCol) {
        int score = 1;
        
        for (int i = 0; i < DIRECTIONS.length; i++) {
            int startHeight = grid[startRow][startCol];
            int row = startRow + DIRECTIONS[i][0];
            int col = startCol + DIRECTIONS[i][1];
            int count = 0;
            
            while (row >= 0 && row < grid.length && col >= 0 && col < grid[row].length) {
                int height = grid[row][col];

                if (height < startHeight) {
                    count++;
                } else if (height >= startHeight) {
                    count++;
                    break;
                }
                
                row += DIRECTIONS[i][0];
                col += DIRECTIONS[i][1];
            }

            score *= count;
        }

        return score;
    }
}
