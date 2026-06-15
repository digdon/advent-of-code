package day_20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

public class Day20 {

    private static final Map<Integer, char[][]> tileDefinitionMap = new HashMap<>();
    private static final Map<Integer, Map<TileDirection, Integer>> tileConnectionMap = new HashMap<>();
    private static final List<Integer> cornerTiles = new ArrayList<>();

    public static enum TileDirection {
        ABOVE, RIGHT, BELOW, LEFT, NO_MATCH
    }

    public static void main(String[] args) {
        List<String> tileInputList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        
        try {
            while ((inputLine = reader.readLine()) != null) {
                tileInputList.add(inputLine);
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
        
        // Build the tiles
        int tileNumber = 0;
        char[][] tileGrid = null;
        int tileRow = 0;
        
        for (String tileData : tileInputList) {
            if (tileData.startsWith("Tile")) {
                tileNumber = Integer.valueOf(tileData.substring(tileData.indexOf(' ') + 1, tileData.length() - 1));
                tileGrid = null;
            } else if (tileData.length() == 0) {
                tileDefinitionMap.put(tileNumber, tileGrid);
            } else {
                if (tileGrid == null) {
                    tileGrid = new char[tileData.length()][tileData.length()];
                    tileRow = 0;
                }
                
                for (int i = 0; i < tileData.length(); i++) {
                    tileGrid[tileRow][i] = tileData.charAt(i);
                }
                
                tileRow++;
            }
        }

        if (tileGrid != null) {
            tileDefinitionMap.put(tileNumber, tileGrid);
        }

        partOne();

        System.out.println(cornerTiles);
        
        int[][] numberGrid = buildTileNumberGrid();
        
        for (int i = 0; i < numberGrid.length; i++) {
            for (int j = 0; j < numberGrid[i].length; j++) {
                System.out.print(numberGrid[i][j] + "   ");
            }
            
            System.out.println();
        }
        
        partTwo(numberGrid);
    }

    private static void partOne() {
        List<Integer> tileNumberList = new ArrayList<>(tileDefinitionMap.keySet());
        Collections.sort(tileNumberList);
        Queue<Integer> queue = new LinkedList<>();
        queue.add(tileNumberList.get(0));
        
        do {
            Integer tileNumber = queue.remove();
            Map<TileDirection, Integer> adjoiningTiles = findAdjoiningTiles(tileNumber, tileDefinitionMap);
            
            if (adjoiningTiles.size() == 2) {
                cornerTiles.add(tileNumber);
            }

            Map<TileDirection, Integer> tileConnections = tileConnectionMap.get(tileNumber);
            
            if (tileConnections == null) {
                tileConnections = new HashMap<>();
                tileConnectionMap.put(tileNumber, tileConnections);
            }
            
            for (Entry<TileDirection, Integer> entry : adjoiningTiles.entrySet()) {
                TileDirection adjoiningDirection = entry.getKey();
                int adjoiningTile = entry.getValue();
                
                // Add the adjoining tile info to the connection map
                if (tileConnections.containsKey(adjoiningDirection)) {
                    // Already in there.... better check to see if something has changed
                    int currentAdjoiningTile = tileConnections.get(adjoiningDirection);

                    if (currentAdjoiningTile != adjoiningTile) {
                        // Something changed - huh??
                        System.out.println("Tile " + tileNumber + ": " + adjoiningDirection + " changing from " + currentAdjoiningTile + " to " + entry.getValue());
                        System.exit(0);
                    }
                } else {
                    tileConnections.put(adjoiningDirection, adjoiningTile);
                }
                
                // For each entry, add the opposite direction info to the appropriate tile connection entry
                TileDirection opposite = TileDirection.NO_MATCH;
                
                if (adjoiningDirection == TileDirection.ABOVE) {
                    opposite = TileDirection.BELOW;
                } else if (adjoiningDirection == TileDirection.RIGHT) {
                    opposite = TileDirection.LEFT;
                } else if (adjoiningDirection == TileDirection.BELOW) {
                    opposite = TileDirection.ABOVE;
                } else if (adjoiningDirection == TileDirection.LEFT) {
                    opposite = TileDirection.RIGHT;
                }
                
                Map<TileDirection, Integer> adjoiningConnections = tileConnectionMap.get(adjoiningTile);
                
                if (adjoiningConnections == null) {
                    adjoiningConnections = new HashMap<>();
                    tileConnectionMap.put(adjoiningTile, adjoiningConnections);
                    queue.add(adjoiningTile);
                } else {
                    if (adjoiningConnections.containsKey(opposite)) {
                        int currentTile = adjoiningConnections.get(opposite);
                        
                        if (currentTile != tileNumber) {
                            System.out.println("Adjoining tile " + adjoiningTile + " " + opposite + " changing from "
                                    + currentTile + " to " + tileNumber);
                            System.exit(0);
                        }
                    }
                }
                
                adjoiningConnections.put(opposite, tileNumber);
            }
        } while (queue.size() > 0);
        
        long value = 1;
        
        for (int tile : cornerTiles) {
            value *= tile;
        }
        
        System.out.println("Part one: " + value);
    }

    private static Map<TileDirection, Integer> findAdjoiningTiles(int tileNumber, Map<Integer, char[][]> tileMap) {
        char[][] firstTile = tileMap.get(tileNumber);
        Map<TileDirection, Integer> adjoiningMap = new HashMap<>();
        
        for (Entry<Integer, char[][]> entry : tileMap.entrySet()) {
            if (entry.getKey() == tileNumber) {
                // A tile cannot adjoin to itself - skip it
                continue;
            }

            char[][] secondTile = entry.getValue();
            TileDirection matchedEdge = TileDirection.NO_MATCH;
            boolean flipped = false;
            
            while (true) {
                for (int rotations = 0; rotations < 4; rotations++) {
                    matchedEdge = findMatchingEdge(firstTile, secondTile);
    
                    if (matchedEdge != TileDirection.NO_MATCH) {
                        tileMap.put(entry.getKey(), secondTile);
                        break;
                    } else {
                        secondTile = rotateGridCW(secondTile);
                    }
                }
                
                if (matchedEdge != TileDirection.NO_MATCH) {
                    break;
                }

                if (flipped == true) {
                    break;
                }
                
//                System.out.println("Flipping tile " + entry.getKey());
                secondTile = flipGridHorizontal(secondTile);
                flipped = true;
            }
            
            if (matchedEdge != TileDirection.NO_MATCH) {
                adjoiningMap.put(matchedEdge, entry.getKey());
            }
        }
        
        return adjoiningMap;
    }

    private static TileDirection findMatchingEdge(char[][] first, char[][] second) {
        // Does top of first match bottom of second?
        boolean edgeMatches = true;
        
        for (int col = 0; col < first.length; col++) {
            if (first[0][col] != second[second.length - 1][col]) {
                edgeMatches = false;
                break;
            }
        }

        if (edgeMatches) {
            return TileDirection.ABOVE;
        }
        
        // Does right of first match of left of second?
        edgeMatches = true;
        
        for (int row = 0; row < first.length; row++) {
            if (first[row][first.length - 1] != second[row][0]) {
                edgeMatches = false;
                break;
            }
        }

        if (edgeMatches) {
            return TileDirection.RIGHT;
        }
        
        // Does bottom of first match top of second?
        edgeMatches = true;
        
        for (int col = 0; col < first.length; col++) {
            if (first[first.length - 1][col] != second[0][col]) {
                edgeMatches = false;
                break;
            }
        }

        if (edgeMatches) {
            return TileDirection.BELOW;
        }
        
        // Does left of first match right of second?
        edgeMatches = true;
        
        for (int row = 0; row < first.length; row++) {
            if (first[row][0] != second[row][second.length - 1]) {
                edgeMatches = false;
                break;
            }
        }

        if (edgeMatches) {
            return TileDirection.LEFT;
        }
        
        return TileDirection.NO_MATCH;
    }
    
    private static char[][] rotateGridCW(char[][] inputGrid) {
        int gridSize = inputGrid.length;
        char[][] outputGrid = new char[gridSize][gridSize];
        
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                outputGrid[col][gridSize - row - 1] = inputGrid[row][col];
            }
        }
        
        return outputGrid;
    }
    
