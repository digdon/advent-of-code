package day_18;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class Day18 {

    public static void main(String[] args) {
        List<String> expressionList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        
        try {
            while ((inputLine = reader.readLine()) != null) {
                expressionList.add(inputLine);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        long partOneTotal = 0;
        long partTwoTotal = 0;
        
        for (String expression : expressionList) {
            Queue<Character> parsedExpr = parseExpressionLeftToRight(expression);
            long value = evaluateExpression(parsedExpr);
            partOneTotal += value;
            
            parsedExpr = parseExpressionPrecedence(expression);
            value = evaluateExpression(parsedExpr);
            partTwoTotal += value;
        }
        
        System.out.println("Part one: " + partOneTotal);
        System.out.println("Part two: " + partTwoTotal);
    }
    
    private static Queue<Character> parseExpressionLeftToRight(String expression) {
        Queue<Character> output = new LinkedList<>();
        Stack<Character> operatorStack = new Stack<>();
        
        for (int pos = expression.length() - 1; pos >= 0; pos--) {
            char token = expression.charAt(pos);
            
            if (Character.isWhitespace(token)) {
                continue;
            }
            
            if (Character.isDigit(token)) {
                output.add(token);
            } else if (token == ')') {
                operatorStack.add(token);
            } else if (token == '(') {
                while (operatorStack.peek() != ')' && operatorStack.isEmpty() == false) {
                    output.add(operatorStack.pop());
                }
                
                if (operatorStack.peek() != ')') {
                    System.out.println("parenthesis mismatch");
                    System.exit(0);
                } else {
                    operatorStack.pop();
                }
            } else {
                operatorStack.add(token);
            }
        }

        while (operatorStack.isEmpty() == false) {
            output.add(operatorStack.pop());
        }
        
        return output;
    }
    
    private static Queue<Character> parseExpressionPrecedence(String expression) {
        Queue<Character> output = new LinkedList<>();
        Stack<Character> operatorStack = new Stack<>();
        
        for (int pos = expression.length() - 1; pos >= 0; pos--) {
            char token = expression.charAt(pos);
            
            if (Character.isWhitespace(token)) {
                continue;
            }
            
            if (Character.isDigit(token)) {
                output.add(token);
            } else if (token == ')') {
                operatorStack.add(token);
            } else if (token == '(') {
                while (operatorStack.peek() != ')' && operatorStack.isEmpty() == false) {
                    output.add(operatorStack.pop());
                }
                
                if (operatorStack.peek() != ')') {
                    System.out.println("parenthesis mismatch");
                    System.exit(0);
                } else {
                    operatorStack.pop();
                }
            } else {
                while (operatorStack.size() > 0) {
                    if (token == '*' && operatorStack.peek() == '+') {
                        output.add(operatorStack.pop());
                    } else {
                        break;
                    }
                }
                
                operatorStack.add(token);
            }
        }

        while (operatorStack.isEmpty() == false) {
            output.add(operatorStack.pop());
        }
        
        return output;
    }
    
    private static long evaluateExpression(Queue<Character> parsedExpr) {
        Stack<Long> stack = new Stack<>();
        
        for (Character token : parsedExpr) {
            if (Character.isDigit(token)) {
                stack.push(Long.valueOf(token - '0'));
            } else {
                long value1 = stack.pop();
                long value2 = stack.pop();
                
                switch (token) {
                    case '+':
                        stack.push(value1 + value2);
                        break;
                        
                    case '*':
                        stack.push(value1 * value2);
                        break;
                }
            }
        }
        
        return stack.pop();
    }
}
