package day_09;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day09 {

    private static final Pattern INPUT_PATTERN = Pattern.compile("^(\\w+)\\s+to\\s+(\\w+)\\s*=\\s*(\\d+)$");
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        Map<String, Map<String, Integer>> pointMap = new HashMap<>();
        
        for (String input : inputLines) {
            Matcher matcher = INPUT_PATTERN.matcher(input);
            
            if (matcher.matches() == false) {
                continue;
            }
            
            String start = matcher.group(1);
            String end = matcher.group(2);
            int distance = Integer.parseInt(matcher.group(3));
            
            Map<String, Integer> edge = pointMap.get(start);
            
            if (edge == null) {
                edge = new HashMap<>();
                pointMap.put(start, edge);
            }
            
            edge.put(end, distance);
            
            edge = pointMap.get(end);
            
            if (edge == null) {
                edge = new HashMap<>();
                pointMap.put(end, edge);
            }
            
            edge.put(start, distance);
        }
        
        for (String start : pointMap.keySet()) {
            generatePath(pointMap, start);
        }

        int shortestLength = Integer.MAX_VALUE;
        List<String> shortestPath = null;
        int longestLength = 0;
        List<String> longestPath = null;
        
        for (List<String> path : pathList) {
            int length = calculatePathLength(path, pointMap);
            
            if (length < shortestLength) {
                shortestLength = length;
                shortestPath = path;
            }
            
            if (length> longestLength) {
                longestLength = length;
                longestPath = path;
            }
        }
        
        System.out.println("Part one: " + shortestLength);
        System.out.println(shortestPath);
        
        System.out.println("Part two: " + longestLength);
        System.out.println(longestPath);
    }

    private static List<List<String>> pathList = new ArrayList<>();
    
    private static void generatePath(Map<String, Map<String, Integer>> graph, String start) {
        List<String> path = new ArrayList<>();
        path.add(start);
        nextStep(graph, start, path);
    }

    private static void nextStep(Map<String, Map<String, Integer>> graph, String point, List<String> path) {
        if (path.size() == graph.keySet().size()) {
            pathList.add(new ArrayList<>(path));
            return;
        }
        
        Map<String, Integer> map = graph.get(point);
        
        for (String next : map.keySet()) {
            if (path.contains(next) == false) {
                path.add(next);
                nextStep(graph, next, path);
                path.remove(next);
            }
        }
    }
    
    private static int calculatePathLength(List<String> path, Map<String, Map<String, Integer>> graph) {
        int length = 0;

        for (int i = 0; i < path.size() - 1; i++) {
            Map<String, Integer> current = graph.get(path.get(i));
            length += current.get(path.get(i + 1));
        }
        
        return length;
    }
}
