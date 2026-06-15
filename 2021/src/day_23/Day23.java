package day_23;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class Day23 {
    private static final int[] COST = { 1, 10, 100, 1000 }; // Energy costs for A, B, C, D
    private static final int[] ROOM_POS = { 2, 4, 6, 8 }; // Hallway positions above each room - 1 less than actual positions in the data because we ignore the first '#'
    private static final int HALL_SIZE = 11;
    private static int ROOM_DEPTH = 2; // Set to 4 for the larger version

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        // Part 1
        char[][] initialRooms = buildRoomConfig(inputLines);

        long start = System.currentTimeMillis();
        int part1Score = solve(initialRooms);
        System.out.println(String.format("Part 1: %d (%dms)", part1Score, System.currentTimeMillis() - start));

        // Part 2
        ROOM_DEPTH = 4;
        inputLines.add(3, "  #D#C#B#A#  ");
        inputLines.add(4, "  #D#B#A#C#  ");
        
        initialRooms = buildRoomConfig(inputLines);
        start = System.currentTimeMillis();
        int part2Score = solve(initialRooms);
        System.out.println(String.format("Part 2: %d (%dms)", part2Score, System.currentTimeMillis() - start));
    }

    private static char[][] buildRoomConfig(List<String> inputLines) {
        // Need to build the initial room configuration
        char[][] roomSetup = new char[ROOM_POS.length][ROOM_DEPTH];
        
        for (int i = 2; i < inputLines.size() - 1; i++) {
            String line = inputLines.get(i);
            int roomPos = 0;
            
            for (int j = 0; j < line.length(); j++) {
                char ch = line.charAt(j);
                
                if (Character.isAlphabetic(ch)) {
                    roomSetup[roomPos][i - 2] = ch;
                    roomPos++;
                }
            }
        }
        
        return roomSetup;
    }
    
    static class State implements Comparable<State> {
        int[] hallway;
        char[][] rooms;
        int cost;

        State(int[] hallway, char[][] rooms, int cost) {
            this.hallway = hallway.clone();
            this.rooms = new char[4][ROOM_DEPTH];
            for (int i = 0; i < 4; i++) {
                this.rooms[i] = rooms[i].clone();
            }
            this.cost = cost;
        }

        @Override
        public int compareTo(State other) {
            return Integer.compare(this.cost, other.cost);
        }

        @Override
        public int hashCode() {
            return Arrays.deepHashCode(rooms) * 31 + Arrays.hashCode(hallway);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            
            if (obj instanceof State) {
                State other = (State)obj;
                return Arrays.equals(hallway, other.hallway) && Arrays.deepEquals(rooms, other.rooms);
            }
            
            return false;
        }
    }

    public static int solve(char[][] initialRooms) {
        PriorityQueue<State> pq = new PriorityQueue<>();
        Map<State, Integer> visited = new HashMap<>();

        int[] initialHallway = new int[HALL_SIZE];
        Arrays.fill(initialHallway, -1);
        pq.add(new State(initialHallway, initialRooms, 0));

        while (!pq.isEmpty()) {
            State current = pq.poll();
            
            if (isInOrder(current.rooms)) {
                return current.cost;
            }

            if (visited.containsKey(current) && visited.get(current) <= current.cost) {
                continue;
            }
            
            visited.put(current, current.cost);

            generateNextStates(current, pq);
        }

        return -1; // Should never happen if the puzzle is solvable
    }

    private static void generateNextStates(State state, PriorityQueue<State> pq) {
        // Move amphipods from hallway to rooms if possible
        for (int i = 0; i < HALL_SIZE; i++) {
            if (state.hallway[i] != -1) {
                int amphipodType = state.hallway[i];
                int roomIdx = amphipodType;
                int roomPos = ROOM_POS[roomIdx];
                if (canMoveToRoom(state, i, roomIdx)) {
                    int steps = Math.abs(i - roomPos) + (ROOM_DEPTH - roomSize(state.rooms[roomIdx]));
                    int newCost = state.cost + steps * COST[amphipodType];

                    char[][] newRooms = deepCopy(state.rooms);
                    newRooms[roomIdx][roomSize(newRooms[roomIdx])] = (char)('A' + amphipodType);
                    int[] newHallway = state.hallway.clone();
                    newHallway[i] = -1;
                    pq.add(new State(newHallway, newRooms, newCost));
                }
            }
        }

        // Move amphipods from rooms to hallway
        for (int roomIdx = 0; roomIdx < 4; roomIdx++) {
            if (!canLeaveRoom(state.rooms[roomIdx], roomIdx)) {
                continue;
            }
            
            int amphipodType = state.rooms[roomIdx][0] - 'A';
            int roomPos = ROOM_POS[roomIdx];
            for (int i = 0; i < HALL_SIZE; i++) {
                if (state.hallway[i] == -1 && isPathClear(state.hallway, roomPos, i) && i != ROOM_POS[0]
                        && i != ROOM_POS[1] && i != ROOM_POS[2] && i != ROOM_POS[3]) {
                    int steps = Math.abs(i - roomPos) + (ROOM_DEPTH - roomSize(state.rooms[roomIdx]) + 1);
                    int newCost = state.cost + steps * COST[amphipodType];

                    char[][] newRooms = deepCopy(state.rooms);
                    System.arraycopy(newRooms[roomIdx], 1, newRooms[roomIdx], 0, ROOM_DEPTH - 1);
                    newRooms[roomIdx][ROOM_DEPTH - 1] = '.';
                    int[] newHallway = state.hallway.clone();
                    newHallway[i] = amphipodType;
                    pq.add(new State(newHallway, newRooms, newCost));
                }
            }
        }
    }

    private static boolean isInOrder(char[][] rooms) {
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[i].length; j++) {
                if (rooms[i][j] != (char)('A' + i)) {
                    return false;
                }
            }
        }
        
        return true;
    }

    private static boolean canMoveToRoom(State state, int hallPos, int roomIdx) {
        return isPathClear(state.hallway, hallPos, ROOM_POS[roomIdx])
                && onlyCorrectInRoom(state.rooms[roomIdx], roomIdx);
    }

    private static boolean canLeaveRoom(char[] room, int roomIdx) {
        for (char c : room) {
            if (c != '.' && c - 'A' != roomIdx) {
                return true;
            }
        }
        
        return false;
    }

    private static boolean onlyCorrectInRoom(char[] room, int roomIdx) {
        for (char c : room) {
            if (c != '.' && c - 'A' != roomIdx) {
                return false;
            }
        }
        
        return true;
    }

    private static boolean isPathClear(int[] hallway, int from, int to) {
        int min = Math.min(from, to);
        int max = Math.max(from, to);

        for (int i = min + 1; i < max; i++) {
            if (hallway[i] != -1) {
                return false;
            }
        }
        
        return true;
    }

    private static int roomSize(char[] room) {
        int size = 0;

        for (char c : room) {
            if (c != '.') {
                size++;
            }
        }
        
        return size;
    }

    private static char[][] deepCopy(char[][] rooms) {
        char[][] copy = new char[rooms.length][rooms[0].length];
        
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[i].length; j++) {
                copy[i][j] = rooms[i][j];
            }
        }
        
        return copy;
    }
}
