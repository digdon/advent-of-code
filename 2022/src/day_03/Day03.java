package day_03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day03 {

    public static void main(String[] args) throws IOException {
        // Load in the puzzle input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        // Part 1 - Process each backpack
        int priorityScore = 0;
        
        for (String backpack : inputLines) {
            String comp1 = backpack.substring(0, backpack.length() / 2);
            String comp2 = backpack.substring(backpack.length() / 2);

            // Build set of items in compartment 1
            Set<Character> comp1Set = new HashSet<>();
            
            for (int i = 0; i < comp1.length(); i++) {
                comp1Set.add(comp1.charAt(i));
            }
            
            // Now see what in compartment 2 also exists in compartment 1
            for (int i = 0; i < comp2.length(); i++) {
                char item = comp2.charAt(i);
                
                if (comp1Set.contains(item)) {
                    priorityScore += ((item >= 'a' && item <= 'z') ? (item - 'a' + 1) : (item - 'A' + 27));
                    break;
                }
            }
        }
        
        System.out.println("Part 1: " + priorityScore);
        
        // Part 2
        priorityScore = 0;
        int groupCount = 0;
        Set<Character> groupSet = null;
        
        for (String packItems : inputLines) {
            if (groupCount == 0) {
                // Build set of items in pack #1
                Set<Character> packSet = new HashSet<>();
                
                for (int i = 0; i < packItems.length(); i++) {
                    packSet.add(packItems.charAt(i));
                }

                groupSet = packSet;
                groupCount++;
            } else if (groupCount == 1) {
                // Build set of items in pack #2
                Set<Character> packSet = new HashSet<>();
                
                for (int i = 0; i < packItems.length(); i++) {
                    packSet.add(packItems.charAt(i));
                }

                // Keep only duplicate items between packs
                groupSet.retainAll(packSet);
                groupCount++;
            } else {
                for (int i = 0; i < packItems.length(); i++) {
                    char item = packItems.charAt(i);
                    
                    if (groupSet.contains(item)) {
                        priorityScore += ((item >= 'a' && item <= 'z') ? (item - 'a' + 1) : (item - 'A' + 27));
                        break;
                    }
                }
                
                groupCount = 0;
            }
        }
        
        System.out.println("Part 2: " + priorityScore);
    }
}
