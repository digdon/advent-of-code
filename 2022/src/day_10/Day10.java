package day_10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day10 {

    public static void main(String[] args) throws IOException {
        // Load in the puzzle input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        int runningCycleCount = 0;
        int regX = 1;
        int waitingAddValue = 0;
        int instCycleCount = 0;
        int signalStrengthSum = 0;
        List<String> crtLines = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        
        for (String line : inputLines) {
            String[] parts = line.split("\\s+");
            
            if (parts[0].equals("noop")) {
                waitingAddValue = 0;
                instCycleCount = 1;
            } else {
                waitingAddValue = Integer.parseInt(parts[1]);
                instCycleCount = 2;
            }

            // Let's process the instruction
            while (instCycleCount > 0) {
                runningCycleCount++;
                instCycleCount--;

                // Part 1 stuff
                if (runningCycleCount == 20 || (runningCycleCount - 20) % 40 == 0) {
                    signalStrengthSum += (runningCycleCount * regX);
                }

                // Part 2 stuff
                int pixelPos = (runningCycleCount - 1) % 40;
                
                if (pixelPos >= (regX - 1) && pixelPos <= (regX + 1)) {
                    sb.append('#');
                } else {
                    sb.append(' ');
                }
                
                if (runningCycleCount % 40 == 0) {
                    // CRT line is complete, add to list
                    crtLines.add(sb.toString());
                    sb = new StringBuilder();
                }

                if (instCycleCount == 0) {
                    regX += waitingAddValue;
                }
            }
        }
        
        System.out.println("Part 1: " + signalStrengthSum);
        System.out.println("Part 2:");
        crtLines.forEach(l -> System.out.println(l));
    }
}
