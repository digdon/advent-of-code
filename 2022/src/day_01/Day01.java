package day_01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day01 {

    public static void main(String[] args) throws IOException {
        // Load in the puzzle input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        // Process the input lines
        List<Integer> elfCalorieCounts = new ArrayList<>();
        int calorieCount = 0;
        
        for (String line : inputLines) {
            if (line.length() == 0) {
                // End of current elf list
                elfCalorieCounts.add(calorieCount);
                calorieCount = 0;
                continue;
            }
            
            int itemCalories = Integer.parseInt(line);
            calorieCount += itemCalories;
        }
        
        elfCalorieCounts.add(calorieCount);

        Collections.sort(elfCalorieCounts, Collections.reverseOrder());
        
        System.out.println(String.format("Part 1: Max elf calories = %d", elfCalorieCounts.get(0)));
        
        int top3Counts = 0;
        
        for (int i = 0; i < Math.min(3, elfCalorieCounts.size()); i++) {
            top3Counts += elfCalorieCounts.get(i);
        }
        
        System.out.println(String.format("Part 2: Top 3 calorie count total = %d", top3Counts));
    }
}
