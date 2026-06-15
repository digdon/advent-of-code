package day_13;

import java.util.Scanner;

public class Day13 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int minTime = scanner.nextInt();
        String[] busList = scanner.next().split(",");
        scanner.close();
        
        // Part one
        int closestBusId = 0;
        int closestBusTime = Integer.MAX_VALUE;
        
        for (int i = 0; i < busList.length; i++) {
            if (busList[i].equals("x")) {
                continue;
            }
            
            int id = Integer.valueOf(busList[i]);
            int multiplier = minTime / id;
            int nextTime = id * multiplier;
            
            if (nextTime < minTime) {
                nextTime += id;
            }

            if (nextTime < closestBusTime) {
                closestBusId = id;
                closestBusTime = nextTime;
            }
            
            System.out.println(String.format("%d -> %d, %d", id, multiplier, nextTime));
        }
        
        int timeDiff = closestBusTime - minTime;
        System.out.println("Part one: " + closestBusId + ", " + timeDiff + " mins -> " + (timeDiff * closestBusId));
        
        // Part two
        int[] buses = new int[busList.length];
        int[] offsets = new int[busList.length];
        long timestamp = 0;
        long step = 1;
        
        for (int i = 0; i < busList.length; i++) {
            if (busList[i].equals("x")) {
                buses[i] = -1;
            } else {
                buses[i] = Integer.valueOf(busList[i]);
            }
            
            offsets[i] = i;
        }

        for (int i = 0; i < buses.length; i++) {
            if (buses[i] == -1) {
                continue;
            }

            System.out.println("timestamp = " + timestamp);
            System.out.println("step = " + step);
            System.out.println("bus = " + buses[i] + ", offset = " + offsets[i]);
            
            while ((timestamp + offsets[i]) % buses[i] != 0) {
                timestamp += step;
                System.out.println(timestamp + ", " + (timestamp + offsets[i]) + ", " + (timestamp + offsets[i]) % buses[i]);
            }
            
            step *= buses[i];
        }
        
        System.out.println("Part two: " + timestamp);
    }
}
