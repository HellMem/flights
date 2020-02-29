package graphs.price;

import models.Airport;

public class PriceEdge {

    public Airport origin, destination;

    public double price;

    public PriceEdge(Airport origin, Airport destination, double price) {
        this.origin = origin;
        this.destination = destination;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "origin='" + origin.getCityName() + '\'' +
                ", destination='" + destination.getCityName() + '\'' +
                ", price=" + price +
                '}';
    }
}
