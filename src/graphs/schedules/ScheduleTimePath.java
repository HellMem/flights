package graphs.schedules;

import java.util.ArrayList;
import java.util.List;

public class ScheduleTimePath implements Comparable<ScheduleTimePath> {

    public double totalTime;

    private List<SingleScheduleTimeEdge> edges;

    public ScheduleTimePath() {
        edges = new ArrayList<>();
        totalTime = 0;
    }

    public void add(SingleScheduleTimeEdge edge) {
        edges.add(edge);

        totalTime += edge.schedule.getArrival() - edge.schedule.getDeparture();
    }

    public int size() {
        return edges.size();
    }

    public void setEdges(List<SingleScheduleTimeEdge> edges) {
        totalTime = 0;

        for (SingleScheduleTimeEdge edge : edges) {
            add(edge);
        }

    }


    public ScheduleTimePath cloneUpTo(int upTo) {
        List<SingleScheduleTimeEdge> edges = new ArrayList<>();

        if (upTo > edges.size()) upTo = this.edges.size();

        for (int i = 0; i < upTo; i++) {
            edges.add(this.edges.get(i));
        }


        ScheduleTimePath newPath = new ScheduleTimePath();
        newPath.setEdges(edges);
        return newPath;
    }

    public List<SingleScheduleTimeEdge> getEdges() {
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

        for (SingleScheduleTimeEdge edge : edges) {
            stringEdges += edge.toString() + "\n";
        }

        return stringEdges;
    }

    public void addPath(ScheduleTimePath path) {
        edges.addAll(path.getEdges());

        totalTime += path.totalTime;
    }


    protected ScheduleTimePath clone() {
        ScheduleTimePath newDistancePath = new ScheduleTimePath();

        newDistancePath.setEdges(edges);

        return newDistancePath;
    }

    @Override
    public int compareTo(ScheduleTimePath timePath2) {
        double path2Time = timePath2.totalTime;
        if (totalTime == path2Time)
            return 0;
        if (totalTime > path2Time)
            return 1;
        return -1;
    }


    public boolean equals(ScheduleTimePath path2) {
        if (path2 == null)
            return false;

        List<SingleScheduleTimeEdge> edges2 = path2.getEdges();

        int numEdges1 = edges.size();
        int numEdges2 = edges2.size();

        if (numEdges1 != numEdges2) {
            return false;
        }

        for (int i = 0; i < numEdges1; i++) {
            SingleScheduleTimeEdge edge1 = edges.get(i);
            SingleScheduleTimeEdge edge2 = edges2.get(i);
            if (!edge1.origin.equals(edge2.origin))
                return false;
            if (!edge1.destination.equals(edge2.destination))
                return false;
        }

        return true;
    }
}
