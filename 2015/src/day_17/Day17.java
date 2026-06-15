package day_17;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class Day17 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        int[] numbers = new int[inputLines.size()];
        int numCount = 0;
        
        for (String line : inputLines) {
            try {
                int number = Integer.parseInt(line);
                numbers[numCount++] = number;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number: " + line);
                System.exit(1);
            }
        }

//        bruteForce(numbers);
        recursion(numbers);
    }

    private static final int TARGET_VOLUME = 150;
    
    private static int part1Count = 0;
    private static int minContainers = Integer.MAX_VALUE;
    private static int minContainerCount = 0;
    
    private static void recursion(int[] numbers) {
        part1Count = 0;
        minContainers = Integer.MAX_VALUE;
        minContainerCount = 0;
        
        long startTime = System.currentTimeMillis();
        
        findCombinations(numbers, 0, 0, 0);
        
        System.out.println("Part 1: " + part1Count);
        System.out.println("Part 2: " + minContainerCount);
        System.out.println(String.format("Time: %d ms", System.currentTimeMillis() - startTime));
    }

    private static void findCombinations(int []numbers, int index, int currentTotal, int containerCount) {
        if (currentTotal == TARGET_VOLUME) {
            // Target reached, so count this combination
            part1Count++;
            
            if (containerCount < minContainers) {
                minContainers = containerCount;
                minContainerCount = 1;
            } else if (containerCount == minContainers) {
                minContainerCount++;
            }
            
            return;
        }
        
        if (currentTotal > TARGET_VOLUME || index >= numbers.length) {
            // Either we've exceeded the target or run out of containers
            return;
        }
        
        // Move to the next container, including the current container
        findCombinations(numbers, index + 1, currentTotal + numbers[index], containerCount + 1);
        
        // Move to the next container, but exclude the current container
        findCombinations(numbers, index + 1, currentTotal, containerCount);
    }
    
    private static void bruteForce(int[] numbers) {
        long startTime = System.currentTimeMillis();
        int numCount = numbers.length;
        int part1Count = 0;
        int minContainers = Integer.MAX_VALUE;
        int minContainerCount = 0;
        
        for (int i = 0; i < 1 << numCount; i++) {
            int value = 0;
            int containerCount = 0;
            
            for (int j = 0; j < numCount; j++) {
                if ((i & (1 << j)) > 0) {
                    value += numbers[j];
                    containerCount++;
                }
            }
            
            if (value == TARGET_VOLUME) {
                part1Count++;
                
                if (containerCount < minContainers) {
                    minContainers = containerCount;
                    minContainerCount = 1;
                } else if (containerCount == minContainers) {
                    minContainerCount++;
                }
            }
        }
        
        System.out.println("Part 1: " + part1Count);
        System.out.println("Part 2: " + minContainerCount);
        System.out.println(String.format("Time: %d ms", System.currentTimeMillis() - startTime));
    }
}
