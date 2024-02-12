





package search_methods;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Search_Methods {
    public static void main(String[] args) {
        // Load data from files and create necessary data structures
        Map<String, List<String>> adjacencyList = loadAdjacencyData("D:\\adjacencies.txt");
        Map<String, City> cities = loadCityData("D:\\coordinates.csv");

        Scanner scanner = new Scanner(System.in);
        boolean continueSearch = true;

        while (continueSearch) {
            System.out.println("Enter the starting city: ");
            String startCity = scanner.nextLine();

            System.out.println("Enter the ending city: ");
            String endCity = scanner.nextLine();

            if (!adjacencyList.containsKey(startCity) || !adjacencyList.containsKey(endCity)) {
                System.out.println("Both cities must be in the database. Try again.");
                continue;
            }

            System.out.println("Select a search method:");
            System.out.println("1. Undirected Brute-Force");
            System.out.println("2. Breadth-First Search");
            System.out.println("3. Depth-First Search");
            System.out.println("4. Iterative Deepening Depth-First Search");
            System.out.println("5. Best-First Search");
            System.out.println("6. A* Search");
            System.out.println("Enter the method number: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            List<String> route = null;
            long startTime = System.currentTimeMillis();

            switch (choice) {
                case 1:
                    // Implement undirected brute-force search (if necessary)
                    break;
                case 2:
                    // Implement Breadth-First Search
                    BFSRouteFinder bfsRouteFinder = new BFSRouteFinder(adjacencyList);
                    route = bfsRouteFinder.findRoute(startCity, endCity);
                    break;
                case 3:
                    // Implement Depth-First Search
                    DFSRouteFinder dfsRouteFinder = new DFSRouteFinder(adjacencyList);
                    route = dfsRouteFinder.findRoute(startCity, endCity);
                    break;
                case 4:
                    // Implement Iterative Deepening Depth-First Search
                    IDDFSRouteFinder iddfsRouteFinder = new IDDFSRouteFinder(adjacencyList);
                    route = iddfsRouteFinder.findRoute(startCity, endCity);
                    break;
                case 5:
                    // Implement Best-First Search
                    BestFSRouteFinder bestFSRouteFinder = new BestFSRouteFinder(adjacencyList, cities);
                    route = bestFSRouteFinder.findRoute(startCity, endCity);
                    break;
                case 6:
                    // Implement A* Search
                    AStarRouteFinder aStarRouteFinder = new AStarRouteFinder(adjacencyList, cities);
                    route = aStarRouteFinder.findRoute(startCity, endCity);
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid search method.");
            }

            long endTime = System.currentTimeMillis();

            if (route != null) {
                System.out.println("Route found:");
                for (String city : route) {
                    System.out.println(city);
                }
                double totalTime = (endTime - startTime) / 1000.0; // Convert to seconds
                System.out.println("Total time: " + totalTime + " seconds");

                // Calculate and display the total distance (node to node) for the cities visited on the route
                double totalDistance = calculateTotalDistance(route, cities);
                System.out.println("Total distance: " + totalDistance + " kilometers");
            } else {
                System.out.println("No route found.");
            }

            System.out.println("Do you want to search again? (yes/no): ");
            String response = scanner.nextLine().toLowerCase();
            continueSearch = response.equals("yes");
        }

        System.out.println("Thank you for using the route finder!");
    }

    // Implement methods for loading data, calculating distance, and other utility functions here

    // Rest of the code remains the same
    private static Map<String, List<String>> loadAdjacencyData(String fileName) {
        Map<String, List<String>> adjacencyList = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    String cityA = parts[0];
                    String cityB = parts[1];

                    // Add bidirectional adjacency (A -> B and B -> A)
                    adjacencyList.computeIfAbsent(cityA, k -> new ArrayList<>()).add(cityB);
                    adjacencyList.computeIfAbsent(cityB, k -> new ArrayList<>()).add(cityA);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return adjacencyList;
    }

 
// Load city data (coordinates) from a CSV file
private static Map<String, City> loadCityData(String fileName) {
    Map<String, City> cities = new HashMap<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                String cityName = parts[0].trim();
                double latitude = Double.parseDouble(parts[1].trim());
                double longitude = Double.parseDouble(parts[2].trim());

                // Create a City object and add it to the map
                City city = new City(cityName, latitude, longitude);
                cities.put(cityName, city);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return cities;
}


    // Calculate the total distance for the cities visited on the route
    private static double calculateTotalDistance(List<String> route, Map<String, City> cities) {
        double totalDistance = 0.0;
        for (int i = 0; i < route.size() - 1; i++) {
            String currentCity = route.get(i);
            String nextCity = route.get(i + 1);

            City current = cities.get(currentCity);
            City next = cities.get(nextCity);

            if (current != null && next != null) {
                // Calculate distance between current and next city (e.g., using Haversine formula)
                double distance = calculateDistance(current, next);
                totalDistance += distance;
            }
        }
        return totalDistance;
    }

    // Calculate the distance between two cities using the Haversine formula
    private static double calculateDistance(City city1, City city2) {
        double lat1 = Math.toRadians(city1.getLatitude());
        double lon1 = Math.toRadians(city1.getLongitude());
        double lat2 = Math.toRadians(city2.getLatitude());
        double lon2 = Math.toRadians(city2.getLongitude());

        double earthRadius = 6371; // Radius of the Earth in kilometers
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }
}
