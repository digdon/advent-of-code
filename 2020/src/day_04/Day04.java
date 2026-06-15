package day_04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day04 {

    private static final Set<String> FIELDS = new HashSet<>(Arrays.asList("ecl", "pid", "eyr", "hcl", "byr", "iyr", "hgt", "cid"));
    
    public static void main(String[] args) {
        List<String> inputText = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;

        try {
            while ((inputLine = reader.readLine()) != null) {
                inputText.add(inputLine);
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

        int validPassports = countValidPassports(inputText, Day04::isValidPassportPartOne);
        System.out.println("Part one: " + validPassports);

        validPassports = countValidPassports(inputText, Day04::isValidPassportPartTwo);
        System.out.println("Part two: " + validPassports);
    }
    
    private static int countValidPassports(List<String> data, Function<Map<String, String>, Boolean> validate) {
        int validCount = 0;
        Map<String, String> fieldsFound = new HashMap<>();
        FIELDS.forEach(f -> fieldsFound.put(f, null));

        for (String line : data) {
            if (line.length() == 0) {
                if (validate.apply(fieldsFound)) {
                    validCount++;
                }

                fieldsFound.clear();
                FIELDS.forEach(f -> fieldsFound.put(f, null));
            }

            String[] fields = line.split(" ");

            for (int i = 0; i < fields.length; i++) {
                String[] fieldParts = fields[i].split(":");

                if (fieldsFound.containsKey(fieldParts[0])) {
                    fieldsFound.put(fieldParts[0], fieldParts[1]);
                }
            }
        }

        if (validate.apply(fieldsFound)) {
            validCount++;
        }

        return validCount;
    }
    
    private static boolean isValidPassportPartOne(Map<String, String> passportFields) {
        return passportFields.entrySet().stream().allMatch(e -> e.getKey().equals("cid") ? true : e.getValue() != null); 
    }
    
    private static final Pattern HCL_PATTERN = Pattern.compile("^#[0-9a-f]{6}$");
    private static final Pattern PID_PATTERN = Pattern.compile("^[0-9]{9}$");
    private static final Pattern HGT_PATTERN = Pattern.compile("^([0-9]+)(cm|in)$");
    private static final Set<String> EYE_COLOUR_SET = new HashSet<>(Arrays.asList("amb", "blu", "brn", "gry", "grn", "hzl", "oth"));
    
    private static boolean isValidPassportPartTwo(Map<String, String> passportFields) {
        for (Entry<String, String> entry : passportFields.entrySet()) {
            boolean isValid = false;
            String value = entry.getValue();
            
            if (value == null && entry.getKey() != "cid") {
                return false;
            }
            
            switch (entry.getKey()) {
                case "byr":
                    isValid = isValidNumber(value, 1920, 2002);
                    break;

                case "iyr":
                    isValid = isValidNumber(value, 2010, 2020);
                    break;
                    
                case "eyr":
                    isValid = isValidNumber(value, 2020, 2030);
                    break;
                    
                case "hgt":
                    isValid = isValidHeight(value);
                    break;
                    
                case "hcl":
                    isValid = HCL_PATTERN.matcher(value).matches();
                    break;
                    
                case "ecl":
                    isValid = EYE_COLOUR_SET.contains(value);
                    break;
                    
                case "pid":
                    isValid = PID_PATTERN.matcher(value).matches();
                    break;
                    
                case "cid":
                    // This is ok
                    isValid = true;
                    break;
                    
                default:
                    return false;
            }
            
            if (isValid == false) {
                return false;
            }
        }
        
        return true;
    }
    
    private static boolean isValidNumber(String input, int min, int max) {
        if (input == null) {
            return false;
        }
        
        try {
            int value = Integer.valueOf(input);
            return (value >= min && value <= max);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private static boolean isValidHeight(String input) {
        Matcher matcher = HGT_PATTERN.matcher(input);
        
        if (matcher.matches() == false) {
            return false;
        }

        String type = matcher.group(2);
            
        if (type.equals("cm")) {
            return isValidNumber(matcher.group(1), 150, 193);
        } else if (type.equals("in")) {
            return isValidNumber(matcher.group(1), 59, 76);
        } else {
            return false;
        }
    }
}
