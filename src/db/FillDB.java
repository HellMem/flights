package db;

import models.Airport;
import models.Route;
import models.Schedule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FillDB {


    private final static String url = "jdbc:sqlite:C:/sqlite/db/flights";

    private static void createDatabase() {


        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static final String TABLE_AIRPORTS = "Airports";
    private static final String COLUMN_NAME_AIRPORTID = "airportID";
    private static final String COLUMN_NAME_AIRPORTNAME = "airportName";
    private static final String COLUMN_NAME_CITYNAME = "cityName";
    private static final String COLUMN_NAME_COUNTRY = "country";
    private static final String COLUMN_NAME_AIRPORTSTRINGID = "airportsStringID";
    private static final String COLUMN_NAME_LAT = "lat";
    private static final String COLUMN_NAME_LON = "lon";
    private static final String COLUMN_NAME_WORLD_PART = "worldPart";

    private static final String TABLE_ROUTES = "Routes";
    private static final String COLUMN_NAME_ID = "routeID";
    private static final String COLUMN_NAME_ORIGIN = "origin";
    private static final String COLUMN_NAME_DESTINATION = "destination";


    private static final String TABLE_SCHEDULES = "models.Schedule";
    private static final String COLUMN_NAME_SCHEDULE_ID = "scheduleID";
    private static final String COLUMN_NAME_ROUTEID = "routeID";
    private static final String COLUMN_NAME_DEPARTURE = "departure";
    private static final String COLUMN_NAME_ARRIVAL = "arrival";

    private static void createTables() {
        String createTableAirports = "CREATE TABLE " + TABLE_AIRPORTS + " ("
                + COLUMN_NAME_AIRPORTID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME_AIRPORTNAME + " TEXT,"
                + COLUMN_NAME_CITYNAME + " TEXT,"
                + COLUMN_NAME_COUNTRY + " TEXT,"
                + COLUMN_NAME_AIRPORTSTRINGID + " TEXT,"
                + COLUMN_NAME_LAT + " REAL,"
                + COLUMN_NAME_LON + " REAL,"
                + COLUMN_NAME_WORLD_PART + " TEXT"
                + ");";


        String createTableRoutes = "CREATE TABLE " + TABLE_ROUTES + " ("
                + COLUMN_NAME_ID + " TEXT,"
                + COLUMN_NAME_ORIGIN + " TEXT,"
                + COLUMN_NAME_DESTINATION + " TEXT"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(createTableAirports);
            stmt.execute(createTableRoutes);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private static void createTablesSchedules() {


        String createTableSchedules = "CREATE TABLE " + TABLE_SCHEDULES + " ("
                + COLUMN_NAME_SCHEDULE_ID + " INTEGER,"
                + COLUMN_NAME_ROUTEID + " INTEGER,"
                + COLUMN_NAME_DEPARTURE + " TEXT,"
                + COLUMN_NAME_ARRIVAL + " TEXT"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table

            stmt.execute(createTableSchedules);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private static void addAirport(Airport airport) {
        String sql = "INSERT INTO " + TABLE_AIRPORTS + " (" +
                COLUMN_NAME_AIRPORTID + ", " +
                COLUMN_NAME_AIRPORTNAME + "," +
                COLUMN_NAME_CITYNAME + "," +
                COLUMN_NAME_COUNTRY + "," +
                COLUMN_NAME_AIRPORTSTRINGID + "," +
                COLUMN_NAME_LAT + "," +
                COLUMN_NAME_LON + "," +
                COLUMN_NAME_WORLD_PART +
                ") VALUES(?,?,?,?,?,?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, airport.getAirportID());
            pstmt.setString(2, airport.getAirportName());
            pstmt.setString(3, airport.getCityName());
            pstmt.setString(4, airport.getCountry());
            pstmt.setString(5, airport.getairPortStringID());
            pstmt.setDouble(6, airport.getLat());
            pstmt.setDouble(7, airport.getLon());
            pstmt.setString(8, airport.getWorldPart());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void addSchedule(Schedule schedule) {
        String sql = "INSERT INTO " + TABLE_SCHEDULES + " (" +
                COLUMN_NAME_SCHEDULE_ID + ", " +
                COLUMN_NAME_ROUTEID + "," +
                COLUMN_NAME_DEPARTURE + "," +
                COLUMN_NAME_ARRIVAL +
                ") VALUES(?,?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, schedule.getScheduleID());
            pstmt.setInt(2, schedule.getRouteID());
            pstmt.setDouble(3, schedule.getDeparture());
            pstmt.setDouble(4, schedule.getArrival());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private static void addRoute(Route route) {

        String sql = "INSERT INTO " + TABLE_ROUTES + " (" +
                COLUMN_NAME_ID + ", " +
                COLUMN_NAME_ORIGIN + ", " +
                COLUMN_NAME_DESTINATION +
                ") VALUES(?,?,?)";


        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, route.getIdRoute());
            pstmt.setString(2, route.getOrigin());
            pstmt.setString(3, route.getDestination());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteRoutes() {
        String sql = "DELETE FROM " + TABLE_ROUTES;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private static void deleteAirports() {
        String sql = "DELETE FROM " + TABLE_AIRPORTS;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static double calculateDistance(Airport airportOrigin, Airport airportDest) {
        double earthRadius = 6371.0;
        double lat1 = airportOrigin.getLat();
        double lon1 = airportOrigin.getLon();

        double lat2 = airportDest.getLat();
        double lon2 = airportDest.getLon();

        double dlon = deg2rad(lon2 - lon1);
        double dlat = deg2rad(lat2 - lat1);

        double a = (Math.sin(dlat / 2) * Math.sin(dlat / 2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * (Math.sin(dlon / 2) * Math.sin(dlon / 2));
        double angle = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = angle * earthRadius;

        return distance;
    }

    private static double deg2rad(double degree) {
        return degree * (Math.PI / 180);
    }

    private static void generateRandomSchedules() {
        List<Route> routes = QueryDB.getRoutes();


        List<Route> filteredRoutes = new ArrayList<>();


        for (Route route : routes) {
            if (route.getOrigin().equals("MEX") || route.getDestination().equals("MEX")) {

            } else {
                filteredRoutes.add(route);
            }
        }

        routes = filteredRoutes;

        Random random = new Random();

        int scheduleId = 1;

        for (Route route : routes) {

            Airport origin = QueryDB.getAirport(route.getOrigin());
            Airport destination = QueryDB.getAirport(route.getDestination());

            double distance = calculateDistance(origin, destination);

            double flightHours = distance / 300;

            int randomFlightSchedules = random.nextInt(3) + 2;


            for (int i = 0; i < randomFlightSchedules; i++) {
                int randomHour = random.nextInt(24) + 1;

                if (randomHour + flightHours <= 24) {
                    double departureHour = randomHour;
                    double arrivalHour = (double) randomHour + flightHours;

                    Schedule schedule = new Schedule(scheduleId, route.getIdRoute(), departureHour, Math.floor(arrivalHour * 100) / 100);

                    scheduleId++;


                    addSchedule(schedule);


                }
            }
        }
    }

}

