package day_24;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;

public class Day24 {

    private static final int DIGIT_COUNT = 14;
    
    public static void main(String[] args) throws IOException {
        // Load in the puzzle input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        Stack<Tuple> stack = new Stack<>();
        
        int[] maxModel = new int[DIGIT_COUNT];
        int[] minModel = new int[DIGIT_COUNT];
        
        for (int i = 0; i < inputLines.size(); i += 18) {
            int divZ = Integer.parseInt(inputLines.get(i + 4).split(" ")[2]);
            int addX = Integer.parseInt(inputLines.get(i + 5).split(" ")[2]);
            int addY = Integer.parseInt(inputLines.get(i + 15).split(" ")[2]);
            
            if (divZ != 26) {
                // Store the digit slot and the "add y" value for later
                stack.push(new Tuple(i / 18, addY));
            } else {
                // Here comes the heavy lifting
                Tuple tuple = stack.pop();
                
                // Solve digit i and w where (digit i) + addX + (popped addY) = w
                int addValue = addX + tuple.addY();
                int start = Math.max(1, 1 - addValue);
                int end = Math.min(9,  9 - addValue);
                maxModel[tuple.pos()] = end;        // other w (digit i)
                maxModel[i / 18] = end + addValue;  // current w
                minModel[tuple.pos()] = start;      // other w (digit i)
                minModel[i / 18] = start + addValue; // current w
            }
        }
        
        List<Instruction> program = compile(inputLines);
        Map<Character, Integer> registerMap = new HashMap<>(Map.of('w', 0, 'x', 0, 'y', 0, 'z', 0));

        Queue<Integer> inputQueue = new LinkedList<>();
        
        // Part 1 results
        for (int i = 0; i < maxModel.length; i++) {
            inputQueue.offer(maxModel[i]);
        }
        
        runProgram(program, inputQueue, registerMap);
        System.out.println(registerMap);
        String maxModelString = Arrays.stream(maxModel).mapToObj(String::valueOf).collect(Collectors.joining());
        
        if (registerMap.get('z') == 0) {
            System.out.println("Part 1: " + maxModelString);
        } else {
            System.out.println("Error with model number " + maxModelString);
        }

        // Part 2 results
        for (int i = 0; i < minModel.length; i++) {
            inputQueue.offer(minModel[i]);
        }
        
        runProgram(program, inputQueue, registerMap);
        System.out.println(registerMap);        
        String minModelString = Arrays.stream(minModel).mapToObj(String::valueOf).collect(Collectors.joining());
        
        if (registerMap.get('z') == 0) {
            System.out.println("Part 2: " + minModelString);
        } else {
            System.out.println("Error with model number " + minModelString);
        }
    }
    
    private record Tuple(int pos, int addY) {}
    
    private record Instruction(OpCode opCode, Character a, Character b, Integer i) {}
    
    private enum OpCode {
        INPUT,
        ADD_A_B,
        ADD_A_I,
        MUL_A_B,
        MUL_A_I,
        DIV_A_B,
        DIV_A_I,
        MOD_A_B,
        MOD_A_I,
        EQL_A_B,
        EQL_A_I
    }
    
    private static List<Instruction> compile(List<String> inputLines) {
        List<Instruction> program = new ArrayList<>(inputLines.size());
        
        for (String line : inputLines) {
            String[] parts = line.split("\\s+");
            
            // Get the target register
            Character targetRegister = parts[1].charAt(0);
            
            // Get the value, which may be an int or refer to a register
            Character sourceRegister = null;
            Integer sourceValue = null;
            
            if (parts.length > 2) {
                char temp = parts[2].charAt(0);
                
                if (temp >= 'w' && temp <= 'z') {
                    // refers to a register
                    sourceRegister = temp;
                } else {
                    sourceValue = Integer.parseInt(parts[2]);
                }
            }
            
            OpCode opCode = null;
            switch (parts[0]) {
                case "inp":
                    opCode = OpCode.INPUT;
                    break;
                    
                case "add":
                    opCode = sourceRegister != null ? OpCode.ADD_A_B : OpCode.ADD_A_I;
                    break;
                    
                case "mul":
                    opCode = sourceRegister != null ? OpCode.MUL_A_B : OpCode.MUL_A_I;
                    break;
                    
                case "div":
                    opCode = sourceRegister != null ? OpCode.DIV_A_B : OpCode.DIV_A_I;
                    break;
                    
                case "mod":
                    opCode = sourceRegister != null ? OpCode.MOD_A_B : OpCode.MOD_A_I;
                    break;
                    
                case "eql":
                    opCode = sourceRegister != null ? OpCode.EQL_A_B : OpCode.EQL_A_I;
                    break;
            };
            
            program.add(new Instruction(opCode, targetRegister, sourceRegister, sourceValue));
        }
        
        return program;
    }
    
    private static boolean runProgram(List<Instruction> program, Queue<Integer> inputQueue, Map<Character, Integer> registerMap) {
        for (Instruction inst : program) {
            int result = switch (inst.opCode()) {
                case INPUT -> inputQueue.remove();
                case ADD_A_B -> registerMap.get(inst.a()) + registerMap.get(inst.b());
                case ADD_A_I -> registerMap.get(inst.a()) + inst.i();
                case MUL_A_B -> registerMap.get(inst.a()) * registerMap.get(inst.b());
                case MUL_A_I -> registerMap.get(inst.a()) * inst.i();
                case DIV_A_B -> registerMap.get(inst.a()) / registerMap.get(inst.b());
                case DIV_A_I -> registerMap.get(inst.a()) / inst.i();
                case MOD_A_B -> registerMap.get(inst.a()) % registerMap.get(inst.b());
                case MOD_A_I -> registerMap.get(inst.a()) % inst.i();
                case EQL_A_B -> registerMap.get(inst.a()) == registerMap.get(inst.b()) ? 1 : 0;
                case EQL_A_I -> registerMap.get(inst.a()) == inst.i() ? 1 : 0;
            };
            
            registerMap.put(inst.a(), result);
        }
        
        return true;
    }
}
