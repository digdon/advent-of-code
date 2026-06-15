package day_02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class Day02 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        // Part 1
        int depth = 0;
        int horizontal = 0;
        
        for (String command : inputLines) {
            String[] parts = command.split("\s+");
            int value = Integer.parseInt(parts[1]);
            
            if (parts[0].equalsIgnoreCase("forward")) {
                horizontal += value;
            } else if (parts[0].equalsIgnoreCase("up")) {
                depth -= value;
            } else if (parts[0].equalsIgnoreCase("down")) {
                depth += value;
            } else {
                System.out.println("unknown command: " + parts[0]);
                System.exit(-1);
            }
        }
        
        System.out.println("horizontal = " + horizontal);
        System.out.println("depth = " + depth);
        System.out.println("Part 1: " + (horizontal * depth));

        // Part 2
        depth = 0;
        horizontal = 0;
        int aim = 0;
        
        for (String command : inputLines) {
            String[] parts = command.split("\s+");
            int value = Integer.parseInt(parts[1]);
            
            if (parts[0].equalsIgnoreCase("forward")) {
                horizontal += value;
                depth += (aim * value);
            } else if (parts[0].equalsIgnoreCase("up")) {
                aim -= value;
            } else if (parts[0].equalsIgnoreCase("down")) {
                aim += value;
            } else {
                System.out.println("unknown command: " + parts[0]);
                System.exit(-1);
            }
        }
        
        System.out.println("horizontal = " + horizontal);
        System.out.println("depth = " + depth);
        System.out.println("Part 2: " + (horizontal * depth));
    }
}
