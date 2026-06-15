package day_20;

public class Day20 {

    public static void main(String[] args) {
        int target = 34000000;

        part1Sieve(target);
        part2Sieve(target);
    }

    private static void part1Sieve(int target) {
        long startTime = System.currentTimeMillis();
        
//        int limit = target / 10 / 3;
//        int[] houses = new int[limit + 1];
//        
//        for (int elf = 1; elf <= limit; elf++) {
//            for (int i = elf; i <= limit; i += elf) {
//                houses[i] += elf * 10;
//            }
//        }

        int lowerBound = target / 10 / 5;
        int upperBound = target / 10 / 3;
        int[] houses = new int[upperBound - lowerBound + 1];
        
        for (int elf = 1; elf <= upperBound; elf++) {
            int remainder = lowerBound % elf;
            int startHouse = lowerBound + (remainder == 0 ? 0 : elf - remainder);

            for (int i = startHouse; i <= upperBound; i += elf) {
                houses[i - lowerBound] += elf * 10;
            }
        }
        
        int minHouse = 0;
        
        for (int i = 1; i < houses.length; i++) {
            if (houses[i] >= target) {
                minHouse = i;
                break;
            }
        }

        minHouse += lowerBound;
        
        System.out.println(String.format("Part 1 (sieve): %d (%d ms)", minHouse, System.currentTimeMillis() - startTime));
    }
    
    private static void part2Sieve(int target) {
        long startTime = System.currentTimeMillis();
        
//        int limit = target / 11 / 3;
//        int[] houses = new int[limit + 1];
//        
//        for (int elf = 1; elf <= limit; elf++) {
//            for (int i = elf, houseCount = 0; i <= limit && houseCount < 50; i += elf, houseCount++) {
//                houses[i] += elf * 11;
//            }
//        }
        
        int lowerBound = target / 10 / 5;
        int upperBound = target / 10 / 3;
        int[] houses = new int[upperBound - lowerBound + 1];
        
        for (int elf = 1; elf <= upperBound; elf++) {
            int remainder = lowerBound % elf;
            int startHouse = lowerBound + (remainder == 0 ? 0 : elf - remainder);

            for (int i = startHouse, houseCount = startHouse / elf; i <= upperBound && houseCount < 50; i += elf, houseCount++) {
                houses[i - lowerBound] += elf * 11;
            }
        }

        int minHouse = 0;
        
        for (int i = 1; i < houses.length; i++) {
            if (houses[i] >= target) {
                minHouse = i;
                break;
            }
        }
        
        minHouse += lowerBound;

        System.out.println(String.format("Part 2 (sieve): %d (%d ms)", minHouse, System.currentTimeMillis() - startTime));
    }
}
