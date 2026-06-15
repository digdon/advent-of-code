package day_11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day11 {

    public static void main(String[] args) throws IOException {
        // Load in the puzzle input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        // Part 1
        List<Monkey> monkeyList = buildMonkeyList(inputLines);
        Map<Integer, Monkey> monkeyMap = monkeyList.stream().collect(Collectors.toMap(Monkey::getId, Function.identity()));
        runPartOne(monkeyList, monkeyMap);
        
        // Part 2
        monkeyList = buildMonkeyList(inputLines);
        monkeyMap = monkeyList.stream().collect(Collectors.toMap(Monkey::getId, Function.identity()));
        runPartTwo(monkeyList, monkeyMap);
    }
    
    private static List<Monkey> buildMonkeyList(List<String> inputLines) {
        // Process the input to build the monkey list
        List<Monkey> monkeyList = new ArrayList<>();
        Monkey currentMonkey = null;
        
        for (String line : inputLines) {
            if (line.length() == 0) {
                // empty line, continue
                continue;
            }
            
            String[] parts = line.split("[:,\\s]+");
            
            if (parts[0].startsWith("Monkey")) {
                if (currentMonkey != null) {
                    // Monkey already being built. It's now finished, so add it to the list/map
                    monkeyList.add(currentMonkey);
                }
                
                int monkeyId = Integer.parseInt(parts[1]);
                currentMonkey = new Monkey(monkeyId);
            } else if (parts[1].equals("Starting")) {
                // Starting item list
                for (int i = 3; i < parts.length; i++) {
                    currentMonkey.addItem(Integer.parseInt(parts[i]));
                }
            } else if (parts[1].equals("Operation")) {
                // The mathematical operation to perform
                if (parts[5].equals("+")) {
                    currentMonkey.setOperation(Operation.ADD);
                    currentMonkey.setOperand(Long.parseLong(parts[6]));
                } else {
                    if (parts[6].equals("old")) {
                        currentMonkey.setOperation(Operation.SQUARE);
                    } else {
                        currentMonkey.setOperation(Operation.MULTIPLY);
                        currentMonkey.setOperand(Long.parseLong(parts[6]));
                    }
                }
            } else if (parts[1].equals("Test")) {
                currentMonkey.setTestDivisible(Long.parseLong(parts[4]));
            } else if (parts[1].equals("If")) {
                if (parts[2].equals("true")) {
                    currentMonkey.setTestTrue(Integer.parseInt(parts[6]));
                } else {
                    currentMonkey.setTestFalse(Integer.parseInt(parts[6]));
                }
            }
        }
        
        monkeyList.add(currentMonkey);
        
        return monkeyList;
    }

    private static void runPartOne(List<Monkey> monkeyList, Map<Integer, Monkey> monkeyMap) {
        runTheRounds(20, 3, monkeyList, monkeyMap);
        
        List<Long> list = monkeyList.stream()
                .map(m -> m.getInspectionCount())
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        
        System.out.println("Part 1: " + (list.get(0) * list.get(1)));
    }


    private static void runPartTwo(List<Monkey> monkeyList, Map<Integer, Monkey> monkeyMap) {
        runTheRounds(10000, 1, monkeyList, monkeyMap);
        
        List<Long> list = monkeyList.stream()
                .map(m -> m.getInspectionCount())
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        
        System.out.println("Part 2: " + (list.get(0) * list.get(1)));
    }

    private static void runTheRounds(int roundCount, long relief, List<Monkey> monkeyList, Map<Integer, Monkey> monkeyMap) {
        long mainDivisor = monkeyList.stream().map(m -> m.getTestDivisible()).reduce(1l,  (a, b) -> a * b);
        
        for (int round = 1; round <= roundCount; round++) {
            for (Monkey monkey : monkeyList) {
                ListIterator<Long> iterator = monkey.getItems().listIterator();
                
                while (iterator.hasNext()) {
                    long worry = iterator.next();
                    
                    // apply the worry operation
                    switch (monkey.getOperation()) {
                        case ADD -> worry += monkey.getOperand();
                        case MULTIPLY -> worry *= monkey.getOperand();
                        case SQUARE -> worry *= worry;
                        default ->
                            throw new IllegalArgumentException("Unexpected value: " + monkey.getOperation());
                    }
                    
                    monkey.incrementInspectionCount();
                    
                    // apply the boredom operation
                    worry /= relief;

                    // apply the master divisor
                    worry %= mainDivisor;

                    // run the test
                    long result = worry % monkey.getTestDivisible();
                    int targetMonkey = (result == 0 ? monkey.getTestTrue() : monkey.getTestFalse());
                    
                    // move item to target monkey
                    iterator.remove();
                    monkeyMap.get(targetMonkey).addItem(worry);
                }
            }
            
            if (round == 1 || round == 20 || round % 1000 == 0) {
                System.out.println("== After round " + round + " ==");
                monkeyList.forEach(m -> System.out.println(String.format("Monkey %d inspected items %d times", m.getId(), m.getInspectionCount())));
                System.out.println();
            }
        }
    }
}
