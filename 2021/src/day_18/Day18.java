package day_18;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Day18 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        // Part 1 stuff
        String fish = inputLines.get(0);
        
        for (int i = 1; i < inputLines.size(); i++) {
            StringBuilder newFish = new StringBuilder();
            newFish.append("[").append(fish).append(",").append(inputLines.get(i)).append("]");
            fish = reduce(newFish.toString());
        }

        System.out.println(String.format("Part 1: %s -> %d", fish, magnitude(fish)));

        // Part 2 stuff
        int maxMag = 0;
        String a = null, b = null;
        
        for (int i = 0; i < inputLines.size(); i++) {
            String first = inputLines.get(i);
            
            for (int j = 0; j < inputLines.size(); j++) {
                String second = inputLines.get(j);
                
                if (first.equals(second)) {
                    continue;
                }
                
                StringBuilder newFish = new StringBuilder();
                newFish.append("[").append(first).append(",").append(second).append("]");
                fish = reduce(newFish.toString());
                int mag = magnitude(fish);
                
                if (mag > maxMag) {
                    maxMag = mag;
                    a = first;
                    b = second;
                }
            }
        }
        
        System.out.println("Part 2:");
        System.out.println(a);
        System.out.println(b);
        System.out.println(maxMag);
    }
    
    private static int magnitude(String fish) {
        Stack<Integer> stack = new Stack<>();
        
        for (int i = 0; i < fish.length(); i++) {
            char ch = fish.charAt(i);
            
            if (Character.isDigit(ch)) {
                stack.push(ch - '0');
            } else if (ch == ']') {
                int second = stack.pop() * 2;
                int first = stack.pop() * 3;
                stack.push(first + second);
            }
        }
        
        return stack.pop();
    }

    private static String reduce(String fish) {
        boolean actionTaken = false;
        
        do {
            actionTaken = false;
            
            // Look for a pair to be exploded
            int depth = 0;
            int explodeStart = -1;
            
            for (int i = 0; i < fish.length(); i++) {
                char ch = fish.charAt(i);
                
                if (ch == '[') {
                    if (++depth == 5) {
                        explodeStart = i;
                    }
                } else if (ch == ']') {
                    if (--depth == 4) {
                        fish = explodeFish(fish, explodeStart, i);
                        actionTaken = true;
                        break;
                    }
                }
            }
            
            if (explodeStart != -1) {
                continue;
            }
            
            // Look for a number to be split
            int numStart = -1;
            int numEnd = -1;
            int splitValue = -1;
            boolean split = false;
            
            for (int i = 0; i < fish.length(); i++) {
                if (Character.isDigit(fish.charAt(i))) {
                    if (numStart == -1) {
                        numStart = i;
                    }
                } else {
                    if (numStart != -1) {
                        splitValue = Integer.parseInt(fish.substring(numStart, i));
                        
                        if (splitValue >= 10) {
                            numEnd = i - 1;
                            split = true;
                            break;
                        } else {
                            numStart = -1;
                        }
                    }
                }
            }
            
            if (split) {
                StringBuilder newFish = new StringBuilder(fish.substring(0, numStart));
                newFish.append("[").append(splitValue / 2).append(",").append((splitValue + 1) / 2).append("]");
                newFish.append(fish.substring(numEnd + 1));
                fish = newFish.toString();
                actionTaken = true;
            }
        } while (actionTaken);
        
        return fish;
    }
    
    private static String explodeFish(String fish, int explodeStart, int explodeEnd) {
        StringBuilder newFish = new StringBuilder();
        String[] parts = fish.substring(explodeStart + 1, explodeEnd).split(",");
        int first = Integer.parseInt(parts[0]);
        int second = Integer.parseInt(parts[1]);
        
        // Look for previous number
        int pos = explodeStart;
        int prevStart = -1;
        int prevEnd = -1;
        
        while (pos > 1) {
            if (Character.isDigit(fish.charAt(pos))) {
                if (prevEnd == -1) {
                    prevEnd = pos;
                }
            } else {
                if (prevEnd != -1) {
                    prevStart = pos + 1;
                    break;
                }
            }
            
            pos--;
        }
        
        // Look for next number
        pos = explodeEnd + 1;
        int nextStart = -1;
        int nextEnd = -1;

        while (pos < fish.length()) {
            if (Character.isDigit(fish.charAt(pos))) {
                if (nextStart == -1) {
                    nextStart = pos;
                }
            } else {
                if (nextStart != -1) {
                    nextEnd = pos - 1;
                    break;
                }
            }
            
            pos++;
        }
        
        if (prevStart == -1) {
            newFish.append(fish.substring(0, explodeStart));
            newFish.append("0");
        } else {
            newFish.append(fish.substring(0, prevStart));
            newFish.append(Integer.parseInt(fish.substring(prevStart, prevEnd + 1)) + first);
            newFish.append(fish.substring(prevEnd + 1, explodeStart));
            newFish.append("0");
        }

        if (nextStart == -1) {
            newFish.append(fish.substring(explodeEnd + 1));
        } else {
            newFish.append(fish.substring(explodeEnd + 1, nextStart));
            newFish.append(Integer.parseInt(fish.substring(nextStart, nextEnd + 1)) + second);
            newFish.append(fish.substring(nextEnd + 1));
        }
        
        return newFish.toString();
    }
}

