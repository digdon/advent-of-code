package day_20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day20 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        // Build the image enhancement algorithm
        String data = inputLines.get(0);
        boolean[] enhancementCode = new boolean[data.length()];
        
        for (int i = 0; i < data.length(); i++) {
            enhancementCode[i] = data.charAt(i) == '#' ? true : false;
        }
        
        // A special check to see if we're using trick data or not
        boolean infinityFlip = (enhancementCode[0] == true && enhancementCode[enhancementCode.length - 1] == false);
        
        // Build the starting image
        Image image = new Image();
        
        for (int i = 2; i < inputLines.size(); i++) {
            int y = i - 2;
            String line = inputLines.get(i);
            
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == '#') {
                    image.lightPixel(new Point(x, y));
                }
            }
        }

        boolean infinityValue = false;
        long start = System.currentTimeMillis();
        
        // Process the image
        for (int i = 0; i < 50; i++) {
            if (i == 2) {
                System.out.println("Part 1: " + image.lightedPixels.size());
            }
            
            image = processImage(image, enhancementCode, infinityValue);

            if (infinityFlip) {
                infinityValue = !infinityValue;
            }
        }
        
        displayImage(image);
        System.out.println("Part 2: " + image.lightedPixels.size());
        System.out.println(System.currentTimeMillis() - start);
    }

    private static Image processImage(Image image, boolean[] enhancement, boolean infinityValue) {
        Image newImage = new Image();
        Point tempPoint = new Point(0, 0);

        // Examine every pixel in the input image. However, because the input image affects pixels outside
        // its immediate range, we need to expand in each direction by 1 pixel
        for (int y = image.minY - 1; y <= image.maxY + 1; y++) {
            for (int x = image.minX - 1; x <= image.maxX + 1; x++) {
                // This is the current pixel we're examining/calculating/processing
                // We need to grab the values of the 9 pixels that surround it (8 + the pixel itself)
                StringBuilder sb = new StringBuilder();
                
                for (int i = 0; i < DIRECTIONS.length; i++) {
                    int newX = x + DIRECTIONS[i][0];
                    int newY = y + DIRECTIONS[i][1];

                    if (newX < image.minX || newX > image.maxX
                            || newY < image.minY || newY > image.maxY) {
                        sb.append(infinityValue ? 1 : 0);
                    } else {
                        tempPoint.x = newX;
                        tempPoint.y = newY;
                        sb.append(image.isPixelLit(tempPoint) ? 1 : 0);
                    }
                }
                
                // Convert the binary string into a int value
                int index = Integer.parseInt(sb.toString(), 2);
                
                // Set the current pixel in the new image to the value indicated by the index
                if (enhancement[index]) {
                    newImage.lightPixel(new Point(x, y));
                }
            }
        }
        
        return newImage;
    }
    
    private static void displayImage(Image image) {
        Point tempPoint = new Point(0, 0);
        
        for (int y = image.minY; y <= image.maxY; y++) {
            for (int x = image.minX; x <= image.maxX; x++) {
                tempPoint.x = x;
                tempPoint.y = y;
                System.out.print(image.isPixelLit(tempPoint) ? '#' : '.');
            }
            
            System.out.println();
        }
    }
    
    private static final int[][] DIRECTIONS = {
            { -1, -1 },
            {  0, -1 },
            {  1, -1 },
            { -1,  0 },
            {  0,  0 },
            {  1,  0 },
            { -1,  1 },
            {  0,  1 },
            {  1,  1}
    };
    
    static class Image {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        Set<Point> lightedPixels = new HashSet<>();
        
        void lightPixel(Point point) {
            if (point.x < minX) {
                minX = point.x;
            }
            
            if (point.x > maxX) {
                maxX = point.x;
            }
            
            if (point.y < minY) {
                minY = point.y;
            }
            
            if (point.y > maxY) {
                maxY = point.y;
            }
            
            lightedPixels.add(point);
        }
        
        boolean isPixelLit(Point point) {
            return lightedPixels.contains(point);
        }
    }
    
    static class Point {
        int x;
        int y;
        
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            
            if ((o2 instanceof Point) == false) {
                return false;
            }
            
            Point point2 = (Point)o2;
            
            return (this.x == point2.x && this.y == point2.y);
        }
        
        @Override
        public int hashCode() {
            int tmp = (y + ((x + 1) / 2));
            return x + (tmp * tmp);
        }
        
        @Override
        public String toString() {
            return String.format("%d,%d", x, y);
        }
    }
}
