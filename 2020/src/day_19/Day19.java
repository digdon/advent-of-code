package day_19;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day19 {
    
    public static void main(String[] args) {
        InputData inputData = getInput();
        String regex = inputData.rules.get(0);
        
        while (!regex.matches("^[a-z \"\\|\\(\\)]+$")) {
            final StringBuilder builder = new StringBuilder();
            for (String part : regex.split(" ")) {
                if (part.matches("[0-9]+")) {
                    builder.append("( ").append(inputData.rules.get(Integer.parseInt(part))).append(" )");
                } else {
                    builder.append(part).append(' ');
                }
            }

            regex = builder.toString();
        }

        System.out.println(regex);
        final String pattern = "^" + regex.replaceAll("[ \"]", "") + "$";
        System.out.println(pattern);
        System.out.println(inputData.messages.stream().filter(a -> a.matches(pattern)).count());
        
        inputData.rules.put(8, "42 | 42 8");
        inputData.rules.put(11, "42 31 | 42 11 31");

        regex = inputData.rules.get(0);
        long prev = 0;
        while (true) {
            final StringBuilder builder = new StringBuilder();
            for (String part : regex.split(" ")) {
                if (part.matches("[0-9]+")) {
                    builder.append("( ").append(inputData.rules.get(Integer.parseInt(part))).append(" )");
                } else {
                    builder.append(part).append(' ');
                }
            }

            regex = builder.toString();

            final String pattern2 = "^" + regex.replaceAll("([ \"])|42|31", "") + "$";

            final long count = inputData.messages.stream().filter(a -> a.matches(pattern2)).count();
            if (count > 0 && count == prev) {
//                return count;
                break;
            }
            
            prev = count;
        }
        
        System.out.println(prev);
    }
    
    private static InputData getInput() {
        final List<String> input = getInputLines();
        final HashMap<Integer, String> rules = new HashMap<>();
        int i = 0;
        for (;; i++) {
            final String line = input.get(i);
            if (line.isEmpty()) {
                i++;
                break;
            }

            final String[] parts = line.split(": ");
            rules.put(Integer.parseInt(parts[0]), parts[1]);
        }

        final List<String> messages = new ArrayList<>();
        for (; i < input.size(); i++) {
            messages.add(input.get(i));
        }

        return new InputData(rules, messages);
    }

    private static List<String> getInputLines() {
        List<String> inputLines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        
        try {
            while ((inputLine = reader.readLine()) != null) {
                inputLines.add(inputLine);
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
        
        return inputLines;
    }

    private static class InputData {
        public final HashMap<Integer, String> rules;
        public final List<String> messages;

        public InputData(HashMap<Integer, String> rules, List<String> messages) {
            this.rules = rules;
            this.messages = messages;
        }
    }
}
