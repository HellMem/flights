package models;

import java.text.NumberFormat;

public class Price implements Comparable<Price> {
    private static final double INF = Double.MAX_VALUE;

    public double price;
    public String node;

    public Price() {
        price = INF;
        node = "";
    }

    public Price(String node, double price) {
        this.price = price;
        this.node = node;
    }


    public String getPriceFormat() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(price);
    }

    @Override
    public Price clone() {
        return new Price(node, price);
    }

    @Override
    public String toString() {
        return "models.Price{" +
                "price=" + getPriceFormat() +
                ", node='" + node + '\'' +
                '}';
    }

    @Override
    public int compareTo(Price price2) {
        double path2Price = price2.price;
        if (price == path2Price)
            return 0;
        if (price > path2Price)
            return -1;
        return 1;
    }
}
