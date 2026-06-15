package day_13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day13 {

    private static final Pattern INPUT_PATTERN = Pattern.compile("(\\w+) would (gain|lose) (\\d+) .* to (\\w+)\\.");
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        Map<String, Map<String, Integer>> happyMap = new HashMap<>();
        
        for (String line : inputLines) {
            Matcher matcher = INPUT_PATTERN.matcher(line);
            
            if (matcher.matches() == false) {
                continue;
            }
            
            String name = matcher.group(1);
            boolean lose = matcher.group(2).equalsIgnoreCase("lose");
            int value = Integer.parseInt(matcher.group(3));
            
            if (lose) {
                value = -value;
            }
            
            String nextTo = matcher.group(4);
            
            happyMap.computeIfAbsent(name, k -> new HashMap<>()).put(nextTo, value);
        }

        // Part 1 stuff
        long start = System.currentTimeMillis();
        List<String> nameList = new ArrayList<>(happyMap.keySet());
        List<List<String>> permutations = new ArrayList<>();
        permuteNames(nameList, 0, permutations);
        System.out.println(permutations.size());
        int maxHappiness = computeHappiness(permutations, happyMap);
        System.out.println(String.format("Part 1: %d (%d)", maxHappiness, System.currentTimeMillis() - start));
        
        // Part 2 stuff
        start = System.currentTimeMillis();
        nameList.add("me");
        permutations.clear();
        permuteNames(nameList, 0, permutations);
        System.out.println(permutations.size());
        maxHappiness = computeHappiness(permutations, happyMap);
        System.out.println(String.format("Part 2: %d (%d)", maxHappiness, System.currentTimeMillis() - start));
    }
    
    private static int computeHappiness(List<List<String>> permutations, Map<String, Map<String, Integer>> happyMap) {
        Map<String, Integer> emptyMap = new HashMap<>();
        int maxHappiness = 0;
        List<String> maxList = null;
        
        for (List<String> list : permutations) {
            int totalHappiness = 0;
            
            for (int i = 0; i < list.size() - 1; i++) {
                // Get happiness person A gets from sitting next to person B
                totalHappiness += happyMap.getOrDefault(list.get(i), emptyMap).getOrDefault(list.get(i + 1), 0);

                // Get happiness person B gets from sitting next to person A
                totalHappiness += happyMap.getOrDefault(list.get(i + 1), emptyMap).getOrDefault(list.get(i), 0);
            }

            // Need to complete the circle - get happiness from first person sitting next to last person and vice versa
            totalHappiness += happyMap.getOrDefault(list.getFirst(), emptyMap).getOrDefault(list.getLast(), 0);
            totalHappiness += happyMap.getOrDefault(list.getLast(), emptyMap).getOrDefault(list.getFirst(), 0);
            
            if (totalHappiness > maxHappiness) {
                maxHappiness = totalHappiness;
                maxList = list;
            }
        }
        
        System.out.println(maxHappiness);
        System.out.println(maxList);
        
        return maxHappiness;
    }
    
    private static void permuteNames(List<String> nameList, int start, List<List<String>> results) {
        if (start == nameList.size() - 1) {
            results.add(new ArrayList<>(nameList));
            return;
        }
        
        for (int i = start; i < nameList.size(); i++) {
            Collections.swap(nameList, start, i);
            permuteNames(nameList, start + 1, results);
            Collections.swap(nameList, start, i);
        }
    }
}
