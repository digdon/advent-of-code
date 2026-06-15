package day_13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day13 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        // Process the input
        // Start by looking for point data
        Set<Point> paper = new HashSet<>();
        
        int lineCount = 0;
        
        while (lineCount < inputLines.size()) {
            String line = inputLines.get(lineCount++).strip();
            
            if (line.length() == 0) {
                // The end of the point data
                break;
            }
            
            String[] parts = line.split(",");
            paper.add(new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
        }
        
        // Part 1 - apply only the first fold
        String foldInstruction = inputLines.get(lineCount++);
        paper = foldPaper(paper, foldInstruction);
        
        System.out.println("Part 1: " + paper.size());
        
        // Now parse out the fold instructions
        while (lineCount < inputLines.size()) {
            foldInstruction = inputLines.get(lineCount++);
            paper = foldPaper(paper, foldInstruction);
        }

        System.out.println("Part 2: ");
        displayPaper(paper);
    }
    
    private static Set<Point> foldPaper(Set<Point> paper, String instruction) {
        String[] parts = instruction.strip().split("\\s+");
        String[] foldParts = parts[parts.length - 1].split("=");
        int foldLocation = Integer.parseInt(foldParts[1]);
        int xFold = -1;
        int yFold = -1;
        
        if (foldParts[0].equals("x")) {
            xFold = foldLocation;
        } else {
            yFold = foldLocation;
        }
        
        Set<Point> newPaper = new HashSet<>();
        
        for (Point point : paper) {
            int x = point.x;
            int y = point.y;
            
            if (xFold != -1) {
                if (x > xFold) {
                    x -= ((x - xFold) * 2);
                }
            }
            
            if (yFold != -1) {
                if (y > yFold) {
                    y -= ((y - yFold) * 2);
                }
            }
            
            newPaper.add(new Point(x, y));
        }
        
        return newPaper;
    }

    private static void displayPaper(Set<Point> paper) {
        int maxX = 0;
        int maxY = 0;
        
        // Determine size of paper
        for (Point point : paper) {
            if (point.x > maxX) {
                maxX = point.x;
            }
            
            if (point.y > maxY) {
                maxY = point.y;
            }
        }
        
        boolean[][] grid = new boolean[maxY + 1][maxX + 1];
        
        for (Point point : paper) {
            grid[point.y][point.x] = true;
        }
        
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                System.out.print(grid[row][col] ? '#' : '.');
            }
            
            System.out.println();
        }
        
        System.out.println();
    }
    
    static class Point {
        int x;
        int y;
        
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            
            if ((o2 instanceof Point) == false) {
                return false;
            }
            
            Point point2 = (Point)o2;
            
            return (this.x == point2.x && this.y == point2.y);
        }
        
        @Override
        public int hashCode() {
            int tmp = (y + ((x + 1) / 2));
            return x + (tmp * tmp);
        }
        
        @Override
        public String toString() {
            return String.format("%d,%d", x, y);
        }
    }
}
