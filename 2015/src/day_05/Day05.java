package day_05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day05 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        int niceCount = 0;
        
        for (String input : inputLines) {
            if (isNicePartOne(input)) {
                niceCount++;
            }
        }
        
        System.out.println("Part one: " + niceCount);

        niceCount = 0;

        for (String input : inputLines) {
            if (isNicePartTwo(input)) {
                niceCount++;
            }
        }

        System.out.println("Part two: " + niceCount);
    }
    
    private static boolean isNicePartOne(String input) {
        int vowelCount = 0;
        boolean doubleFound = false;
        char prev = ' ';

        for (int i = 0; i < input.length(); i++) {
            char token = input.charAt(i);
            
            if (token == prev) {
                doubleFound = true;
            }
            
            if ((token == 'b' && prev == 'a')
                        || (token == 'd' && prev == 'c')
                        || (token == 'q' && prev == 'p')
                        || (token == 'y' && prev == 'x')) {
                    return false;
            }
            
            if (token == 'a' || token == 'e' || token == 'i' || token == 'o' || token == 'u') {
                vowelCount++;
            }
            
            prev = token;
        }
        
        return vowelCount >= 3 && doubleFound;
    }

    private static final Pattern XYX_PATTERN = Pattern.compile("(.).\\1");
    
    private static boolean isNicePartTwo(String input) {
        Matcher matcher = XYX_PATTERN.matcher(input);
        
        if (matcher.find() == false) {
            return false;
        }

        for (int i = 0; i < input.length() - 1; i++) {
            String temp = input.substring(i, i + 2);
            
            if (input.substring(i + 2).contains(temp)) {
                return true;
            }
        }
        
        return false;
    }
}
