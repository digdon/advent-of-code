package day_16;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day16 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        for (String line : inputLines) {
            StringBuilder sb = new StringBuilder();
            
            if (line.length() % 2 != 0) {
                line = "0" + line;
            }
            
            for (int pos = 0; pos < line.length(); pos += 2) {
                int value = Integer.parseInt(line.substring(pos, pos + 2), 16);
                String binary = "0000000" + Integer.toBinaryString(value);
                binary = binary.substring(binary.length() - 8);
                sb.append(binary);
            }

            String binaryString = sb.toString();
            
            List<Packet> packetList = parsePacket(binaryString);

            // Part 1
            int versionTotal = calculateVersionTotal(packetList);
            System.out.println("Part 1: " + versionTotal);
            
            // Part 2
            for (Packet packet : packetList) {
                long packetValue = getPacketValue(packet);
                System.out.println("Part 2: " + packetValue);
            }
        }
    }

    private static long getPacketValue(Packet packet) {
        if (packet.type == 0) {
            // SUM operator
            long sumValue = 0;
            
            for (Packet subPacket : packet.subPackets) {
                sumValue += getPacketValue(subPacket);
            }
            
            return sumValue;
        } else if (packet.type == 1) {
            // PRODUCT operator
            long productValue = 1;
            
            for (Packet subPacket : packet.subPackets) {
                productValue *= getPacketValue(subPacket);
            }
            
            return productValue;
        } else if (packet.type == 2) {
            // MINIMUM operator
            long min = Long.MAX_VALUE;
            
            for (Packet subPacket : packet.subPackets) {
                long value = getPacketValue(subPacket);
                
                if (value < min) {
                    min = value;
                }
            }
            
            return min;
        } else if (packet.type == 3) {
            // MAXIMUM operator
            long max = Long.MIN_VALUE;
            
            for (Packet subPacket : packet.subPackets) {
                long value = getPacketValue(subPacket);
                
                if (value > max) {
                    max = value;
                }
            }
            
            return max;
        } else if (packet.type == 4) {
            // Literal value
            return packet.value;
        } else {
            long value1 = getPacketValue(packet.subPackets.get(0));
            long value2 = getPacketValue(packet.subPackets.get(1));
            
            if (packet.type == 5) {
                // GREATER THAN operator
                return value1 > value2 ? 1 : 0;
            } else if (packet.type == 6) {
                // LESS THAN operator
                return value1 < value2 ? 1 : 0;
            } else { // if (packet.type == 7) {
                // EQUALS operator
                return (value1 == value2) ? 1 : 0;
            }
        }
    }
    
    private static List<Packet> parsePacket(String binary) {
        int pos = 0;
        List<Packet> packetList = new ArrayList<>();
        
        while (pos < binary.length() - 7) {
            Packet packet = getAPacket(binary, pos);
            packetList.add(packet);
            pos += packet.bitLength;
        }
        
        return packetList;
    }

    private static Packet getAPacket(String binary, int pos) {
        int start = pos;
        
        // Get version
        int version = Integer.parseInt(binary.substring(pos, pos + 3), 2);
        pos += 3;
        
        // Get type
        int type = Integer.parseInt(binary.substring(pos, pos + 3), 2);
        pos += 3;

        // What type of packet is it?
        if (type == 4) {
            // Literal packet
            StringBuilder sb = new StringBuilder();

            while (pos < binary.length() - 4) {
                String valueStr = binary.substring(pos, pos + 5);
                sb.append(valueStr.substring(1));
                pos += 5;

                if (valueStr.charAt(0) == '0') {
                    break;
                }
            }
            
            long value = Long.parseLong(sb.toString(), 2);
            return new Packet(pos - start, version, type, value);
        } else {
            // Any type other than '4' is an operator packet
            // Work out the sub-packet details
            // What's the length type?
            int lengthType = Integer.parseInt(binary.substring(pos, pos + 1));
            pos += 1;

            // 0 = 15 bits, 1 = 11 bits
            int lengthBitsLength = (lengthType == 0 ? 15 : 11);
            
            // Pull out the value
            int lengthValue = Integer.parseInt(binary.substring(pos, pos + lengthBitsLength), 2);
            pos += lengthBitsLength;
            
            if (lengthType == 0) {
                // The 15-bit length represents the total number of bits for the sub-packets contained in this packet
                // Grab the sub-packets bits and send for parsing
                String subPacketBinary = binary.substring(pos, pos + lengthValue);
                List<Packet> subPacketList = parsePacket(subPacketBinary);
                return new Packet((pos + lengthValue) - start, version, type, subPacketList);
            } else {
                // The 11-bit length represents the number of sub-packets contained in this packet
                // Fetch this number of packets
                List<Packet> packetList = new ArrayList<>();
                
                for (int count = 0; count < lengthValue; count++) {
                    Packet packet = getAPacket(binary, pos);
                    packetList.add(packet);
                    pos += packet.bitLength;
                }
                
                return new Packet(pos - start, version, type, packetList);
            }
        }
    }

    private static int calculateVersionTotal(List<Packet> packetList) {
        int total = 0;
        
        for (Packet packet : packetList) {
            total += packet.version;
            
            if (packet.subPackets != null) {
                total += calculateVersionTotal(packet.subPackets);
            }
        }
        
        return total;
    }
    
    static class Packet {
        int bitLength;
        int version;
        int type;
        long value;
        List<Packet> subPackets;
        
        Packet(int bitLength, int version, int type) {
            this.bitLength = bitLength;
            this.version = version;
            this.type = type;
        }

        Packet(int bitLength, int version, int type, long value) {
            this(bitLength, version, type);
            this.value = value;
        }

        Packet(int bitLength, int version, int type, List<Packet> subPackets) {
            this(bitLength, version, type);
            this.subPackets = subPackets;
        }
        
        void addSubPacket(Packet packet) {
            if (subPackets == null) {
                subPackets = new ArrayList<>();
            }
            
            subPackets.add(packet);
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append("bitLength=").append(bitLength);
            sb.append(", ");
            sb.append("version=").append(version);
            sb.append(", ");
            sb.append("type=").append(type);
            sb.append(", ");
            sb.append("value=").append(value);
            sb.append(", ");
            sb.append("subPackets=").append(subPackets);
            
            return sb.toString();
        }
    }
}
