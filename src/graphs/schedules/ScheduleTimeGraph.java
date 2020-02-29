package graphs.schedules;

import graphs.schedules.ScheduleTimeEdge;
import models.Airport;
import models.Schedule;

import java.util.*;

public class ScheduleTimeGraph {

    private static final double INF = Double.MAX_VALUE;

    private HashMap<String, HashMap<String, ScheduleTimeEdge>> adjList;


    public ScheduleTimeGraph() {
        adjList = new HashMap<>();
    }

    public void addEdge(Airport origin, Airport destination, List<Schedule> schedules) {
        if (!adjList.containsKey(origin.getairPortStringID()))
            adjList.put(origin.getairPortStringID(), new HashMap<>());

        if (!adjList.containsKey(destination.getairPortStringID()))
            adjList.put(destination.getairPortStringID(), new HashMap<>());

        ScheduleTimeEdge edge = new ScheduleTimeEdge(schedules, origin, destination);

        HashMap<String, ScheduleTimeEdge> mapDestinations = adjList.get(origin.getairPortStringID());

        mapDestinations.put(destination.getairPortStringID(), edge);
    }


    public static class Path {
        String node;
        double currentHour = INF;
        double timePast = INF;

        public Path(String node, double currentHour, double timePast) {
            this.node = node;
            this.currentHour = currentHour;
            this.timePast = timePast;
        }

        public Path() {
        }

        @Override
        public String toString() {
            return "Path{" +
                    "node='" + node + '\'' +
                    ", Hora Salida=" + currentHour +
                    ", Hora Llegada=" + (timePast) +
                    '}';
        }

        @Override
        protected Path clone() {
            return new Path(node, currentHour, timePast);
        }
    }


    public ScheduleTimePath shortestTime(String origin, String destination, double departureHour) {
        ArrayDeque<String> pendingNodes = new ArrayDeque<>();
        ArrayDeque<Double> pendingPastTime = new ArrayDeque<>();
        ArrayDeque<Double> pendingHours = new ArrayDeque<>();

        HashMap<String, Boolean> visited = new HashMap<>();
        HashMap<String, Double> timePast = new HashMap<>();
        HashMap<String, Double> hours = new HashMap<>();
        HashMap<String, Path> parents = new HashMap<>();
        HashMap<String, SingleScheduleTimeEdge> parentsPath = new HashMap<>();


        for (String key : adjList.keySet()) {
            visited.put(key, false);
            timePast.put(key, INF);
            hours.put(key, INF);
            parents.put(key, new Path());
            parentsPath.put(key, new SingleScheduleTimeEdge());
        }


        pendingNodes.push(origin);
        pendingHours.push(departureHour);
        pendingPastTime.push(0.0);

        while (!pendingNodes.isEmpty()) {
            String node = pendingNodes.pop();
            double newCurrentHour = pendingHours.pop();
            double newPastTime = pendingPastTime.pop();


            if (visited.get(node) && newCurrentHour > hours.get(node) && newPastTime > timePast.get(node))
                continue;

            hours.put(node, newCurrentHour);
            timePast.put(node, newPastTime);


            if (!visited.get(node))
                visited.put(node, true);

            for (String key : adjList.keySet()) {
                ScheduleTimeEdge currentEdge = adjList.get(node).get(key);

                if (currentEdge == null) continue;

                double bestHour = INF;
                double currentTimePast = 0;

                SingleScheduleTimeEdge bestTimeEdge = new SingleScheduleTimeEdge();
                bestTimeEdge.origin = currentEdge.origin;
                bestTimeEdge.destination = currentEdge.destination;


                for (Schedule schedule : currentEdge.getSchedules()) {
                    double currentHour = schedule.getDeparture();
                    if (currentHour >= newCurrentHour && currentHour < bestHour) {
                        bestHour = currentHour;
                        currentTimePast = schedule.getArrival();

                        bestTimeEdge.schedule = schedule;
                    }
                }

                if (bestHour < INF) {
                    pendingNodes.push(key);

                    pendingHours.push(currentTimePast);
                    pendingPastTime.push(currentTimePast);

                    if (bestHour < parents.get(key).currentHour && bestHour + currentTimePast < parents.get(key).timePast) {
                        parentsPath.get(key).schedule = bestTimeEdge.schedule;
                        parentsPath.get(key).origin = bestTimeEdge.origin;
                        parentsPath.get(key).destination = bestTimeEdge.destination;


                        parents.get(key).currentHour = bestHour;
                        parents.get(key).timePast = currentTimePast;

                        parents.get(key).node = node;
                    }
                }
            }
        }





        return getPath(origin, destination, parentsPath);
    }

