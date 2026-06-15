package day_05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day05 {

    private static Pattern INPUT_PATTERN = Pattern.compile("(\\d+),(\\d+) -> (\\d+),(\\d+)");
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        // Part 1
        long count = drawLines(inputLines, false).values().stream().filter(v -> v > 1).count();
        System.out.println("Part 1: " + count);
        
        // Part 2
        count = drawLines(inputLines, true).values().stream().filter(v -> v > 1).count();
        System.out.println("Part 2: " + count);
    }

    private static Map<String, Integer> drawLines(List<String> inputLines, boolean includePart2) {
        Map<String, Integer> points = new HashMap<>();
        
        for (String line : inputLines) {
            Matcher matcher = INPUT_PATTERN.matcher(line);
            
            if (matcher.matches() == false) {
                continue;
            }
            
            int x1 = Integer.parseInt(matcher.group(1));
            int y1 = Integer.parseInt(matcher.group(2));
            int x2 = Integer.parseInt(matcher.group(3));
            int y2 = Integer.parseInt(matcher.group(4));

            if (x1 == x2) {
                // vertical line
                int yStart = Math.min(y1, y2);
                int yEnd = Math.max(y1, y2);
                
                for (int y = yStart; y <= yEnd; y++) {
                    plotPoint(points, x1, y);
                }
            } else if (y1 == y2) {
                // horizontal line
                int xStart = Math.min(x1, x2);
                int xEnd = Math.max(x1, x2);
                
                for (int x = xStart; x <= xEnd; x++) {
                    plotPoint(points, x, y1);
                }
            } else {
                // diagonal
                if (includePart2) {
                    int xStep = x1 < x2 ? 1 : -1;
                    int yStep = y1 < y2 ? 1 : -1;
                    int steps = Math.abs(x1 - x2);

                    for (int i = 0, x = x1, y = y1; i <= steps; i++, x += xStep, y += yStep) {
                        plotPoint(points, x, y);
                    }
                }
            }
        }
        
        return points;
    }
    
    private static void plotPoint(Map<String, Integer> points, int x, int y) {
        String key = String.format("%d,%d", x, y);
        Integer count = points.get(key);
        
        if (count == null) {
            count = 0;
        }
        
        points.put(key, count + 1);
    }
}
