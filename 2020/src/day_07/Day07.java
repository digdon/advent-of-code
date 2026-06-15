package day_07;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day07 {

    private static final Pattern CONTENT_PATTERN = Pattern.compile("^(\\d+)\\s+(.*)\\s+bag(s)?$");
    
    public static void main(String[] args) {
        List<String> rulesList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;

        
        try {
            while ((inputLine = reader.readLine()) != null) {
                rulesList.add(inputLine);
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

        Map<String, List<BagData>> contentMap = new HashMap<>();
        
        for (String rule : rulesList) {
            String[] ruleParts = rule.split(" bags contain ");
            String[] contentParts = ruleParts[1].split("(,\\s*)|\\.");

            List<BagData> bagContentList = new ArrayList<>();
            
            for (int i = 0; i < contentParts.length; i++) {
                if (contentParts[i].equals("no other bags")) {
                    continue;
                }
                
                Matcher matcher = CONTENT_PATTERN.matcher(contentParts[i]);
                
                if (matcher.matches()) {
                    bagContentList.add(new BagData(Integer.valueOf(matcher.group(1)), matcher.group(2)));
                }
            }
            
            contentMap.put(ruleParts[0], bagContentList);
        }

        // Part one
        int foundCount = 0;
        
        for (String key : contentMap.keySet()) {
            if (bagContains("shiny gold", key, contentMap) == true) {
                foundCount++;
            }
        }
        
        System.out.println("Part one: " + foundCount);
        
        // Part two
        int bagTotal = bagCount("shiny gold", contentMap);
        System.out.println("Part two: " + bagTotal);
    }
    
    private static boolean bagContains(String targetColour, String bag, Map<String, List<BagData>> contentMap) {
        List<BagData> list = contentMap.get(bag);

        boolean found = false;
        
        for (BagData item : list) {
            if (item.getColour().equals(targetColour)) {
                found = true;
                break;
            } else {
                found |= bagContains(targetColour, item.getColour(), contentMap);
            }
        }
        
        return found;
    }

    private static int bagCount(String colour, Map<String, List<BagData>> contentMap) {
        List<BagData> list = contentMap.get(colour);
        int subBagCount = 0;
        
        for (BagData item : list) {
            int count = bagCount(item.getColour(), contentMap);
            subBagCount += (item.getCount() * count) + item.getCount();
        }
        
        return subBagCount;
    }
    
    static class BagData {
        private int count;
        private String colour;
        
        public BagData(int count, String colour) {
            this.count = count;
            this.colour = colour;
        }
        
        public int getCount() {
            return count;
        }
        
        public String getColour() {
            return colour;
        }
    }
}
