package day_08;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class Day08 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        int literalCount = 0;
        int memoryCount = 0;
        int encodedCount = 0;
        
        for (String input : inputLines) {
            literalCount += input.length();
            String value = evaluateString(input);
            
            if (value == null) {
                System.out.println("Invalid string literal " + input);
            } else {
                System.out.println(value);
                memoryCount += value.length();
            }
            
            String encodedString = encodeString(input);
            System.out.println(encodedString);
            encodedCount += encodedString.length();
        }
        
        System.out.println("Part one: " + (literalCount - memoryCount));
        System.out.println("Part two: " + (encodedCount - literalCount));
    }
    
    private static String evaluateString(String input) {
        if (input.length() < 2) {
            return null;
        }
        
        if (input.charAt(0) != '\"' || (input.charAt(input.length() - 1) != '\"' && input.charAt(input.length() - 2) != '\\')) {
            return null;
        }
        
        StringBuilder sb = new StringBuilder();
        
        int pos = 1;
        
        while (pos < input.length() - 1) {
            char token = input.charAt(pos);
            
            if (token == '\"') {
                // quote all by itself - invalid
                return null;
            } else if (token == '\\') {
                pos++;
                char token2 = input.charAt(pos);
                
                if (token2 == 'x') {
                    if (pos + 2 > input.length()) {
                        // not enough characters for hex value
                        return null;
                    }
                    
                    int number = Integer.parseInt(input.substring(pos + 1, pos + 3), 16);
                    sb.append((char)number);
                    pos += 2;
                } else {
                    sb.append(token2);
                }
            } else {
                sb.append(token);
            }
            
            pos++;
        }
        
        return sb.toString();
    }
    
    private static String encodeString(String input) {
        StringBuilder sb = new StringBuilder();
        sb.append('\"');

        for (int pos = 0; pos < input.length(); pos++) {
            char token = input.charAt(pos);
            
            if (token == '\"' || token == '\\') {
                sb.append('\\');
            }
            
            sb.append(token);
        }
        
        sb.append('\"');
        
        return sb.toString();
    }
}
