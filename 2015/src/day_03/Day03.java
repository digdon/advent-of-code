package day_03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day03 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        partOne(inputLines.get(0));
        partTwo(inputLines.get(0));
    }
    
    private static void partOne(String directions) {
        int x = 0;
        int y = 0;
        Map<String, Integer> deliveryMap = new HashMap<>();
        deliveryMap.put(generatePoint(x, y), 1);
        
        for (int i = 0; i < directions.length(); i++) {
            switch (directions.charAt(i)) {
                case '^':
                    y += 1;
                    break;
                    
                case '>':
                    x += 1;
                    break;
                    
                case 'v':
                    y -= 1;
                    break;
                    
                case '<':
                    x -= 1;
                    break;
            }
            
            String point = generatePoint(x, y);
            Integer count = deliveryMap.get(point);
            
            if (count == null) {
                count = 0;
            }
            
            deliveryMap.put(point, count + 1);
        }
        
        System.out.println("Part one: " + deliveryMap.size());
    }
    
    private static void partTwo(String directions) {
        int santaX = 0;
        int santaY = 0;
        int robotX = 0;
        int robotY = 0;
        Map<String, Integer> deliveryMap = new HashMap<>();
        deliveryMap.put(generatePoint(santaX, santaY), 1);
        deliveryMap.put(generatePoint(robotX, robotY), 1);
        boolean santa = true;
        
        for (int i = 0; i < directions.length(); i++) {
            switch (directions.charAt(i)) {
                case '^':
                    if (santa) {
                        santaY += 1;
                    } else {
                        robotY += 1;
                    }
                    break;
                    
                case '>':
                    if (santa) {
                        santaX += 1; 
                    } else {
                        robotX += 1;
                    }
                    break;
                    
                case 'v':
                    if (santa) {
                        santaY -= 1;
                    } else {
                        robotY -= 1;
                    }
                    break;
                    
                case '<':
                    if (santa) {
                        santaX -= 1;
                    } else {
                        robotX -= 1;
                    }
                    break;
            }

            String point = santa ? generatePoint(santaX, santaY) : generatePoint(robotX, robotY);
            Integer count = deliveryMap.get(point);
            
            if (count == null) {
                count = 0;
            }

            deliveryMap.put(point, count + 1);
            santa = !santa;
        }
        
        System.out.println("Part two: " + deliveryMap.size());
    }
    
    private static String generatePoint(int x, int y) {
        return String.format("%05d,%05d", x, y);
    }
}
