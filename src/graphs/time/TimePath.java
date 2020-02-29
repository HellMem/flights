package graphs.time;

import java.util.ArrayList;
import java.util.List;

public class TimePath implements Comparable<TimePath> {

    public double totalTime;

    private List<TimeEdge> edges;

    public TimePath() {
        edges = new ArrayList<>();
        totalTime = 0;
    }

    public void add(TimeEdge edge) {
        edges.add(edge);

        totalTime += edge.time;

    }

    public int size() {
        return edges.size();
    }

    public void setEdges(List<TimeEdge> edges) {
        totalTime = 0;

        for (TimeEdge edge : edges) {
            totalTime += edge.time;
        }

        this.edges = edges;
    }


    public TimePath cloneUpTo(int upTo) {
        List<TimeEdge> edges = new ArrayList<>();

        if (upTo > edges.size()) upTo = this.edges.size();

        for (int i = 0; i < upTo; i++) {
            edges.add(this.edges.get(i));
        }


        TimePath newPath = new TimePath();
        newPath.setEdges(edges);
        return newPath;
    }

    public List<TimeEdge> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        return "Path{" +
                "totalTime=" + totalTime +
                ", \nedges=" + printEdges() +
                '}';
    }


    private String printEdges() {
        String stringEdges = "\n";

        for (TimeEdge edge : edges) {
            stringEdges += edge.toString() + "\n";
        }

        return stringEdges;
    }

    public void addPath(TimePath path) {
        edges.addAll(path.getEdges());

        totalTime += path.totalTime;
    }


    protected TimePath clone() {
        TimePath newDistancePath = new TimePath();

        newDistancePath.setEdges(edges);

        return newDistancePath;
    }

    @Override
    public int compareTo(TimePath timePath2) {
        double path2Time = timePath2.totalTime;
        if (totalTime == path2Time)
            return 0;
        if (totalTime > path2Time)
            return 1;
        return -1;
    }


    public boolean equals(TimePath path2) {
        if (path2 == null)
            return false;

        List<TimeEdge> edges2 = path2.getEdges();

        int numEdges1 = edges.size();
        int numEdges2 = edges2.size();

        if (numEdges1 != numEdges2) {
            return false;
        }

        for (int i = 0; i < numEdges1; i++) {
            TimeEdge edge1 = edges.get(i);
            TimeEdge edge2 = edges2.get(i);
            if (!edge1.origin.equals(edge2.origin))
                return false;
            if (!edge1.destination.equals(edge2.destination))
                return false;
        }

        return true;
    }
}
