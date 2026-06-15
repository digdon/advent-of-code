package day_06;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class Day06 {

    public static void main(String[] args) throws IOException {
        // Load in the puzzle input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        for (String line : inputLines) {
            int markerPos = 0;
            
            for (int i = 0; i < line.length() - 3; i++) {
                long distinctCharCount = line.substring(i, i + 4).chars().distinct().count();
                
                if (distinctCharCount == 4) {
                    markerPos = i + 4;
                    break;
                }
            }
            
            int messagePos = 0;
            
            for (int i = 0; i < line.length() - 13; i++) {
                long distinctCharCount = line.substring(i, i + 14).chars().distinct().count();
                
                if (distinctCharCount == 14) {
                    messagePos = i + 14;
                    break;
                }
            }

            System.out.println("Part 1: start of packet marker = " + markerPos);
            System.out.println("Part 2: start of message marker = " + messagePos);
        }
    }
}
