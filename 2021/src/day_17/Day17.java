package day_17;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day17 {

    public static void main(String[] args) {
        String input = "target area: x=253..280, y=-73..-46";
//        String input = "target area: x=20..30, y=-10..-5";
        
        Pattern pattern = Pattern.compile("x=(-?\\d+)\\.\\.(-?\\d+),\\s*y=(-?\\d+)\\.\\.(-?\\d+)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find() == false) {
            System.out.println("Bad input data: " + input);
            System.exit(-1);
        }

        int x1 = Integer.parseInt(matcher.group(1));
        int x2 = Integer.parseInt(matcher.group(2));
        int y1 = Integer.parseInt(matcher.group(3));
        int y2 = Integer.parseInt(matcher.group(4));
        
        int xmin = Math.min(x1, x2);
        int xmax = Math.max(x1, x2);
        int ymin = Math.min(y1, y2);
        int ymax = Math.max(y1, y2);

        int yvMax = ymax < 0 ? (Math.abs(ymin) - 1) : ymax;
        int maxY = (yvMax * (yvMax + 1)) / 2;
        
        System.out.println("Part 1: " + maxY);

        // Part 2 stuff
        Set<String> velocitySet = new HashSet<>();
        
        int startXv = (int)Math.sqrt(xmin); // Calculate reasonable start for Xv (ie, not at 0, not at xmin)
        int startYv = yvMax;
        
        for (int xv = startXv; xv <= xmax; xv++) {
            for (int yv = startYv; yv >= ymin; yv--) {
                int currXv = xv;
                int currYv = yv;
                int x = 0, y = 0;
                
                while (x <= xmax && y >= ymin) {
                    x += currXv;
                    y += currYv;
                    
                    if (x >= xmin && x <= xmax && y >= ymin && y <= ymax) {
                        velocitySet.add(String.format("%d,%d", xv, yv));
                        break;
                    }
                    
                    currXv = Math.max(0, currXv - 1);
                    currYv--;
                }
            }
        }

        System.out.println("Part 2: " + velocitySet.size());
    }
}
