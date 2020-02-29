package graphs.time;

import models.Airport;

public class TimeEdge {
    public Airport origin, destination;

    public double time;

    public TimeEdge(Airport origin, Airport destination, double time) {
        this.origin = origin;
        this.destination = destination;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "origin='" + origin.getCityName() + '\'' +
                ", destination='" + destination.getCityName() + '\'' +
                ", time=" + time +
                '}';
    }
}
