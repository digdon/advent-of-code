package day_03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Day03 {

    public static void main(String[] args) {
        List<String> gridList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
    
        try {
            while ((inputLine = reader.readLine()) != null) {
                gridList.add(inputLine);
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
        System.out.println("Part one: " + treeCount(gridList, 3, 1));
        
        // Part two
        int[] rightValues = { 1, 3, 5, 7, 1 };
        int[] downValues = { 1, 1, 1, 1, 2 };
        long runningTotal = 1;
        
        for (int i = 0; i < rightValues.length; i++) {
            int count = treeCount(gridList, rightValues[i], downValues[i]);
            System.out.println(count);
            runningTotal *= count;
        }
        
        System.out.println("Part two: " + runningTotal);
    }
    
    private static int treeCount(List<String> grid, int right, int down) {
        int row = 0;
        int col = 0;
        int treeCount = 0;
        
        while (row < grid.size()) {
            row += down;
            col += right ;

            if (row >= grid.size()) {
                break;
            }
            
            String string = grid.get(row);
            col = col % string.length();
            
            if (grid.get(row).charAt(col) == '#') {
                treeCount++;
            }
        }
        
        return treeCount;
    }
}
