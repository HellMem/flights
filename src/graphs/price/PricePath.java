package graphs.price;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class PricePath implements Comparable<PricePath> {
    public double totalPrice;

    private List<PriceEdge> edges;

    public PricePath() {
        edges = new ArrayList<>();
        totalPrice = 0;
    }

    public void add(PriceEdge edge) {
        edges.add(edge);

        totalPrice += edge.price;

    }

    public int size() {
        return edges.size();
    }

    public void setEdges(List<PriceEdge> edges) {
        totalPrice = 0;

        for (PriceEdge edge : edges) {
            totalPrice += edge.price;
        }

        this.edges = edges;
    }


    public PricePath cloneUpTo(int upTo) {
        List<PriceEdge> edges = new ArrayList<>();

        if (upTo > edges.size()) upTo = this.edges.size();

        for (int i = 0; i < upTo; i++) {
            edges.add(this.edges.get(i));
        }


        PricePath newPath = new PricePath();
        newPath.setEdges(edges);
        return newPath;
    }

    public List<PriceEdge> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        return "Path{" +
                "totalPrice=" + getPriceFormat() +
                ", \nedges=" + printEdges() +
                '}';
    }

    public String getPriceFormat() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(totalPrice);
    }


    private String printEdges() {
        String stringEdges = "\n";

        for (PriceEdge edge : edges) {
            stringEdges += edge.toString() + "\n";
        }

        return stringEdges;
    }

    public void addPath(PricePath path) {
        edges.addAll(path.getEdges());

        totalPrice += path.totalPrice;
    }


    protected PricePath clone() {
        PricePath newPricePath = new PricePath();

        newPricePath.setEdges(edges);

        return newPricePath;
    }

    @Override
    public int compareTo(PricePath pricePath2) {
        double path2Price = pricePath2.totalPrice;
        if (totalPrice == path2Price)
            return 0;
        if (totalPrice > path2Price)
            return -1;
        return 1;
    }


    public boolean equals(PricePath path2) {
        if (path2 == null)
            return false;

        List<PriceEdge> edges2 = path2.getEdges();

        int numEdges1 = edges.size();
        int numEdges2 = edges2.size();

        if (numEdges1 != numEdges2) {
            return false;
        }

        for (int i = 0; i < numEdges1; i++) {
            PriceEdge edge1 = edges.get(i);
            PriceEdge edge2 = edges2.get(i);
            if (!edge1.origin.equals(edge2.origin))
                return false;
            if (!edge1.destination.equals(edge2.destination))
                return false;
        }

        return true;
    }
}
