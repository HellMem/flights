package graphs.distance;

import models.Airport;

public  class DistanceEdge {


    public Airport origin, destination;

    public double distance;

    public DistanceEdge(Airport origin, Airport destination, double distance) {
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "origin='" + origin.getCityName() + '\'' +
                ", destination='" + destination.getCityName() + '\'' +
                ", distance=" + distance +
                '}';
    }
}