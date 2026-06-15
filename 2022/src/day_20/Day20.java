package day_20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Day20 {

    public static void main(String[] args) throws IOException {
        // Load in the puzzle input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        Number zero = null;
        List<Number> encryptList = new LinkedList<>();
        
        for (int i = 0; i < inputLines.size(); i++) {
            long value = Long.parseLong(inputLines.get(i));
            Number number = new Number(value, i);
            
            if (value == 0) {
                zero = number;
            }
            
            encryptList.add(number);
        }
        
        long results = mixTheList(encryptList, zero, 1);
        System.out.println("Part 1: " + results);
        
        encryptList = encryptList.stream().map(number -> new Number(number.value() * 811589153, number.position())).collect(Collectors.toList());
        results = mixTheList(encryptList, zero, 10);
        System.out.println("Part 2: " + results);
    }
    
    private static long mixTheList(List<Number> encryptList, Number zero, int mixCount) {
        List<Number> mixList = new LinkedList<>(encryptList);
        int itemCount = mixList.size();
        
        for (int i = 0; i < mixCount; i++) {
            for (Number number : encryptList) {
                int index = mixList.indexOf(number);
                int newPos = (int)((index + number.value()) % (itemCount - 1));
                
                if (newPos < 0) {
                    newPos = itemCount + newPos - 1;
                }
                
                mixList.remove(index);
                mixList.add(newPos, number);
            }
        }
        
        int zeroIndex = mixList.indexOf(zero);
        int index1000 = (1000 + zeroIndex) % itemCount;
        int index2000 = (2000 + zeroIndex) % itemCount;
        int index3000 = (3000 + zeroIndex) % itemCount;
        
        return mixList.get(index1000).value() + mixList.get(index2000).value() + mixList.get(index3000).value();
    }
    
    record Number(long value, int position) {}
}
