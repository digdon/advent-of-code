package day_07;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day07 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        Map<String, Wire> wireMap = new HashMap<>();

        for (String input : inputLines) {
            String[] split = input.split("\\s+->\\s+");
            String destWire = split[1];
            String[] operationParts = split[0].split("\\s+");
            
            if (operationParts.length == 1) {
                Wire inst = new Wire(Operation.SET, operationParts[0], null);
                wireMap.put(destWire, inst);
            } else if (operationParts.length == 2) {
                wireMap.put(destWire, new Wire(Operation.NOT, operationParts[1], null));
            } else {
                if (operationParts[1].equals("AND")) {
                    wireMap.put(destWire, new Wire(Operation.AND, operationParts[0], operationParts[2]));
                } else if (operationParts[1].equals("OR")) {
                    wireMap.put(destWire, new Wire(Operation.OR, operationParts[0], operationParts[2]));
                } else if (operationParts[1].equals("LSHIFT")) {
                    wireMap.put(destWire, new Wire(Operation.LSHIFT, operationParts[0], operationParts[2]));
                } else if (operationParts[1].equals("RSHIFT")) {
                    wireMap.put(destWire, new Wire(Operation.RSHIFT, operationParts[0], operationParts[2]));
                }
            }
        }
        
        processWire("a", wireMap);
        System.out.println("Part one: " + wireMap.get("a").value);
        
        for (Wire wire : wireMap.values()) {
            wire.value = 0;
        }
        
        wireMap.put("b", new Wire(Operation.SET, "956", null));
        processWire("a", wireMap);
        System.out.println("Part two: " + wireMap.get("a").value);
    }
    
    private static void processWire(String wireLabel, Map<String, Wire> wireMap) {
        Wire wire = wireMap.get(wireLabel);
        
        if (wire.value != 0) {
            return;
        }
        
        int value = 0;
        
        if (wire.op == Operation.SET || wire.op == Operation.NOT) {
            if (Character.isDigit(wire.var1.charAt(0)) == false) {
                // A wire label, not an integer value, so we need to work it out first
                processWire(wire.var1, wireMap);
                value = wireMap.get(wire.var1).value;
            } else {
                value = Integer.parseInt(wire.var1);
            }

            if (wire.op == Operation.NOT) {
                value = ~value;
            }
        } else if (wire.op == Operation.AND || wire.op == Operation.OR) {
            int value1 = 0;
            int value2 = 0;
            
            if (Character.isDigit(wire.var1.charAt(0)) == false) {
                // A wire label, not an integer value, so we need to work it out first
                processWire(wire.var1, wireMap);
                value1 = wireMap.get(wire.var1).value;
            } else {
                value1 = Integer.parseInt(wire.var1);
            }
            
            if (Character.isDigit(wire.var2.charAt(0)) == false) {
                // A wire label, not an integer value, so we need to work it out first
                processWire(wire.var2, wireMap);
                value2 = wireMap.get(wire.var2).value;
            } else {
                value2 = Integer.parseInt(wire.var2);
            }
            
            value = (wire.op == Operation.AND) ? (value1 & value2) : (value1 | value2);
        } else {
            int value1 = 0;
            int value2 = Integer.parseInt(wire.var2);
            
            if (Character.isDigit(wire.var1.charAt(0)) == false) {
                // A wire label, not an integer value, so we need to work it out first
                processWire(wire.var1, wireMap);
                value1 = wireMap.get(wire.var1).value;
            } else {
                value1 = Integer.parseInt(wire.var1);
            }
            
            if (Character.isDigit(wire.var2.charAt(0)) == false) {
                // A wire label, not an integer value, so we need to work it out first
                processWire(wire.var2, wireMap);
                value2 = wireMap.get(wire.var2).value;
            } else {
                value2 = Integer.parseInt(wire.var2);
            }
            
            value = (wire.op == Operation.LSHIFT) ? (value1 << value2) : (value1 >>> value2);
        }
        
        wire.value = value & 0xffff;
    }
    
    enum Operation { SET, AND, OR, NOT, LSHIFT, RSHIFT }
    
    static class Wire {
        String var1;
        String var2;
        Operation op;
        int value;
        
        public Wire(Operation op, String var1, String var2) {
            this.op = op;
            this.var1 = var1;
            this.var2 = var2;
        }
    }
}
