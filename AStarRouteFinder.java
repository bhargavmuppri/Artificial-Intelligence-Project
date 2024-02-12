package search_methods;

import java.util.*;

public class AStarRouteFinder {
    private Map<String, List<String>> adjacencyList;
    private Map<String, City> cities;

    public AStarRouteFinder(Map<String, List<String>> adjacencyList, Map<String, City> cities) {
        this.adjacencyList = adjacencyList;
        this.cities = cities;
    }

    public List<String> findRoute(String startCity, String endCity) {
        if (!cities.containsKey(startCity) || !cities.containsKey(endCity)) {
            System.out.println("Start city or end city not found in the cities database.");
            return null;
        }

        PriorityQueue<CityNode> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(node -> node.totalCost));
        Map<String, String> parentMap = new HashMap<>();
        Map<String, Double> gValues = new HashMap<>();
        Set<String> visited = new HashSet<>();

        gValues.put(startCity, 0.0);
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
                        double tentativeGValue = gValues.get(currentCity) + costToNeighbor;

                        if (!gValues.containsKey(neighbor) || tentativeGValue < gValues.get(neighbor)) {
                            gValues.put(neighbor, tentativeGValue);
                            double totalCost = tentativeGValue + calculateHeuristic(neighbor, endCity);

                            priorityQueue.add(new CityNode(neighbor, totalCost));
                            parentMap.put(neighbor, currentCity);
                        }
                    }
                }
            }
        }

        // No route found
        return null;
    }

    private double calculateCost(String currentCity, String neighbor) {
        // Calculate the cost based on the distance between city coordinates using the Haversine formula
        City current = cities.get(currentCity);
        City next = cities.get(neighbor);

        double lat1 = current.getLatitude();
        double lon1 = current.getLongitude();
        double lat2 = next.getLatitude();
        double lon2 = next.getLongitude();

        double earthRadius = 6371; // Radius of the Earth in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        return distance;
    }

    private double calculateHeuristic(String currentCity, String endCity) {
        // Calculate the heuristic (estimated cost) based on the straight-line distance
        City current = cities.get(currentCity);
        City end = cities.get(endCity);

        double lat1 = current.getLatitude();
        double lon1 = current.getLongitude();
        double lat2 = end.getLatitude();
        double lon2 = end.getLongitude();

        double earthRadius = 6371; // Radius of the Earth in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        return distance;
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

    private static class CityNode {
        String city;
        double totalCost;

        public CityNode(String city, double totalCost) {
            this.city = city;
            this.totalCost = totalCost;
        }
    }
}
