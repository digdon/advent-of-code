package day_21;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Day21 {

    public static void main(String[] args) {
        List<String> foodList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        
        try {
            while ((inputLine = reader.readLine()) != null) {
                foodList.add(inputLine);
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
        
        Map<String, List<String>> allergenIngredientMap = new HashMap<>();
        Map<String, Integer> ingredientCountMap = new HashMap<>();
        Set<String> nonAllergenSet = new HashSet<>();
        
        for (String food : foodList) {
            String[] ingredientList = null;
            String[] allergenList = null;
            int allergenIdx = food.indexOf("(contains ");
            
            if (allergenIdx > -1) {
                // contains allergens - get the list
                allergenList = food.substring(food.indexOf(' ', allergenIdx) + 1, food.length() - 1).split("\\s*,\\s*");
                ingredientList = food.substring(0, allergenIdx - 1).split(" ");
            } else {
                ingredientList = food.split(" ");
            }
            
            nonAllergenSet.addAll(Arrays.asList(ingredientList));
            Set<String> ingredientSet = new HashSet<>(Arrays.asList(ingredientList));

            for (int i = 0; i < ingredientList.length; i++) {
                Integer count = ingredientCountMap.get(ingredientList[i]);
                
                if (count == null) {
                    count = 0;
                }
                
                ingredientCountMap.put(ingredientList[i], ++count);
            }
            
            for (String allergen : allergenList) {
                List<String> allergenIngredientList = allergenIngredientMap.get(allergen);
                
                if (allergenIngredientList == null) {
                    allergenIngredientMap.put(allergen, new ArrayList<>(ingredientSet));
                } else {
                    allergenIngredientList.retainAll(ingredientSet);
                }
            }
        }
        
        Set<String> completed = new HashSet<>();
        
        // With what's left, look for single-item sets - we know these are worked out and that means other
        // allergens cannot be that ingredient, so we can remove those from the other allergen sets
        while (completed.size() < allergenIngredientMap.size()) {
            for (String key : allergenIngredientMap.keySet()) {
                if (completed.contains(key)) {
                    // Already worked this column out - skip
                    continue;
                }

                List<String> set = allergenIngredientMap.get(key);
                
                if (set.size() == 1) {
                    // Down to last possibility - remove this from others
                    for (Entry<String, List<String>> entry : allergenIngredientMap.entrySet()) {
                        if (entry.getKey().equals(key) == false) {
                            entry.getValue().removeAll(set);
                        }
                    }
                    
                    completed.add(key);
                    nonAllergenSet.removeAll(set);
                }
            }
        }
        
        int total = 0;
        
        for (String nonAllergen : nonAllergenSet) {
            total += ingredientCountMap.get(nonAllergen);
        }
        
        System.out.println("Part one: " + total);

        List<String> sortedKeyList = new ArrayList<>(allergenIngredientMap.keySet());
        Collections.sort(sortedKeyList);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < sortedKeyList.size(); i++) {
            String key = sortedKeyList.get(i);
            String value = allergenIngredientMap.get(key).get(0);
            
            if (i > 0) {
                sb.append(',');
            }

            sb.append(value);
        }
        
        System.out.println("Part two: " + sb.toString());
    }
}
