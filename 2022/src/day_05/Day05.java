package day_05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day05 {

    private static final Pattern MOVEMENT_COMMAND = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");
    
    public static void main(String[] args) throws IOException {
        // Load in the puzzle input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        boolean processingColumns = true;
        List<String> stackDefinitions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        int maxStackDefLineLength = 0;
        
        while ((line = reader.readLine()) != null) {
            if (line.length() == 0) {
                continue;
            } else if (line.charAt(1) == '1') {
                processingColumns = false;
                continue;
            } else if (processingColumns) {
                stackDefinitions.add(line);
                
                if (line.length() > maxStackDefLineLength) {
                    maxStackDefLineLength = line.length();
                }
            } else {
                commands.add(line);
            }
        }
        
        reader.close();
        
        // Let's work out how many columns there are
        int columnCount = (maxStackDefLineLength + 1) / 4;
        
        // Part 1 - CrateMover 9000
        List<LinkedList<Character>> stackList = buildInitialStackList(stackDefinitions, columnCount);
        processMovementCommands(stackList, commands, false);
        System.out.println("Part 1 (CrateMover 9000): " + buildResultsString(stackList));
        
        // Part 2 - CrateMover 9001
        // Re-build initial stacks
        stackList = buildInitialStackList(stackDefinitions, columnCount);
        processMovementCommands(stackList, commands, true);
        System.out.println("Part 2 (CrateMover 9001): " + buildResultsString(stackList));
    }
    
    private static List<LinkedList<Character>> buildInitialStackList(List<String> stackData, int columnCount) {
        List<LinkedList<Character>> columnLists = new ArrayList<>(columnCount);
        
        for (int i = 0; i < columnCount; i++) {
            columnLists.add(new LinkedList<>());
        }
        
        for (String line : stackData) {
            for (int i = 0; i < columnCount; i++) {
                // Fetch column value
                int pos = (i * 4) + 1;
                char letter = line.charAt(pos);
                
                if (letter != ' ') {
                    columnLists.get(i).add(letter);
                }
            }
        }

        return columnLists;
    }
    
    private static void processMovementCommands(List<LinkedList<Character>> stackList, List<String> movements, boolean grouped) {
        for (String command : movements) {
            Matcher matcher = MOVEMENT_COMMAND.matcher(command);
            
            if (matcher.find() == false) {
                continue;
            }
            
            int itemCount = Integer.parseInt(matcher.group(1));
            int origin = Integer.parseInt(matcher.group(2)) - 1;
            int dest = Integer.parseInt(matcher.group(3)) - 1;
            
            for (int i = 0; i < itemCount; i++) {
                stackList.get(dest).add(grouped ? i : 0, stackList.get(origin).pop());
            }
        }
    }
    
    private static String buildResultsString(List<LinkedList<Character>> stackList) {
        StringBuilder sb = new StringBuilder();
        
        for (LinkedList<Character> stack : stackList) {
            sb.append(stack.peekFirst());
        }
        
        return sb.toString();
    }
}
