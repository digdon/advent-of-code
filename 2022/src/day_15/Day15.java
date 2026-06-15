package day_15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day15 {

    private static final Pattern BEACON_PATTERN = Pattern.compile(".*x=(-?\\d+),\\s*y=(-?\\d+):\\s*.*x=(-?\\d+),\\s*y=(-?\\d+)");
    
    public static void main(String[] args) throws IOException {
        // Load in the puzzle input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        List<Sensor> sensorList = new ArrayList<>();
        List<Point> beaconList = new ArrayList<>();
        
        for (String line : inputLines) {
            Matcher matcher = BEACON_PATTERN.matcher(line);
            
            if (matcher.matches() == false) {
                System.out.println("Invalid input line: " + line);
                continue;
            }

            int sensorX = Integer.parseInt(matcher.group(1));
            int sensorY = Integer.parseInt(matcher.group(2));
            
            int beaconX = Integer.parseInt(matcher.group(3));
            int beaconY = Integer.parseInt(matcher.group(4));
            
            int distance = Math.abs(sensorX - beaconX) + Math.abs(sensorY - beaconY);

            Point sensorPoint = new Point(sensorX, sensorY);
            Point beaconPoint = new Point(beaconX, beaconY);
            
            Sensor sensor = new Sensor(sensorPoint, beaconPoint, distance);
            sensorList.add(sensor);
            System.out.println(sensor);
            
            beaconList.add(beaconPoint);
        }

        long start = System.currentTimeMillis();
        part1(sensorList, beaconList);
        System.out.println(System.currentTimeMillis() - start);
        part2(sensorList, beaconList);
    }
    
    private static final int TARGET_ROW = 2000000;
//    private static final int TARGET_ROW = 10;
  
    private static void part1(List<Sensor> sensorList, List<Point> beaconList) {
        Set<Point> columnsHit = new HashSet<>();
        
        for (Sensor sensor : sensorList) {
            int senX = sensor.location().x();
            int senY = sensor.location().y();
            int distance = sensor.distance();
            
            int x = senX - distance;
            int y = senY;

            // Can we skip this sensor altogether?
            if ((senY < TARGET_ROW && senY + distance < TARGET_ROW) 
                    || (senY > TARGET_ROW && senY - distance > TARGET_ROW)) {
                System.out.println("Skipping sensor");
                continue;
            }
            
            for (int i = distance; i >= 0; i--) {
                boolean targetHit = false;
                
                if (senY < TARGET_ROW) {
                    if (y >= TARGET_ROW) {
                        targetHit = true;
                    }
                    // We're going up
                    y++;
                } else if (senY > TARGET_ROW) {
                    if (y <= TARGET_ROW) {
                        targetHit = true;
                    }
                    // We're going down
                    y--;
                } else {
                    targetHit = true;
                    // We're already on the target line
                }
                
                if (targetHit) {
                    columnsHit.add(new Point(x, TARGET_ROW));
                    columnsHit.add(new Point(senX + Math.abs(x - senX), TARGET_ROW));
                }
                
                x++;
            }
        }
        
        // Pull out points on target row that we know are beacons
        for (Point beacon : beaconList) {
            if (beacon.y() == TARGET_ROW) {
                columnsHit.remove(beacon);
            }
        }
        
        System.out.println("Part 1: " + columnsHit.size());
    }

    private static final int SEARCH_SPACE_MAX = 4000000;
//    private static final int SEARCH_SPACE_MAX = 20;
    
    private static void part2(List<Sensor> sensorList, List<Point> beaconList) {
        int emptyRow = 0, emptyCol = 0;
        
        for (int row = 0; row <= SEARCH_SPACE_MAX; row++) {
            if (row % 100000 == 0) {
                System.out.println(row);
            }
            
            // For each target row, check to see what columns are hit by the sensors
            List<Range> columnsHit = new ArrayList<>();
            
            for (Sensor sensor : sensorList) {
                int distance = sensor.distance();
                int senY = sensor.location().y();
                int senX = sensor.location().x();
                
                if ((senY - distance > row) || (senY + distance < row)) {
                    // Sensor can't reach this row - skip
                    continue;
                }
                
                // Sensor sees this row - work out how many columns it can see
                int lineDistance = Math.abs(row - senY);
                int columnCount = distance - lineDistance;
                int minCol = Math.max(0, senX - columnCount);
                int maxCol = Math.min(SEARCH_SPACE_MAX, senX + columnCount);
                columnsHit.add(new Range(minCol, maxCol));
            }
            
            columnsHit.sort((r1, r2) -> r1.start() - r2.start());
            int start = 0, end = 0;
            boolean gapFound = false;
            
            for (Range range : columnsHit) {
                if (range.start() > end) {
                    System.out.println(String.format("Found a gap: row = %d, last = %d, range = %s", row, end, range));
                    gapFound = true;
                    emptyRow = row;
                    emptyCol = end + 1;
                    break;
                }
                
                if (range.start() < start) {
                    start = range.start();
                }

                if (range.end() > end) {
                    end = range.end();
                }
            }
            
            if (gapFound) {
                break;
            }
        }
        
        long result = ((long)emptyCol * 4000000l) + emptyRow;
        System.out.println("Part 2: " + result);
    }
    
    record Point(int x, int y) {}
    
    record Range(int start, int end) {}
    
    record Sensor(Point location, Point closestBeacon, int distance) {}
}