    public List<ScheduleTimePath> getBestFivePaths(String origin, String destination, double departureHour) {
        List<ScheduleTimePath> bestPaths = new ArrayList<>();
        PriorityQueue<ScheduleTimePath> candidates = new PriorityQueue<>();

        ScheduleTimePath bestPath = shortestTime(origin, destination, departureHour);
        bestPaths.add(bestPath);

        for (int k = 1; k < 5; k++) {
            ScheduleTimePath previousPath = bestPaths.get(k - 1);

            for (int i = previousPath.size() - 1; i >= 0; i--) {
                List<SingleScheduleTimeEdge> removedEdges = new ArrayList<>();

                String currentNode = previousPath.getEdges().get(i).origin.getairPortStringID();

                ScheduleTimePath rootPath = previousPath.cloneUpTo(i);

                for (ScheduleTimePath currentPath : bestPaths) {
                    ScheduleTimePath stub = currentPath.cloneUpTo(i);

                    if (rootPath.equals(stub)) {
                        SingleScheduleTimeEdge edgeToRemove = currentPath.getEdges().get(i);
                        removeEdge(edgeToRemove.origin.getairPortStringID(), edgeToRemove.destination.getairPortStringID(), edgeToRemove.schedule.getDeparture(), edgeToRemove.schedule.getArrival());
                        removedEdges.add(edgeToRemove);
                    }
                }

                for (SingleScheduleTimeEdge rootPathEdge : rootPath.getEdges()) {
                    String nodeToRemove = rootPathEdge.origin.getairPortStringID();

                    if (!nodeToRemove.equals(currentNode)) {
                        removedEdges.addAll(removeNode(nodeToRemove));
                    }
                }

                ScheduleTimePath currentPath = shortestTime(currentNode, destination, departureHour);

                if (currentPath.size() > 0) {
                    ScheduleTimePath totalPath = rootPath.clone();
                    totalPath.addPath(currentPath);

                    if (!candidates.contains(totalPath))
                        candidates.add(totalPath);
                }

                for (SingleScheduleTimeEdge edge : removedEdges) {
                    if (edge != null)
                        addEdge(edge.origin, edge.destination, edge.schedule.getDeparture(), edge.schedule.getArrival());
                }

            }


            boolean isNewPath;
            do {
                bestPath = candidates.poll();
                isNewPath = true;

                if (bestPath != null) {
                    for (ScheduleTimePath path : bestPaths) {
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


    public List<SingleScheduleTimeEdge> removeNode(String node) {
        List<SingleScheduleTimeEdge> edges = new ArrayList<>();
        if (adjList.containsKey(node)) {
            HashMap<String, ScheduleTimeEdge> edgesMap = adjList.remove(node);

            for (Map.Entry<String, ScheduleTimeEdge> entry : edgesMap.entrySet()) {
                ScheduleTimeEdge scheduleTimeEdge = entry.getValue();


                for (Schedule schedule : scheduleTimeEdge.getSchedules()) {
                    SingleScheduleTimeEdge singleScheduleTimeEdge = new SingleScheduleTimeEdge();
                    singleScheduleTimeEdge.schedule = schedule;
                    singleScheduleTimeEdge.origin = scheduleTimeEdge.origin;
                    singleScheduleTimeEdge.destination = scheduleTimeEdge.destination;

                    edges.add(singleScheduleTimeEdge);
                }


            }
        }

        edges.addAll(removeEdgesToNode(node));

        return edges;
    }


    public List<SingleScheduleTimeEdge> removeEdgesToNode(String node) {
        List<SingleScheduleTimeEdge> edges = new ArrayList<>();

        for (String key : adjList.keySet()) {
            if (adjList.get(key).containsKey(node)) {
                ScheduleTimeEdge scheduleTimeEdge = adjList.get(key).remove(node);

                for (Schedule schedule : scheduleTimeEdge.getSchedules()) {
                    SingleScheduleTimeEdge singleScheduleTimeEdge = new SingleScheduleTimeEdge();

                    singleScheduleTimeEdge.origin = scheduleTimeEdge.origin;
                    singleScheduleTimeEdge.destination = scheduleTimeEdge.destination;
                    singleScheduleTimeEdge.schedule = schedule;

                    edges.add(singleScheduleTimeEdge);
                }


            }
        }

        return edges;
    }

    private void addEdge(Airport origin, Airport destination, double departure, double arrival) {
        if (!adjList.containsKey(origin.getairPortStringID()))
            adjList.put(origin.getairPortStringID(), new HashMap<>());

        if (!adjList.get(origin.getairPortStringID()).containsKey(destination.getairPortStringID())) {
            adjList.get(origin.getairPortStringID()).put(destination.getairPortStringID(), new ScheduleTimeEdge(new ArrayList<>(), origin, destination));
        }

        List<Schedule> schedules = adjList.get(origin.getairPortStringID()).get(destination.getairPortStringID()).getSchedules();

        schedules.add(new Schedule(0, 0, departure, arrival));
    }

    private void removeEdge(String origin, String destination, double departure, double arrival) {
        List<Schedule> schedules = adjList.get(origin).get(destination).getSchedules();

        for (int i = 0; i < schedules.size(); i++) {
            Schedule currentSchedule = schedules.get(i);

            if (currentSchedule.getArrival() == arrival && currentSchedule.getDeparture() == departure) {
                schedules.remove(i);
                break;
            }
        }

    }


    private ScheduleTimePath getPath(String origin, String destination, HashMap<String, SingleScheduleTimeEdge> parents) {
        ScheduleTimePath path = new ScheduleTimePath();
        ArrayDeque<SingleScheduleTimeEdge> edgesQueue = new ArrayDeque<>();

        String currentNode = destination;

        SingleScheduleTimeEdge current = parents.get(currentNode);

        while (current != null) {


            edgesQueue.push(current);

            if (current.origin == null) break;

            currentNode = current.origin.getairPortStringID();

            if (currentNode.equals(origin)) break;

            current = parents.get(currentNode);
        }


        while (!edgesQueue.isEmpty()) {
            if (edgesQueue.peek().origin == null) break;
            path.add(edgesQueue.pop());
        }


        return path;
    }
}
