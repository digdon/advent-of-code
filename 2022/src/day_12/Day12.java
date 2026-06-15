package day_12;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class Day12 {

    public static void main(String[] args) throws IOException {
        // Load in the puzzle input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        // Build grid
        LinkedList<Point> startPointList = new LinkedList<>();
        char[][] grid = new char[inputLines.size()][];
        
        for (int row = 0; row < inputLines.size(); row++) {
            grid[row] = inputLines.get(row).toCharArray();
            
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == 'S') {
                    grid[row][col] = 'a';
                    startPointList.addFirst(new Point(col, row));
                } else if (grid[row][col] == 'a') {
                    // Another starting point
                    startPointList.add(new Point(col, row));
                }
            }
        }
        
        int part1PathLength = 0;
        int part2PathLength = Integer.MAX_VALUE;

        for (int i = 0; i < startPointList.size(); i++) {
            Point start = startPointList.get(i);
            Stack<Point> path = findPath(grid, start);
            
            if (path == null) {
                // No path from this start point
                continue;
            }
            
            int length = path.size();
            
            if (length < part2PathLength) {
                part2PathLength = length;
            }
            
            if (i == 0) {
                part1PathLength = length;
            }
        }
        
        System.out.println("Part 1: " + part1PathLength);
        System.out.println("Part 2: " + part2PathLength);
    }
    
    private static Stack<Point> findPath(char[][] grid, Point start) {
        // Work out the path, via BFS
        Queue<Point> pointQueue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();
        visited.add(start);
        pointQueue.add(start);
        Map<Point, Point> parentMap = new HashMap<>();
        Point found = null;
        
        while (pointQueue.isEmpty() == false) {
            Point point = pointQueue.remove();
            int row = point.y;
            int col = point.x;
            int currElevation = grid[row][col];
            
            if (grid[row][col] == 'E') {
                // Found the target
                found = point;
                break;
            }

            for (int i = 0; i < DIRECTIONS.length; i++) {
                int nextRow = row + DIRECTIONS[i][0];
                int nextCol = col + DIRECTIONS[i][1];

                if (nextRow >= 0 && nextRow < grid.length && nextCol >= 0 && nextCol < grid[nextRow].length) {
                    int nextElevation= grid[nextRow][nextCol];
                    
                    if (nextElevation - currElevation <= 1) {
                        Point nextPoint = new Point(nextCol, nextRow);
                        
                        if (visited.contains(nextPoint) == false) {
                            visited.add(nextPoint);
                            parentMap.put(nextPoint, point);
                            pointQueue.add(nextPoint);
                        }
                    }
                }
            }
        }
        
        if (found == null) {
            return null;
        }
        
        Point current = parentMap.get(found);
        Stack<Point> pathStack = new Stack<>();

        while (current != null) {
            pathStack.push(current);
            current = parentMap.get(current);
        }
        
        return pathStack;
    }
    
    private static final int[][] DIRECTIONS = {
            { -1, 0 },  // up
            { 1, 0 },   // down
            { 0, -1 },  // left
            { 0, 1 }    // right
    };
    
    record Point(int x, int y) {}
}
