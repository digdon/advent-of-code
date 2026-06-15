package day_22;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day22 {
    
    record Point(int x, int y) {}

    static class StartEnd {
        int start;
        int end;
    }
    
    enum Direction { RIGHT, UP, LEFT, DOWN }
    
    private static final Direction[] DIRECTIONS = Direction.values();
    
    private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("(\\d+)([^0-9]+)?");
    
    public static void main(String[] args) throws IOException {
        // Load in the puzzle input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        Set<Point> wallSet = new HashSet<>();
        Map<Integer, StartEnd> columnData = new HashMap<>();
        Map<Integer, StartEnd> rowData = new HashMap<>();
        
        int locX = 0, locY = 0;
        boolean startSet = false;
        
        for (int y = 0; y < inputLines.size(); y++) {
            String line = inputLines.get(y);
            
            if (line.length() == 0) {
                // Map is done
                break;
            }
            
            char[] chars = line.toCharArray();
            
            for (int x = 0; x < chars.length; x++) {
                if (chars[x] == ' ') {
                    continue;
                } else if (chars[x] == '#') {
                    wallSet.add(new Point(x, y));
                } else if (startSet == false) {
                    locX = x;
                    locY = y;
                    startSet = true;
                }
            }
        }
        
        int dirPos = 0;
        
        System.out.println(String.format("Start position: x=%d, y=%d, facing %s", locX, locY, DIRECTIONS[dirPos]));
        
        // Process the instruction line
        String instructions = inputLines.get(inputLines.size() - 1);
        Matcher matcher = INSTRUCTION_PATTERN.matcher(instructions);
        
        while (matcher.find()) {
            // Move first
            
            // Now turn
            String turnString = matcher.group(2);
            dirPos += (turnString.charAt(0) == 'L' ? -1 : 1);
            
            if (dirPos < 0) {
                dirPos = DIRECTIONS.length - 1;
            } else {
                dirPos %= DIRECTIONS.length;
            }
        }
    }
}
