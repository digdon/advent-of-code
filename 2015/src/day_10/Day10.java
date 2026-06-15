package day_10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Day10 {
    
    private static final int PART_1_ITERATIONS = 40;
    private static final int PART_2_ITERATIONS = 50;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        String input = inputLines.getFirst();

        long startTime = System.currentTimeMillis();
        System.out.println(String.format("Part 1 (brute force): %d (%d ms)", iterateProcessing(input, PART_1_ITERATIONS), System.currentTimeMillis() - startTime));
        
        startTime = System.currentTimeMillis();
        System.out.println(String.format("Part 2 (brute force): %d (%d ms)", iterateProcessing(input, PART_2_ITERATIONS), System.currentTimeMillis() - startTime));
        
        startTime = System.currentTimeMillis();
        System.out.println(String.format("Part 1 (conway): %d (%d ms)", conwayProcessing(input, PART_1_ITERATIONS), System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        System.out.println(String.format("Part 2 (conway): %d (%d ms)", conwayProcessing(input, PART_2_ITERATIONS), System.currentTimeMillis() - startTime));
    }

    private static int iterateProcessing(String input, int count) {
        for (int iter = 0; iter < count; iter++) {
            input = processString(input);
        }
        
        return input.length();
    }
    
    private static String processString(String input) {
        StringBuilder sb = new StringBuilder();
        char prev = input.charAt(0);
        int count = 1;
        
        for (int i = 1; i < input.length(); i++) {
            char token = input.charAt(i);
            
            if (token == prev) {
                count++;
            } else {
                sb.append(count);
                sb.append(prev);
                count = 1;
            }
            
            prev = token;
        }

        sb.append(count);
        sb.append(prev);
        
        return sb.toString();
    }

    private static long conwayProcessing(String input, int count) {
        String startElement = SEQUENCE_TO_ELEMENT.get(input);
        Map<String, Integer> elementCounts = new HashMap<>();
        elementCounts.put(startElement, 1);
        
        for (int i = 0; i < count; i++) {
            Map<String, Integer> newCounts = new HashMap<>();
            
            for (Entry<String, Integer> entry : elementCounts.entrySet()) {
                String element = entry.getKey();
                int ec = entry.getValue();

                for (String decayElement : DECAY_TABLE.get(element)) {
                    newCounts.put(decayElement, newCounts.getOrDefault(decayElement, 0) + ec);
                }
            }
            
            elementCounts = newCounts;
        }
        
        long totalCount = 0;
        
        for (Entry<String, Integer> entry : elementCounts.entrySet()) {
            totalCount += ELEMENT_TO_SEQUENCE.get(entry.getKey()).length() * entry.getValue();
        }
        
        return totalCount;
    }
    
    public record ElementRecord(String element, String sequence, List<String> decayList) {}

    private static final List<ElementRecord> ELEMENT_RECORDS;
    private static final Map<String, String> SEQUENCE_TO_ELEMENT;
    private static final Map<String, String> ELEMENT_TO_SEQUENCE;
    private static final Map<String, List<String>> DECAY_TABLE;
    
    static {
        List<ElementRecord> elementRecordList = new ArrayList<>();
        elementRecordList.add(new ElementRecord("H", "22", List.of("H")));
        elementRecordList.add(new ElementRecord("He", "13112221133211322112211213322112", List.of("Hf", "Pa", "H", "Ca", "Li")));
        elementRecordList.add(new ElementRecord("Li", "312211322212221121123222112", List.of("He")));
        elementRecordList.add(new ElementRecord("Be", "111312211312113221133211322112211213322112", List.of("Ge", "Ca", "Li")));
        elementRecordList.add(new ElementRecord("B", "1321132122211322212221121123222112", List.of("Be")));
        elementRecordList.add(new ElementRecord("C", "3113112211322112211213322112", List.of("B")));
        elementRecordList.add(new ElementRecord("N", "111312212221121123222112", List.of("C")));
        elementRecordList.add(new ElementRecord("O", "132112211213322112", List.of("N")));
        elementRecordList.add(new ElementRecord("F", "31121123222112", List.of("O")));
        elementRecordList.add(new ElementRecord("Ne", "111213322112", List.of("F")));
        elementRecordList.add(new ElementRecord("Na", "123222112", List.of("Ne")));
        elementRecordList.add(new ElementRecord("Mg", "3113322112", List.of("Pm", "Na")));
        elementRecordList.add(new ElementRecord("Al", "1113222112", List.of("Mg")));
        elementRecordList.add(new ElementRecord("Si", "1322112", List.of("Al")));
        elementRecordList.add(new ElementRecord("P", "311311222112", List.of("Ho", "Si")));
        elementRecordList.add(new ElementRecord("S", "1113122112", List.of("P")));
        elementRecordList.add(new ElementRecord("Cl", "132112", List.of("S")));
        elementRecordList.add(new ElementRecord("Ar", "3112", List.of("Cl")));
        elementRecordList.add(new ElementRecord("K", "1112", List.of("Ar")));
        elementRecordList.add(new ElementRecord("Ca", "12", List.of("K")));
        elementRecordList.add(new ElementRecord("Sc", "3113112221133112", List.of("Ho", "Pa", "H", "Ca", "Co")));
        elementRecordList.add(new ElementRecord("Ti", "11131221131112", List.of("Sc")));
        elementRecordList.add(new ElementRecord("V", "13211312", List.of("Ti")));
        elementRecordList.add(new ElementRecord("Cr", "31132", List.of("V")));
        elementRecordList.add(new ElementRecord("Mn", "111311222112", List.of("Cr", "Si")));
        elementRecordList.add(new ElementRecord("Fe", "13122112", List.of("Mn")));
        elementRecordList.add(new ElementRecord("Co", "32112", List.of("Fe")));
        elementRecordList.add(new ElementRecord("Ni", "11133112", List.of("Zn", "Co")));
        elementRecordList.add(new ElementRecord("Cu", "131112", List.of("Ni")));
        elementRecordList.add(new ElementRecord("Zn", "312", List.of("Cu")));
        elementRecordList.add(new ElementRecord("Ga", "13221133122211332", List.of("Eu", "Ca", "Ac", "H", "Ca", "Zn")));
        elementRecordList.add(new ElementRecord("Ge", "31131122211311122113222", List.of("Ho", "Ga")));
        elementRecordList.add(new ElementRecord("As", "11131221131211322113322112", List.of("Ge", "Na")));
        elementRecordList.add(new ElementRecord("Se", "13211321222113222112", List.of("As")));
        elementRecordList.add(new ElementRecord("Br", "3113112211322112", List.of("Se")));
        elementRecordList.add(new ElementRecord("Kr", "11131221222112", List.of("Br")));
        elementRecordList.add(new ElementRecord("Rb", "1321122112", List.of("Kr")));
        elementRecordList.add(new ElementRecord("Sr", "3112112", List.of("Rb")));
        elementRecordList.add(new ElementRecord("Y", "1112133", List.of("Sr", "U")));
        elementRecordList.add(new ElementRecord("Zr", "12322211331222113112211", List.of("Y", "H", "Ca", "Tc")));
        elementRecordList.add(new ElementRecord("Nb", "1113122113322113111221131221", List.of("Er", "Zr")));
        elementRecordList.add(new ElementRecord("Mo", "13211322211312113211", List.of("Nb")));
        elementRecordList.add(new ElementRecord("Tc", "311322113212221", List.of("Mo")));
        elementRecordList.add(new ElementRecord("Ru", "132211331222113112211", List.of("Eu", "Ca", "Tc")));
        elementRecordList.add(new ElementRecord("Rh", "311311222113111221131221", List.of("Ho", "Ru")));
        elementRecordList.add(new ElementRecord("Pd", "111312211312113211", List.of("Rh")));
        elementRecordList.add(new ElementRecord("Ag", "132113212221", List.of("Pd")));
        elementRecordList.add(new ElementRecord("Cd", "3113112211", List.of("Ag")));
        elementRecordList.add(new ElementRecord("In", "11131221", List.of("Cd")));
        elementRecordList.add(new ElementRecord("Sn", "13211", List.of("In")));
        elementRecordList.add(new ElementRecord("Sb", "3112221", List.of("Pm", "Sn")));
        elementRecordList.add(new ElementRecord("Te", "1322113312211", List.of("Eu", "Ca", "Sb")));
        elementRecordList.add(new ElementRecord("I", "311311222113111221", List.of("Ho", "Te")));
        elementRecordList.add(new ElementRecord("Xe", "11131221131211", List.of("I")));
        elementRecordList.add(new ElementRecord("Cs", "13211321", List.of("Xe")));
        elementRecordList.add(new ElementRecord("Ba", "311311", List.of("Cs")));
        elementRecordList.add(new ElementRecord("La", "11131", List.of("Ba")));
        elementRecordList.add(new ElementRecord("Ce", "1321133112", List.of("La", "H", "Ca", "Co")));
        elementRecordList.add(new ElementRecord("Pr", "31131112", List.of("Ce")));
        elementRecordList.add(new ElementRecord("Nd", "111312", List.of("Pr")));
        elementRecordList.add(new ElementRecord("Pm", "132", List.of("Nd")));
        elementRecordList.add(new ElementRecord("Sm", "311332", List.of("Pm", "Ca", "Zn")));
        elementRecordList.add(new ElementRecord("Eu", "1113222", List.of("Sm")));
        elementRecordList.add(new ElementRecord("Gd", "13221133112", List.of("Eu", "Ca", "Co")));
        elementRecordList.add(new ElementRecord("Tb", "3113112221131112", List.of("Ho", "Gd")));
        elementRecordList.add(new ElementRecord("Dy", "111312211312", List.of("Tb")));
        elementRecordList.add(new ElementRecord("Ho", "1321132", List.of("Dy")));
        elementRecordList.add(new ElementRecord("Er", "311311222", List.of("Ho", "Pm")));
        elementRecordList.add(new ElementRecord("Tm", "11131221133112", List.of("Er", "Ca", "Co")));
        elementRecordList.add(new ElementRecord("Yb", "1321131112", List.of("Tm")));
        elementRecordList.add(new ElementRecord("Lu", "311312", List.of("Yb")));
        elementRecordList.add(new ElementRecord("Hf", "11132", List.of("Lu")));
        elementRecordList.add(new ElementRecord("Ta", "13112221133211322112211213322113", List.of("Hf", "Pa", "H", "Ca", "W")));
        elementRecordList.add(new ElementRecord("W", "312211322212221121123222113", List.of("Ta")));
        elementRecordList.add(new ElementRecord("Re", "111312211312113221133211322112211213322113", List.of("Ge", "Ca", "W")));
        elementRecordList.add(new ElementRecord("Os", "1321132122211322212221121123222113", List.of("Re")));
        elementRecordList.add(new ElementRecord("Ir", "3113112211322112211213322113", List.of("Os")));
        elementRecordList.add(new ElementRecord("Pt", "111312212221121123222113", List.of("Ir")));
        elementRecordList.add(new ElementRecord("Au", "132112211213322113", List.of("Pt")));
        elementRecordList.add(new ElementRecord("Hg", "31121123222113", List.of("Au")));
        elementRecordList.add(new ElementRecord("Tl", "111213322113", List.of("Hg")));
        elementRecordList.add(new ElementRecord("Pb", "123222113", List.of("Tl")));
        elementRecordList.add(new ElementRecord("Bi", "3113322113", List.of("Pm", "Pb")));
        elementRecordList.add(new ElementRecord("Po", "1113222113", List.of("Bi")));
        elementRecordList.add(new ElementRecord("At", "1322113", List.of("Po")));
        elementRecordList.add(new ElementRecord("Rn", "311311222113", List.of("Ho", "At")));
        elementRecordList.add(new ElementRecord("Fr", "1113122113", List.of("Rn")));
        elementRecordList.add(new ElementRecord("Ra", "132113", List.of("Fr")));
        elementRecordList.add(new ElementRecord("Ac", "3113", List.of("Ra")));
        elementRecordList.add(new ElementRecord("Th", "1113", List.of("Ac")));
        elementRecordList.add(new ElementRecord("Pa", "13", List.of("Th")));
        elementRecordList.add(new ElementRecord("U", "3", List.of("Pa")));
        
        ELEMENT_RECORDS = Collections.unmodifiableList(elementRecordList);
        
        Map<String, String> sequenceToElementMap = new HashMap<>();
        Map<String, String> elementToSequenceMap = new HashMap<>();
        Map<String, List<String>> decayTableMap = new HashMap<>();
        for (ElementRecord rec : ELEMENT_RECORDS) {
            sequenceToElementMap.put(rec.sequence(), rec.element());
            elementToSequenceMap.put(rec.element(), rec.sequence());
            decayTableMap.put(rec.element(), rec.decayList());
        }
        SEQUENCE_TO_ELEMENT = Collections.unmodifiableMap(sequenceToElementMap);
        ELEMENT_TO_SEQUENCE = Collections.unmodifiableMap(elementToSequenceMap);
        DECAY_TABLE = Collections.unmodifiableMap(decayTableMap);
    }
}