package day_19;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class Day19 {

    record Vector3D(int x, int y, int z) {}
    record Pair(Vector3D a, Vector3D b) {}

    private interface Rotate {
        Vector3D rotate(int x, int y, int z);
    }

    private static final Rotate[] ROTATIONS = {
            (x, y, z) -> new Vector3D(x, y, z),    // Base - no rotation
            (x, y, z) -> new Vector3D(z, y, -x),   // y: 90, p: 0, r: 0
            (x, y, z) -> new Vector3D(-x, y, -z),  // y: 180, p: 0, r: 0
            (x, y, z) -> new Vector3D(-z, y, x),   // y: -90, p: 0, r: 0
            (x, y, z) -> new Vector3D(-y, x, z),   // y: 0, p: 90, r: 0
            (x, y, z) -> new Vector3D(z, x, y),    // y: 90, p: 90, r: 0 (could also be y: 0, p: 90, r: 90)
            (x, y, z) -> new Vector3D(y, x, -z),   // y: 180, p: 90, r: 0 (could also be y: 0, p: 90, r: 180)
            (x, y, z) -> new Vector3D(-z, x, -y),  // y: -90, p: 90, r: 0 (could also be y: 0, p: 90, r: -90)
            (x, y, z) -> new Vector3D(y, -x, z),
            (x, y, z) -> new Vector3D(z, -x, -y),
            (x, y, z) -> new Vector3D(-y, -x, -z),
            (x, y, z) -> new Vector3D(-z, -x, y),
            (x, y, z) -> new Vector3D(x, -z, y),
            (x, y, z) -> new Vector3D(y, -z, -x),
            (x, y, z) -> new Vector3D(-x, -z, -y),
            (x, y, z) -> new Vector3D(-y, -z, x),
            (x, y, z) -> new Vector3D(x, -y, -z),
            (x, y, z) -> new Vector3D(-z, -y, -x),
            (x, y, z) -> new Vector3D(-x, -y, z),
            (x, y, z) -> new Vector3D(z, -y, x),
            (x, y, z) -> new Vector3D(x, z, -y),
            (x, y, z) -> new Vector3D(-y, z, -x),
            (x, y, z) -> new Vector3D(-x, z, y),
            (x, y, z) -> new Vector3D(y, z, x)
    };

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        Map<Integer, List<Vector3D>> scannerBeacons = new HashMap<>();
        
        List<Vector3D> activeList = null;
        
        for (String line : inputLines) {
            if (line.length() == 0) {
                continue;
            } else if (line.contains("scanner")) {
                int pos = line.indexOf(' ', line.indexOf("scanner")) + 1;
                int scannerNumber = Integer.parseInt(line.substring(pos, line.indexOf(' ', pos)));
                activeList = new ArrayList<>();
                scannerBeacons.put(scannerNumber, activeList);
            } else {
                String[] parts = line.split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                int z = Integer.parseInt(parts[2]);
                activeList.add(new Vector3D(x, y, z));
            }
        }

        Map<Integer, Map<Vector3D, Set<Integer>>> scannerBeaconDistances = new HashMap<>();
        
        for (Entry<Integer, List<Vector3D>> entry : scannerBeacons.entrySet()) {
            scannerBeaconDistances.put(entry.getKey(), calculateDistances(entry.getValue()));
        }

        List<Map<Vector3D, Set<Integer>>> anchorBeacons = new ArrayList<>();
        anchorBeacons.add(scannerBeaconDistances.get(0));
        List<Vector3D> scannerOffsets = new ArrayList<>();
        
        List<Integer> scannerList = new ArrayList<>(scannerBeacons.keySet());
        scannerList.sort(null);

        Deque<Integer> queue = new LinkedList<>(scannerList);
        queue.remove();
        
        while (queue.isEmpty() == false) {
            int scannerNumber = queue.remove();
//            System.out.println("Processing scanner " + scannerNumber);
            boolean rotationFound = false;
            
            for (Map<Vector3D, Set<Integer>> anchor : anchorBeacons) {
                List<Pair> matchPairs = new ArrayList<>();
                Map<Vector3D, Set<Integer>> s2 = scannerBeaconDistances.get(scannerNumber);
                
                for (Entry<Vector3D,Set<Integer>> b1 : anchor.entrySet()) {
                    Set<Integer> b1Dists = b1.getValue();
                    
                    for (Entry<Vector3D, Set<Integer>> b2 : s2.entrySet()) {
                        Set<Integer> intersect = new HashSet<>(b2.getValue());
                        intersect.retainAll(b1Dists);
                        
                        if (intersect.size() >= 12) {
                            matchPairs.add(new Pair(b1.getKey(), b2.getKey()));
                        }
                    }
                }
                
                if (matchPairs.size() == 0) {
                    // No beacon matches - try the next anchor
                    continue;
                }
                
                // Start working through rotations
//                System.out.println("overlaps found - finding rotation");
                
                for (int i = 0; i < ROTATIONS.length; i++) {
                    Set<Vector3D> diffSet = new HashSet<>();
                    
                    for (Pair mp : matchPairs) {
                        diffSet.add(difference(mp.a(), ROTATIONS[i].rotate(mp.b().x(), mp.b().y(), mp.b().z())));
                    }
                    
                    if (diffSet.size() == 1) {
                        // All of the anchor -> rotated beacons have the same vector, so we've found the correct rotation
                        // Rotate all of the beacons in the second scanner and translate them based on the difference vector
                        rotationFound = true;
                        Vector3D diff = diffSet.iterator().next();
                        final int x = i;
                        List<Vector3D> newBeacons = scannerBeacons.get(scannerNumber)
                            .stream()
                            .map(b -> ROTATIONS[x].rotate(b.x(), b.y(), b.z()))
                            .map(b -> new Vector3D(b.x() + diff.x(), b.y() + diff.y(), b.z() + diff.z()))
                            .toList();
                        
                        // Add new beacons to anchor set and recalculate anchor distances
                        anchorBeacons.add(calculateDistances(newBeacons));
                        
                        // Record scanner offset
                        scannerOffsets.add(diff);
                        
                        break;
                    }
                }
                
                if (rotationFound == true) {
                    break;
                }
            }
            
            if (rotationFound == false) {
                queue.add(scannerNumber);
                continue;
            }
        }
        
        Set<Vector3D> anchorSet = new HashSet<>();
        anchorBeacons.forEach(ab -> anchorSet.addAll(ab.keySet()));
        System.out.println("Part 1: " + anchorSet.size());

        int maxDistance = 0;
        
        for (int i = 0; i < scannerOffsets.size() - 1; i++) {
            for (int j = i + 1; j < scannerOffsets.size(); j++) {
                maxDistance = Math.max(maxDistance, dist(scannerOffsets.get(i), scannerOffsets.get(j)));
            }
        }
        
        System.out.println("Part 2: " + maxDistance);
    }

    private static int dist(Vector3D a, Vector3D b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y()) + Math.abs(a.z() - b.z());
    }
    
    private static Map<Vector3D, Set<Integer>> calculateDistances(List<Vector3D> list) {
        Map<Vector3D, Set<Integer>> beaconDistances = new HashMap<>();
        for (Vector3D source : list) {
            beaconDistances.put(source, list.stream().map(b -> dist(source, b)).collect(Collectors.toSet()));
        }
        
        return beaconDistances;
    }
    
    private static Vector3D difference(Vector3D a, Vector3D b) {
        return new Vector3D(a.x() - b.x(), a.y() - b.y(), a.z() - b.z());
    }
}
