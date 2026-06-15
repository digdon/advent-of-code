package day_19;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class Day19 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        Map<String, List<String>> replacementsMap = new HashMap<>();
        
        for (String line : inputLines) {
            if (line.isBlank()) {
                break;
            }
            
            String[] parts = line.split(" => ");
            String from = parts[0];
            String to = parts[1];

            replacementsMap.compute(from, (key, toList) -> {
                if (toList == null) {
                    toList = new ArrayList<>();
                }
                toList.add(to);
                return toList;
            });
        }
        
        String molecule = inputLines.get(inputLines.size() - 1);

        part1(replacementsMap, molecule);
        part2(molecule);
    }
    
    private static void part1(Map<String, List<String>> replacementsMap, String molecule) {
        long startTime = System.currentTimeMillis();
        
        Set<String> generated = new HashSet<>();
        
        for (Entry<String, List<String>> entry : replacementsMap.entrySet()) {
            String from = entry.getKey();
            List<String> toList = entry.getValue();

            for (int idx = 0; idx < molecule.length(); ) {
                int pos = molecule.indexOf(from, idx);
                
                if (pos != -1) {
                    // Found a spot for substitution
                    for (String to : toList) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(molecule.substring(0, pos)).append(to).append(molecule.substring(pos + from.length()));
                        generated.add(sb.toString());
                    }
                    
                    idx = pos + from.length();
                } else {
                    idx++;
                }
            }
        }

        System.out.println(String.format("Part 1: %d (%d ms)", generated.size(), System.currentTimeMillis() - startTime));
    }
    
    private static void part2(String molecule) {
        long startTime = System.currentTimeMillis();
        
        int symbolCount = 0, rnCount = 0, arCount = 0, yCount = 0;
        
        for (int i = 0; i < molecule.length(); i++) {
            char ch = molecule.charAt(i);
            
            if (Character.isUpperCase(ch)) {
                if (ch == 'Y') {
                    yCount++;
                } else if (ch == 'R' && molecule.charAt(i + 1) == 'n') {
                    rnCount++;
                } else if (ch == 'A' && molecule.charAt(i + 1) == 'r') {
                    arCount++;
                }
                
                symbolCount++;
            }
        }
        
        int answer = symbolCount - rnCount - arCount - (2 * yCount) - 1;
        System.out.println(String.format("Part 2: %d (%d ms)", answer, System.currentTimeMillis() - startTime));
    }
}
