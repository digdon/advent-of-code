package day_07;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Day07 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = reader.readLine();
        reader.close();
        
        String[] parts = inputLine.split(",");
        int[] positions = new int[parts.length];
        int positionSums = 0;
        
        for (int i = 0; i < parts.length; i++) {
            int value = Integer.parseInt(parts[i]);
            positions[i] = value;
            positionSums += value;
        }

        Arrays.sort(positions);
        
        int itemCount = positions.length;

        // Part 1
        // Calculate the median
        int median = 0;
        
        if (itemCount % 2 == 1) {
            median = positions[(itemCount / 2) + 1];
        } else {
            int middle = itemCount / 2;
            median = (positions[middle] + positions[middle - 1]) / 2;
        }

        // Calculate the fuel costs (just summing the diffs between the current position and target [the median])
        int fuelCount = 0;
        
        for (int i = 0; i < itemCount; i++) {
            fuelCount += Math.abs(positions[i] - median);
        }
        
        System.out.println("Part 1: " + fuelCount);
        
        // Part 2
        // Calculate the mean
        double mean = (double)positionSums / (double)itemCount;

        // mean may not be a whole number, so calculate based on the floor and ceiling
        int floorFuelCount = calculateFuelCost(positions, (int)Math.floor(mean));
        int ceilFuelCount = calculateFuelCost(positions, (int)Math.ceil(mean));
        
        System.out.println("Part 2: " + (floorFuelCount < ceilFuelCount ? floorFuelCount : ceilFuelCount));
    }
    
    private static int calculateFuelCost(int[] positions, int target) {
        int fuelTotal = 0;
        
        for (int i = 0; i < positions.length; i++) {
            int diff = Math.abs(positions[i] - target);
            fuelTotal += ((diff * (diff + 1)) / 2);
        }
        
        return fuelTotal;
    }
}
