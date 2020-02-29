package db;

import models.Airport;
import models.Route;
import models.Schedule;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QueryDB {

    private static final String TABLE_AIRPORTS = "Airports";
    private static final String COLUMN_NAME_AIRPORTID = "airportID";
    private static final String COLUMN_NAME_AIRPORTNAME = "airportName";
    private static final String COLUMN_NAME_CITYNAME = "cityName";
    private static final String COLUMN_NAME_COUNTRY = "country";
    private static final String COLUMN_NAME_AIRPORTSTRINGID = "airportsStringID";
    private static final String COLUMN_NAME_LAT = "lat";
    private static final String COLUMN_NAME_LON = "lon";
    private static final String COLUMN_NAME_WORLD_PART = "worldPart";

    //private final static String url = "jdbc:sqlite:C:/sqlite/db/flights";
    private final static String url = "jdbc:sqlite:flights";

    private static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    public static List<Airport> getAirports(String country) {
        List<Airport> airports = new ArrayList<>();


        String sql = "SELECT * FROM " + TABLE_AIRPORTS + " WHERE " + COLUMN_NAME_COUNTRY + " = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, country);
            ResultSet rs = pstmt.executeQuery();

            HashMap<String, Boolean> addedCities = new HashMap<>();

            while (rs.next()) {
                int airportID = rs.getInt((COLUMN_NAME_AIRPORTID));
                String airportName = rs.getString((COLUMN_NAME_AIRPORTNAME));
                String cityname = rs.getString((COLUMN_NAME_CITYNAME));
                String airportStringID = rs.getString((COLUMN_NAME_AIRPORTSTRINGID));
                double lat = rs.getDouble((COLUMN_NAME_LAT));
                double lon = rs.getDouble((COLUMN_NAME_LON));
                String worldPart = rs.getString((COLUMN_NAME_WORLD_PART));

                if (!addedCities.containsKey(airportStringID)) {

                    airports.add(new Airport(airportID, airportName, cityname, country, airportStringID, lat, lon, worldPart));

                    addedCities.put(airportStringID, true);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return airports;
    }

    public static Airport getAirport(String stringID) {
        List<Airport> airports = new ArrayList<>();


        String sql = "SELECT * FROM " + TABLE_AIRPORTS + " WHERE " + COLUMN_NAME_AIRPORTSTRINGID + " = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, stringID);
            ResultSet rs = pstmt.executeQuery();


            while (rs.next()) {
                int airportID = rs.getInt((COLUMN_NAME_AIRPORTID));
                String airportName = rs.getString((COLUMN_NAME_AIRPORTNAME));
                String cityname = rs.getString((COLUMN_NAME_CITYNAME));
                String airportStringID = rs.getString((COLUMN_NAME_AIRPORTSTRINGID));
                String country = rs.getString(COLUMN_NAME_COUNTRY);
                double lat = rs.getDouble((COLUMN_NAME_LAT));
                double lon = rs.getDouble((COLUMN_NAME_LON));
                String worldPart = rs.getString((COLUMN_NAME_WORLD_PART));


                airports.add(new Airport(airportID, airportName, cityname, country, airportStringID, lat, lon, worldPart));


            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return airports.get(0);
    }

    private static final String TABLE_ROUTES = "Routes";
    private static final String COLUMN_NAME_ORIGIN = "origin";
    private static final String COLUMN_NAME_DESTINATION = "destination";
    private static final String COLUMN_NAME_ID = "routeID";

    public static List<Route> getRoutes() {
        List<Route> routes = new ArrayList<>();

        String sql = "SELECT * FROM " + TABLE_ROUTES;
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                int idRoute = rs.getInt(COLUMN_NAME_ID);
                String origin = rs.getString((COLUMN_NAME_ORIGIN));
                String destination = rs.getString((COLUMN_NAME_DESTINATION));

                routes.add(new Route(idRoute, origin, destination));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return routes;
    }

    public static List<Route> getRoutesByCity(String city){
        List<Route> routes = new ArrayList<>();

        return routes;
    }


    private static final String TABLE_SCHEDULES = "models.Schedule";
    private static final String COLUMN_NAME_SCHEDULE_ID = "scheduleID";
    private static final String COLUMN_NAME_ROUTEID = "routeID";
    private static final String COLUMN_NAME_DEPARTURE = "departure";
    private static final String COLUMN_NAME_ARRIVAL = "arrival";

    public static List<Schedule> getSchedules() {
        List<Schedule> schedules = new ArrayList<>();

        String sql = "SELECT * FROM " + TABLE_SCHEDULES;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                int scheduleID = rs.getInt(COLUMN_NAME_SCHEDULE_ID);
                int idRoute = rs.getInt(COLUMN_NAME_ROUTEID);
                double departure = rs.getDouble((COLUMN_NAME_DEPARTURE));
                double arrival = rs.getDouble((COLUMN_NAME_ARRIVAL));

                schedules.add(new Schedule(scheduleID, idRoute, departure, arrival));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return schedules;
    }

    public static List<Schedule> getOriginDestinationSchedules(int routeID) {
        List<Schedule> schedules = new ArrayList<>();


        String sql = "SELECT * FROM " + TABLE_SCHEDULES + " WHERE " + COLUMN_NAME_ROUTEID + " = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, routeID);
            ResultSet rs = pstmt.executeQuery();


            while (rs.next()) {
                int scheduleID = rs.getInt(COLUMN_NAME_SCHEDULE_ID);
                int idRoute = rs.getInt(COLUMN_NAME_ROUTEID);
                double departure = rs.getDouble((COLUMN_NAME_DEPARTURE));
                double arrival = rs.getDouble((COLUMN_NAME_ARRIVAL));

                schedules.add(new Schedule(scheduleID, idRoute, departure, arrival));


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return schedules;
    }
}
