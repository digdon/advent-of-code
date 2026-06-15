package day_01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Day01 {

    public static void main(String[] args) {
        List<Integer> expenseList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;

        try {
            while ((inputLine = reader.readLine()) != null) {
                String[] split = inputLine.split("\\s+");
                
                for (int i = 0; i < split.length; i++) {
                    expenseList.add(Integer.valueOf(split[i]));
                }
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
        for (int i = 0; i < expenseList.size(); i++) {
        	for (int j = 0; j < expenseList.size(); j++) {
        	    if (i == j) {
        	        continue;
        	    }
        	    
        	    int valueOne = expenseList.get(i);
        	    int valueTwo = expenseList.get(j);
        	    
        	    if (valueOne + valueTwo == 2020) {
        	        System.out.println(String.format("%d, %d -> %d", valueOne, valueTwo, (valueOne * valueTwo)));
        	    }
        	}
        }
        
        // Part two
        for (int i = 0; i < expenseList.size(); i++) {
            for (int j = 0; j < expenseList.size(); j++) {
                for (int k = 0; k < expenseList.size(); k++) {
                    if (i == j || i == k || j == k) {
                        continue;
                    }
                
                    int valueOne = expenseList.get(i);
                    int valueTwo = expenseList.get(j);
                    int valueThree = expenseList.get(k);
                    
                    if ((valueOne + valueTwo + valueThree) == 2020) {
                        System.out.println(String.format("%d, %d, %d -> %d", valueOne, valueTwo, valueThree, (valueOne * valueTwo * valueThree)));
                    }
                }
            }
        }
    }
}
