package models;

public class Time implements Comparable<Time> {
    private static final double INF = Double.MAX_VALUE;

    public double time;
    public String node;

    public Time() {
        time = INF;
        node = "";
    }

    public Time(String node, double time) {
        this.time = time;
        this.node = node;
    }

    @Override
    public Time clone() {
        return new Time(node, time);
    }

    @Override
    public String toString() {
        return "models.Airport.Distance{" +
                "distance=" + time +
                ", node='" + node + '\'' +
                '}';
    }

    @Override
    public int compareTo(Time time2) {
        double path2Time = time2.time;
        if (time == path2Time)
            return 0;
        if (time > path2Time)
            return 1;
        return -1;
    }
}