    private static char[][] flipGridHorizontal(char[][] inputGrid) {
        int gridSize = inputGrid.length;
        char[][] outputGrid = new char[gridSize][gridSize];

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                outputGrid[row][gridSize - col - 1] = inputGrid[row][col];
            }
        }

        return outputGrid;
    }

    private static int[][] buildTileNumberGrid() {
        // Let's find the top-left corner
        int topLeftTile = 0;
        int nextRowTile = 0;
        int nextColTile = 0;
        
        for (int tileNumber : cornerTiles) {
            boolean hasRight = false;
            boolean hasBelow = false;
            
            Map<TileDirection, Integer> map = tileConnectionMap.get(tileNumber);
            
            for (Entry<TileDirection, Integer> entry : map.entrySet()) {
                if (entry.getKey() == TileDirection.RIGHT) {
                    hasRight = true;
                    nextColTile = entry.getValue();
                } else if (entry.getKey() == TileDirection.BELOW) {
                    hasBelow = true;
                    nextRowTile = entry.getValue();
                }
            }
            
            if (hasBelow && hasRight) {
                topLeftTile = tileNumber;
                break;
            }
        }

        int gridSize = (int)Math.sqrt(tileDefinitionMap.size());
        int[][] tileNumberGrid = new int[gridSize][gridSize];
        int row = 0;
        int col = 0;
        tileNumberGrid[row][col++] = topLeftTile;
        
        while (true) {
            if (nextColTile == 0) {
                row++;
                col = 0;
                tileNumberGrid[row][col++] = nextRowTile;
                Map<TileDirection, Integer> map = tileConnectionMap.get(nextRowTile);
                
                if (map.containsKey(TileDirection.BELOW)) {
                    nextRowTile = map.get(TileDirection.BELOW);
                } else {
                    nextRowTile = 0;
                }
                
                nextColTile = map.get(TileDirection.RIGHT);
            }

            tileNumberGrid[row][col++] = nextColTile;
            
            Map<TileDirection, Integer> map = tileConnectionMap.get(nextColTile);
            
            if (map.containsKey(TileDirection.RIGHT)) {
                nextColTile = map.get(TileDirection.RIGHT);
            } else {
                nextColTile = 0;
            }
            
            if (nextColTile == 0 && nextRowTile == 0) {
                break;
            }
        }
        
        return tileNumberGrid;
    }

    private static void partTwo(int[][] tileNumberGrid) {
        // Build the full image (piece together tiles, removing borders from each tile
        char[][] tile = tileDefinitionMap.get(tileNumberGrid[0][0]);
        int tileSize = tile.length - 2;
        char[][] fullImage = new char[tileNumberGrid.length * tileSize][tileNumberGrid.length * tileSize];
        int hashCount = 0;
        
        for (int tileRow = 0; tileRow < tileNumberGrid.length; tileRow++) {
            for (int tileCol = 0; tileCol < tileNumberGrid.length; tileCol++) {
                int tileNumber = tileNumberGrid[tileRow][tileCol];
                char[][] tileData = tileDefinitionMap.get(tileNumber);
                
                for (int row = 1; row < tileData.length - 1; row++) {
                    for (int col = 1; col < tileData[row].length - 1; col++) {
                        if (tileData[row][col] == '#') {
                            hashCount++;
                        }
                        
                        fullImage[(tileRow * tileSize) + (row - 1)][(tileCol * tileSize) + (col - 1)] = tileData[row][col];
                    }
                }
            }
        }
        
        // Now we start looking for the sea monster
        int monstersFound = 0;
        boolean flipped = false;
        
        while (true) {
            for (int rotations = 0; rotations < 4; rotations++) {
                monstersFound = findMonsters(fullImage);
                
                if (monstersFound > 0) {
                    break;
                } else {
                    fullImage = rotateGridCW(fullImage);
                }
            }

            if (monstersFound > 0) {
                break;
            }
            
            if (flipped == true) {
                break;
            }
            
            fullImage = flipGridHorizontal(fullImage);
            flipped = true;
        }
        
        System.out.println("Monsters found: " + monstersFound);
        
        int patternHashCount = 0;
        
        for (int i = 0; i < seaMonsterPattern.length; i++) {
            for (int j = 0; j < seaMonsterPattern[i].length(); j++) {
                if (seaMonsterPattern[i].charAt(j) == '#') {
                    patternHashCount++;
                }
            }
        }
        
        int waterRoughness = hashCount - (patternHashCount * monstersFound);
        
        System.out.println("Part two: " + waterRoughness);
    }
    
    private static final String[] seaMonsterPattern = new String[] {
            "                  # ",
            "#    ##    ##    ###",
            " #  #  #  #  #  #   "
    };
    
    private static int findMonsters(char[][] image) {
        int patternLength = seaMonsterPattern[0].length();
        int monsterCount = 0;

        for (int row = 0; row < (image.length - (seaMonsterPattern.length - 1)); row++) {
            for (int col = 0; col < image[row].length - (patternLength - 1); col++) {
                if (lineContainsPattern(image, row, col, 0)) {
                    boolean allFound = lineContainsPattern(image, row + 1, col, 1);
                    allFound &= lineContainsPattern(image, row + 2, col, 2);
                    
                    if (allFound) {
                        monsterCount++;
                    }
                }
            }
        }
        
        return monsterCount;
    }

    private static boolean lineContainsPattern(char[][] image, int row, int col, int patternPart) {
        String patternLine = seaMonsterPattern[patternPart];
        
        for (int i = 0; i < patternLine.length(); i++) {
            if (patternLine.charAt(i) == '#' && image[row][col + i] != '#') {
                return false;
            }
        }
        
        return true;
    }
}
