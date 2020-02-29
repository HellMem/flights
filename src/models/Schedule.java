package models;

public class Schedule {
    private int scheduleID, routeID;
    private double departure;
    private double arrival;

    public Schedule(int scheduleID, int routeID, double departure, double arrival) {
        this.scheduleID = scheduleID;
        this.routeID = routeID;
        this.departure = departure;
        this.arrival = arrival;
    }

    public int getScheduleID() {
        return scheduleID;
    }

    public int getRouteID() {
        return routeID;
    }

    public double getDeparture() {
        return departure;
    }

    public double getArrival() {
        return arrival;
    }

    @Override
    public String toString() {
        return "models.Schedule{" +
                ", salida = " + departure +
                ", llegada = " + arrival +
                '}';
    }
}
