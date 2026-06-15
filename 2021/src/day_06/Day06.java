package day_06;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day06 {

    private static final int FISH_INCREMENT = 10240;
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        // Part 1
        int[] fishArray = new int[FISH_INCREMENT];
        int maxFish = 0;
        
        for (String line : inputLines) {
            String[] parts = line.split(",");
            
            for (int i = 0; i < parts.length; i++) {
                fishArray[maxFish++] = Integer.parseInt(parts[i]);
            }
        }
        
        for (int day = 1; day <= 80; day++) {
            int count = maxFish;
            
            for (int i = 0; i < count; i++) {
                if (fishArray[i] == 0) {
                    if (maxFish == fishArray.length - 1) {
                        fishArray = Arrays.copyOf(fishArray, maxFish + FISH_INCREMENT);
                    }
                    
                    fishArray[i] = 6;
                    fishArray[maxFish++] = 8;
                } else {
                    fishArray[i]--;
                }
            }
        }
        
        System.out.println(maxFish);
        
        // Part 2
        BigInteger[] dayCounts = new BigInteger[9];
        
        for (int i = 0; i < dayCounts.length; i++) {
            dayCounts[i] = BigInteger.ZERO;
        }

        for (String line : inputLines) {
            String[] parts = line.split(",");
            
            for (int i = 0; i < parts.length; i++) {
                int day = Integer.parseInt(parts[i]);
                dayCounts[day] = dayCounts[day].add(BigInteger.ONE);
            }
        }

        for (int day = 1; day <= 256; day++) {
            BigInteger zeroCount = dayCounts[0];
            
            for (int i = 1; i < dayCounts.length; i++) {
                dayCounts[i - 1] = dayCounts[i];
            }
            
            dayCounts[6] = dayCounts[6].add(zeroCount);
            dayCounts[8] = zeroCount;
        }

        BigInteger totalFish = BigInteger.ZERO;
        
        for (int i = 0; i < dayCounts.length; i++) {
            totalFish = totalFish.add(dayCounts[i]);
        }
        
        System.out.println(totalFish);
    }
}
