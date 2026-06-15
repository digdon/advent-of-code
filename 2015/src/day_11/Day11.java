package day_11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day11 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        String origPw = inputLines.getFirst();
        
        String nextPw = incrementPw(origPw);
        System.out.println("Part 1: " + nextPw);
        
        nextPw = incrementPw(nextPw);
        System.out.println("Part 2: " + nextPw);
    }
    
    private static String incrementPw(String origPw) {
        byte[] pwChars = origPw.getBytes();

        // Look for illegal chars first, 'blanking the rest of the pw if found
        for (int i = 0; i < pwChars.length; i++) {
            byte value = pwChars[i];
            
            if (value == 'i' || value == 'l' || value == 'o') {
                pwChars[i]++;
                
                for (int j = i + 1; j < pwChars.length; j++) {
                    pwChars[j] = 'a';
                }
                
                break;
            }
        }

//        System.out.println("Step 1: " + new String(pwChars));

        boolean validPwFound = false;
        
        while (!validPwFound) {
            boolean carry = true;
            
            for (int pos = pwChars.length - 1; pos > 0 && carry; pos--) {
                pwChars[pos]++;
                
                switch (pwChars[pos]) {
                    case 'z' + 1:
                        pwChars[pos] = 'a';
                        break;
                        
                    case 'i':
                    case 'l':
                    case 'o':
                        pwChars[pos]++;
                        
                        for (int i = pos + 1; i < pwChars.length; i++) {
                            pwChars[i] = 'a';
                        }
                        
                        carry = false;
                        break;
                        
                    default:
                        carry = false;
                }
            }
            
//            System.out.println("Step 2: " + new String(pwChars));
            
            // Look for straight
            boolean straightFound = false;
            
            for (int i = 0; i < pwChars.length - 2; i++) {
                boolean found = true;
                
                for (int j = 1; j < 3; j++) {
                    if (pwChars[i + j] - pwChars[i] != j) {
                        found = false;
                        break;
                    }
                }
                
                if (found) {
                    straightFound = true;
                    break;
                }
            }
            
//            System.out.println("Step 3: straight found - " + straightFound);

            if (!straightFound) {
                continue;
            }
            
            // Look for 2 different pairs
            Set<Byte> foundPairs = new HashSet<>();
            
            for (int i = 0; i < pwChars.length - 1; i++) {
                if (pwChars[i] == pwChars[i + 1]) {
                    foundPairs.add(pwChars[i]);
                    i++;
                }
            }
            
//            System.out.println("Step 4: 2 unique pairs - " + (foundPairs.size() >= 2));
            
            if (foundPairs.size() < 2) {
                continue;
            }
            
            validPwFound = true;
        }
        
        return new String(pwChars);
    }
}
