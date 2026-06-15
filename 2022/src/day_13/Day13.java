package day_13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day13 {

    public static void main(String[] args) throws IOException {
        // Load in the puzzle input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        List<List<Item>> packetList = new ArrayList<>();

        for (String line : inputLines) {
            if (line.length() == 0) {
                // Empty line - skipping
                continue;
            }
            
            packetList.add(parsePacket(line.substring(1, line.length() - 1)));
        }

        int part1Sum = 0;
        
        for (int i = 0, pairCount = 1; i < packetList.size() - 1; i += 2, pairCount++) {
            List<Item> packet1 = packetList.get(i);
            List<Item> packet2 = packetList.get(i + 1);

            if (compare(packet1, packet2) < 1) {
                part1Sum += pairCount;
            }
        }
        
        System.out.println("Part 1: " + part1Sum);

        List<Item> divider1 = parsePacket("[[2]]");
        List<Item> divider2 = parsePacket("[[6]]");
        packetList.add(divider1);
        packetList.add(divider2);
        
        packetList.sort((p1, p2) -> compare(p1, p2));
        
        int part2Key = 1;
        
        for (int i = 0; i < packetList.size(); i++) {
            List<Item> packet = packetList.get(i);
            
            if (packet.equals(divider1) || packet.equals(divider2)) {
                part2Key *= (i + 1);
            }
        }
        
        System.out.println("Part 2: " + part2Key);
    }

    private static int compare(List<Item> listA, List<Item> listB) {
        for (int i = 0; i < listA.size(); i++) {
            if (i > listB.size() - 1) {
                return 1;
            }
            
            Item item1 = listA.get(i);
            Item item2 = listB.get(i);
            
            if (item1.getSubList() != null && item2.getSubList() != null) {
                // Both items are sub-lists, so grab those lists and compare
                int value = compare(item1.getSubList(), item2.getSubList());

                if (value != 0) {
                    return value;
                }
            } else if (item1.getSubList() == null && item2.getSubList() == null) {
                // Both items are integer values, so compare directly
                if (item1.getValue() < item2.getValue()) {
                    return -1;
                } else if (item1.getValue() > item2.getValue()) {
                    return 1;
                }
            } else {
                // A mix of items - convert integer value to list and then compare lists
                List<Item> tempList1 = null;
                List<Item> tempList2 = null;
                
                if (item1.getSubList() == null) {
                    // Item 1 needs to be converted into a list
                    tempList1 = Arrays.asList(item1);
                    tempList2 = item2.getSubList();
                } else {
                    // Item 2 needs to be converted into a list
                    tempList2 = Arrays.asList(item2);
                    tempList1 = item1.getSubList();
                }
                
                // Both items are now sub-lists, so compare those lists
                int value = compare(tempList1, tempList2);

                if (value != 0) {
                    return value;
                }
            }
        }

        if (listA.size() < listB.size()) {
            return -1;
        }
        
        return 0;
    }
    
    @SuppressWarnings("unused")
    private static String generatePacketString(List<Item> packet) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        for (int i = 0; i < packet.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            
            Item item = packet.get(i);
            
            if (item.getSubList() != null) {
                sb.append(generatePacketString(item.getSubList()));
            } else {
                sb.append(item.getValue());
            }
        }
        
        sb.append(']');
        
        return sb.toString();
    }
    
    private static List<Item> parsePacket(String packetData) {
//        System.out.println("Parsing packet: " + packetData);
        List<Item> itemList = new ArrayList<>();

        for (int pos = 0; pos < packetData.length(); pos++) {
            if (packetData.charAt(pos) == '[') {
                // A list - let's determine the contents
                int depth = 0;
                int start = pos + 1;
                
                while (pos < packetData.length()) {
                    char current = packetData.charAt(pos++);
                    
                    if (current == '[') {
                        depth++;
                    } else if (current == ']') {
                        depth--;
                        
                        if (depth == 0) {
                            // Found the end bracket for this list - parse it
                            Item item = new Item();
                            item.setSubList(parsePacket(packetData.substring(start, pos - 1)));
                            itemList.add(item);
                            break;
                        }
                    }
                }
            } else if (packetData.charAt(pos) == ',') {
                continue;
            } else {
                // A value - find the end
                int start = pos;
                
                while (pos < packetData.length() && Character.isDigit(packetData.charAt(pos))) {
                    pos++;
                }
                
                int value = Integer.parseInt(packetData.substring(start, pos));
                Item item = new Item();
                item.setValue(value);
                itemList.add(item);
            }
        }
        
        return itemList;
    }
    
    static class Item {
        private int value;
        private List<Item> subList;
        
        public void setValue(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
        
        public void setSubList(List<Item> list) {
            this.subList = list;
        }
        
        public List<Item> getSubList() {
            return subList;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            sb.append("value=").append(value);
            sb.append(", ");
            sb.append("subList=").append(subList);
            sb.append(']');
            return sb.toString();
        }
    }
}
