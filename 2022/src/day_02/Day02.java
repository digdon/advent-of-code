package day_02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day02 {

    private static final char OPPONENT_ROCK = 'A';
    private static final char OPPONENT_PAPER = 'B';
    private static final char OPPONENT_SCISSORS = 'C';
    private static final char MY_ROCK = 'X';
    private static final char MY_PAPER = 'Y';
    private static final char MY_SCISSORS = 'Z';
    private static final char PART2_LOSE = 'X';
    private static final char PART2_DRAW = 'Y';
    private static final char PART2_WIN = 'Z';

    public static void main(String[] args) throws IOException {
        // Load in the puzzle input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        // Build the various outcomes

        // Part 1 map - winning, losing, draw moves and the appropriate score for each
        Map<Character, Map<Character, Integer>> part1Map = new HashMap<>();
        part1Map.put(OPPONENT_ROCK, Map.of(MY_PAPER, 6, MY_SCISSORS, 0, MY_ROCK, 3));
        part1Map.put(OPPONENT_PAPER, Map.of(MY_SCISSORS, 6, MY_ROCK, 0, MY_PAPER, 3));
        part1Map.put(OPPONENT_SCISSORS, Map.of(MY_ROCK, 6, MY_PAPER, 0, MY_SCISSORS, 3));

        // Part 2 map - maps X/Y/Z to losing, draw, and winning moves, respectively
        Map<Character, Map<Character, Character>> part2Map = new HashMap<>();
        part2Map.put(OPPONENT_ROCK, Map.of(PART2_LOSE, MY_SCISSORS, PART2_DRAW, MY_ROCK, PART2_WIN, MY_PAPER));
        part2Map.put(OPPONENT_PAPER, Map.of(PART2_LOSE, MY_ROCK, PART2_DRAW, MY_PAPER, PART2_WIN, MY_SCISSORS));
        part2Map.put(OPPONENT_SCISSORS, Map.of(PART2_LOSE, MY_PAPER, PART2_DRAW, MY_SCISSORS, PART2_WIN, MY_ROCK));
        
        // Run the rounds
        int part1TotalScore = 0;
        int part2TotalScore = 0;
        
        for (String line : inputLines) {
            char opponentMove = line.charAt(0);
            char myMove = line.charAt(2);

            // Part 1
            int roundScore = (myMove == MY_ROCK ? 1 : (myMove == MY_PAPER ? 2 : 3));
            roundScore += part1Map.get(opponentMove).get(myMove);
            part1TotalScore += roundScore;
            
            // Part 2
            // Start by mapping letter to appropriate move
            char mappedMove = part2Map.get(opponentMove).get(myMove);
            roundScore = (mappedMove == MY_ROCK ? 1 : (mappedMove == MY_PAPER ? 2 : 3));
            roundScore += (myMove == PART2_LOSE ? 0 : (myMove == PART2_DRAW ? 3 : 6));
            part2TotalScore += roundScore;
        }
        
        System.out.println(part1TotalScore);
        System.out.println(part2TotalScore);
    }
}
