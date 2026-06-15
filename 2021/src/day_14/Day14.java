package day_14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day14 {

    private static final Pattern INSERTION_PATTERN = Pattern.compile("(\\w+)\\s+\\S\\S\\s+(\\w+)");
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        String template = inputLines.get(0);

        // Build the pair transformations - eg, HH maps to HN and NH
        Map<String, List<String>> transformations = new HashMap<>();
        Map<String, Character> addedLetter = new HashMap<>();
        Map<String, Long> pairCountMap = new HashMap<>();
        Map<Character, Long> letterCountMap = new HashMap<>();
        
        for (int i = 2; i < inputLines.size(); i++) {
            Matcher matcher = INSERTION_PATTERN.matcher(inputLines.get(i));
            
            if (matcher.matches()) {
                String pair = matcher.group(1);
                String insert = matcher.group(2);
                List<String> mapList = new ArrayList<>();
                mapList.add(pair.charAt(0) + insert);
                mapList.add(insert + pair.charAt(1));
                transformations.put(pair, mapList);
                addedLetter.put(pair, insert.charAt(0));
            }
        }
        
        // Prime with the original template
        for (int pos = 0; pos < template.length(); pos++) {
            char letter = template.charAt(pos);
            letterCountMap.compute(letter, (k, v) -> v == null ? 1 : v + 1);
            
            if (pos < template.length() - 1) {
                String pair = template.substring(pos, pos + 2);
                pairCountMap.compute(pair, (k, v) -> v == null ? 1 : v + 1);
            }
        }

        // Process the iterations
        for (int i = 1; i <= 40; i++) {
            Map<String, Long> nextPairCountMap = new HashMap<>();
            
            pairCountMap.forEach((key, count) -> {
                // Find the transformation map, and increase the value of each pair in the mapping list
                transformations.get(key).forEach(pair -> nextPairCountMap.compute(pair, (k, v) -> v == null ? count : v + count));
                
                // We know what letter has been added, based on the transformation. Increase that value too
                letterCountMap.compute(addedLetter.get(key), (k, v) -> v == null ? 1 : v + count);
            });
            
            pairCountMap = nextPairCountMap;
            
            if (i == 10) {
                System.out.println("Part 1: " + calculateResult(letterCountMap));
            }
        }
        
        System.out.println(pairCountMap);
        System.out.println(letterCountMap);
        System.out.println("Part 2: " + calculateResult(letterCountMap));
    }
    
    public static long calculateResult(Map<Character, Long> letterCounts) {

        LinkedList<Long> values = new LinkedList<>(letterCounts.values());
        Collections.sort(values);
        
        return values.getLast() - values.getFirst();
    }
}
