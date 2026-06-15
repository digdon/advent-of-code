package day_15;

import java.util.HashMap;
import java.util.Map;

public class Day15 {

    public static void main(String[] args) {
        String puzzleInput = "1,0,16,5,17,4";
        System.out.println("Part one: " + playTheGame(puzzleInput, 2020));
        System.out.println("Part two: " + playTheGame(puzzleInput, 30000000));
    }
    
    private static int playTheGame(String input, int maxTurns) {
        Map<Integer, Integer> recentMap = new HashMap<>();
        int turn = 0;

        String[] startNumbers = input.split(",");
        
        for (int i = 0; i < startNumbers.length - 1; i++) {
            // Process everything but the last number
            int number = Integer.valueOf(startNumbers[i]);
            turn++;
            recentMap.put(number, turn);
        }

        // Grab the last starting number and start working from there
        int current = 0;
        int nextNumber = Integer.valueOf(startNumbers[startNumbers.length - 1]);
        
        do {
            current = nextNumber;
            turn++;
            Integer lastSeen = recentMap.get(current);
            
            if (lastSeen == null || lastSeen == turn) {
                // New number - the next one must be zero
                nextNumber = 0;
            } else {
                nextNumber = turn - lastSeen;
            }
            
            recentMap.put(current, turn);
        } while (turn < maxTurns);
        
        return current;
    }
}
