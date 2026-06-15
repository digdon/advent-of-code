package day_07;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class Day07 {
    
    public static void main(String[] args) throws IOException {
        // Load in the puzzle input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();

        Folder<File> fileSystem = new Folder<>("/");
        Folder<File> currentFolder = fileSystem;
        
        for (String line : inputLines) {
            if (line.startsWith("$ cd")) {
                // Switching to a directory
                String name = line.substring(line.lastIndexOf(' ') + 1);
                
                if (name.equals("/")) {
                    // Go back to root
                    currentFolder = fileSystem;
                } else if (name.equals("..")) {
                    // Go up a level, if we can
                    if (currentFolder.getParent() != null) {
                        currentFolder = currentFolder.getParent();
                    }
                } else {
                    // Try going into specified folder
                    Optional<Folder<File>> subFolder = currentFolder.getChildNodes().stream().filter(n -> n.getName().equals(name)).findFirst();
                    
                    if (subFolder.isPresent()) {
                        currentFolder = subFolder.get();
                    } else {
                        System.out.println("Folder " + name + " is unknown in " + currentFolder.getName());
                        System.exit(0);
                    }
                }
            } else if (line.startsWith("$ ls")) {
                // Directory listing - parse and build elements after this
                continue;
            } else {
                String[] parts = line.split("\\s+");
                
                // Directory item
                if (parts[0].equals("dir")) {
                    // Child directory
                    Folder<File> folder = new Folder<>(parts[1], currentFolder);
                    currentFolder.addChildNode(folder);
                } else {
                    // File
                    File file = new File(parts[1], Long.valueOf(parts[0]));
                    currentFolder.addDataItem(file);
                }
            }
        }
        
        calculateFolderSize(fileSystem);

        long freeSpace = 70000000 - fileSystem.getSize();
        long requiredSpace = 30000000 - freeSpace;
        
        long part1Total = 0;
        long part2Value = fileSystem.getSize();
        
        Queue<Folder<File>> queue = new ArrayDeque<>();
        queue.add(fileSystem);
        
        while (queue.isEmpty() == false) {
            Folder<File> item = queue.remove();
            long size = item.getSize();
            
            if (size <= 100000) {
                part1Total += size;
            }

            if (size >= requiredSpace && size < part2Value) {
                part2Value = size;
            }
            
            if (item.getChildNodes() != null) {
                queue.addAll(item.getChildNodes());
            }
        }
        
        System.out.println("Part 1: " + part1Total);
        
        System.out.println("Part 2: " + part2Value);
    }

    private static void calculateFolderSize(Folder<File> folder) {
        long counter = 0;
        
        if (folder.getChildNodes() != null) {
            for (Folder<File> subFolder : folder.getChildNodes()) {
                calculateFolderSize(subFolder);
                counter += subFolder.getSize();
            }
        }
        
        if (folder.getDataItems() != null) {
            for (File item : folder.getDataItems()) {
                counter += item.size();
            }
        }
        
        folder.setSize(counter);
    }
    
    static class Folder<T> {
        private String name;
        private Folder<T> parentNode;
        private Set<Folder<T>> childNodes;
        private Set<T> dataList;
        private long size;
        
        public Folder(String name) {
            this.name = name;
        }
        
        public Folder(String name, Folder<T> parent) {
            this(name);
            this.parentNode = parent;
        }
        
        public String getName() {
            return name;
        }
        
        public Folder<T> getParent() {
            return parentNode;
        }
        
        public void addChildNode(Folder<T> node) {
            if (childNodes == null) {
                childNodes = new HashSet<>();
            }
            
            childNodes.add(node);
        }

        public Set<Folder<T>> getChildNodes() {
            return childNodes;
        }
        
        public void addDataItem(T dataItem) {
            if (dataList == null) {
                dataList = new HashSet<>();
            }
            
            dataList.add(dataItem);
        }
        
        public Set<T> getDataItems() {
            return dataList;
        }
        
        public void setSize(long size) {
            this.size = size;
        }
        
        public long getSize() {
            return size;
        }
    }
    
    record File(String name, long size) {}
}
