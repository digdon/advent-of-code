package day_01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day01 {

    private static final int WINDOW_SIZE = 3;
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        // Part 1
        int prevValue = 0;
        int increaseCount = 0;
        
        for (String text : inputLines) {
            int value = Integer.parseInt(text);
            
            if (prevValue != 0) {
                if (value > prevValue) {
                    increaseCount++;
                }
            }
            
            prevValue = value;
        }
        
        System.out.println("Part 1: " + increaseCount);
        
        // Part 2
        List<Integer> windowSums = new ArrayList<>();
        List<Integer> windowCounts = new ArrayList<>();
        int itemCount = 0;
        
        for (String text : inputLines) {
            int value = Integer.parseInt(text);
            
            for (int step = WINDOW_SIZE - 1; step >= 0; step--) {
                int window = itemCount - step;
                
                if (window >= 0) {
                    if (windowSums.size() < (window + 1)) {
                        windowSums.add(value);
                    } else {
                        windowSums.set(window, windowSums.get(window) + value);
                    }
                    
                    if (windowCounts.size() < (window + 1)) {
                        windowCounts.add(1);
                    } else {
                        windowCounts.set(window, windowCounts.get(window) + 1);
                    }
                }
            }
            
            itemCount++;
        }
        
        prevValue = 0;
        increaseCount = 0;
        
        for (int i = 0; i < windowSums.size(); i++) {
            int value = windowSums.get(i);
            
            if (windowCounts.get(i) < 3) {
                // Not a full window, skip it
                continue;
            }
            
            if (prevValue != 0 && value > prevValue) {
                increaseCount++;
            }
            
            prevValue = value;
        }
        
        System.out.println("Part 2: " + increaseCount);
    }
}
