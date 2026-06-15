package day_05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day05 {

    private static final Pattern BOARDING_PASS_ELEMENTS = Pattern.compile("^([FB]+)([LR]+)$");
    
    public static void main(String[] args) {
        List<String> boardingPassList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;

        try {
            while ((inputLine = reader.readLine()) != null) {
                boardingPassList.add(inputLine);
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
        
        int max = 0;
        SortedSet<Integer> seatSet = new TreeSet<>();
        
        for (String bp : boardingPassList) {
            int seatId = calculateSeatId(bp);
            
            if (seatId > max) {
                max = seatId;
            }
            
            seatSet.add(seatId);
        }
        
        System.out.println("Part one: " + max);
        Integer[] seatList = seatSet.toArray(new Integer[seatSet.size()]);
        
        for (int i = 0; i < seatList.length - 1; i++) {
            int first = seatList[i];
            int second = seatList[i + 1];
            
            if (second - first == 2) {
                System.out.println(String.format("Part two: Found empty seat: %d <-> %d = %d", first, second, first + 1));
            }
        }
    }
    
    private static int calculateSeatId(String boardingPass) {
        Matcher matcher = BOARDING_PASS_ELEMENTS.matcher(boardingPass);
        
        if (matcher.matches() == false) {
            return -1;
        }
        
        String rowString = matcher.group(1);
        String columnString = matcher.group(2);
        
        int row = calculateBSP(rowString);
        int col = calculateBSP(columnString);
        
        return (row * 8) + col;
    }
    
    private static int calculateBSP(String input) {
        int min = 0;
        int max = (int)(Math.pow(2, input.length())) - 1;
        
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            
            switch (c) {
                case 'F':
                case 'L':
                    max = max - ((max - min) / 2) - 1;
                    break;
                    
                case 'B':
                case 'R':
                    min = min + ((max - min) / 2) + 1;
                    break;
                    
                default:
                    return -1;
            }
        }
        
        return min;
    }
}
