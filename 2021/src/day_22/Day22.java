package day_22;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day22 {
    
    private static final Pattern XYZ_PATTERN = Pattern.compile("(on|off)\\s+x=(-?\\d+)\\.\\.(-?\\d+),y=(-?\\d+)\\.\\.(-?\\d+),z=(-?\\d+)\\.\\.(-?\\d+)");
    
    record Cuboid(long x1, long x2, long y1, long y2, long z1, long z2) {}
    record Step(boolean on, Cuboid cuboid) {}

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        List<Step> steps = new ArrayList<>();
        
        for (String line : inputLines) {
            // Parse out the steps
            Matcher matcher = XYZ_PATTERN.matcher(line);
            
            if (matcher.matches() == false) {
                System.out.println(line + " doesn't match regex");
                System.exit(-1);
            }

            boolean on = matcher.group(1).equalsIgnoreCase("on");
            
            int x1 = Integer.parseInt(matcher.group(2));
            int x2 = Integer.parseInt(matcher.group(3));
            int y1 = Integer.parseInt(matcher.group(4));
            int y2 = Integer.parseInt(matcher.group(5));
            int z1 = Integer.parseInt(matcher.group(6));
            int z2 = Integer.parseInt(matcher.group(7));
            
            steps.add(new Step(on, new Cuboid(x1, x2, y1, y2, z1, z2)));
        }

        List<Cuboid> part1CubeList = new ArrayList<>();
        List<Cuboid> part2CubeList = new ArrayList<>();
        
        for (Step step : steps) {
            Cuboid cube = step.cuboid();

            // For part 1, we only process steps with cuboids within the -50 to 50 range
            if (cube.x1() >= -50 && cube.x2() <= 50
                    && cube.y1() >= -50 && cube.y2() <= 50
                    && cube.z1() >= -50 && cube.y2() <= 50) {
                part1CubeList = applyStep(step, part1CubeList);
            }
            
            part2CubeList = applyStep(step, part2CubeList);
        }
        
        System.out.println("Part 1: " + part1CubeList.stream().mapToLong(c -> calculateSize(c)).sum());
        System.out.println("Part 2: " + part2CubeList.stream().mapToLong(c -> calculateSize(c)).sum());
    }
    
    private static List<Cuboid> applyStep(Step step, List<Cuboid> onCubeList) {
        List<Cuboid> newCubeList = new ArrayList<>();
        boolean stepContained = false;

        for (Cuboid cuboid : onCubeList) {
            // check to see if the step cuboid overlaps with the current cuboid from the list
            if (doesOverlap(step.cuboid(), cuboid) == false) {
                // No overlap, add unmodified cuboid to new list
                newCubeList.add(cuboid);
                continue;
            }
            
            // step cuboid DOES overlap
            
            if (step.on() && containedBy(step.cuboid(), cuboid)) {
                // step cuboid is fully contained by current cuboid and because step is to turn on, we can just keep current cuboid
                // and lose the step entirely
                newCubeList.add(cuboid);
                stepContained = true;
            } else if (containedBy(cuboid, step.cuboid())) {
                // current cuboid is fully contained by step cuboid - regardless of whether step is on or off
                // we just drop the current cuboid
                continue;
            } else {
                // step cuboid partially overlaps current cuboid - break up current to make space for step
                newCubeList.addAll(makeSpace(cuboid, step.cuboid()));
            }
        }

        if (step.on() && !stepContained) {
            newCubeList.add(step.cuboid());
        }
        
        return newCubeList;
    }
    
    private static boolean doesOverlap(Cuboid one, Cuboid two) {
        return ((one.x1() >= two.x1() && one.x1() <= two.x2())
                    || (one.x2() <= two.x2() && one.x2() >= two.x1())
                    || (one.x1() < two.x1() && one.x2() > two.x2()))
                && ((one.y1() >= two.y1() && one.y1() <= two.y2())
                        || (one.y2() <= two.y2() && one.y2() >= two.y1())
                        || (one.y1() < two.y1() && one.y2() > two.y2()))
                && ((one.z1() >= two.z1() && one.z1() <= two.z2())
                        || (one.z2() <= two.z2() && one.z2() >= two.z1())
                        || (one.z1() < two.z1() && one.z2() > two.z2()));
    }

    private static boolean containedBy(Cuboid first, Cuboid second) {
        return first.x1() >= second.x1() && first.x2() <= second.x2()
                && first.y1() >= second.y1() && first.y2() <= second.y2()
                && first.z1() >= second.z1() && first.z2() <= second.z2();
    }
    
    private static List<Cuboid> makeSpace(Cuboid orig, Cuboid next) {
        List<Cuboid> pieces = new ArrayList<>();

        long startX = Math.max(orig.x1(), next.x1());
        long endX = Math.min(orig.x2(), next.x2());
        long startY = Math.max(orig.y1(), next.y1());
        long endY = Math.min(orig.y2(), next.y2());
        long startZ = Math.max(orig.z1(), next.z1());
        long endZ = Math.min(orig.z2(), next.z2());

        // Slice along X positions
        if (startX > orig.x1()) {
            pieces.add(new Cuboid(orig.x1(), startX - 1, orig.y1(), orig.y2(), orig.z1(), orig.z2()));
        }
        
        if (endX < orig.x2()) {
            pieces.add(new Cuboid(endX + 1, orig.x2(), orig.y1(), orig.y2(), orig.z1(), orig.z2()));
        }

        // Split along Y positions
        if (startY > orig.y1()) {
            pieces.add(new Cuboid(startX, endX, orig.y1(), startY - 1, orig.z1(), orig.z2()));
        }
        
        if (endY < orig.y2()) {
            pieces.add(new Cuboid(startX, endX, endY + 1, orig.y2(), orig.z1(), orig.z2()));
        }
        
        // Split along Z positions
        if (startZ > orig.z1()) {
            pieces.add(new Cuboid(startX, endX, startY, endY, orig.z1(), startZ - 1));
        }
        
        if (endZ < orig.z2()) {
            pieces.add(new Cuboid(startX, endX, startY, endY, endZ + 1, orig.z2()));
        }
        
        return pieces;
    }
    
    private static long calculateSize(Cuboid item) {
        return (item.x2() - item.x1() + 1) * (item.y2() - item.y1() + 1) * (item.z2() - item.z1() + 1);
    }
}
