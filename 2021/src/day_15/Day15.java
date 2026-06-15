package day_15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class Day15 {

    record Point(int x, int y) implements Comparable<Point> {
        @Override
        public int compareTo(Point that) {
            if (this.y == that.y) {
                return this.x - that.x;
            } else {
                return this.y - that.y;
            }
        }
    }
    
    record Node(Point point, int cost) implements Comparable<Node> {
        @Override
        public int compareTo(Node that) {
            int pointCompare = this.point.compareTo(that.point);
            
            if (pointCompare == 0) {
                return Integer.compare(this.cost, that.cost);
            } else {
                return pointCompare;
            }
        }
    }

    private static final int EXPANSION = 5;
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        // Build grid
        int[][] grid = new int[inputLines.size()][inputLines.get(0).length()];
        
        for (int row = 0; row < inputLines.size(); row++) {
            String line = inputLines.get(row);
            
            for (int col = 0; col < line.length(); col++) {
                grid[row][col] = line.charAt(col) - '0';
            }
        }
        
        // Part 1 stuff
        List<Point> path = dijkstra(grid, new Point(0, 0), new Point(grid[0].length - 1, grid.length - 1));
        int cost = path.stream().skip(1).mapToInt(p -> grid[p.y()][p.x()]).sum();        
        System.out.println("Part 1: " + cost);
        
        // Part 2 stuff
        int[][] biggerGrid = new int[grid.length * EXPANSION][grid[0].length * EXPANSION];

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                for (int j = 0; j < EXPANSION; j++) {
                    for (int i = 0; i < EXPANSION; i++) {
                        int value = (grid[row][col] + i + j);
                        if (value > 9) {
                            value -= 9;
                        }
                        int biggerRow = grid.length * j + row;
                        int biggerCol = grid[row].length * i + col;
                        biggerGrid[biggerRow][biggerCol] = value;
                    }
                }
            }
        }
        
        path = dijkstra(biggerGrid, new Point(0, 0), new Point(biggerGrid[0].length - 1, biggerGrid.length - 1));
        cost = path.stream().skip(1).mapToInt(p -> biggerGrid[p.y()][p.x()]).sum();        
        System.out.println("Part 2: " + cost);
    }

    private static List<Point> dijkstra(int[][] grid, Point start, Point target) {
        Map<Point, Integer> distMap = new HashMap<>();
        Map<Point, Point> prevMap = new HashMap<>();

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(start, 0));
        distMap.put(start, 0);
        
        while (pq.isEmpty() == false) {
            Node current = pq.remove();
            int currentCost = distMap.get(current.point());
            
            if (current.point().equals(target)) {
                break;
            }
            
            for (int i = 0; i < DIRECTIONS.length; i++) {
                int x = current.point().x() + DIRECTIONS[i][0];
                int y = current.point().y() + DIRECTIONS[i][1];
                
                if (y < 0 || y > grid.length - 1 || x < 0 || x > grid[y].length - 1) {
                    // Outside of the grid, skip this one
                    continue;
                }
                
                Point neighbour = new Point(x, y);
                int altCost = currentCost + grid[y][x]; // The cost of the path thus far, plus the cost of the neighbour
                Integer neighbourCost = distMap.get(neighbour);
                
                if (neighbourCost == null) {
                    neighbourCost = Integer.MAX_VALUE;
                }
                
                if (altCost < neighbourCost) {
                    // New alternate cost/path is less than the recorded one, so replace with the new one
                    distMap.put(neighbour, altCost);
                    prevMap.put(neighbour, current.point());
                    pq.add(new Node(neighbour, altCost));
                }
            }
        }

        LinkedList<Point> path = new LinkedList<>();
        
        for (Point point = target; point != null; point = prevMap.get(point)) {
            path.addFirst(point);
        }
        
        return path;
    }
    
    private static final int[][] DIRECTIONS = {
            { 0, -1 },
            { 0, 1 },
            { -1, 0 },
            { 1, 0 }
    };

    @SuppressWarnings("unused")
    private static void displayPath(int[][] grid, List<Point> path) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                Point currentPoint = new Point(col, row);
                
                if (path.contains(currentPoint)) {
                    System.out.print("\033[31m" + grid[row][col] + "\033[0m");
                } else {
                    System.out.print(grid[row][col]);
                }
            }
            
            System.out.println();
        }
    }
}
