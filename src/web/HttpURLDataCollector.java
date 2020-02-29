package web;

import models.Airport;
import models.Route;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpURLDataCollector {


    public List<Route> getRoutes() {
        List<Route> routes = new ArrayList<>();

        try {
            URL airportsURL = new URL("https://raw.githubusercontent.com/jpatokal/openflights/master/data/routes.dat");
            HttpURLConnection con = (HttpURLConnection) airportsURL.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {

                    String interconnectionsDataArray[] = inputLine.split(",");
                    response.append(inputLine);
                    Route route = new Route(0, interconnectionsDataArray[2], interconnectionsDataArray[4]);

                    routes.add(route);
                }
                in.close();

                // print result
                System.out.println(response.toString());
            } else {
                System.out.println("GET request did not work");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return routes;
    }

    public List<Airport> getAirports() {
        List<Airport> airports = new ArrayList<>();

        try {
            URL airportsURL = new URL("https://raw.githubusercontent.com/jpatokal/openflights/master/data/airports.dat");
            HttpURLConnection con = (HttpURLConnection) airportsURL.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {

                    inputLine = inputLine.replace("\"", "");
                    String airportDataArray[] = inputLine.split(",");
                    response.append(inputLine);
                    if (airportDataArray.length <= 14) {
                        Airport airport = new Airport(Integer.parseInt(airportDataArray[0]) - 1, airportDataArray[1], airportDataArray[2], airportDataArray[3], airportDataArray[4], Double.parseDouble(airportDataArray[6]), Double.parseDouble(airportDataArray[7]), airportDataArray[11]);
                        airports.add(airport);
                    } else {
                        Airport airport = new Airport(Integer.parseInt(airportDataArray[0]) - 1, airportDataArray[1] + " , " + airportDataArray[2], airportDataArray[3], airportDataArray[4], airportDataArray[5], Double.parseDouble(airportDataArray[7]), Double.parseDouble(airportDataArray[8]), airportDataArray[12]);
                        airports.add(airport);
                    }
                }
                in.close();

                // print result
                //System.out.println(response.toString());
            } else {
                System.out.println("GET request did not work");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }


        return airports;
    }
}
