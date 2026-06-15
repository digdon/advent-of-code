package day_24;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day24 {

    public static void main(String[] arg) {
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
        
        // Part one
        Set<String> blackTileSet = new HashSet<>();
        
        for (String tileSteps : tileInputList) {
            String tileCoords = calculateTile(tileSteps);
            
            if (blackTileSet.contains(tileCoords)) {
                // Tile is in the set, so it's black - remove it to switch to white
                blackTileSet.remove(tileCoords);
            } else {
                // no entry means it's white, so change to black by creating an entry
                blackTileSet.add(tileCoords);
            }
                
        }
        
        System.out.println("Part one: " + blackTileSet.size());
        
        // Part two
        Set<String> tempTiles = blackTileSet;
        
        for (int i = 0; i < 100; i++) {
            tempTiles = flipTiles(tempTiles);
        }
        
        System.out.println("Part two: " + tempTiles.size());
    }
    
    private static String calculateTile(String tileSteps) {
        int x = 0;
        int y = 0;
        int z = 0;
        int skip = 0;
        
        while (tileSteps.length() > 0) {
            if (tileSteps.startsWith("e") || tileSteps.startsWith("w")) {
                skip = 1;
                x += (tileSteps.charAt(0) == 'e') ? 1 : -1;
                y += (tileSteps.charAt(0) == 'e') ? -1 : 1;
            } else {
                skip = 2;
                
                if (tileSteps.startsWith("ne")) {
                    x += 1;
                    z += -1;
                } else if (tileSteps.startsWith("nw")) {
                    y += 1;
                    z += -1;
                } else if (tileSteps.startsWith("se")) {
                    y += -1;
                    z += 1;
                } else if (tileSteps.startsWith("sw")) {
                    x += -1;
                    z += 1;
                }
            }
            
            tileSteps = tileSteps.substring(skip);
        }
        
        return formatCoordinates(x, y, z);
    }
    
    private static Set<String> flipTiles(Set<String> inputSet) {
        Set<String> outputSet = new HashSet<>();
        Set<String> whiteProcessingSet = new HashSet<>();

        // Start by examining every black tile
        for (String tileCoordsString : inputSet) {
            String[] coords = tileCoordsString.split(",");
            int x = Integer.valueOf(coords[0]);
            int y = Integer.valueOf(coords[1]);
            int z = Integer.valueOf(coords[2]);
            int blackNeighbours = 0;

            // Let's examine every neighbour
            for (int i = 0; i < NEIGHBOURS.length; i++) {
                int nX = x + NEIGHBOURS[i][0];
                int nY = y + NEIGHBOURS[i][1];
                int nZ = z + NEIGHBOURS[i][2];
                String neighbourCoords = formatCoordinates(nX, nY, nZ);
                
                if (inputSet.contains(neighbourCoords)) {
                    // black neighbour
                    blackNeighbours++;
                } else {
                    // A white tile with a known black neighbour - we'll have to check this later
                    whiteProcessingSet.add(neighbourCoords);
                }
            }
            
            if (blackNeighbours == 0 || blackNeighbours > 2) {
                // flip it to white (ie, do not include in new set
            } else {
                outputSet.add(tileCoordsString);
            }
        }

        // Now look at white tiles that have at least 1 black neighbour
        for (String tileCoordsString : whiteProcessingSet) {
            String[] coords = tileCoordsString.split(",");
            int x = Integer.valueOf(coords[0]);
            int y = Integer.valueOf(coords[1]);
            int z = Integer.valueOf(coords[2]);
            int blackNeighbours = 0;

            // Let's examine every neighbour
            for (int i = 0; i < NEIGHBOURS.length; i++) {
                int nX = x + NEIGHBOURS[i][0];
                int nY = y + NEIGHBOURS[i][1];
                int nZ = z + NEIGHBOURS[i][2];
                String neighbourCoords = formatCoordinates(nX, nY, nZ);
                
                if (inputSet.contains(neighbourCoords)) {
                    // black neighbour
                    blackNeighbours++;
                }
            }
            
            if (blackNeighbours == 2) {
                // flip the white tile to black
                outputSet.add(tileCoordsString);
            }
        }
        
        return outputSet;
    }

    private static final int[][] NEIGHBOURS = {
            { 0, 1, -1 },   // nw
            { 1, 0, -1 },   // ne
            { -1, 0, 1 },   // sw
            { 0, -1, 1 },   // se
            { -1, 1, 0 },   // w
            { 1, -1, 0 }    // e
    };
    
    private static String formatCoordinates(int x, int y, int z) {
        return String.format("%05d,%05d,%05d", x, y, z);
    }
}
