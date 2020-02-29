package graphs.price;

import models.Airport;
import models.Price;

import java.util.*;

public class PriceGraph {

    private static final double INF = Double.MAX_VALUE;
    private HashMap<String, HashMap<String, PriceEdge>> adjDistanceList;


    public PriceGraph() {
        adjDistanceList = new HashMap<>();
    }


    public void addEdge(Airport airportOrigin, Airport airportDest) {

        if (!adjDistanceList.containsKey(airportOrigin.getairPortStringID()))
            adjDistanceList.put(airportOrigin.getairPortStringID(), new HashMap<>());

        if (!adjDistanceList.containsKey(airportDest.getairPortStringID()))
            adjDistanceList.put(airportDest.getairPortStringID(), new HashMap<>());


        double price = calculatePrice();

        PriceEdge edge = new PriceEdge(airportOrigin, airportDest, price);

        HashMap<String, PriceEdge> mapDestinations = adjDistanceList.get(airportOrigin.getairPortStringID());

        mapDestinations.put(airportDest.getairPortStringID(), edge);
    }

    public void removeEdge(String origin, String destination) {
        adjDistanceList.get(origin).put(destination, null);
    }


    private double calculatePrice() {
        return Math.random() * 10_0000 + 500;
    }



    //https://www.programcreek.com/java-api-examples/index.php?project_name=bsmock/k-shortest-paths#
    //https://en.wikipedia.org/wiki/K_shortest_path_routing

    public List<PricePath> getBestFivePaths(String origin, String destination) {
        List<PricePath> bestPaths = new ArrayList<>();
        PriorityQueue<PricePath> candidates = new PriorityQueue<>();

        PricePath bestPath = bestPrice(origin, destination);
        bestPaths.add(bestPath);

        for (int k = 1; k < 5; k++) {
            PricePath previousPath = bestPaths.get(k - 1);

            for (int i = previousPath.size() - 1; i >= 0; i--) {
                List<PriceEdge> removedEdges = new ArrayList<>();

                String currentNode = previousPath.getEdges().get(i).origin.getairPortStringID();

                PricePath rootPath = previousPath.cloneUpTo(i);

                for (PricePath currentPath : bestPaths) {
                    PricePath stub = currentPath.cloneUpTo(i);

                    if (rootPath.equals(stub)) {
                        PriceEdge edgeToRemove = currentPath.getEdges().get(i);
                        removeEdge(edgeToRemove.origin.getairPortStringID(), edgeToRemove.destination.getairPortStringID());
                        removedEdges.add(edgeToRemove);
                    }
                }

                for (PriceEdge rootPathEdge : rootPath.getEdges()) {
                    String nodeToRemove = rootPathEdge.origin.getairPortStringID();

                    if (!nodeToRemove.equals(currentNode)) {
                        removedEdges.addAll(removeNode(nodeToRemove));
                    }
                }

                PricePath currentPath = bestPrice(currentNode, destination);

                if (currentPath.size() > 0) {
                    PricePath totalPath = rootPath.clone();
                    totalPath.addPath(currentPath);

                    if (!candidates.contains(totalPath))
                        candidates.add(totalPath);
                }

                for (PriceEdge edge : removedEdges) {
                    if (edge != null)
                        addEdge(edge.origin, edge.destination);
                }

            }


            boolean isNewPath;
            do {
                bestPath = candidates.poll();
                isNewPath = true;

                if (bestPath != null) {
                    for (PricePath path : bestPaths) {
                        if (path.equals(bestPath)) {
                            isNewPath = false;
                            break;
                        }
                    }
                }

            } while (!isNewPath);

            if (bestPath == null || bestPath.size() == 0) {
                break;
            }

            bestPaths.add(bestPath);


        }

        return bestPaths;
    }

    public List<PriceEdge> removeNode(String node) {
        List<PriceEdge> edges = new ArrayList<>();
        if (adjDistanceList.containsKey(node)) {
            HashMap<String, PriceEdge> edgesMap = adjDistanceList.remove(node);

            for (Map.Entry<String, PriceEdge> entry : edgesMap.entrySet()) {
                edges.add(entry.getValue());
            }
        }

        edges.addAll(removeEdgesToNode(node));

        return edges;
    }

    public List<PriceEdge> removeEdgesToNode(String node) {
        List<PriceEdge> edges = new ArrayList<>();

        for (String key : adjDistanceList.keySet()) {
            if (adjDistanceList.get(key).containsKey(node)) {
                PriceEdge edge = adjDistanceList.get(key).remove(node);

                edges.add(edge);
            }
        }

        return edges;
    }


    public PricePath bestPrice(String origin, String destination) {
        ArrayDeque<String> pendingNodes = new ArrayDeque<>();
        ArrayDeque<Double> pendingDistances = new ArrayDeque<>();

        HashMap<String, Boolean> visited = new HashMap<>();
        HashMap<String, Double> distances = new HashMap<>();
        HashMap<String, Price> parents = new HashMap<>();

        for (String key : adjDistanceList.keySet()) {
            distances.put(key, INF);
            visited.put(key, false);
            parents.put(key, new Price());
        }


        pendingNodes.push(origin);
        pendingDistances.push(0.0);

        while (!pendingNodes.isEmpty()) {
            String node = pendingNodes.pop();
            double newD = pendingDistances.pop();

            if (visited.get(node) && newD > distances.get(node))
                continue;

            distances.put(node, newD);


            if (!visited.get(node))
                visited.put(node, true);

            for (String key : adjDistanceList.keySet()) {
                PriceEdge currentEdge = adjDistanceList.get(node).get(key);

                if (currentEdge == null) continue;


                if (currentEdge.price < INF) {
                    pendingNodes.push(key);
                    pendingDistances.push(distances.get(node) + currentEdge.price);
                    if (distances.get(node) + currentEdge.price < parents.get(key).price) {
                        parents.get(key).price = distances.get(node) + currentEdge.price;
                        parents.get(key).node = node;
                    }
                }
            }
        }


        return getPath(origin, destination, parents);
    }


    private PricePath getPath(String origin, String destination, HashMap<String, Price> parents) {
        PricePath path = new PricePath();
        ArrayDeque<PriceEdge> edgesQueue = new ArrayDeque<>();

        String currentNode = destination;

        Price current = parents.get(currentNode);

        while (current != null) {
            HashMap<String, PriceEdge> currentMap = adjDistanceList.get(current.node);

            if (currentMap == null) break;

            PriceEdge edge = currentMap.get(currentNode);

            if (edge == null) break;

            edgesQueue.push(edge);
            currentNode = current.node;

            if (currentNode.equals(origin)) break;

            current = parents.get(current.node);
        }


        while (!edgesQueue.isEmpty())
            path.add(edgesQueue.pop());


        return path;
    }
}
