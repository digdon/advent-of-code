package day_02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day02 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        // Part one
        int totalPaperArea = 0;
        int totalRibbonLength = 0;
        
        for (String data : inputLines) {
            List<Integer> dimensions = Arrays.asList(data.split("x")).stream().map(x -> Integer.valueOf(x)).collect(Collectors.toList());
            Collections.sort(dimensions);
            int l = dimensions.get(0);
            int w = dimensions.get(1);
            int h = dimensions.get(2);
            
            int area = (2 * l * w) + (2 * w * h) + (2 * h * l);
            area += (l * w);
            totalPaperArea += area;
            
            int ribbon = (l * 2) + (w * 2) + (l * w * h);
            totalRibbonLength += ribbon;
        }
        
        System.out.println("Part one: " + totalPaperArea);
        System.out.println("Part two: " + totalRibbonLength);
    }
}
