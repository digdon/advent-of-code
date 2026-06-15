package day_16;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day16 {

    enum DataMode { FIELD_RANGES, MY_TICKET, NEARBY_TICKETS }
    
    public static void main(String[] args) {
        List<String> ticketInfoList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        
        try {
            while ((inputLine = reader.readLine()) != null) {
                ticketInfoList.add(inputLine);
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

        // Process input
        Map<String, List<MinMax>> fieldRangeMap = new HashMap<>();
        List<Integer> myTicket = new ArrayList<>();
        List<List<Integer>> nearbyTickets = new ArrayList<>();
        
        DataMode currentMode = DataMode.FIELD_RANGES;
        
        for (String data : ticketInfoList) {
            if (data.length() == 0) {
                continue;
            }

            if (data.startsWith("your ticket")) {
                currentMode = DataMode.MY_TICKET;
                continue;
            } else if (data.startsWith("nearby ticket")) {
                currentMode = DataMode.NEARBY_TICKETS;
                continue;
            }

            if (currentMode == DataMode.FIELD_RANGES) {
                int delimPos = data.indexOf(":");
                String fieldName = data.substring(0, delimPos);
                List<MinMax> rangeList = generateRangeList(data.substring(delimPos + 1));
                fieldRangeMap.put(fieldName, rangeList);
            } else if (currentMode == DataMode.MY_TICKET) {
                myTicket = generateValueList(data);
            } else {
                nearbyTickets.add(generateValueList(data));
            }
        }

        // Part one
        int errorRate = 0;
        
        ListIterator<List<Integer>> ticketIter = nearbyTickets.listIterator();
        
        while (ticketIter.hasNext()) {
            boolean remove = false;
            
            for (Integer value : ticketIter.next()) {
                if (isValidValue(value, fieldRangeMap) == false) {
                    errorRate += value;
                    remove = true;
                }
            }
            
            if (remove) {
                // Do this as setup for part two
                ticketIter.remove();
            }
        }

        System.out.println("Part one: " + errorRate);
        
        // Part two
        Map<Integer, Set<String>> cannotBeMap = new HashMap<>();
        
        // Look at each ticket
        for (List<Integer> ticketInfo : nearbyTickets) {
            // Look at each field of the ticket
            for (int column = 0; column < ticketInfo.size(); column++) {
                int fieldValue = ticketInfo.get(column);
                Set<String> cannotBe = cannotBeMap.get(column);
                
                if (cannotBe == null) {
                    cannotBe = new HashSet<>();
                }

                // Look at each entry in the field range map
                for (Entry<String, List<MinMax>> entry : fieldRangeMap.entrySet()) {
                    if (cannotBe.contains(entry.getKey())) {
                        // Already determined that this ticket field cannot be this column number
                        continue;
                    }
                    
                    boolean possibility = false;
                    
                    for (MinMax range : entry.getValue()) {
                        if (fieldValue >= range.min && fieldValue <= range.max) {
                            possibility = true;
                            break;
                        } else {
                            possibility = false;
                        }
                    }
                    
                    if (possibility == false) {
                        // Current value is not valid for the field, so we know that this column cannot be this field
                        cannotBe.add(entry.getKey());
                    }
                }
                
                cannotBeMap.put(column, cannotBe);
            }
        }
        
        // We've worked out what each column CANNOT be - let's start paring it down
        Map<Integer, List<String>> canBeMap = new HashMap<>();
        Set<Integer> completed = new HashSet<>();
        
        for (int i = 0; i < fieldRangeMap.size(); i++) {
            canBeMap.put(i, new ArrayList<>(fieldRangeMap.keySet()));
        }

        // We can start be removing everything we already know is not allowed
        canBeMap.forEach((c, l) -> l.removeAll(cannotBeMap.get(c)));

        // With what's left, look for single-item lists - we know these are worked out and that means other
        // columns cannot be that field, so we can remove those from the other can-be lists
        while (completed.size() < fieldRangeMap.size()) {
            for (int i = 0; i < fieldRangeMap.size(); i++) {
                if (completed.contains(i)) {
                    // Already worked this column out - skip
                    continue;
                }

                List<String> canBe = canBeMap.get(i);
                
                if (canBe.size() == 1) {
                    // Column down to only 1 possibility - remove from others
                    for (int pos = 0; pos < fieldRangeMap.size(); pos++) {
                        if (pos != i) {
                            canBeMap.get(pos).removeAll(canBe);
                        }
                    }
                    
                    completed.add(i);
                }
            }
        }
        
        long partTwoValue = 1;
        
        for (Entry<Integer, List<String>> entry : canBeMap.entrySet()) {
            if (entry.getValue().get(0).startsWith("departure")) {
                partTwoValue *= myTicket.get(entry.getKey());
            }
        }
        
        System.out.println("Part two: " + partTwoValue);
    }

    private static List<Integer> generateValueList(String input) {
        List<Integer> valueList = new ArrayList<>();
        String[] parts = input.split(",");
        
        for (int i = 0; i < parts.length; i++) {
            valueList.add(Integer.valueOf(parts[i]));
        }
        
        return valueList;
    }
        
    private static final Pattern RANGE_PATTERN = Pattern.compile("(\\d+-\\d+)");
    
    private static List<MinMax> generateRangeList(String input) {
        List<MinMax> rangeList = new ArrayList<>();
        Matcher matcher = RANGE_PATTERN.matcher(input);
        
        while (matcher.find()) {
            String[] parts = matcher.group().split("-");
            rangeList.add(new MinMax(Integer.valueOf(parts[0]), Integer.valueOf(parts[1])));
        }
        
        return rangeList;
    }

    private static boolean isValidValue(int value, Map<String, List<MinMax>> rangeMap) {
        for (List<MinMax> rangeList : rangeMap.values()) {
            for (MinMax range : rangeList) {
                if (value >= range.min && value <= range.max) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    static class MinMax {
        int min;
        int max;
        
        public MinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }
}
