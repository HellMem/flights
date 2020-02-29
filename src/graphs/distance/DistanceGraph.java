package graphs.distance;

import graphs.distance.DistanceEdge;
import models.Airport;

import java.util.*;

public class DistanceGraph {


    private static final double INF = Double.MAX_VALUE;
    private HashMap<String, HashMap<String, DistanceEdge>> adjDistanceList;


    public DistanceGraph() {
        adjDistanceList = new HashMap<>();
    }


    public void addEdge(Airport airportOrigin, Airport airportDest) {

        if (!adjDistanceList.containsKey(airportOrigin.getairPortStringID()))
            adjDistanceList.put(airportOrigin.getairPortStringID(), new HashMap<>());

        if (!adjDistanceList.containsKey(airportDest.getairPortStringID()))
            adjDistanceList.put(airportDest.getairPortStringID(), new HashMap<>());


        double distance = calculateDistance(airportOrigin, airportDest);
        double time = calculateTime(distance);
        double price = calculatePrice();

        DistanceEdge edge = new DistanceEdge(airportOrigin, airportDest, distance);

        HashMap<String, DistanceEdge> mapDestinations = adjDistanceList.get(airportOrigin.getairPortStringID());

        mapDestinations.put(airportDest.getairPortStringID(), edge);
    }

    public void removeEdge(String origin, String destination) {
        adjDistanceList.get(origin).put(destination, null);
    }

    private double calculateDistance(Airport airportOrigin, Airport airportDest) {
        double earthRadius = 6371.0;
        double lat1 = airportOrigin.getLat();
        double lon1 = airportOrigin.getLon();

        double lat2 = airportDest.getLat();
        double lon2 = airportDest.getLon();

        double dlon = deg2rad(lon2 - lon1);
        double dlat = deg2rad(lat2 - lat1);

        double a = (Math.sin(dlat / 2) * Math.sin(dlat / 2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * (Math.sin(dlon / 2) * Math.sin(dlon / 2));
        double angle = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = angle * earthRadius;

        return distance;
    }


    private double deg2rad(double degree) {
        return degree * (Math.PI / 180);
    }

    private double calculatePrice() {
        return Math.random() * 10_0000 + 500;
    }

    private double calculateTime(double distance) {
        return Math.random() * (distance / 800.0 + distance / 300.0);
    }


    public List<DistancePath> getBestFivePaths(String origin, String destination) {
        List<DistancePath> bestPaths = new ArrayList<>();
        PriorityQueue<DistancePath> candidates = new PriorityQueue<>();

        DistancePath bestPath = shortestDistance(origin, destination);
        bestPaths.add(bestPath);

        for (int k = 1; k < 5; k++) {
            DistancePath previousPath = bestPaths.get(k - 1);

            for (int i = previousPath.size() - 1; i >= 0; i--) {
                List<DistanceEdge> removedEdges = new ArrayList<>();

                String currentNode = previousPath.getEdges().get(i).origin.getairPortStringID();

                DistancePath rootPath = previousPath.cloneUpTo(i);

                for (DistancePath currentPath : bestPaths) {
                    DistancePath stub = currentPath.cloneUpTo(i);

                    if (rootPath.equals(stub)) {
                        DistanceEdge edgeToRemove = currentPath.getEdges().get(i);
                        removeEdge(edgeToRemove.origin.getairPortStringID(), edgeToRemove.destination.getairPortStringID());
                        removedEdges.add(edgeToRemove);
                    }
                }

                for (DistanceEdge rootPathEdge : rootPath.getEdges()) {
                    String nodeToRemove = rootPathEdge.origin.getairPortStringID();

                    if (!nodeToRemove.equals(currentNode)) {
                        removedEdges.addAll(removeNode(nodeToRemove));
                    }
                }

                DistancePath currentPath = shortestDistance(currentNode, destination);

                if (currentPath.size() > 0) {
                    DistancePath totalPath = rootPath.clone();
                    totalPath.addPath(currentPath);

                    if (!candidates.contains(totalPath))
                        candidates.add(totalPath);
                }

                for (DistanceEdge edge : removedEdges) {
                    if (edge != null)
                        addEdge(edge.origin, edge.destination);
                }

            }


            boolean isNewPath;
            do {
                bestPath = candidates.poll();
                isNewPath = true;

                if (bestPath != null) {
                    for (DistancePath path : bestPaths) {
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

    public List<DistanceEdge> removeNode(String node) {
        List<DistanceEdge> edges = new ArrayList<>();
        if (adjDistanceList.containsKey(node)) {
            HashMap<String, DistanceEdge> edgesMap = adjDistanceList.remove(node);

            for (Map.Entry<String, DistanceEdge> entry : edgesMap.entrySet()) {
                edges.add(entry.getValue());
            }
        }

        edges.addAll(removeEdgesToNode(node));

        return edges;
    }

    public List<DistanceEdge> removeEdgesToNode(String node) {
        List<DistanceEdge> edges = new ArrayList<>();

        for (String key : adjDistanceList.keySet()) {
            if (adjDistanceList.get(key).containsKey(node)) {
                DistanceEdge edge = adjDistanceList.get(key).remove(node);

                edges.add(edge);
            }
        }

        return edges;
    }


    public DistancePath shortestDistance(String origin, String destination) {
        ArrayDeque<String> pendingNodes = new ArrayDeque<>();
        ArrayDeque<Double> pendingDistances = new ArrayDeque<>();

        HashMap<String, Boolean> visited = new HashMap<>();
        HashMap<String, Double> distances = new HashMap<>();
        HashMap<String, Airport.Distance> parents = new HashMap<>();

        for (String key : adjDistanceList.keySet()) {
            distances.put(key, INF);
            visited.put(key, false);
            parents.put(key, new Airport.Distance());
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
                DistanceEdge currentEdge = adjDistanceList.get(node).get(key);

                if (currentEdge == null) continue;


                if (currentEdge.distance < INF) {
                    pendingNodes.push(key);
                    pendingDistances.push(distances.get(node) + currentEdge.distance);
                    if (distances.get(node) + currentEdge.distance < parents.get(key).distance) {
                        parents.get(key).distance = distances.get(node) + currentEdge.distance;
                        parents.get(key).node = node;
                    }
                }
            }
        }


        return getPath(origin, destination, parents);
    }


    private DistancePath getPath(String origin, String destination, HashMap<String, Airport.Distance> parents) {
        DistancePath path = new DistancePath();
        ArrayDeque<DistanceEdge> edgesQueue = new ArrayDeque<>();

        String currentNode = destination;

        Airport.Distance current = parents.get(currentNode);

        while (current != null) {
            HashMap<String, DistanceEdge> currentMap = adjDistanceList.get(current.node);

            if (currentMap == null) break;

            DistanceEdge edge = currentMap.get(currentNode);

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
