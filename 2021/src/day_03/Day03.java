package day_03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Day03 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        partOne(inputLines);
        partTwo(inputLines);
    }
    
    private static void partOne(List<String> inputLines) {
        int gamma = 0;
        int epsilon = 0;
        int valueLength = inputLines.get(0).length();

        for (int pos = 0; pos < valueLength; pos++) {
            int zeroCount = 0;
            int oneCount = 0;
            
            for (String value : inputLines) {
                if (value.charAt(pos) == '0') {
                    zeroCount++;
                } else {
                    oneCount++;
                }
            }
            
            if (zeroCount < oneCount) {
                epsilon += (1 << (valueLength - pos - 1));
            } else {
                gamma += (1 << (valueLength - pos - 1));
            }
        }
        
        System.out.println(gamma);
        System.out.println(epsilon);
        System.out.println("Part 1: " + (gamma * epsilon));
    }
    
    private static void partTwo(List<String> list) {
        List<String> oxygenList = new LinkedList<>(list);
        reduceList(oxygenList, (zero, one) -> zero > one ? '0' : '1');
        
        List<String> co2List = new LinkedList<>(list);
        reduceList(co2List, (zero, one) -> zero <= one ? '0' : '1');

        System.out.println(oxygenList);
        System.out.println(co2List);
        int oxygen = Integer.parseInt(oxygenList.get(0), 2);
        int co2 = Integer.parseInt(co2List.get(0), 2);
        System.out.println("Part 2: " + (oxygen * co2));
    }
    
    private static void reduceList(List<String> list, BiFunction<Integer, Integer, Character> matcher) {
        int valueLength = list.get(0).length();
        
        for (int pos = 0; pos < valueLength; pos++) {
            if (list.size() == 1) {
                break;
            }
            
            int zeroCount = 0;
            int oneCount = 0;
            
            for (String value : list) {
                if (value.charAt(pos) == '0') {
                    zeroCount++;
                } else {
                    oneCount++;
                }
            }

            char matchChar = matcher.apply(zeroCount, oneCount);
            
            for (Iterator<String> iter = list.iterator(); iter.hasNext();) {
                String entry = iter.next();

                if (entry.charAt(pos) != matchChar) {
                    iter.remove();
                }
            }
        }
    }
}
