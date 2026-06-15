package day_14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day14 {

    public static void main(String[] args) {
        List<String> initList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        
        try {
            while ((inputLine = reader.readLine()) != null) {
                initList.add(inputLine);
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

        partOne(initList);
        partTwo(initList);
    }
    
    private static void partOne(List<String> initList) {
        Map<Integer, BigInteger> memoryMap = new HashMap<>();
        Set<Integer> addressSet = new HashSet<>();
        Byte[] bitmask = new Byte[36];
        
        for (String command : initList) {
            String[] parts = command.split("\\s*=\\s*");
            
            if (parts[0].equals("mask")) {
                for (int i = 0; i < parts[1].length(); i++) {
                    char bitValue = parts[1].charAt(i);
                    
                    if (bitValue == 'X') {
                        bitmask[i] = null;
                    } else {
                        bitmask[i] = Byte.valueOf((byte)((bitValue == '1') ? 0x01 : 0x00));
                    }
                }
            } else {
                int address = Integer.valueOf(parts[0].substring(parts[0].lastIndexOf('[') + 1, parts[0].length() - 1));
                BigInteger value = new BigInteger(parts[1]);

                // Apply mask
                for (int i = 0; i < bitmask.length; i++) {
                    if (bitmask[i] == null) {
                        continue;
                    } else if (bitmask[i] == 1) {
                        value = value.setBit(35 - i);
                    } else if (bitmask[i] == 0) {
                        value = value.clearBit(35 - i);
                    }
                }

                memoryMap.put(address, value);
                addressSet.add(address);
            }
        }

        long total = 0;

        for (int address : addressSet) {
            BigInteger value = memoryMap.get(address);
            total += value.longValueExact();
        }
        
        System.out.println("Part one: " + total);
    }
    
    private static void partTwo(List<String> initList) {
        Map<Long, Long> memoryMap = new HashMap<>();
        Set<Long> addressSet = new HashSet<>();
        char[] bitmask = new char[36];
        char[] addressBinary = new char[bitmask.length];
        
        for (String command : initList) {
            String[] parts = command.split("\\s*=\\s*");
            
            if (parts[0].equals("mask")) {
                for (int i = 0; i < parts[1].length(); i++) {
                    bitmask[i] = parts[1].charAt(i);
                }
            } else {
                long address = Integer.valueOf(parts[0].substring(parts[0].lastIndexOf('[') + 1, parts[0].length() - 1));
                long value = Long.valueOf(parts[1]);
                
                String binaryString = Long.toBinaryString(address);
                Arrays.fill(addressBinary, '0');

                for (int i = 0; i < binaryString.length(); i++) {
                    addressBinary[addressBinary.length - binaryString.length() + i] = binaryString.charAt(i);
                }
                
                List<String> addressList = buildAddressList(bitmask, addressBinary);
                
                for (String item : addressList) {
                    address = Long.parseLong(item, 2);
                    memoryMap.put(address, value);
                    addressSet.add(address);
                }
            }
        }
        
        long total = 0;

        for (Long address : addressSet) {
            Long value = memoryMap.get(address);
            total += value;
        }
        
        System.out.println("Part two: " + total);
    }

    static class Node {
        private char value;
        private Node left;
        private Node right;
        
        public Node(char value) {
            this.value = value;
        }
    }

    private static List<String> buildAddressList(char[] mask, char[] address) {
        Node root = new Node('0');
        buildTree(root, mask, address, 0);
        List<String> addressList = new ArrayList<>();
        walkTree(root, "", addressList);
        
        return addressList;
    }
    
    private static void buildTree(Node node, char[] mask, char[] address, int pos) {
        if (pos >= address.length) {
            return;
        }

        if (mask[pos] == '0') {
            node.left = new Node(address[pos]);
            buildTree(node.left, mask, address, pos + 1);
        } else if (mask[pos] == '1') {
            node.left = new Node('1');
            buildTree(node.left, mask, address, pos + 1);
        } else {
            node.left = new Node('0');
            buildTree(node.left, mask, address, pos + 1);
            
            node.right = new Node('1');
            buildTree(node.right, mask, address, pos + 1);
        }
    }
    
    private static void walkTree(Node node, String path, List<String> addressList) {
        if (node == null) {
            addressList.add(path);
            return;
        }
        
        walkTree(node.left, path + node.value, addressList);

        if (node.right != null) {
            walkTree(node.right, path + node.value, addressList);
        }
    }
}
