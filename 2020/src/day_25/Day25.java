package day_25;

public class Day25 {

    private static final int cardPublicKey = 8458505;
    private static final int doorPublicKey = 16050997;
    
    public static void main(String[] args) {
        int cardLoopSize = calculateLoopSize(cardPublicKey);
        System.out.println(cardLoopSize);
        int doorLoopSize = calculateLoopSize(doorPublicKey);
        System.out.println(doorLoopSize);

        // Part one
        long encryptKey = calculateKey(doorPublicKey, cardLoopSize);
        System.out.println("Part one: " + encryptKey);
    }

    private static final int DIVISOR = 20201227;
    
    private static int calculateLoopSize(int publicKey) {
        int loopSize = 0;
        int transform = 1;
        int subjectNumber = 7;
        
        while (transform != publicKey) {
            loopSize++;
            transform *= subjectNumber;
            transform %= DIVISOR;
        }
        
        return loopSize;
    }
    
    private static long calculateKey(int publicKey, int loopSize) {
        long transform = 1;
        
        for (int i = 0; i < loopSize; i++) {
            transform *= publicKey;
            transform %= DIVISOR;
        }
        
        return transform;
    }
}
