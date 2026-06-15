package day_11;

import java.util.LinkedList;

public class Monkey {

    private int id;
    private LinkedList<Long> itemList = new LinkedList<>();
    private Operation operation;
    private long operand;
    private long testDivisible;
    private int testTrue;
    private int testFalse;
    private long inspectionCount = 0;
    
    public Monkey(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public void addItem(long item) {
        itemList.add(item);
    }
    
    public LinkedList<Long> getItems() {
        return itemList;
    }
    
    public void setOperation(Operation op) {
        this.operation = op;
    }
    
    public Operation getOperation() {
        return operation;
    }
    
    public void setOperand(long operand) {
        this.operand = operand;
    }
    
    public long getOperand() {
        return operand;
    }
    
    public void setTestDivisible(long value) {
        this.testDivisible = value;
    }
    
    public long getTestDivisible() {
        return testDivisible;
    }
    
    public void setTestTrue(int value) {
        this.testTrue = value;
    }
    
    public int getTestTrue() {
        return testTrue;
    }
    
    public void setTestFalse(int value) {
        this.testFalse = value;
    }
    
    public int getTestFalse() {
        return testFalse;
    }
    
    public void incrementInspectionCount() {
        inspectionCount++;
    }
    
    public long getInspectionCount() {
        return inspectionCount;
    }
}
