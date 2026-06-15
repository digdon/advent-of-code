package day_21;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day21 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/day_21/items.txt")));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        int mode = 0; // 0 = weapons, 1 = armour, 2 = rings
        Pattern itemPattern = Pattern.compile("^(\\S+( \\S+)*) +(\\d+) +(\\d+) +(\\d+)$");
        List<Item> weaponList = new ArrayList<>();
        List<Item> armourList = new ArrayList<>();
        List<Item> ringList = new ArrayList<>();
        
        for (String line : inputLines) {
            if (line.isBlank()) {
                continue;
            }
            
            if (line.startsWith("Weapons:")) {
                mode = 0;
                continue;
            } else if (line.startsWith("Armor:")) {
                mode = 1;
                continue;
            } else if (line.startsWith("Rings:")) {
                mode = 2;
                continue;
            }
            
            Matcher matcher = itemPattern.matcher(line);
            
            if (matcher.matches()) {
                String name = matcher.group(1);
                int cost = Integer.parseInt(matcher.group(3));
                int damage = Integer.parseInt(matcher.group(4));
                int armour = Integer.parseInt(matcher.group(5));
                
                Item item = new Item(name, damage, armour, cost);
                
                switch (mode) {
                    case 0 -> weaponList.add(item);
                    case 1 -> armourList.add(item);
                    case 2 -> ringList.add(item);
                }
            }
        }
        
        part1(weaponList, armourList, ringList);
        part2(weaponList, armourList, ringList);
    }
    
    private static void part1(List<Item> weaponList, List<Item> armourList, List<Item> ringList) {
        long startTime = System.currentTimeMillis();
        
        int playerHP = 100;
        int bossHP = 109, bossDamage = 8, bossArmour = 2;
        int minCostToWin = Integer.MAX_VALUE;
        
        for (Item weapon : weaponList) {
            for (int armourIdx = -1; armourIdx < armourList.size(); armourIdx++) {
                for (int ringIdx1 = -1; ringIdx1 < ringList.size(); ringIdx1++) {
                    for (int ringIdx2 = -1; ringIdx2 < ringList.size(); ringIdx2++) {
                        if (ringIdx1 == ringIdx2 && ringIdx1 != -1) {
                            continue; // Can't wear the same ring twice
                        }
                        
                        int totalCost = weapon.cost + 
                                (armourIdx != -1 ? armourList.get(armourIdx).cost : 0) + 
                                (ringIdx1 != -1 ? ringList.get(ringIdx1).cost : 0) + 
                                (ringIdx2 != -1 ? ringList.get(ringIdx2).cost : 0);
                        int playerDamage = weapon.damage + 
                                (armourIdx != -1 ? armourList.get(armourIdx).damage : 0) + 
                                (ringIdx1 != -1 ? ringList.get(ringIdx1).damage : 0) + 
                                (ringIdx2 != -1 ? ringList.get(ringIdx2).damage : 0);
                        int playerArmour =
                                (armourIdx != -1 ? armourList.get(armourIdx).armour : 0) + 
                                (ringIdx1 != -1 ? ringList.get(ringIdx1).armour : 0) + 
                                (ringIdx2 != -1 ? ringList.get(ringIdx2).armour : 0);
                        
//                        System.out.println(String.format("weapon: %s, armour: %s, ring 1: %s, ring 2: %s | damage: %d, cost: %d",
//                                weapon.name,
//                                armourIdx == -1 ? "None" : armourList.get(armourIdx).name,
//                                ringIdx1 == -1 ? "None" : ringList.get(ringIdx1).name,
//                                ringIdx2 == -1 ? "None" : ringList.get(ringIdx2).name,
//                                playerDamage,
//                                totalCost));
                        
                        if (totalCost >= minCostToWin) {
                            // This loadout is more expensive than a known winning loadout - skip it
                            continue;
                        }
                        
                        // Time for battle
                        int playerEffectiveDamage = Math.max(1, playerDamage - bossArmour);
                        int bossEffectiveDamage = Math.max(1, bossDamage - playerArmour);
                        
                        int turnsToKillBoss = (int) Math.ceil((double) bossHP / playerEffectiveDamage);
                        int turnsToKillPlayer = (int) Math.ceil((double) playerHP / bossEffectiveDamage);
                        
                        if (turnsToKillBoss <= turnsToKillPlayer) {
//                            System.out.println("Player wins with cost: " + totalCost);
                            
                            if (totalCost < minCostToWin) {
                                minCostToWin = totalCost;
                            }
                        }
                    }
                }
            }
        }
        
        System.out.println(String.format("Part 1: %d (%d ms)", minCostToWin, System.currentTimeMillis() - startTime));
    }

    private static void part2(List<Item> weaponList, List<Item> armourList, List<Item> ringList) {
        long startTime = System.currentTimeMillis();
        
        int playerHP = 100;
        int bossHP = 109, bossDamage = 8, bossArmour = 2;
        int maxCostToLose = 0;
        
        for (Item weapon : weaponList) {
            for (int armourIdx = -1; armourIdx < armourList.size(); armourIdx++) {
                for (int ringIdx1 = -1; ringIdx1 < ringList.size(); ringIdx1++) {
                    for (int ringIdx2 = -1; ringIdx2 < ringList.size(); ringIdx2++) {
                        if (ringIdx1 == ringIdx2 && ringIdx1 != -1) {
                            continue; // Can't wear the same ring twice
                        }
                        
                        int totalCost = weapon.cost + 
                                (armourIdx != -1 ? armourList.get(armourIdx).cost : 0) + 
                                (ringIdx1 != -1 ? ringList.get(ringIdx1).cost : 0) + 
                                (ringIdx2 != -1 ? ringList.get(ringIdx2).cost : 0);
                        int playerDamage = weapon.damage + 
                                (armourIdx != -1 ? armourList.get(armourIdx).damage : 0) + 
                                (ringIdx1 != -1 ? ringList.get(ringIdx1).damage : 0) + 
                                (ringIdx2 != -1 ? ringList.get(ringIdx2).damage : 0);
                        int playerArmour =
                                (armourIdx != -1 ? armourList.get(armourIdx).armour : 0) + 
                                (ringIdx1 != -1 ? ringList.get(ringIdx1).armour : 0) + 
                                (ringIdx2 != -1 ? ringList.get(ringIdx2).armour : 0);
                        
//                        System.out.println(String.format("weapon: %s, armour: %s, ring 1: %s, ring 2: %s | damage: %d, cost: %d",
//                                weapon.name,
//                                armourIdx == -1 ? "None" : armourList.get(armourIdx).name,
//                                ringIdx1 == -1 ? "None" : ringList.get(ringIdx1).name,
//                                ringIdx2 == -1 ? "None" : ringList.get(ringIdx2).name,
//                                playerDamage,
//                                totalCost));
                        
                        if (totalCost <= maxCostToLose) {
                            // This loadout is cheaper than a known losing loadout - skip it
                            continue;
                        }
                        
                        // Time for battle
                        int playerEffectiveDamage = Math.max(1, playerDamage - bossArmour);
                        int bossEffectiveDamage = Math.max(1, bossDamage - playerArmour);
                        
                        int turnsToKillBoss = (int) Math.ceil((double) bossHP / playerEffectiveDamage);
                        int turnsToKillPlayer = (int) Math.ceil((double) playerHP / bossEffectiveDamage);
                        
                        if (turnsToKillBoss > turnsToKillPlayer) {
//                            System.out.println("Boss wins with cost: " + totalCost);

                            if (totalCost > maxCostToLose) {
                                maxCostToLose = totalCost;
                            }
                        }
                    }
                }
            }
        }
        
        System.out.println(String.format("Part 2: %d (%d ms)", maxCostToLose, System.currentTimeMillis() - startTime));
    }

    record Item(String name, int damage, int armour, int cost) {}
}
