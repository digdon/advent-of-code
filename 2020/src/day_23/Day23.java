package day_23;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Day23 {

    public static void main(String[] args) {
        String input = "952438716";
        
        List<Integer> cupList = new ArrayList<>();
        
        for (String cup : input.split("")) {
            cupList.add(Integer.valueOf(cup));
        }
        
        partOne(cupList);
        
        // Part two
        Map<Integer, Cup> cupMap = playTheGame(cupList, 1000000, 10000000);
        Cup star = cupMap.get(1).next;
        BigInteger starValue = BigInteger.valueOf(star.value);
        starValue = starValue.multiply(BigInteger.valueOf(star.next.value));
        System.out.println("Part two: " + starValue);
    }
    
    private static void partOne(List<Integer> originalCupList) {
        LinkedList<Integer> cupList = new LinkedList<>(originalCupList);
        int maxCup = Collections.max(cupList);
        
        for (int move = 1; move <= 100; move++) {
            int currentCup = cupList.remove();
            
            // First, we remove the 3 cups after the current one
            List<Integer> removedList = new ArrayList<>();
            removedList.add(cupList.remove());
            removedList.add(cupList.remove());
            removedList.add(cupList.remove());

            // Re-add current cup to end of queue
            cupList.add(currentCup);
            
            // Calcuate a destination
            int destination = (currentCup == 1) ? maxCup : currentCup - 1;
            
            while (removedList.contains(destination)) {
                destination--;
                destination = (destination < 1) ? maxCup : destination;
            }
            
            // Find the destination cup
            int insertPos = 0;
            
            for (int cup : cupList) {
                if (cup == destination) {
                    break;
                } else {
                    insertPos++;
                }
            }
            
            // Insert the removed cups
            for (int cup : removedList) {
                cupList.add(++insertPos, cup);
            }
        }
        
        while (cupList.peek() != 1) {
            cupList.add(cupList.remove());
        }
        
        cupList.remove();
        
        StringBuilder sb = new StringBuilder();
        for (int cup : cupList) {
            sb.append(cup);
        }
        
        System.out.println("Part one: " + sb.toString());
    }

    private static class Cup {
        final int value;
        Cup next;
        
        Cup(int value) {
            this.value = value;
        }
    }
    
    private static Map<Integer, Cup> playTheGame(List<Integer> cupList, int maxCup, int totalMoves) {
        Map<Integer, Cup> cupMap = new HashMap<>();
        Cup head = new Cup(cupList.get(0));
        cupMap.put(head.value, head);
        Cup tail = head;
        
        for (int i = 1; i < cupList.size(); i++) {
            Cup cup = new Cup(cupList.get(i));
            cupMap.put(cup.value, cup);
            tail.next = cup;
            tail = cup;
        }

        for (int i = Collections.max(cupList) + 1; i <= maxCup; i++) {
            Cup cup = new Cup(i);
            cupMap.put(cup.value, cup);
            tail.next = cup;
            tail = cup;
        }

        for (int move = 0; move < totalMoves; move++) {
            Cup current = head;

            // Determine 3 cups after current
            Cup remove1 = current.next;
            Cup remove3 = remove1.next.next;

            // Make item after last removed cup the head of the list
            head = remove3.next;

            // Re-add current cup to end of list
            tail.next = current;
            tail = current;
            tail.next = null;
            
            // Calculate a destination cup
            int destination = (current.value == 1) ? maxCup : current.value - 1;
            
            while (destination == remove1.value || destination == remove1.next.value || destination == remove3.value) {
                destination--;
                destination = (destination < 1) ? maxCup : destination;
            }
            
            // Re-add removed cups after destination
            Cup destCup = cupMap.get(destination);
            remove3.next = destCup.next;
            destCup.next = remove1;
        }
        
        return cupMap;
    }
}
