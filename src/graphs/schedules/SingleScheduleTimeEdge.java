package graphs.schedules;

import models.Airport;
import models.Schedule;

public class SingleScheduleTimeEdge {


    private static final double INF = Double.MAX_VALUE;

    public Schedule schedule;
    public Airport origin, destination;


    public SingleScheduleTimeEdge() {
    }

    public SingleScheduleTimeEdge(Schedule schedule, Airport origin, Airport destination) {
        this.schedule = schedule;
        this.origin = origin;
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "graphs.schedules.SingleScheduleTimeEdge{" +
                "schedule = " + schedule +
                ", origen = " + origin.getCityName() +
                ", destino = " + destination.getCityName() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SingleScheduleTimeEdge) {
            SingleScheduleTimeEdge singleScheduleTimeEdge = (SingleScheduleTimeEdge) obj;
            boolean sameSchedule = schedule.getArrival() == singleScheduleTimeEdge.schedule.getArrival() && schedule.getDeparture() == singleScheduleTimeEdge.schedule.getDeparture();
            boolean isEqual = singleScheduleTimeEdge.origin.getairPortStringID().equals(origin.getairPortStringID()) && singleScheduleTimeEdge.destination.getairPortStringID().equals(destination.getairPortStringID()) && sameSchedule;

            return isEqual;
        }

        return false;
    }
}
