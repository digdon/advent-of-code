package day_12;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day12 {

    private static final List<String> pathList = new ArrayList<>();
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        // Build graph
        Map<String, List<String>> graph = new HashMap<>();
        
        for (String line : inputLines) {
            String[] parts = line.split("-");
            String nodeA = parts[0];
            String nodeB = parts[1];
            
            // Set up source -> dest link
            List<String> neighbours = graph.get(nodeA);
            
            if (neighbours == null) {
                neighbours = new ArrayList<>();
                graph.put(nodeA, neighbours);
            }
            
            neighbours.add(nodeB);
            
            // Set up dest -> source link
            neighbours = graph.get(nodeB);
            
            if (neighbours == null) {
                neighbours = new ArrayList<>();
                graph.put(nodeB, neighbours);
            }
            
            neighbours.add(nodeA);
        }
        
        System.out.println(graph);

        // Part 1
        walkGraph(graph, "start", new LinkedList<>(), false);
        
        System.out.println("Part 1: " + pathList.size());
        
        // Part 2
        pathList.clear();
        
        walkGraph(graph, "start", new LinkedList<>(), true);
        
        System.out.println("Part 2: " + pathList.size());
    }
    
    private static void walkGraph(Map<String, List<String>> graph, String node, LinkedList<String> path, boolean doubleSmall) {
        List<String> nextList = graph.get(node);
        
        if (nextList == null) {
            return;
        }

        path.addLast(node);

        for (String next : graph.get(node)) {
            if (next.equals("start")) {
                // We never go back to 'start' node
                continue;
            } else if (next.equals("end")) {
                // Found a full path - build it
                pathList.add(path.stream().collect(Collectors.joining(",")) + ",end");
            } else {
                boolean visitNext = false;

                if (next.equals(next.toUpperCase())) {
                    // We can visit big caves (upper case) multiple times
                    visitNext = true;
                } else {
                    if (doubleSmall == true) {
                        // We can visit big caves (upper case) multiple times. A single small cave (lower case) can be visited twice.
                        // All other small caves can only be visited once
                        visitNext = okToVisitNode(next, path);
                    } else {
                        // We can visit small caves (lower case) only once
                        visitNext = (next.equals(next.toLowerCase()) && path.contains(next) == false);
                    }
                }
                
                if (visitNext) {
                    walkGraph(graph, next, path, doubleSmall);
                }
            }
        }
        
        path.removeLast();
    }
    
    private static boolean okToVisitNode(String nodeToVisit, List<String> path) {
        Map<String, Integer> countMap = new HashMap<>();
        String doubleVisitNode = null;
        
        for (String node : path) {
            if (node.equals(node.toUpperCase())) {
                // We don't count big caves, because we can visit them indefinitely
                continue;
            }
            
            Integer count = countMap.get(node);
            
            if (count == null) {
                count = 0;
            }
            
            count++;
            countMap.put(node, count);
            
            if (count >= 2) {
                // Found a small cave that has been visited twice
                doubleVisitNode = node;
            }
        }

        if (doubleVisitNode != null) {
            if (doubleVisitNode.equals(nodeToVisit)) {
                // the cave we're trying to go to has already been visited twice
                return false;
            } else {
                // a different cave has been seen twice, but we can go to the target if it has not yet been visited
                return !countMap.containsKey(nodeToVisit);
            }
        }
        
        return true; 
    }
}
