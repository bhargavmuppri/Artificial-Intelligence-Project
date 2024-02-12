/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package search_methods;

import java.util.*;

public class IDDFSRouteFinder {
    private Map<String, List<String>> adjacencyList;

    public IDDFSRouteFinder(Map<String, List<String>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public List<String> findRoute(String startCity, String endCity) {
        int depthLimit = 0;

        while (true) {
            Set<String> visited = new HashSet<>();
            List<String> path = new ArrayList<>();

            if (dfs(startCity, endCity, visited, path, depthLimit)) {
                // Reconstruct and return the path
                return path;
            }

            depthLimit++; // Increase the depth limit if no route is found at the current limit
        }
    }

    private boolean dfs(String currentCity, String endCity, Set<String> visited, List<String> path, int depthLimit) {
        if (currentCity.equals(endCity)) {
            path.add(currentCity);
            return true; // Found the destination
        }

        if (depthLimit == 0) {
            return false; // Reached depth limit without finding the destination
        }

        visited.add(currentCity);
        path.add(currentCity);

        for (String neighbor : adjacencyList.get(currentCity)) {
            if (!visited.contains(neighbor)) {
                if (dfs(neighbor, endCity, visited, path, depthLimit - 1)) {
                    return true;
                }
            }
        }

        path.remove(path.size() - 1); // Backtrack
        return false;
    }
}

