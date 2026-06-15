package day_01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class Day01 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLInes = reader.lines().collect(Collectors.toList());
        reader.close();
        
        // Part one
        String input = inputLInes.get(0);
        int floor = 0;
        
        for (int i = 0; i < input.length(); i++) {
            floor += (input.charAt(i) == '(') ? 1 : -1;
        }
        
        System.out.println("Part one: " + floor);
        
        // Part two
        floor = 0;
        int pos = 0;
        
        while (pos < input.length() && floor != -1) {
            floor += (input.charAt(pos) == '(') ? 1 : -1;
            pos++;
        }
        
        System.out.println("Part two: " + pos);
    }
}
