package day_06;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day06 {

    public static void main(String[] args) {
        List<String> inputList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;

        
        try {
            while ((inputLine = reader.readLine()) != null) {
                inputList.add(inputLine);
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
        int questionCount = 0;
        Set<Character> yesSet = new HashSet<>();
        
        for (String line : inputList) {
            if (line.length() == 0) {
                // end of record
                questionCount += yesSet.size();
                yesSet.clear();
            } else {
                for (int i = 0; i < line.length(); i++) {
                    yesSet.add(line.charAt(i));
                }
            }
        }
        
        questionCount += yesSet.size();
        System.out.println("Part one: " + questionCount);
        
        // Part two
        Map<Character, Integer> allYesMap = new HashMap<>();
        int runningAllYesCount = 0;
        int personCount = 0;
        
        for (String line : inputList) {
            if (line.length() == 0) {
                // end of record
                int allYesCount = 0;
                
                for (Integer count : allYesMap.values()) {
                    if (count == personCount) {
                        allYesCount++;
                    }
                }

                runningAllYesCount += allYesCount;
                personCount = 0;
                allYesMap.clear();
            } else {
                personCount++;
                
                for (int i = 0; i < line.length(); i++) {
                    Integer count = allYesMap.get(line.charAt(i));
                    
                    if (count == null) {
                        count = 0;
                    }
                    
                    allYesMap.put(line.charAt(i), count + 1);
                }
            }
        }
        
        int allYesCount = 0;
        
        for (Integer count : allYesMap.values()) {
            if (count == personCount) {
                allYesCount++;
            }
        }

        runningAllYesCount += allYesCount;
        System.out.println("Part two: " + runningAllYesCount);
    }
}
