package day_04;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Day04 {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String baseKey = "yzbqklnj";
        System.out.println("Part one: " + findHashCounter(baseKey, "00000"));
        System.out.println("Part two: " + findHashCounter(baseKey, "000000"));
    }

    private static int findHashCounter(String baseKey, String target) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        int counter = 0;

        while (true) {
            byte[] digest = md.digest((baseKey + counter).getBytes());
            String hex = bytesToHexArray(digest);
            
            if (hex.startsWith(target)) {
                break;
            } else {
                counter++;
            }
        }
        
        return counter;
    }
    
    private static final char[] hexDigits = "0123456789abcdef".toCharArray();
    
    private static String bytesToHexArray(byte[] bytes) {
        char[] hexString = new char[bytes.length * 2];
        
        for (int i = 0; i < bytes.length; i++) {
            int value = bytes[i] & 0xff;
            hexString[i * 2] = hexDigits[value >>> 4];
            hexString[i * 2 + 1] = hexDigits[value & 0x0f];
        }
        
        return String.valueOf(hexString);
    }
}
