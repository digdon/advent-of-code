package day_02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day02 {

    private static final Pattern ENTRY_PATTERN = Pattern.compile("(\\d+)\\-(\\d+)\\s+(\\w+):\\s*(\\w+)");
    
    public static void main(String[] args) {
        List<String> pwList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
    
        try {
            while ((inputLine = reader.readLine()) != null) {
                pwList.add(inputLine);
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

        // Part one
        int partOneCount = 0;
        
        for (String entry : pwList) {
            Matcher matcher = ENTRY_PATTERN.matcher(entry);
            
            if (matcher.matches()) {
                int min = Integer.valueOf(matcher.group(1));
                int max = Integer.valueOf(matcher.group(2));
                char letter = matcher.group(3).charAt(0);
                String pw = matcher.group(4);
                
                String patternString = "^([^" + letter + "]*" + letter + "){" + min + "," + max + "}[^" + letter + "]*$";
                boolean matches = Pattern.matches(patternString, pw);
                System.out.println(entry + " -> " + matches);
                
                if (matches) {
                    partOneCount++;
                }
            }
        }
        
        System.out.println("Part one result: " + partOneCount);
        
        // Part two
        int partTwoCount = 0;
        
        for (String entry : pwList) {
            Matcher matcher = ENTRY_PATTERN.matcher(entry);
            
            if (matcher.matches()) {
                int posOne = Integer.valueOf(matcher.group(1));
                int posTwo = Integer.valueOf(matcher.group(2));
                char letter = matcher.group(3).charAt(0);
                String pw = matcher.group(4);
                
                char charOne = pw.charAt(posOne - 1);
                char charTwo = pw.charAt(posTwo - 1);
                
                if (charOne == letter ^ charTwo == letter) {
                    partTwoCount++;
                }
            }
        }
        
        System.out.println("Part two result: " + partTwoCount);
    }
}
