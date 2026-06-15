package day_09;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day09 {

    public static void main(String[] args) throws IOException {
        // Load in the puzzle input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        int tailVisits = moveTheRope(2, inputLines);
        System.out.println("Part 1: " + tailVisits);
        
        tailVisits = moveTheRope(10, inputLines);
        System.out.println("Part 2 : " + tailVisits);
    }

    private static int moveTheRope(int knotCount, List<String> inputLines) {
        Point[] knots = new Point[knotCount];
        
        for (int i = 0; i < knotCount; i++) {
            knots[i] = new Point(0, 0);
        }

        Set<Point> tailVisits = new HashSet<>();
        tailVisits.add(knots[knotCount - 1].getLocation());

        for (String line : inputLines) {
            char direction = line.charAt(0);
            int numSteps = Integer.valueOf(line.substring(2));
            int[] dirChange = DIRECTION_MAP.get(direction);
            
            for (int step = 0; step < numSteps; step++) {
                
                // Move the first knot
                knots[0].move(knots[0].x + dirChange[1], knots[0].y + dirChange[0]);
                
                // Now check each remaining knot, to see if it needs to move
                for (int i = 1; i < knotCount; i++) {
                    int xDiff = knots[i - 1].x - knots[i].x;
                    int yDiff = knots[i - 1].y - knots[i].y;
                    int xDiffAbs = Math.abs(xDiff);
                    int yDiffAbs = Math.abs(yDiff);
                    
                    if (xDiffAbs > 1 || yDiffAbs > 1) {
                        // Knot needs to move
                        int xMove = 0, yMove = 0;
                        
                        if (((xDiffAbs + yDiffAbs) > 2)) { // && (xDiff != -1 && yDiff != 1)) {
                            xMove = xDiff - (xDiff / 2);
                            yMove = yDiff - (yDiff / 2);
                        } else {
                            xMove = xDiff / 2;
                            yMove = yDiff / 2;
                        }

                        knots[i].move(knots[i].x + xMove, knots[i].y + yMove);
                        
                        if (i == knotCount - 1) {
                            tailVisits.add(knots[i].getLocation());
                        }
                    }
                }
            }
        }

        displayLocations(tailVisits);
        return tailVisits.size();
    }

    private static void displayLocations(Set<Point> data) {
        int minX = 0, maxX = 0, minY = 0, maxY = 0;
        
        for (Point point : data) {
            minX = Math.min(minX, point.x);
            maxX = Math.max(maxX, point.x);
            minY = Math.min(minY, point.y);
            maxY = Math.max(maxY, point.y);
        }
        
        char[][] grid = new char[maxY - minY + 1][maxX - minX + 1];
        
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                grid[row][col] = '.';
            }
        }
        
        for (Point point : data) {
            int x = point.x - minX;
            int y = point.y - minY;
            grid[y][x] = '#';
        }
        
        grid[0 - minY][0 - minX] = 's';
        
        for (int row = grid.length - 1; row >= 0; row--) {
            for (int col = 0; col < grid[row].length; col++) {
                System.out.print(grid[row][col]);
            }
            System.out.println();
        }
    }
    
    private static Map<Character, int[]> DIRECTION_MAP = Map.of(
            'U', new int[] { 1, 0 },
            'D', new int[] { -1, 0 },
            'L', new int[] { 0, -1 },
            'R', new int[] { 0, 1 }
            );
}
