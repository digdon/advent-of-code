package day_10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

public class Day10 {

    private static final Map<Character, Character> MATCHES = Map.of('(', ')', '[', ']', '{', '}', '<', '>');
    private static final Map<Character, Integer> ERROR_VALUES = Map.of(')', 3, ']', 57, '}', 1197, '>', 25137);
    private static final Map<Character, Integer> COMPLETION_VALUES = Map.of('(', 1, '[', 2, '{', 3, '<', 4);
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        int errorScore = 0;
        List<BigInteger> completionScores = new ArrayList<>();

        for (Iterator<String> iter = inputLines.iterator(); iter.hasNext(); ) {
            String line = iter.next();
            Stack<Character> stack = new Stack<>();
            boolean corrupt = false;
            
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                
                if (c == '{' || c == '[' || c == '(' || c == '<') {
                    stack.push(c);
                } else {
                    char open = stack.pop();
                    
                    if (c != MATCHES.get(open)) {
                        // corrupt
                        corrupt = true;
                        errorScore += ERROR_VALUES.get(c);
                        break;
                    }
                }
            }

            if (corrupt) {
                iter.remove();
            } else {
                BigInteger score = BigInteger.ZERO;
                
                while (stack.isEmpty() == false) {
                    char c = stack.pop();
                    score = score.multiply(BigInteger.valueOf(5)).add(BigInteger.valueOf(COMPLETION_VALUES.get(c)));
                }
                
                completionScores.add(score);
            }
        }
        
        //Part 1
        System.out.println("Part 1: " + errorScore);
        
        //Part 2
        Collections.sort(completionScores);
        System.out.println(completionScores);
        System.out.println("Part 2: " + completionScores.get(completionScores.size() / 2));
    }
}
