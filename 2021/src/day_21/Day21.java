package day_21;

import java.util.HashMap;
import java.util.Map;

public class Day21 {

    public static void main(String[] args) {
//        int p1Pos = 4;
//        int p2Pos = 8;
        int p1Pos = 6;
        int p2Pos = 9;
        
        partOne(p1Pos, p2Pos);
        partTwo(p1Pos, p2Pos);
    }

    private static void partOne(int p1Pos, int p2Pos) {
        int p1Score = 0;
        int p2Score = 0;

        int dieValue = 1;
        int rollCount = 0;
        boolean p1Turn = true;
        
        while (p1Score < 1000 && p2Score < 1000) {
            int rollTotal = 0;
            
            for (int i = 0; i < 3; i++) {
                rollTotal += dieValue;
                dieValue = (dieValue % 100) + 1;
                rollCount++;
            }
            
            if (p1Turn) {
                p1Pos = movePlayer(p1Pos, rollTotal);
                p1Score += p1Pos;
            } else {
                p2Pos = movePlayer(p2Pos, rollTotal);
                p2Score += p2Pos;
            }
            
            p1Turn = !p1Turn;
        }

        System.out.println("Part 1: " + rollCount * Math.min(p1Score, p2Score));
    }
    
    private static int movePlayer(int pos, int steps) {
        return ((pos - 1 + steps) % 10) + 1;
    }
    
    private static void partTwo(int p1Pos, int p2Pos) {
        UniverseResult results = playGame(0, 0, p1Pos, p2Pos, 0, 0);
        
        System.out.println("Part 2: " + Math.max(results.p1Wins, results.p2Wins));
    }

    private static final Map<String, UniverseResult> UNIVERSE_RESULTS = new HashMap<>();
    
    private static UniverseResult playGame(int p1Score, int p2Score, int p1Pos, int p2Pos, int turn, int rollTotal) {
        boolean isP1Turn = turn < 3;
        boolean lastP1Throw = turn == 2;
        boolean lastP2Throw = turn == 5;
        
        String cacheKey = String.format("%d-%d-%d-%d-%d-%d", p1Score, p2Score, p1Pos, p2Pos, turn, rollTotal);

        UniverseResult results = null;

        if (p1Score >= 21) {
            results = new UniverseResult(1, 0);
        } else if (p2Score >= 21) {
            results = new UniverseResult(0, 1);
        } else {
            if (UNIVERSE_RESULTS.containsKey(cacheKey)) {
                return UNIVERSE_RESULTS.get(cacheKey);
            }
            
            int nextTurn = (turn + 1) % 6;
            
            long p1Wins = 0;
            long p2Wins = 0;
            
            for (int roll = 1; roll <= 3; roll++) {
                UniverseResult result = playGame(
                        isP1Turn ? p1Score + (lastP1Throw ? movePlayer(p1Pos, roll)  : 0) : p1Score,
                        !isP1Turn ? p2Score + (lastP2Throw ? movePlayer(p2Pos, roll)  : 0) : p2Score,
                        isP1Turn ? movePlayer(p1Pos, roll) : p1Pos,
                        !isP1Turn ? movePlayer(p2Pos, roll) : p2Pos,
                        nextTurn,
                        turn % 3 == 0 ? 0 : rollTotal + roll
                );

                p1Wins = p1Wins += result.p1Wins;
                p2Wins = p2Wins += result.p2Wins;
            }
            
            results = new UniverseResult(p1Wins, p2Wins);
        }
        
        UNIVERSE_RESULTS.put(cacheKey, results);
        
        return results;
    }
    
    static class UniverseResult {
        long p1Wins;
        long p2Wins;
        
        public UniverseResult(long p1Wins, long p2Wins) {
            this.p1Wins = p1Wins;
            this.p2Wins = p2Wins;
        }
    }
}
