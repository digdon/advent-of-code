package day_12;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Day12 {

    private static final int[][] DIRECTIONS = new int[][] {
        { 0, 1 },
        { 1, 0 },
        { 0, -1 },
        { -1, 0 }
    };
    
    public static void main(String[] args) {
        List<String> instructionList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        
        try {
            while ((inputLine = reader.readLine()) != null) {
                instructionList.add(inputLine);
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

        partOne(instructionList);
        partTwo(instructionList);
    }
    
    private static void partOne(List<String> instructionList) {
        int x = 0;
        int y = 0;
        int direction = 90;
        
        for (String instruction : instructionList) {
            char movement = instruction.charAt(0);
            int value = Integer.valueOf(instruction.substring(1));
            
            switch (movement) {
                case 'F':
                    x += (DIRECTIONS[direction / 90][0] * value);
                    y += (DIRECTIONS[direction / 90][1] * value);
                    break;
                    
                case 'N':
                    y += value;
                    break;
                    
                case 'E':
                    x += value;
                    break;
                    
                case 'S':
                    y -= value;
                    break;
                    
                case 'W':
                    x -= value;
                    break;
                    
                case 'L':
                    direction = (direction + (360 - value)) % 360;
                    break;
                    
                case 'R':
                    direction = (direction + value) % 360;
                    break;
            }                        
        }
        
        System.out.println(String.format("x = %d, y = %d", x, y));
        System.out.println("Part one: " + (Math.abs(x) + Math.abs(y)));
    }
    
    private static void partTwo(List<String> instructionList) {
        int wpX = 10;
        int wpY = 1;
        int shipX = 0;
        int shipY = 0;
        
        for (String instruction : instructionList) {
            char movement = instruction.charAt(0);
            int value = Integer.valueOf(instruction.substring(1));
            
            switch (movement) {
                case 'F':
                    shipX += (wpX * value);
                    shipY += (wpY * value);
                    break;
                    
                case 'N':
                    wpY += value;
                    break;
                    
                case 'S':
                    wpY -= value;
                    break;
                    
                case 'E':
                    wpX += value;
                    break;
                    
                case 'W':
                    wpX -= value;
                    break;

                case 'R':
                    value = -value; // To account for right-hand orientation
                    
                case 'L':
                    double radians = Math.toRadians(value);
                    double tempX = wpX * Math.cos(radians) - wpY * Math.sin(radians);
                    double tempY = wpX * Math.sin(radians) + wpY * Math.cos(radians);
                    wpX = (int)Math.round(tempX);
                    wpY = (int)Math.round(tempY);
                    break;
            }
        }
        
        System.out.println(String.format("x = %d, y = %d", shipX, shipY));
        System.out.println("Part one: " + (Math.abs(shipX) + Math.abs(shipY)));
    }
}
