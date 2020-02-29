package graphs.distance;

import java.util.ArrayList;
import java.util.List;

public class DistancePath implements Comparable<DistancePath> {
    public double totalDistance;

    private List<DistanceEdge> edges;

    public DistancePath() {
        edges = new ArrayList<>();
        totalDistance = 0;
    }

    public void add(DistanceEdge edge) {
        edges.add(edge);

        totalDistance += edge.distance;

    }

    public int size() {
        return edges.size();
    }

    public void setEdges(List<DistanceEdge> edges) {
        totalDistance = 0;

        for (DistanceEdge edge : edges) {
            totalDistance += edge.distance;
        }

        this.edges = edges;
    }


    public DistancePath cloneUpTo(int upTo) {
        List<DistanceEdge> edges = new ArrayList<>();

        if (upTo > edges.size()) upTo = this.edges.size();

        for (int i = 0; i < upTo; i++) {
            edges.add(this.edges.get(i));
        }


        DistancePath newPath = new DistancePath();
        newPath.setEdges(edges);
        return newPath;
    }

    public List<DistanceEdge> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        return "Path{" +
                "totalDistance=" + totalDistance +
                ", \nedges=" + printEdges() +
                '}';
    }


    private String printEdges() {
        String stringEdges = "\n";

        for (DistanceEdge edge : edges) {
            stringEdges += edge.toString() + "\n";
        }

        return stringEdges;
    }

    public void addPath(DistancePath path) {
        edges.addAll(path.getEdges());

        totalDistance += path.totalDistance;
    }


    protected DistancePath clone() {
        DistancePath newDistancePath = new DistancePath();

        newDistancePath.setEdges(edges);

        return newDistancePath;
    }

    @Override
    public int compareTo(DistancePath distancePath2) {
        double path2Distance = distancePath2.totalDistance;
        if (totalDistance == path2Distance)
            return 0;
        if (totalDistance > path2Distance)
            return 1;
        return -1;
    }


    public boolean equals(DistancePath path2) {
        if (path2 == null)
            return false;

        List<DistanceEdge> edges2 = path2.getEdges();

        int numEdges1 = edges.size();
        int numEdges2 = edges2.size();

        if (numEdges1 != numEdges2) {
            return false;
        }

        for (int i = 0; i < numEdges1; i++) {
            DistanceEdge edge1 = edges.get(i);
            DistanceEdge edge2 = edges2.get(i);
            if (!edge1.origin.equals(edge2.origin))
                return false;
            if (!edge1.destination.equals(edge2.destination))
                return false;
        }

        return true;
    }
}
