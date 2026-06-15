package day_16;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Day16 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        Map<Integer, Map<String, Integer>> sueMap = new HashMap<>();
        
        for (String line : inputLines) {
            int pos = line.indexOf(':');
            String name = line.substring(0, pos);
            
            String[] attrParts = line.substring(pos + 1).trim().split(",\\s*");
            Map<String, Integer> nvMap = new HashMap<>();
            
            for (String attr : attrParts) {
                String[] nv = attr.split(":\\s*");
                nvMap.put(nv[0], Integer.parseInt(nv[1]));
            }
            
            sueMap.put(Integer.parseInt(name.substring(name.lastIndexOf(' ') + 1)), nvMap);
        }
        
        Map<String, Integer> tickerTape = Map.of(
                "children", 3,
                "cats", 7,
                "samoyeds", 2,
                "pomeranians", 3,
                "akitas", 0,
                "vizslas", 0,
                "goldfish", 5,
                "trees", 3,
                "cars", 2,
                "perfumes", 1
                );
        
        // Part 1
        Map<Integer, Map<String, Integer>> part1Map = new HashMap<>(sueMap);
        
        for (Entry<String, Integer> tickerEntry : tickerTape.entrySet()) {
//            System.out.println(String.format("Checking %s = %d", tickerEntry.getKey(), tickerEntry.getValue()));
            Map<Integer, Map<String, Integer>> newMap = new HashMap<>();
            
            for (Entry<Integer, Map<String, Integer>> entry : part1Map.entrySet()) {
                Integer entryValue = entry.getValue().get(tickerEntry.getKey());

                if (entryValue == null || entryValue == tickerEntry.getValue()) {
                    // Possible match - copy to new map
                    newMap.put(entry.getKey(), entry.getValue());
                }
            }

            part1Map = newMap;
//            System.out.println(part1Map.size());
        }
        
        System.out.println("Part 1: " + part1Map.keySet());
        
        // Part 2
        Map<Integer, Map<String, Integer>> part2Map = new HashMap<>(sueMap);
        
        for (Entry<String, Integer> tickerEntry : tickerTape.entrySet()) {
//            System.out.println(String.format("Checking %s = %d", tickerEntry.getKey(), tickerEntry.getValue()));
            Map<Integer, Map<String, Integer>> newMap = new HashMap<>();
            
            for (Entry<Integer, Map<String, Integer>> entry : part2Map.entrySet()) {
                Integer entryValue = entry.getValue().get(tickerEntry.getKey());
                
                if (entryValue == null) {
                    // Unknown value, so a possible match - copy it
                    newMap.put(entry.getKey(), entry.getValue());
                    continue;
                }
                
                boolean match = switch (tickerEntry.getKey()) {
                    case "cats", "trees" -> entryValue > tickerEntry.getValue();
                    case "pomeranians", "goldfish" -> entryValue < tickerEntry.getValue();
                    default -> entryValue == tickerEntry.getValue();
                };
                
                if (match) {
                    newMap.put(entry.getKey(), entry.getValue());
                }
            }

            part2Map = newMap;
//            System.out.println(part2Map.size());
        }
        
        System.out.println("Part 2: " + part2Map.keySet());
    }
}
