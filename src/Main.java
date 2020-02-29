import db.QueryDB;
import graphs.distance.DistanceGraph;
import graphs.distance.DistancePath;
import graphs.price.PriceGraph;
import graphs.price.PricePath;
import graphs.schedules.ScheduleTimeGraph;
import graphs.schedules.ScheduleTimePath;
import graphs.schedules.SingleScheduleTimeEdge;
import graphs.time.TimeGraph;
import graphs.time.TimePath;
import models.Airport;

import java.util.List;

public class Main {

    static void distanceTest() {
        Airport tijAirport = QueryDB.getAirport("TIJ");
        Airport acaAirport = QueryDB.getAirport("ACA");
        Airport tlcAirport = QueryDB.getAirport("TLC");
        Airport cunAirport = QueryDB.getAirport("CUN");
        Airport gdlAirport = QueryDB.getAirport("GDL");
        Airport mexAirport = QueryDB.getAirport("MEX");
        Airport mtyAirport = QueryDB.getAirport("MTY");

        DistanceGraph distanceGraph = new DistanceGraph();

        distanceGraph.addEdge(acaAirport, tlcAirport);
        distanceGraph.addEdge(tlcAirport, cunAirport);
        distanceGraph.addEdge(tijAirport, acaAirport);
        distanceGraph.addEdge(gdlAirport, mexAirport);
        distanceGraph.addEdge(mtyAirport, mexAirport);
        distanceGraph.addEdge(tijAirport, mtyAirport);
        distanceGraph.addEdge(cunAirport, mexAirport);
        distanceGraph.addEdge(gdlAirport, acaAirport);
        distanceGraph.addEdge(gdlAirport, mtyAirport);
        distanceGraph.addEdge(tlcAirport, gdlAirport);

        List<DistancePath> bestDistancePaths = distanceGraph.getBestFivePaths("TIJ", "MEX");


        for (int i = 0; i < bestDistancePaths.size(); i++) {

            System.out.println("CAMINO NO: " + (i + 1));
            System.out.println(bestDistancePaths.get(i));
            System.out.println("**********************");
            System.out.println("**********************");

        }
    }


    static void priceTest() {
        Airport tijAirport = QueryDB.getAirport("TIJ");
        Airport acaAirport = QueryDB.getAirport("ACA");
        Airport tlcAirport = QueryDB.getAirport("TLC");
        Airport cunAirport = QueryDB.getAirport("CUN");
        Airport gdlAirport = QueryDB.getAirport("GDL");
        Airport mexAirport = QueryDB.getAirport("MEX");
        Airport mtyAirport = QueryDB.getAirport("MTY");

        PriceGraph priceGraph = new PriceGraph();

        priceGraph.addEdge(acaAirport, tlcAirport);
        priceGraph.addEdge(tlcAirport, cunAirport);
        priceGraph.addEdge(tijAirport, acaAirport);
        priceGraph.addEdge(gdlAirport, mexAirport);
        priceGraph.addEdge(mtyAirport, mexAirport);
        priceGraph.addEdge(tijAirport, mtyAirport);
        priceGraph.addEdge(cunAirport, mexAirport);
        priceGraph.addEdge(gdlAirport, acaAirport);
        priceGraph.addEdge(gdlAirport, mtyAirport);
        priceGraph.addEdge(tlcAirport, gdlAirport);

        List<PricePath> bestDistancePaths = priceGraph.getBestFivePaths("TIJ", "MEX");


        for (int i = 0; i < bestDistancePaths.size(); i++) {

            System.out.println("CAMINO NO: " + (i + 1));
            System.out.println(bestDistancePaths.get(i));
            System.out.println("**********************");
            System.out.println("**********************");

        }
    }

    static void timeTest() {
        Airport tijAirport = QueryDB.getAirport("TIJ");
        Airport acaAirport = QueryDB.getAirport("ACA");
        Airport tlcAirport = QueryDB.getAirport("TLC");
        Airport cunAirport = QueryDB.getAirport("CUN");
        Airport gdlAirport = QueryDB.getAirport("GDL");
        Airport mexAirport = QueryDB.getAirport("MEX");
        Airport mtyAirport = QueryDB.getAirport("MTY");

        TimeGraph priceGraph = new TimeGraph();

        priceGraph.addEdge(acaAirport, tlcAirport);
        priceGraph.addEdge(tlcAirport, cunAirport);
        priceGraph.addEdge(tijAirport, acaAirport);
        priceGraph.addEdge(gdlAirport, mexAirport);
        priceGraph.addEdge(mtyAirport, mexAirport);
        priceGraph.addEdge(tijAirport, mtyAirport);
        priceGraph.addEdge(cunAirport, mexAirport);
        priceGraph.addEdge(gdlAirport, acaAirport);
        priceGraph.addEdge(gdlAirport, mtyAirport);
        priceGraph.addEdge(tlcAirport, gdlAirport);

        List<TimePath> bestDistancePaths = priceGraph.getBestFivePaths("TIJ", "MEX");


        for (int i = 0; i < bestDistancePaths.size(); i++) {

            System.out.println("CAMINO NO: " + (i + 1));
            System.out.println(bestDistancePaths.get(i));
            System.out.println("**********************");
            System.out.println("**********************");

        }
    }

    static void testSchedule(){
        Airport tijAirport = QueryDB.getAirport("TIJ");
        Airport acaAirport = QueryDB.getAirport("ACA");
        Airport tlcAirport = QueryDB.getAirport("TLC");
        Airport cunAirport = QueryDB.getAirport("CUN");

        ScheduleTimeGraph timeGraph = new ScheduleTimeGraph();

        timeGraph.addEdge(acaAirport, tlcAirport, QueryDB.getOriginDestinationSchedules(2));
        timeGraph.addEdge(tlcAirport, cunAirport, QueryDB.getOriginDestinationSchedules(73));
        timeGraph.addEdge(tijAirport, acaAirport, QueryDB.getOriginDestinationSchedules(538));

        ScheduleTimePath bestPath = timeGraph.shortestTime("TIJ", "TLC", 1);

        for (SingleScheduleTimeEdge singleScheduleTimeEdge : bestPath.getEdges()) {
            System.out.println(singleScheduleTimeEdge);
        }
    }


    public static void main(String[] args) {

        //distanceTest();
        //priceTest();
        timeTest();
        //testSchedule();
    }


}
