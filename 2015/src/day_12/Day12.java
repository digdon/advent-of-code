package day_12;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day12 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        String fullString = inputLines.stream().collect(Collectors.joining());

        System.out.println("Part 1: " + sumAllNumbers(fullString));

        Pattern part2Pattern = Pattern.compile("\":\"red\"");
        
        while (true) {
            Matcher matcher = part2Pattern.matcher(fullString);
            
            if (matcher.find() == false) {
                break;
            }
            
            // Look for opening { and ending }
            int objStart = matcher.start(), objEnd = matcher.end();

            int closeCount = 1;
            
            for (; objStart >= 0; objStart--) {
                char charAt = fullString.charAt(objStart);
                
                if (charAt == '{') {
                    closeCount--;
                    
                    if (closeCount == 0) {
                        break;
                    }
                } else if (charAt == '}') {
                    closeCount++;
                }
            }
            
            if (closeCount > 0 || objStart < 0) {
                System.out.println("Something went wrong");
                System.exit(-1);
            }
            
            int openCount = 1;
            
            for (; objEnd < fullString.length(); objEnd++) {
                char charAt = fullString.charAt(objEnd);
                
                if (charAt == '}') {
                    openCount--;
                    
                    if (openCount == 0) {
                        break;
                    }
                } else if (charAt == '{') {
                    openCount++;
                }
            }
            
            objEnd++;
            
            if (openCount > 0 || objEnd > fullString.length()) {
                System.out.println("Something went wrong");
                System.exit(-1);
            }
            
            fullString = new StringBuilder().append(fullString.substring(0, objStart)).append(fullString.substring(objEnd)).toString();
        }
        
        System.out.println("Part 2: " + sumAllNumbers(fullString));
    }
    
    private static int sumAllNumbers(String input) {
        Pattern part1Pattern = Pattern.compile("-?\\d+");
        Matcher matcher = part1Pattern.matcher(input);
        int total = 0;
        
        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group());
            total += value;
        }
        
        return total;
    }
}
