package graphs.schedules;

import models.Airport;
import models.Schedule;

import java.util.List;

public class ScheduleTimeEdge {

    private static final double INF = Double.MAX_VALUE;

    private List<Schedule> schedules;
    public Airport origin, destination;

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public ScheduleTimeEdge(List<Schedule> schedules, Airport origin, Airport destination) {
        this.schedules = schedules;
        this.origin = origin;
        this.destination = destination;
    }
}
