package day_06;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day06 {
    
    enum Mode { ON, TOGGLE, OFF }

    private static final Pattern RANGE_PATTERN = Pattern.compile("(\\d+,\\d+)\\s+through\\s+(\\d+,\\d+)");
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        boolean[][] onOffGrid = new boolean[1000][1000];
        int[][] brightnessGrid = new int[1000][1000];
        
        for (String input : inputLines) {
            // What light mode are we using?
            Mode lightMode = Mode.TOGGLE;
            
            if (input.startsWith("turn on")) {
                lightMode = Mode.ON;
            } else if (input.startsWith("turn off")) {
                lightMode = Mode.OFF;
            }
            
            // What range are we using?
            String startPoint = null;
            String endPoint = null; 
            Matcher matcher = RANGE_PATTERN.matcher(input);
            
            if (matcher.find()) {
                startPoint = matcher.group(1);
                endPoint = matcher.group(2);
            }
            
            processInstructions(onOffGrid, brightnessGrid, lightMode, startPoint, endPoint);
        }

        int onCount = 0;
        int totalBrightness = 0;
        
        for (int y = 0; y < onOffGrid.length; y++) {
            for (int x = 0; x < onOffGrid[y].length; x++) {
                if (onOffGrid[y][x] == true) {
                    onCount++;
                }
                
                totalBrightness += brightnessGrid[y][x];
            }
        }
        
        System.out.println("Part one: " + onCount);
        System.out.println("Part two: " + totalBrightness);
    }
    
    private static void processInstructions(boolean[][] onOffGrid, int[][] brightnessGrid, Mode mode, String startPoint, String endPoint) {
        int commaPos = startPoint.indexOf(',');
        int startX = Integer.valueOf(startPoint.substring(0, commaPos));
        int startY = Integer.valueOf(startPoint.substring(commaPos + 1));
        
        commaPos = endPoint.indexOf(',');
        int endX = Integer.valueOf(endPoint.substring(0, commaPos));
        int endY = Integer.valueOf(endPoint.substring(commaPos + 1));

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                if (mode == Mode.ON) {
                    onOffGrid[y][x] = true;
                    brightnessGrid[y][x]++;
                } else if (mode == Mode.OFF) {
                    onOffGrid[y][x] = false;
                    brightnessGrid[y][x] = brightnessGrid[y][x] == 0 ? 0 : (brightnessGrid[y][x] - 1);
                } else {
                    onOffGrid[y][x] = !onOffGrid[y][x];
                    brightnessGrid[y][x] += 2;
                }
            }
        }
    }
}
