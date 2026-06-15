package day_25;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class Day25 {

    public static void main(String[] args) throws IOException {
        // Load in the puzzle input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        long snafuSum = inputLines.stream().mapToLong(Day25::snafuToDecimal).sum();

        System.out.println("Part 1: " + decimalToSnafu(snafuSum));
    }
    
    private static long snafuToDecimal(String snafu) {
        long result = 0;
        
        char[] chars = snafu.toCharArray();
        
        for (int i = 0; i < chars.length; i++) {
            long pow = (long)Math.pow(5, chars.length - 1 - i);
            long multiple = switch (chars[i]) {
                case '2' -> 2;
                case '1' -> 1;
                case '0' -> 0;
                case '-' -> -1;
                case '=' -> -2;
                default -> 0;
            };
            
            result += pow * multiple;
        }
        
        return result;
    }
    
    private static String decimalToSnafu(long decimal) {
        StringBuilder sb = new StringBuilder();

        while (decimal > 0) {
            long remainder = decimal % 5;
            
            if (remainder == 3) {
                sb.insert(0,  '=');
                decimal += 5;
            } else if (remainder == 4) {
                sb.insert(0,  '-');
                decimal += 5;
            } else {
                sb.insert(0,  remainder);
            }
            
            decimal /= 5;
        }
        
        return sb.toString();
    }
}
