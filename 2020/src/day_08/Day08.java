package day_08;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day08 {
    
    public static void main(String[] args) {
        List<String> programList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        
        try {
            while ((inputLine = reader.readLine()) != null) {
                programList.add(inputLine);
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
        
        Instruction[] program = new Instruction[programList.size()];
        
        for (int i = 0; i < programList.size(); i++) {
            String[] strings = programList.get(i).split("\\s+");
            program[i] = new Instruction(strings[0], strings[1]);
        }
        
        // Part one
        System.out.println("Part one: " + runProgram(program).getAcc());
        
        // Part two
        for (int i = 0; i < program.length; i++) {
            String operator = program[i].getOperator();
            
            if (operator.equals("nop")) {
                program[i].setOperator("jmp");
            } else if (operator.equals("jmp")) {
                program[i].setOperator("nop");
            }

            Result result = runProgram(program);
            
            if (result.isSuccessful() == false) {
                program[i].setOperator(operator);
            } else {
                System.out.println("Success! acc = " + result.getAcc());
                break;
            }
        }
    }

    private static Result runProgram(Instruction[] program) {
        // Run the program
        int pc = 0;
        int acc = 0;
        int value = 0;
        Set<Integer> touchSet = new HashSet<>();
        boolean exitedProperly = true;
        
        while (pc < program.length) {
            if (touchSet.contains(pc)) {
                System.out.println("Instruction " + pc + " already run - exiting");
                exitedProperly = false;
                break;
            }
            
            touchSet.add(pc);
            String operator = program[pc].getOperator();
            
            switch (operator) {
                case "nop":
                    pc++;
                    break;

                case "acc":
                    value = Integer.valueOf(program[pc].getOperand());
                    acc += value;
                    pc++;
                    break;
                    
                case "jmp":
                    value = Integer.valueOf(program[pc].getOperand());
                    pc += value;
                    break;
                    
                default:
                    System.out.println("unknown instruction " + operator);
                    pc = program.length;
                    exitedProperly = false;
            }
        }

        return new Result(exitedProperly, acc);
    }
    
    static class Instruction {
        private String operator;
        private String operand;
        
        public Instruction(String operator, String operand) {
            this.operator = operator;
            this.operand = operand;
        }
        
        public String getOperator() {
            return operator;
        }
        
        public void setOperator(String operator) {
            this.operator = operator;
        }
        
        public String getOperand() {
            return operand;
        }
    }
    
    static class Result {
        private boolean successful;
        private int acc;
        
        public Result(boolean successful, int acc) {
            this.successful = successful;
            this.acc = acc;
        }
        
        public boolean isSuccessful() {
            return successful;
        }
        
        public int getAcc() {
            return acc;
        }
    }
}
