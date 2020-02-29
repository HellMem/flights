package models;

public class Airport {
    private int airportID;
    private String airportName;
    private String cityName;
    private String country;
    private String airPortStringID;
    private double lat, lon;
    private String worldPart;

    public Airport(int airportID, String airportName, String cityName, String country, String airPortStringID, double lat, double lon, String worldPart) {
        this.airportID = airportID;
        this.airportName = airportName;
        this.cityName = cityName;
        this.country = country;
        this.airPortStringID = airPortStringID;
        this.lat = lat;
        this.lon = lon;
        this.worldPart = worldPart;
    }

    public int getAirportID() {
        return airportID;
    }

    public String getAirportName() {
        return airportName;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCountry() {
        return country;
    }

    public String getairPortStringID() {
        return airPortStringID;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getWorldPart() {
        return worldPart;
    }


    @Override
    public String toString() {
        return "models.Airport{" +
                "airportID=" + airportID +
                ", airportName='" + airportName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", country='" + country + '\'' +
                ", airPortStringID='" + airPortStringID + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", worldPart='" + worldPart + '\'' +
                '}';
    }




    public boolean equals(Airport airport) {
        return (this.airPortStringID.equals(airport.airPortStringID));
    }

    public static class Distance implements Comparable<Distance> {
        private static final double INF = Double.MAX_VALUE;

        public double distance;
        public String node;

        public Distance() {
            distance = INF;
            node = "";
        }

        public Distance(String node, double distance) {
            this.distance = distance;
            this.node = node;
        }

        @Override
        public Distance clone() {
            return new Distance(node, distance);
        }

        @Override
        public String toString() {
            return "models.Airport.Distance{" +
                    "distance=" + distance +
                    ", node='" + node + '\'' +
                    '}';
        }

        @Override
        public int compareTo(Distance distance2) {
            double path2Distance = distance2.distance;
            if (distance == path2Distance)
                return 0;
            if (distance > path2Distance)
                return 1;
            return -1;
        }
    }
}
