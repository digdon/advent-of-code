package day_04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day04 {

    private static final Pattern ASSIGNMENT_PATTERN = Pattern.compile("(\\d+)-(\\d+),(\\d+)-(\\d+)");
    
    public static void main(String[] args) throws IOException {
        // Load in the puzzle input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        // Process the elf pairings
        int part1Count = 0;
        int part2Count = 0;
        
        for (String line : inputLines) {
            Matcher matcher = ASSIGNMENT_PATTERN.matcher(line);
            
            if (matcher.find() == false) {
                continue;
            }
            
            int elf1Min = Integer.parseInt(matcher.group(1));
            int elf1Max = Integer.parseInt(matcher.group(2));
            int elf2Min = Integer.parseInt(matcher.group(3));
            int elf2Max = Integer.parseInt(matcher.group(4));
            
            // Part 1 - Does one elf fall completely within the other?
            if ((elf1Min >= elf2Min && elf1Max <= elf2Max)
                    || (elf2Min >= elf1Min && elf2Max <= elf1Max)) {
                part1Count++;
            }
            
            // Part 2 - Does one elf overlap at all with the other?
            if ((elf1Min >= elf2Min && elf1Max <= elf2Max)  // 1 falls completely within 2
                    || (elf2Min >= elf1Min && elf2Max <= elf1Max)  // 2 falls completely within 1
                    || (elf1Min >= elf2Min && elf1Min <= elf2Max)  // 1 min falls within 2
                    || (elf2Min >= elf1Min && elf2Min <= elf1Max)  // 2 min falls within 1
                    || (elf1Max >= elf2Min && elf1Max <= elf2Max)  // 1 max falls within 2
                    || (elf2Max >= elf1Min && elf2Max <= elf1Max)  // 2 max falls within 1
               ) {
                part2Count++;
            }
        }
        
        System.out.println("Part 1: " + part1Count);
        System.out.println("Part 2: " + part2Count);
    }
}
