package day_09;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Day09 {

    public static void main(String[] args) {
        List<Long> inputList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        
        try {
            while ((inputLine = reader.readLine()) != null) {
                inputList.add(Long.valueOf(inputLine));
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
        
        int preamble = 25;
        LinkedList<Long> runningList = new LinkedList<>();
        
        // Part one
        // Set up preamble
        for (int i = 0; i < preamble; i++) {
            runningList.add(inputList.get(i));
        }

        long invalidNumber = 0;
        
        // Run through the rest of the list
        for (int i = preamble; i < inputList.size(); i++) {
            long target = inputList.get(i);
            boolean targetFound = false;
            
            for (int x = 0; x < runningList.size() - 1; x++) {
                if (targetFound) {
                    break;
                }
                
                long valueOne = runningList.get(x);
                
                for (int y = x + 1; y < runningList.size(); y++) {
                    long valueTwo = runningList.get(y);
                    
                    if (valueOne + valueTwo == target) {
                        targetFound = true;
                        break;
                    }
                }
            }
            
            if (targetFound == false) {
                invalidNumber = target;
                break;
            } else {
                runningList.removeFirst();
                runningList.addLast(target);
            }
        }
        
        System.out.println("Part one: " + invalidNumber);
        
        // Part two
        for (int i = 0; i < inputList.size(); i++) {
            int count = 0;
            long total = 0;
            long min = Long.MAX_VALUE;
            long max = 0;
            
            while (i + count < inputList.size() && total < invalidNumber) {
                long value = inputList.get(i + count);
                
                if (value < min) {
                    min = value;
                } else if (value > max) {
                    max = value;
                }
                
                total += value;
                count++;
            }
            
            if (count >= 2 && total == invalidNumber) {
                System.out.println("Part two: " + (min + max));
                break;
            }
        }
    }
}
