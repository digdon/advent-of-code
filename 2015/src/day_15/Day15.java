package day_15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day15 {

    private static final List<Ingredient> ingredientList = new ArrayList<>();
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        for (String line : inputLines) {
            Map<String, Integer> attrMap = new HashMap<>();
            String[] strings = line.split(":\\s*");
            String name = strings[0];
            String[] attrs = strings[1].split(",\\s+");
            
            for (String attr : attrs) {
                String[] nv = attr.split("\\s+");
                attrMap.put(nv[0], Integer.parseInt(nv[1]));
            }
            
            Ingredient ing = new Ingredient();
            ing.name = name;
            ing.capacity = attrMap.get("capacity");
            ing.durability = attrMap.get("durability");
            ing.flavour = attrMap.get("flavor");
            ing.texture = attrMap.get("texture");
            ing.calories = attrMap.get("calories");
            
            ingredientList.add(ing);
        }

        int[] values = new int[ingredientList.size()];
        enumerate(0, 0, 100, values);
        
        System.out.println("Part 1: " + part1Max);
        System.out.println("Part 2: " + part2Max);
    }

    private static long part1Max = Long.MIN_VALUE;
    private static long part2Max = Long.MIN_VALUE; 
    
    private static void enumerate(int currLevel, int runningTotal, int target, int[] values) {
        if (currLevel == values.length - 1) {
            values[currLevel] = target - runningTotal;
            int capScore = 0, durScore = 0, flaScore = 0, texScore = 0, calScore = 0;
            
            for (int i = 0; i < values.length; i++) {
                Ingredient ing = ingredientList.get(i);
                int value = values[i];
                
                capScore += (ing.capacity * value);
                durScore += (ing.durability * value);
                flaScore += (ing.flavour * value);
                texScore += (ing.texture * value);
                calScore += (ing.calories * value);
            }
            
            long finalScore = Math.max(capScore, 0) * Math.max(durScore, 0) * Math.max(flaScore, 0) * Math.max(texScore, 0);
            
            if (finalScore > part1Max) {
                part1Max = finalScore;
            }
            
            if (calScore == 500 && finalScore > part2Max) {
                part2Max = finalScore;
            }
        } else {
            for (int i = 0; i <= target - runningTotal; i++) {
                values[currLevel] = i;
                runningTotal += i;
                enumerate(currLevel + 1, runningTotal, target, values);
                runningTotal -= i;
            }
        }
    }
    
    static class Ingredient {
        String name;
        int capacity;
        int durability;
        int flavour;
        int texture;
        int calories;
    }
}
