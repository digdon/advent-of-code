package day_21;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day21 {

    public static void main(String[] args) throws IOException {
        // Load in the puzzle input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        Map<String, Expr> expressionMap = new HashMap<>();
        
        for (String line : inputLines) {
            String[] parts = line.split("\\s+");
            String name = parts[0].substring(0, parts[0].length() - 1);
            
            if (parts.length == 2) {
                // name and value only
                expressionMap.put(name, new Expr(Long.valueOf(parts[1]), null, null, null));
            } else {
                // expression
                expressionMap.put(name, new Expr(null, parts[2], parts[1], parts[3]));
            }
        }

        // Part 1
        long result = evaluateExpression("root", expressionMap);
        System.out.println("Part 1: " + result);
        
        // Part 2
        Expr expr = expressionMap.get("root");
        long humnValue = 0;
        
        // Start by working out the gap
        expressionMap.replace("humn", new Expr(humnValue, null, null, null));
        long prevValue = evaluateExpression(expr.var1(), expressionMap);
        long gap = 0;
        
        while (gap == 0) {
            humnValue++;
            expressionMap.replace("humn", new Expr(humnValue, null, null, null));
            long value = evaluateExpression(expr.var1(), expressionMap);
            gap = prevValue - value;
        }
        
        if (Math.abs(gap) > 1) {
            // We can do this because we know values on the left occur twice for two consecutive humn values
            gap /= 2;
        }
        
        while (true) {
            expressionMap.replace("humn", new Expr(humnValue, null, null, null));
            long left = evaluateExpression(expr.var1(), expressionMap);
            long right = evaluateExpression(expr.var2(), expressionMap);

            System.out.println(String.format("humn = %d -> left = %d, right = %d", humnValue, left, right));
            
            if (left == right) {
                // Equal - we're done
                break;
            } else {
                // Essentially a binary search for the value
                long diff = left - right;
                humnValue += (diff / gap);
            }
        }
        
        System.out.println("Part 2: " + humnValue);
    }

    private static long evaluateExpression(String name, Map<String, Expr> exprMap) {
        Expr expr = exprMap.get(name);
        
        if (expr.value() != null) {
            // A direct value - return it
            return expr.value();
        }
        
        long left = evaluateExpression(expr.var1(), exprMap);
        long right = evaluateExpression(expr.var2(), exprMap);
        
        return switch (expr.op().charAt(0)) {
            case '+' -> left + right;
            case '-' -> left - right;
            case '*' -> left * right;
            case '/' -> left / right;
            default -> 0;
        };
    }
    
    record Expr(Long value, String op, String var1, String var2) {}
}
