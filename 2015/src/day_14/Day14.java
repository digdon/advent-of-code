package day_14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day14 {

    private static final Pattern INPUT_PATTERN = Pattern.compile("(\\w+) can fly (\\d+) km/s for (\\d+) .* rest for (\\d+) .*");
    
    private static final int MAX_TIME = 2503;

    record Reindeer(String name, int speed, int goTime, int restTime) {}
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        List<Reindeer> reindeerList = new ArrayList<>();
        
        for (String line : inputLines) {
            Matcher matcher = INPUT_PATTERN.matcher(line);
            
            if (matcher.matches() == false) {
                continue;
            }
            
            String name = matcher.group(1);
            int speed = Integer.parseInt(matcher.group(2));
            int goTime = Integer.parseInt(matcher.group(3));
            int restTime = Integer.parseInt(matcher.group(4));

            reindeerList.add(new Reindeer(name, speed, goTime, restTime));
        }

        // Part 1
        int maxDist = 0;
        String maxReindeer = null;
        
        for (Reindeer reindeer : reindeerList) {
            int dist = calcDistance(reindeer.speed(), reindeer.goTime(), reindeer.restTime(), MAX_TIME);
            
            if (dist > maxDist) {
                maxDist = dist;
                maxReindeer = reindeer.name();
            }
        }

        System.out.println(String.format("Part 1: %d (%s)", maxDist, maxReindeer));
        
        // Part 2
        Map<String, Moving> movingMap = new HashMap<>();
        
        for (Reindeer r : reindeerList) {
            Moving moving = new Moving();
            moving.goTime = r.goTime();
            moving.restTime = r.restTime();
            movingMap.put(r.name(), moving);
        }

        int currMaxDist = 0;
        
        for (int i = 0; i < MAX_TIME; i++) {
            for (Reindeer r : reindeerList) {
                Moving m = movingMap.get(r.name());
                
                if (m.goTime > 0) {
                    m.dist += r.speed();
                    m.goTime--;
                    
                    if (m.dist > currMaxDist) {
                        currMaxDist = m.dist;
                    }
                } else {
                    m.restTime--;
                    
                    if (m.restTime == 0) {
                        m.goTime = r.goTime();
                        m.restTime = r.restTime();
                    }
                }
            }
            
            for (Reindeer r : reindeerList) {
                Moving m = movingMap.get(r.name);
                
                if (m.dist == currMaxDist) {
                    m.score++;
                }
            }
        }
        
        Entry<String,Moving> max = Collections.max(movingMap.entrySet(), (e1, e2) -> e1.getValue().score - e2.getValue().score);
        System.out.println(String.format("Part 2: %d (%s)", max.getValue().score, max.getKey()));
    }
    
    private static int calcDistance(int speed, int goTime, int restTime, int totalTime) {
        int iterations = totalTime / (goTime + restTime);
        int partial = totalTime % (goTime + restTime);
        int dist = ((speed * goTime) * iterations) + (speed * Math.min(goTime, partial));
        
        return dist;
    }
    
    static class Moving {
        int goTime;
        int restTime;
        int dist;
        int score;
    }
}
