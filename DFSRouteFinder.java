/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package search_methods;
import java.util.*;

public class DFSRouteFinder {
  
    private Map<String, List<String>> adjacencyList;

    public DFSRouteFinder(Map<String, List<String>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public List<String> findRoute(String startCity, String endCity) {
        Set<String> visited = new HashSet<>();
        List<String> path = new ArrayList<>();

        if (dfs(startCity, endCity, visited, path)) {
            // Reconstruct and return the path
            Collections.reverse(path);
            return path;
        }

        // No route found
        return null;
    }

    private boolean dfs(String currentCity, String endCity, Set<String> visited, List<String> path) {
        visited.add(currentCity);
        path.add(currentCity);

        if (currentCity.equals(endCity)) {
            return true; // Found the destination
        }

        for (String neighbor : adjacencyList.get(currentCity)) {
            if (!visited.contains(neighbor)) {
                if (dfs(neighbor, endCity, visited, path)) {
                    return true;
                }
            }
        }

        path.remove(path.size() - 1); // Backtrack
        return false;
    }
}

