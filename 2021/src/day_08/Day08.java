package day_08;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day08 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        // Parse the input lines
        List<Note> notes = new ArrayList<>();
        
        for (String line : inputLines) {
            int pos = line.indexOf('|');
            String[] signals = line.substring(0, pos).strip().split("\\s+");
            String[] output = line.substring(pos + 1).strip().split("\\s+");
            notes.add(new Note(signals, output));
        }
        
        // Part 1
        int easyCount = 0;
        
        for (Note note : notes) {
            String[] output = note.output;

            for (int i = 0; i < output.length; i++) {
                int length = output[i].length();
                
                if (length == 2 || length == 3 || length == 4 || length == 7) {
                    // A 1, 4, 7, or 8
                    easyCount++;
                }
            }
        }
        
        System.out.println("Part 1: " + easyCount);
        
        // Part 2
        int runningTotal = 0;
        
        for (Note note : notes) {
            Map<String, Integer> signalMap = buildSignalMap(note.signals);
            
            StringBuilder sb = new StringBuilder();
            
            for (int i = 0; i < note.output.length; i++) {
                String output = note.output[i];
                char[] charArray = output.toCharArray();
                Arrays.sort(charArray);
                output = String.valueOf(charArray);
//                System.out.print(output + " ");
                sb.append(signalMap.get(output));
            }

            int value = Integer.parseInt(sb.toString());
            runningTotal += value;
//            System.out.println(": " + value);
        }
        
        System.out.println("Part 2: " + runningTotal);
    }

    private static Map<String, Integer> buildSignalMap(String[] signals) {
        Map<String, Integer> signalMap = new HashMap<>();

        List<String> listOf5 = new ArrayList<>();
        List<String> listOf6 = new ArrayList<>();
        Set<Character> setCF = null;
        Set<Character> setBD = null;

        // Find known values
        for (int i = 0; i < signals.length; i++) {
            String signal = signals[i];
            
            // Need to re-order the characters in the signal alphabetically
            char[] charArray = signal.toCharArray();
            Arrays.sort(charArray);
            signal = String.valueOf(charArray);
            
            int length = signal.length();
            
            if (length == 2) {
                setCF = signal.chars().mapToObj(c -> (char)c).collect(Collectors.toSet());
                signalMap.put(signal, 1);
            } else if (length == 3) {
                signalMap.put(signal, 7);
            } else if (length == 4) {
                setBD = signal.chars().mapToObj(c -> (char)c).collect(Collectors.toSet());
                signalMap.put(signal, 4);
            } else if (length == 7) {
                signalMap.put(signal, 8);
            } else if (length == 5) {
                listOf5.add(signal);
            } else if (length == 6) {
                listOf6.add(signal);
            }
        }
        
        setBD.removeAll(setCF);

        // Work through the list of 5-wire signals
        for (String signal : listOf5) {
            Set<Character> set = signal.chars().mapToObj(c -> (char)c).collect(Collectors.toSet());
            
            if (set.containsAll(setBD)) {
                // This is a '5'
                signalMap.put(signal, 5);
            } else if (set.containsAll(setCF)) {
                // This is a '3'
                signalMap.put(signal, 3);
            } else {
                // This is a 2, by default
                signalMap.put(signal, 2);
            }
        }

        // Work through the list of 6-wire signals
        for (String signal : listOf6) {
            Set<Character> set = signal.chars().mapToObj(c -> (char)c).collect(Collectors.toSet());
            
            if (set.containsAll(setCF) && set.containsAll(setBD)) {
                // This is a '9'
                signalMap.put(signal, 9);
            } else if (set.containsAll(setBD)) {
                // This is a '6'
                signalMap.put(signal, 6);
            } else {
                // This is a '0', by default
                signalMap.put(signal, 0);
            }
        }
        
        return signalMap;
    }
    
    static class Note {
        String[] signals;
        String[] output;
        
        Note(String[] signals, String[] output) {
            this.signals = signals;
            this.output = output;
        }
    }
}
