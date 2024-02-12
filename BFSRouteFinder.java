/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package search_methods;

import java.util.*;

public class BFSRouteFinder {
    private Map<String, List<String>> adjacencyList;

    public BFSRouteFinder(Map<String, List<String>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public List<String> findRoute(String startCity, String endCity) {
        Queue<String> queue = new LinkedList<>();
        Map<String, String> parentMap = new HashMap<>();
        Set<String> visited = new HashSet<>();

        queue.add(startCity);
        visited.add(startCity);

        while (!queue.isEmpty()) {
            String currentCity = queue.poll();

            if (currentCity.equals(endCity)) {
                // Reconstruct and return the path
                return reconstructPath(parentMap, startCity, endCity);
            }

            for (String neighbor : adjacencyList.get(currentCity)) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    parentMap.put(neighbor, currentCity);
                }
            }
        }

        // No route found
        return null;
    }

    private List<String> reconstructPath(Map<String, String> parentMap, String start, String end) {
        List<String> path = new ArrayList<>();
        String current = end;

        while (!current.equals(start)) {
            path.add(current);
            current = parentMap.get(current);
        }

        path.add(start);
        Collections.reverse(path);
        return path;
    }
}
