package search_methods;

import java.util.*;

/**
 * This class represents a route finder that uses the Best-First Search algorithm
 * to find the best route between two cities in a graph.
 */
public class BestFSRouteFinder {
    private Map<String, List<String>> adjacencyList;
    private Map<String, City> cities;

    /**
     * Creates a new BestFSRouteFinder with the given adjacency list and city data.
     *
     * @param adjacencyList A map representing the adjacency list of cities.
     * @param cities        A map representing city data including coordinates.
     */
    public BestFSRouteFinder(Map<String, List<String>> adjacencyList, Map<String, City> cities) {
        this.adjacencyList = adjacencyList;
        this.cities = cities;
    }

    /**
     * Finds the best route between two cities using the Best-First Search algorithm.
     *
     * @param startCity The starting city.
     * @param endCity   The ending city.
     * @return A list of city names representing the best route, or null if no route is found.
     */
    public List<String> findRoute(String startCity, String endCity) {
        PriorityQueue<CityNode> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(node -> node.totalCost));
        Map<String, String> parentMap = new HashMap<>();
        Set<String> visited = new HashSet<>();

        priorityQueue.add(new CityNode(startCity, 0.0));

        while (!priorityQueue.isEmpty()) {
            CityNode currentNode = priorityQueue.poll();
            String currentCity = currentNode.city;

            if (currentCity.equals(endCity)) {
                // Reconstruct and return the path
                return reconstructPath(parentMap, startCity, endCity);
            }

            if (!visited.contains(currentCity)) {
                visited.add(currentCity);

                for (String neighbor : adjacencyList.get(currentCity)) {
                    if (!visited.contains(neighbor)) {
                        double costToNeighbor = calculateCost(currentCity, neighbor);

                        priorityQueue.add(new CityNode(neighbor, costToNeighbor));
                        parentMap.put(neighbor, currentCity);
                    }
                }
            }
        }

        // No route found
        return null;
    }

    /**
     * Calculates the cost (distance) between two cities based on their coordinates.
     *
     * @param currentCity The current city.
     * @param neighbor    The neighboring city.
     * @return The cost (distance) between the two cities.
     */
    private double calculateCost(String currentCity, String neighbor) {
        // Calculate the cost based on the distance between city coordinates
        City current = cities.get(currentCity);
        City next = cities.get(neighbor);

        if (current != null && next != null) {
            double lat1 = current.getLatitude();
            double lon1 = current.getLongitude();
            double lat2 = next.getLatitude();
            double lon2 = next.getLongitude();

            // Using the Haversine formula to calculate distance
            double earthRadius = 6371; // Radius of the Earth in kilometers
            double dLat = Math.toRadians(lat2 - lat1);
            double dLon = Math.toRadians(lon2 - lon1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                            Math.sin(dLon / 2) * Math.sin(dLon / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distance = earthRadius * c;

            return distance;
        } else {
            return Double.MAX_VALUE; // Return a very large value if coordinates are missing
        }
    }

    /**
     * Reconstructs the path from the end city to the start city using the parent map.
     *
     * @param parentMap  A map representing parent-child relationships in the route.
     * @param start      The starting city.
     * @param end        The ending city.
     * @return A list of city names representing the reconstructed path.
     */
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

    /**
     * Represents a node in the priority queue used for Best-First Search.
     */
    private static class CityNode {
        String city;
        double totalCost;

        public CityNode(String city, double totalCost) {
            this.city = city;
            this.totalCost = totalCost;
        }
    }
}
