package day_23;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class Day23 {

    public static void main(String[] args) throws IOException {
        // Load in the puzzle input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        // Build the starting 'grid'
        Set<Point> elfLocations = new HashSet<>();
        
        for (int y = 0; y < inputLines.size(); y++) {
            char[] chars = inputLines.get(y).toCharArray();
            
            for (int x = 0; x < chars.length; x ++) {
                if (chars[x] == '#') {
                    elfLocations.add(new Point(x, y));
                }
            }
        }
        
        LinkedList<String> considerationList = new LinkedList<>();
        considerationList.add("N");
        considerationList.add("S");
        considerationList.add("W");
        considerationList.add("E");
        
        int round = 0;
        
        while (true) {
            round++;
            
            // First half - movement proposals
            Map<Point, List<Point>> moveProposals = new HashMap<>();
            
            for (Point elf : elfLocations) {
                String proposedDir = null;
                boolean noNeighbours = true;
                
                for (String consider : considerationList) {
                    boolean foundInDirection = false;
                    
                    for (String dir : DIRECTION_CATEGORIES.get(consider)) {
                        int[] directions = DIRECTION_MAP.get(dir);
                        int nearX = elf.x() + directions[0];
                        int nearY = elf.y() + directions[1];
                        Point tempPoint = new Point(nearX, nearY);
                        boolean found = elfLocations.contains(tempPoint);
                        
                        if (found) {
                            foundInDirection = true;
                            noNeighbours = false;
                            break;
                        }
                    }
                    
                    if (foundInDirection == false && proposedDir == null) {
                        // No neighbours in the direction we checked, so it's a possible move.
                        // However, while it's possible to move in multiple directions, we only record the first one that's possible.
                        // We need to do it this way because we have to check all directions, to see if the elf has zero neighbours.
                        proposedDir = consider;
                    }
                }

                // Propose a move
                Point newLocation = null;
                
                if (proposedDir == null || noNeighbours) {
                    // Elf is not moving because it has neighbours in all 4 major areas (N, E, S, W) or it has no neighbours at all,
                    // so new location is essentially the old location (we're doing it this way so that the HashSet contents are
                    // set/updated correctly)
                    newLocation = elf;
                } else {
                    int[] directions = DIRECTION_MAP.get(proposedDir);
                    newLocation = new Point(elf.x() + directions[0], elf.y() + directions[1]);
                }
                
                List<Point> elfList = moveProposals.get(newLocation);

                if (elfList == null) {
                    elfList = new ArrayList<>();
                    moveProposals.put(newLocation, elfList);
                }

                elfList.add(elf);
            }
    
            // Part 2 stuff
            boolean same = true;
            
            for (Entry<Point, List<Point>> entry : moveProposals.entrySet()) {
                if (entry.getValue().size() > 1
                            || entry.getKey().equals(entry.getValue().get(0)) == false) {
                    // An elf is trying to move
                    same = false;
                    break;
                }
            }
            
            if (same) {
                // No elves are changing positions
                System.out.println("Part 2: " + round);
                break;
            }
            
            // Second half - move the elves
            Set<Point> newLocations = new HashSet<>();
            
            for (Entry<Point, List<Point>> entry : moveProposals.entrySet()) {
                if (entry.getValue().size() == 1) {
                    // Only one elf is proposing this location, so make the move
                    newLocations.add(new Point(entry.getKey().x(), entry.getKey().y()));
                } else {
                    for (Point elf : entry.getValue()) {
                        newLocations.add(elf);
                    }
                }
            }
            
            elfLocations = newLocations;
            
            // Shift the consideration order
            considerationList.addLast(considerationList.remove());
            
//            System.out.println("=== Round " + round + " ===");
//            displayGrid(elfLocations);
            
            if (round == 10) {
                // Part 1 stuff
                // First we work out the grid size
                int minY = Integer.MAX_VALUE, maxY= 0;
                int minX = Integer.MAX_VALUE, maxX = 0;
                
                for (Point elf : elfLocations) {
                    minY = Math.min(elf.y(), minY);
                    maxY = Math.max(elf.y(), maxY);
                    minX = Math.min(elf.x(), minX);
                    maxX = Math.max(elf.x(), maxX);
                }

                int result = (maxY - minY + 1) * (maxX - minX + 1) - elfLocations.size();
                System.out.println("Part 1: " + result);
            }
        }
    }
    
    @SuppressWarnings("unused")
    private static void displayGrid(Set<Point> elfLocations) {
        // First we work out the grid size
        int minY = Integer.MAX_VALUE, maxY= 0;
        int minX = Integer.MAX_VALUE, maxX = 0;
        
        for (Point elf : elfLocations) {
            minY = Math.min(elf.y(), minY);
            maxY = Math.max(elf.y(), maxY);
            minX = Math.min(elf.x(), minX);
            maxX = Math.max(elf.x(), maxX);
        }

        // Now we build the grid
        char[][] grid = new char[maxY - minY + 1][maxX - minX + 1];
        
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                grid[y][x] = '.';
            }
        }

        // Fill in the elf locations
        for (Point elf : elfLocations) {
            int x = elf.x() - minX;
            int y = elf.y() - minY;
            grid[y][x] = '#';
        }

        // Print it out
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                System.out.print(grid[y][x]);
            }
            
            System.out.println();
        }
    }

    private static final Map<String, List<String>> DIRECTION_CATEGORIES = Map.of(
            "N", List.of("N", "NE", "NW"),
            "S", List.of("S", "SE", "SW"),
            "W", List.of("W", "NW", "SW"),
            "E", List.of("E", "NE", "SE")
        );
    
    private static final Map<String, int[]> DIRECTION_MAP = Map.of(
            "N",  new int[] { 0, -1 },
            "NE", new int[] { 1, -1 },
            "E",  new int[] { 1, 0 },
            "SE", new int[] { 1, 1 },
            "S",  new int[] { 0, 1 },
            "SW", new int[] { -1, 1 },
            "W",  new int[] { -1, 0 },
            "NW", new int[] { -1, -1 }
        );
    
    record Point(int x, int y) {}
}
