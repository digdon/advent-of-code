package day_17;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day17 {

    public static void main(String[] args) {
        List<String> cubeInitList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        
        try {
            while ((inputLine = reader.readLine()) != null) {
                cubeInitList.add(inputLine);
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
        Set<String> activePoints = new HashSet<>();
        
        // Initialize pocket dimension
        for (int y = 0; y < cubeInitList.size(); y++) {
            String rowData = cubeInitList.get(y);
            
            for (int x = 0; x < rowData.length(); x++) {
                if (rowData.charAt(x) == '#') {
                    activePoints.add(generatePoint(x, y, 0));
                }
            }
        }
        
        // Run the cycles
        Set<String> tempPoints = activePoints;
        
        for (int i = 0; i < 6; i++) {
            tempPoints = runCyclePartOne(tempPoints);
        }
        
        System.out.println("Part one: " + tempPoints.size());
        
        // Part two
        activePoints = new HashSet<>();
        
        // Initialize pocket dimension
        for (int y = 0; y < cubeInitList.size(); y++) {
            String rowData = cubeInitList.get(y);
            
            for (int x = 0; x < rowData.length(); x++) {
                if (rowData.charAt(x) == '#') {
                    activePoints.add(generatePoint(x, y, 0, 0));
                }
            }
        }
        
        // Run the cycles
        tempPoints = activePoints;
        
        for (int i = 0; i < 6; i++) {
            tempPoints = runCyclePartTwo(tempPoints);
        }
        
        System.out.println("Part two: " + tempPoints.size());
    }

    private static String generatePoint(int x, int y, int z) {
        return String.format("%05d,%05d,%05d", x, y, z);
    }
    
    private static String generatePoint(int x, int y, int z, int w) {
        return String.format("%05d,%05d,%05d,%05d", x, y, z, w);
    }
    
    private static Set<String> runCyclePartOne(Set<String> inputSet) {
        Set<String> outputSet = new HashSet<>();
        Set<String> inactiveProcessingSet = new HashSet<>();

        // Start by examining every active point
        for (String pointCoords : inputSet) {
            String[] coords = pointCoords.split(",");
            int x = Integer.valueOf(coords[0]);
            int y = Integer.valueOf(coords[1]);
            int z = Integer.valueOf(coords[2]);
            int activeNeighbours = 0;
            
            // Let's examine every neighbour
            for (int nXdiff = -1; nXdiff <= 1; nXdiff++) {
                int nX = x + nXdiff;
                
                for (int nYdiff = -1; nYdiff <= 1; nYdiff++) {
                    int nY = y + nYdiff;
                    
                    for (int nZdiff = -1; nZdiff <= 1; nZdiff++) {
                        if (nXdiff == 0 && nYdiff == 0 && nZdiff == 0) {
                            continue;
                        }
                        
                        int nZ = z + nZdiff;
                        String neighbourCoords = generatePoint(nX, nY, nZ);
                        
                        if (inputSet.contains(neighbourCoords)) {
                            // active neighbour
                            activeNeighbours++;
                        } else {
                            // An inactive point next to an active point - we'll have to check this later
                            inactiveProcessingSet.add(neighbourCoords);
                        }
                    }
                }
            }
            
            // If active and there are 2 or 3 neighbours, it stays active in the new version
            // Otherwise, it flips to inactive, so we will not add it to the output set
            if (activeNeighbours == 2 || activeNeighbours == 3) {
                outputSet.add(pointCoords);
            }
        }
        
        // Now we look at all of the inactive points that have at least one active neighbour
        for (String point : inactiveProcessingSet) {
            String[] coords = point.split(",");
            int x = Integer.valueOf(coords[0]);
            int y = Integer.valueOf(coords[1]);
            int z = Integer.valueOf(coords[2]);
            int activeNeighbours = 0;
            
            // Let's examine every neighbour
            for (int nXdiff = -1; nXdiff <= 1; nXdiff++) {
                int nX = x + nXdiff;
                
                for (int nYdiff = -1; nYdiff <= 1; nYdiff++) {
                    int nY = y + nYdiff;
                    
                    for (int nZdiff = -1; nZdiff <= 1; nZdiff++) {
                        if (nXdiff == 0 && nYdiff == 0 && nZdiff == 0) {
                            continue;
                        }
                        
                        int nZ = z + nZdiff;
                        String neighbourCoords = generatePoint(nX, nY, nZ);
                        
                        if (inputSet.contains(neighbourCoords)) {
                            // active neighbour
                            activeNeighbours++;
                        }
                    }
                }
            }
            
            if (activeNeighbours == 3) {
                // Inactive point becomes active
                outputSet.add(point);
            }
        }
        
        return outputSet;
    }
    
    private static Set<String> runCyclePartTwo(Set<String> inputSet) {
        Set<String> outputSet = new HashSet<>();
        Set<String> inactiveProcessingSet = new HashSet<>();

        // Start by examining every active point
        for (String pointCoords : inputSet) {
            String[] coords = pointCoords.split(",");
            int x = Integer.valueOf(coords[0]);
            int y = Integer.valueOf(coords[1]);
            int z = Integer.valueOf(coords[2]);
            int w = Integer.valueOf(coords[3]);
            int activeNeighbours = 0;
            
            // Let's examine every neighbour
            for (int nXdiff = -1; nXdiff <= 1; nXdiff++) {
                int nX = x + nXdiff;
                
                for (int nYdiff = -1; nYdiff <= 1; nYdiff++) {
                    int nY = y + nYdiff;
                    
                    for (int nZdiff = -1; nZdiff <= 1; nZdiff++) {
                        int nZ = z + nZdiff;
                        
                        for (int nWdiff = -1; nWdiff <= 1; nWdiff++) {
                            if (nXdiff == 0 && nYdiff == 0 && nZdiff == 0 && nWdiff == 0) {
                                continue;
                            }
                            
                            int nW = w + nWdiff;
                            String neighbourCoords = generatePoint(nX, nY, nZ, nW);
                            
                            if (inputSet.contains(neighbourCoords)) {
                                // active neighbour
                                activeNeighbours++;
                            } else {
                                // An inactive point next to an active point - we'll have to check this later
                                inactiveProcessingSet.add(neighbourCoords);
                            }
                        }
                    }
                }
            }
            
            // If active and there are 2 or 3 neighbours, it stays active in the new version
            // Otherwise, it flips to inactive, so we will not add it to the output set
            if (activeNeighbours == 2 || activeNeighbours == 3) {
                outputSet.add(pointCoords);
            }
        }
        
        // Now we look at all of the inactive points that have at least one active neighbour
        for (String point : inactiveProcessingSet) {
            String[] coords = point.split(",");
            int x = Integer.valueOf(coords[0]);
            int y = Integer.valueOf(coords[1]);
            int z = Integer.valueOf(coords[2]);
            int w = Integer.valueOf(coords[3]);
            int activeNeighbours = 0;
            
            // Let's examine every neighbour
            for (int nXdiff = -1; nXdiff <= 1; nXdiff++) {
                int nX = x + nXdiff;
                
                for (int nYdiff = -1; nYdiff <= 1; nYdiff++) {
                    int nY = y + nYdiff;
                    
                    for (int nZdiff = -1; nZdiff <= 1; nZdiff++) {
                        int nZ = z + nZdiff;
                        
                        for (int nWdiff = -1; nWdiff <= 1; nWdiff++) {
                            if (nXdiff == 0 && nYdiff == 0 && nZdiff == 0 && nWdiff == 0) {
                                continue;
                            }
                            
                            int nW = w + nWdiff;
                            String neighbourCoords = generatePoint(nX, nY, nZ, nW);
                            
                            if (inputSet.contains(neighbourCoords)) {
                                // active neighbour
                                activeNeighbours++;
                            }
                        }
                    }
                }
            }
            
            if (activeNeighbours == 3) {
                // Inactive point becomes active
                outputSet.add(point);
            }
        }
        
        return outputSet;
    }
}
